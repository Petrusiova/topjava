package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.validation.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(@Valid User user) {
        ValidationUtil.validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update(
                    "UPDATE users SET name=:name, email=:email, password=:password, " +
                            "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id",
                    parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("delete from user_roles where user_id = ?", user.getId());
        }
        List<Role> roles = new ArrayList<>();
        roles.addAll(user.getRoles());
        batchUpdateRole(user, roles);
        return user;
    }

    public int[] batchUpdateRole(User user, List<Role> roles) {
        return this.jdbcTemplate.batchUpdate(
                "insert into user_roles  values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, user.getId());
                        ps.setString(2, roles.get(i).name());
                    }

                    public int getBatchSize() {
                        return roles.size();
                    }
                });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(setRolesToUsers(users, id));
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            return DataAccessUtils.singleResult(setRolesToUsers(List.of(user), user.getId()));
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        Set<User> users = new HashSet<>();
        users.addAll(jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER));
        return setRolesToUsers(List.copyOf(users));
    }

    private List<User> setRolesToUsers(List<User> users, Object... args) {
        List<Map<String, Object>> list;
        String sql = "SELECT * FROM user_roles";
        list = args.length == 0 ? jdbcTemplate.queryForList(sql) : jdbcTemplate.queryForList(sql + " WHERE user_id=?", args);
        list.forEach(map -> users.stream().filter(user -> map.get("user_id").equals(user.getId()))
                .forEach(user -> user.addRole(Role.valueOf((String) map.get("ROLE")))));
        return users;
    }
}

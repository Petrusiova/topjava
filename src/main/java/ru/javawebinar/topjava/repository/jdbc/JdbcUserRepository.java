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

import javax.validation.Valid;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
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
        batchUpdateRole(user, user.getRoles());
        return user;
    }

    public int[] batchUpdateRole(User user,  Set<Role> roles) {
        return this.jdbcTemplate.batchUpdate(
                "insert into user_roles  values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, user.getId());
                        Role role = (Role) roles.toArray()[i];
                        ps.setString(2, role.name());
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
        User user = DataAccessUtils.singleResult(users);
        return setRolesToUser(user, id);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        if (user != null) {
            return setRolesToUser(user, user.getId());
        }
        return null;
    }

    @Override
    public List<User> getAll() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList(
                "SELECT * FROM user_roles ur join users u on u.id = ur.user_id");
        Map<Integer, User> users = new HashMap<>();
        for (Map<String, Object> map : list) {

            int id = (int) map.get("ID");
            User user;
            if (users.keySet().contains(id)){
                user = users.get(id);
                user.addRole(Role.valueOf((String) map.get("ROLE")));
            } else {
                user = new User();
                user.setId((int) map.get("ID"));
                user.setName((String) map.get("NAME"));
                user.setEmail((String) map.get("EMAIL"));
                user.setPassword((String) map.get("PASSWORD"));
                Timestamp registered = (Timestamp) map.get("REGISTERED");
                user.setRegistered(Date.from(registered.toInstant()));
                user.setEnabled((Boolean) map.get("ENABLED"));
                user.setCaloriesPerDay((int) map.get("CALORIES_PER_DAY"));
            }
            user.addRole(Role.valueOf((String) map.get("ROLE")));
            users.put(user.getId(), user);
        }

        return new ArrayList<>(users.values());
    }

    private User setRolesToUser(User user, int user_id) {
        List<Map<String, Object>> list =  jdbcTemplate.queryForList("SELECT * FROM user_roles WHERE user_id=?", user_id);
        list.forEach(map -> {if (user.getId().equals(map.get("user_id"))){user.addRole(Role.valueOf((String) map.get("ROLE")));}});
        return user;
    }
}

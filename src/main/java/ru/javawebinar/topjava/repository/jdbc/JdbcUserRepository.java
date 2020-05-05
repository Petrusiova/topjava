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
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            batchUpdateUser(List.of(user));
        }
        Set<Role> roles = user.getRoles();
        if (roles.size() > 0) {
            jdbcTemplate.update("delete from user_roles where user_id = ?", user.getId());
            for (Role role : roles) {
                batchUpdateRole(List.of(user), role);
            }
        }
        return user;
    }

    public int[] batchUpdateUser(List<User> users) {
        return this.jdbcTemplate.batchUpdate(
                "update users set id = ?, name = ?, email = ?, password = ?, registered = ?, enabled =?, calories_per_day = ? where id = ?",
                new BatchPreparedStatementSetter() {

                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, users.get(i).getId());
                        ps.setString(2, users.get(i).getName());
                        ps.setString(3, users.get(i).getEmail());
                        ps.setString(4, users.get(i).getPassword());
                        ps.setObject(5, users.get(i).getRegistered());
                        ps.setBoolean(6, users.get(i).isEnabled());
                        ps.setInt(7, users.get(i).getCaloriesPerDay());
                        ps.setInt(8, users.get(i).getId());
                    }

                    public int getBatchSize() {
                        return 1;
                    }
                });
    }

    public int[] batchUpdateRole(List<User> users, Role role) {
        return this.jdbcTemplate.batchUpdate(
                "insert into user_roles  values (?, ?)",
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i)
                            throws SQLException {
                        ps.setInt(1, users.get(i).getId());
                        ps.setString(2, role.name());
                    }

                    public int getBatchSize() {
                        return 1;
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
        return DataAccessUtils.singleResult(setRolesToUsers(users, DataAccessUtils.singleResult(users).getId()));
    }

    @Override
    public List<User> getAll() {
        Set<User> users = new HashSet<>();
        users.addAll(jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER));
        return setRolesToUsers(List.copyOf(users));
    }

    private List<User> setRolesToUsers(List<User> users, Object... args) {
        List<Map<String, Object>> list;
        String sql = "SELECT user_id, role FROM user_roles";
        list = args.length == 0 ? jdbcTemplate.queryForList(sql) : jdbcTemplate.queryForList(sql + " WHERE user_id=?", args);
        list.forEach(map -> users.stream().filter(user -> map.get("user_id").equals(user.getId()))
                .forEach(user -> user.addRole(Role.valueOf((String) map.get("ROLE")))));
        return users;
    }
}

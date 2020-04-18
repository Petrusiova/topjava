package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles(profiles = {JDBC, POSTGRES_DB})
public class JdbcUserServiceTest extends UserServiceTest{
}

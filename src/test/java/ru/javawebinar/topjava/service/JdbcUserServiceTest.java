package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles(JDBC)
public class JdbcUserServiceTest extends UserServiceTest{
}

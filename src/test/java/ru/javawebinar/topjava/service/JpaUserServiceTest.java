package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.JPA;
import static ru.javawebinar.topjava.Profiles.POSTGRES_DB;

@ActiveProfiles(profiles = {JPA, POSTGRES_DB})
public class JpaUserServiceTest extends UserServiceTest {
}

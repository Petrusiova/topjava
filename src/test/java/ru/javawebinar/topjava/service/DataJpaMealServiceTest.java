package ru.javawebinar.topjava.service;

import org.springframework.test.context.ActiveProfiles;

import static ru.javawebinar.topjava.Profiles.*;

@ActiveProfiles(profiles = {DATAJPA, POSTGRES_DB})
public class DataJpaMealServiceTest extends MealServiceTest {

    
}

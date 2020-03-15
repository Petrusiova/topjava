package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Repository
public class InMemoryUserRepository implements UserRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, User> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repository.remove(id) != null;
    }

    @Override
    public User save(User user) {
        log.info("save {}", user);
        if (user.isNew()) {
            user.setId(counter.incrementAndGet());
            repository.put(user.getId(), user);
            return repository.computeIfPresent(user.getId(), (id, oldMeal) -> user);
        }
        return user;
    }

    @Override
    public User get(int id) {
        log.info("get {}", id);
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        log.info("getAll");
        return new ArrayList<>(new TreeSet<>(repository.values()));
    }

    @Override
    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
//        for (Map.Entry<Integer, User> map : repository.entrySet()) {
//            User currentUser = map.getValue();
//            if (currentUser.getEmail().equals(email)) {
//                return currentUser;
//            }
//        }

        User currentUser;
        try {
            currentUser = (User) repository.entrySet().stream()
                    .filter(map -> map.getValue().getEmail().equals(email)).toArray()[0];
        } catch (ArrayIndexOutOfBoundsException e){
            throw new NotFoundException("No user with email: " + email);
        }
        return currentUser;

    }
}

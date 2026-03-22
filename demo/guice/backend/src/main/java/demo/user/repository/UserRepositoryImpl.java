package demo.user.repository;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import demo.user.model.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor(onConstructor = @__(@Inject))
public class UserRepositoryImpl implements UserRepository {
    private final Provider<EntityManager> em;

    @Transactional
    public void save(User user){
        em.get().persist(user);
    }

    @Transactional
    public Optional<User> findUserByUsername(String username) {
        try {
            User user = em.get()
                    .createQuery("SELECT u FROM User u WHERE u.username = :userName", User.class)
                    .setParameter("userName", username)
                    .getSingleResult();

            return Optional.of(user);
        } catch (NoResultException e) {
            // JPA throws this if nothing is found
            return Optional.empty();
        }
    }
    @Transactional
    public Optional<User> findUserById(Long id) {
        try {
            User user = em.get()
                    .createQuery("SELECT u FROM User u WHERE u.id = :userId", User.class)
                    .setParameter("userId", id)
                    .getSingleResult();

            return Optional.of(user);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}

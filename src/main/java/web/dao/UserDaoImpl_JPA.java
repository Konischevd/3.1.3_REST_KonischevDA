package web.dao;

import org.springframework.stereotype.Repository;
import web.models.Role;
import web.models.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Repository
public class UserDaoImpl_JPA implements UserDao {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Role getRoleByName(String name) {
        try {   // пробуем достать роль
            entityManager
                    .createQuery("select r from Role r where r.role = :name", Role.class)
                    .setParameter("name", name)
                    .getSingleResult();
        } catch (NoResultException e) {
            // если такой роли не существует в БД, то добавляем её
            Role r = new Role();
            r.setRole(name);
            entityManager.persist(r);
        }
        // заново достаём роль из БД
        return entityManager
                .createQuery("select r from Role r where r.role = :name", Role.class)
                .setParameter("name", name)
                .getSingleResult();
    }

    @Override
    public Set<Role> getRolesFromArray(String[] input) {
        if (input == null) {
            return null;
        }
        Set<Role> roles = new HashSet<>();
        for (String s : input) {
            roles.add( getRoleByName(s) );
        }
        return roles;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public User getUser(long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public User getUserByEmail(String email) {
        return entityManager.createQuery("select u from User u where u.email = :login", User.class)
                .setParameter("login", email).getSingleResult();
    }

    @Override
    public void addUser(User user) {
        entityManager.persist(user);
    }

    @Override
    public boolean deleteUser(Long id) {
        return entityManager.createQuery("delete from User u where u.id = :id")
                .setParameter("id", id).executeUpdate() == 1;
    }

    @Override
    public void updateUser(User user) {
        entityManager.merge(user);
    }

    @Override
    public boolean isAdmin(String email) {
        User user = getUserByEmail(email);
        return user.isAdmin();
    }
}
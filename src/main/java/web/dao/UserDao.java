package web.dao;

import web.models.Role;
import web.models.User;

import java.util.List;
import java.util.Set;

public interface UserDao {

    Role getRoleByName(String name);

    Set<Role> getRolesFromArray(String[] input);

    List<User> getAllUsers();

    User getUser(long id);

    User getUserByEmail(String login);

    void addUser(User user);

    boolean deleteUser(Long id);

    void updateUser(User user);

    boolean isAdmin(String email);
}

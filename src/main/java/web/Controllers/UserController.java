package web.Controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import web.models.User;
import web.service.UserService;

import java.security.Principal;
import java.util.List;

@RestController
public class UserController {
    private final UserService service;
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> read() {
        try {
            List<User> all_users = service.getAllUsers();
            return (all_users != null) && (!all_users.isEmpty())
                    ? new ResponseEntity<>(all_users, HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/users/{id}")
    public ResponseEntity<User> read(@PathVariable("id") long id, Principal principal) {
        long authId = service.getUserByEmail(principal.getName()).getId();
        if (service.isAdmin(principal.getName()) || id == authId) {
            try {
                User user = service.getUser(id);
                return user != null
                        ? new ResponseEntity<>(user, HttpStatus.OK)
                        : new ResponseEntity<>(HttpStatus.NOT_FOUND);
            } catch (Exception e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return new ResponseEntity<>(HttpStatus.FORBIDDEN);
    }

    @PostMapping("/users")
    public ResponseEntity<?> create(@RequestBody User user) {
        try {
            service.getUserByEmail(user.getEmail());
            return new ResponseEntity<>("Specified email is busy", HttpStatus.CONFLICT);
        } catch (Exception e) {/*ignore*/}

        try {
            service.addUser(user);
            User addedUser = service.getUserByEmail(user.getEmail());
            return new ResponseEntity<>(addedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @PatchMapping("/users/{id}")
    public ResponseEntity<?> update(@PathVariable("id") long id,
                                    @RequestBody User user) {
        try {
            if (service.getUserByEmail(user.getEmail()).getId() != id) {
                return new ResponseEntity<>("Specified email is busy", HttpStatus.CONFLICT);
            }
        } catch (Exception e) {/*ignore*/}

        try {
            user.setId(id);
            service.updateUser(user);
            User updatedUser = service.getUser(id);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") Long id) {
        try {
            return service.deleteUser(id)
                    ? new ResponseEntity<>(HttpStatus.OK)
                    : new ResponseEntity<>(HttpStatus.NOT_MODIFIED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

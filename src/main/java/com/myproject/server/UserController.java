package com.myproject.server;

import com.myproject.server.models.User;
import com.myproject.server.repositories.UserRepository;
import net.minidev.json.JSONArray;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserRepository repository;

    @RequestMapping(value = "/listUsers", method = RequestMethod.GET)
    public List<String> getAllUsers() {
        List<User> users = repository.findAll();
        ArrayList<String> names = new ArrayList<>();
        for (User user : users) {
            names.add(user.getName());
        }
        return names;
    }


    @RequestMapping(value = "/", params = "name", method = RequestMethod.GET)
    public User getUserByName(@RequestParam("name") String name) {
        return repository.findUserByName(name);
    }

    @GetMapping(value = "/getUserScores")
    public List<User> getUserScoresDesc(){
        return repository.findAll(Sort.by(Sort.Direction.DESC, "score"));

    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public User createUser(@RequestParam String name, @RequestParam String password, @RequestParam int avatar, User user) {
        if (getUserByName(name) == null) {
            user.set_id(ObjectId.get());
            user.setName(name);
            user.setPassword(password);
            user.setAvatar(avatar);
            user.setPlayed(new ArrayList<>());
            repository.save(user);
            System.out.println("Created user:" + user);
            return user;
        } else {
            System.out.println(String.format("User not created. username: %s, password: %s", name, password));
            return new User(ObjectId.get(),"UserAlreadyExist","pwd",1,new ArrayList<String>(),0,0,0);
        }
    }
    /*
        @RequestMapping(value = "/createUser", method = RequestMethod.POST)
        public User createUser(@Valid @RequestBody User user) {
            user.set_id(ObjectId.get());
            repository.save(user);
            return user;
        }


    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public void deleteUser(@PathVariable ObjectId id) {
        repository.delete(repository.findBy_id(id));
    }


    @RequestMapping(value = "/", params = "id", method = RequestMethod.GET)
    public User getUserById(@RequestParam("id") ObjectId id) {
        return repository.findBy_id(id);
    }


 @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public void modifyUserById(@PathVariable("id") ObjectId id, @Valid @RequestBody User user) {
        user.set_id(id);
        repository.save(user);
    }
    */
}


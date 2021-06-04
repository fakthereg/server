package com.myproject.server;

import com.myproject.server.models.File;
import com.myproject.server.models.User;

import com.myproject.server.repositories.UserRepository;


import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.bson.json.JsonObject;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
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
    private Log log = LogFactory.getLog(ServerApplication.class);


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
    public List<User> getUserScoresDesc() {
        return repository.findAll(Sort.by(Sort.Direction.DESC, "score"));
    }

    @PostMapping(value = "/setData/{user}",
            consumes = {"application/json", "application/x-www-form-urlencoded"})
    public void setUserData(@PathVariable String user, @RequestBody String string) {
        log.warn("Received:    " + string);
        int score = 0;
        int correct = 0;
        int wrong = 0;
        User userFromRepo = repository.findUserByName(user);
        try {
            JSONObject jsonObject = new JSONObject(string);
            score = jsonObject.getInt("score");
            correct = jsonObject.getInt("correct");
            wrong = jsonObject.getInt("wrong");
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.warn(String.format("Json parsed: score=%s, correct=%s, wrong=%s", score,correct,wrong));
        userFromRepo.setScore(score);
        userFromRepo.setCorrect(correct);
        userFromRepo.setWrong(wrong);
        repository.save(userFromRepo);
        log.warn("User data set to:    " + repository.findUserByName(user).getScore() + "  " + repository.findUserByName(user).getCorrect() + "  " + repository.findUserByName(user).getWrong());
    }

    @RequestMapping(value = "/createUser", method = RequestMethod.POST)
    public User createUser(@RequestParam String name, @RequestParam String password, @RequestParam int avatar, User
            user) {
        if (getUserByName(name) == null) {
            user.set_id(ObjectId.get());
            user.setName(name);
            user.setPassword(password);
            user.setAvatar(avatar);
            user.setPlayed(new ArrayList<File>());
            repository.save(user);
            log.info("Created user:" + user);
            return user;
        } else {
            log.info(String.format("User not created. username: %s, password: %s", name, password));
            return new User(ObjectId.get(), "UserAlreadyExist", "pwd", 1, new ArrayList<>(), 0, 0, 0);
        }
    }

    @PostMapping(value = "/{name}/addToPlayed",
            consumes = {"application/json", "application/x-www-form-urlencoded"})
    public void addToPlayed(@PathVariable String name, @RequestBody String string) {
        log.warn("addToPlayed Received:   " + string);
        User user = repository.findUserByName(name);
        List<File> played = user.getPlayed();
        File received;
        try {
            received = new File(new JSONObject(string));
            played.add(received);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        user.setPlayed(played);
        repository.save(user);
        log.warn("addToPlayed Returned:   " + repository.findUserByName(name).getPlayed());
    }

    @GetMapping(value = "/getPlayed/{user}/{category}")
    public List<File> getPlayedByUserAndCategory(@PathVariable String user, @PathVariable String category) {
        log.warn("getPlayed Received:    username = " + user + "  category = " + category);
        List<File> result = new ArrayList<>();
        List<File> played = repository.findUserByName(user).getPlayed();
        if (played.size() > 0) {
            for (File file : played) {
                if (file.getCategory().equals(category)) {
                    result.add(file);
                }
            }
        }
        log.warn("getPlayed Returned:    " + result.toString());
        return result;
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


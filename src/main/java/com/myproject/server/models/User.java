package com.myproject.server.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

public class User {
    @Id
    private ObjectId _id;
    private String name;
    private String password;
    private int avatar;
    private List<File> played;
    private int correct;
    private int wrong;
    private int score;


    //Constructors
    public User() {
    }

    public User(ObjectId _id, String name, String password, int avatar, List<File> played, int correct, int wrong, int score) {
        this._id = _id;
        this.name = name;
        this.password = password;
        this.avatar = avatar;
        this.played = played;
        this.correct = correct;
        this.wrong = wrong;
        this.score = score;
    }

    //Getters & Setters

    @JsonSerialize(using= ToStringSerializer.class)
    public ObjectId get_id() {
        return _id;
    }

    @JsonSerialize(using= ToStringSerializer.class)
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getAvatar() {
        return avatar;
    }

    public void setAvatar(int avatar) {
        this.avatar = avatar;
    }

    public List<File> getPlayed() {
        return played;
    }

    public void setPlayed(List<File> played) {
        this.played = played;
    }

    public int getCorrect() {
        return correct;
    }

    public void setCorrect(int correct) {
        this.correct = correct;
    }

    public int getWrong() {
        return wrong;
    }

    public void setWrong(int wrong) {
        this.wrong = wrong;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    @Override
    public String toString() {
        return String.format("_id: %s, username: %s, password: %s", _id, name, password);
    }



}

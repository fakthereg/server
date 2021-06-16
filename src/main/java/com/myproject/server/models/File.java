package com.myproject.server.models;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.bson.types.ObjectId;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.data.annotation.Id;

public class File {
    @Id
    private ObjectId _id;
    private String artist;
    private String title;
    private String category;
    private String filename;
    private boolean answered;
    private boolean correct;

    //Constructor

    public File() {
    }

    public File(ObjectId _id, String artist, String title, String category, String filename, boolean answered, boolean correct) {
        this._id = _id;
        this.artist = artist;
        this.title = title;
        this.category = category;
        this.filename = filename;
        this.answered = answered;
        this.correct = correct;
    }


    public File(JSONObject jsonObject) {
        try {
            this._id = new ObjectId(jsonObject.getString("_id"));
            this.artist = jsonObject.getString("artist");
            this.title = jsonObject.getString("title");
            this.category = jsonObject.getString("category");
            this.filename = jsonObject.getString("filename");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


//Getters & setters


    @JsonSerialize(using= ToStringSerializer.class)
    public ObjectId get_id() {
        return _id;
    }

    @JsonSerialize(using= ToStringSerializer.class)
    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    @Override
    public String toString() {
        return super.toString() +"  " + artist +"  "+ title +"  "+ category +"  "+ filename + "answered: " + answered + " correct: " + correct;
    }
}

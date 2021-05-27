package com.myproject.server.models;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;

public class File {
    @Id
    private ObjectId _id;
    private String name;
    private String category;
    private String filename;

    //Constructor

    public File() {
    }

    public File(ObjectId _id, String name, String category, String filename) {
        this._id = _id;
        this.name = name;
        this.category = category;
        this.filename = filename;
    }

    //Getters & setters


    public ObjectId get_id() {
        return _id;
    }

    public void set_id(ObjectId _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public String toString() {
        return super.toString();
    }
}

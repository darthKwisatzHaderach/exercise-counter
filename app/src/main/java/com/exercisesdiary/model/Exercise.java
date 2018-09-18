package com.exercisesdiary.model;

import com.j256.ormlite.field.DatabaseField;

public class Exercise {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    String name;

    public Exercise(){

    }

    public Exercise(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return "Exercise{" + "id=" + id + ", name='" + name + '\'' + '}';
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

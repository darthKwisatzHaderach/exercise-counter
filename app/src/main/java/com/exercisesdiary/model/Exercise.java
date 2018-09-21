package com.exercisesdiary.model;

import com.j256.ormlite.dao.ForeignCollection;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.List;

@DatabaseTable
public class Exercise {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(unique = true)
    String name;

    @ForeignCollectionField
    private ForeignCollection<ExerciseRun> runs;

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

    public int getId() {
        return id;
    }
}

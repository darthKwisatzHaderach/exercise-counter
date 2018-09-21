package com.exercisesdiary.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class ExerciseRun {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField(
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            index = true,
            columnDefinition = "INTEGER CONSTRAINT FK_NAME REFERENCES parent(id) ON DELETE CASCADE")
    Exercise exercise;

    @DatabaseField
    int count;

    @DatabaseField
    Date date;

    public ExerciseRun(){

    }

    public ExerciseRun(Exercise exercise, int count, Date date){
        this.exercise = exercise;
        this.count = count;
        this.date = date;
    }

    public void setCount(int count){
        this.count = count;
    }

    public int getCount() {
        return count;
    }
}

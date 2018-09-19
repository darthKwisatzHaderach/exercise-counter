package com.exercisesdiary.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.Date;

@DatabaseTable
public class ExerciseRun {

    @DatabaseField(generatedId = true)
    int id;

    @DatabaseField
    int exercise_id;

    @DatabaseField
    int count;

    @DatabaseField
    Date date;

    public ExerciseRun(){

    }

    public ExerciseRun(int exercise_id, int count, Date date){
        this.exercise_id = exercise_id;
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

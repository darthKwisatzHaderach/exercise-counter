package com.exercisesdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import com.exercisesdiary.model.Exercise;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class AddExerciseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);
        Intent intent = getIntent();
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_exercise_menu);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.add_exercise_menu, menu);
        return true;
    }

    public void addExercise(View view){
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();

        EditText editExerciseName = (EditText) findViewById(R.id.editExerciseName);
        String name = editExerciseName.getText().toString();

        exerciseDao.create(new Exercise(name));
        Log.d("demo", name);

        List<Exercise> exercises = exerciseDao.queryForAll();
        Log.d("demo", exercises.toString());

        OpenHelperManager.releaseHelper();
    }
}

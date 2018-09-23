package com.exercisesdiary;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;

import com.exercisesdiary.model.Exercise;
import com.exercisesdiary.model.ExerciseRun;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;

public class EditExerciseActivity extends AppCompatActivity {

    private String oldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_exercise);
        Intent intent = getIntent();
        oldName = intent.getStringExtra(MainActivity.EXERCISE_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_exercise_menu);
        toolbar.setTitle(oldName);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.add_exercise_menu, menu);
        return true;
    }

    public void editExercise(View view) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();

        EditText editExerciseName = (EditText) findViewById(R.id.editExerciseName);
        String name = editExerciseName.getText().toString();

        UpdateBuilder<Exercise, Integer> ub = exerciseDao.updateBuilder();
        ub.where().eq("name", oldName);
        ub.updateColumnValue("name", name);
        ub.update();

        OpenHelperManager.releaseHelper();
        finish();
    }
}

package com.exercisesdiary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.exercisesdiary.model.Exercise;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public void init(){

        TableLayout stk = (TableLayout) findViewById(R.id.exercisesList);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Упражнение ");
        tv0.setTextColor(Color.BLUE);
        tbrow0.addView(tv0);
        stk.addView(tbrow0);

        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();

        List<Exercise> exercises = exerciseDao.queryForAll();

        OpenHelperManager.releaseHelper();

        for (Exercise exercise: exercises) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setText(exercise.getName());
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            stk.addView(tbrow);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.top_menu);
        setSupportActionBar(toolbar);

        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.top_menu, menu);
        return true;
    }

    public boolean add(MenuItem irem) {
        Intent intent = new Intent(this, AddExerciseActivity.class);
        startActivity(intent);
        return true;
    }

    public void openExercice(View view) {
        Intent intent = new Intent(this, ExerciseDetails.class);
        startActivity(intent);
    }
}

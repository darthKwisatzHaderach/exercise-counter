package com.exercisesdiary;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.exercisediary.EXERCISE_NAME";

    public void init(){

        TableLayout stk = (TableLayout) findViewById(R.id.exercisesList);

        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();

        List<Exercise> exercises = exerciseDao.queryForAll();

        OpenHelperManager.releaseHelper();

        for (Exercise exercise: exercises) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setOnClickListener(openExercise);
            t1v.setText(exercise.getName());
            t1v.setTextSize(20);
            t1v.setTextColor(Color.BLACK);
            t1v.setPadding(0, 0, 0, 20);
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
    public void onRestart()
    {
        super.onRestart();
        recreate();
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

    View.OnClickListener openExercise = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            openExercise(view);
    }};

    public void openExercise(View view) {
        String name = ((TextView) view).getText().toString();
        Intent intent = new Intent(this, ExerciseDetails.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }
}

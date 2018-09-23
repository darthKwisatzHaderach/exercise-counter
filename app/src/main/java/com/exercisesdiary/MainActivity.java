package com.exercisesdiary;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.exercisesdiary.model.Exercise;
import com.exercisesdiary.model.ExerciseRun;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.UpdateBuilder;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.exercisediary.EXERCISE_NAME";
    private String exerciseName;

    public void init() throws SQLException {

        TableLayout stk = findViewById(R.id.exercisesList);
        stk.removeAllViews();

        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();
        RuntimeExceptionDao<ExerciseRun, Integer> exerciseRunDao = dbHelper.getExerciseRunRuntimeDao();

        List<Exercise> exercises = exerciseDao.queryForAll();

        for (Exercise exercise : exercises) {

            QueryBuilder<ExerciseRun, Integer> queryBuilder = exerciseRunDao.queryBuilder();
            queryBuilder.where().eq("exercise_id", exercise.getId());
            queryBuilder.orderBy("date", false);
            ExerciseRun run = queryBuilder.queryForFirst();

            Integer count;

            try {
                count = run.getCount();
            }catch (NullPointerException e){
                count = 0;
            }

            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            t1v.setOnClickListener(openExercise);
            t1v.setText(exercise.getName());
            t1v.setTextSize(20);
            t1v.setTextColor(Color.BLACK);
            t1v.setPadding(0, 0, 0, 30);
            t1v.setWidth(550);
            tbrow.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setTag(exercise.getName());
            t2v.setWidth(100);
            t2v.setText(count.toString());
            t2v.setOnClickListener(changeCount);
            t2v.setTextSize(20);
            t2v.setTextColor(Color.BLACK);
            t2v.setPadding(0, 0, 0, 30);
            tbrow.addView(t2v);

            stk.addView(tbrow);
        }

        OpenHelperManager.releaseHelper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.top_menu);
        setSupportActionBar(toolbar);

        try {
            init();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRestart() {
        super.onRestart();
        recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
        }
    };

    public void openExercise(View view) {
        String name = ((TextView) view).getText().toString();
        Intent intent = new Intent(this, ExerciseDetails.class);
        intent.putExtra(EXTRA_MESSAGE, name);
        startActivity(intent);
    }

    View.OnClickListener changeCount = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final String name = (String) v.getTag();
            TextView tv = v.findViewWithTag(name);
            Integer currentCount = Integer.valueOf(tv.getText().toString());
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);

            alert.setTitle(String.format("%s", name));
            final NumberPicker input = new NumberPicker(MainActivity.this);
            input.setMinValue(0);
            input.setMaxValue(9999);
            input.setValue(currentCount);
            alert.setView(input);

            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    Integer count = input.getValue();
                    try {
                        changeExerciseRun(name, count, getCurrentDateWithoutTime());
                        init();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            });
            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    // Canceled.
                }
            });


            alert.show();
        }
    };

    public void changeExerciseRun(String exerciseName, int count, Date date) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();
        RuntimeExceptionDao<ExerciseRun, Integer> exerciseRunDao = dbHelper.getExerciseRunRuntimeDao();

        QueryBuilder<Exercise, Integer> queryBuilder = exerciseDao.queryBuilder();
        queryBuilder.where().eq("name", exerciseName);
        Exercise exercise = queryBuilder.queryForFirst();

        QueryBuilder<ExerciseRun, Integer> qb = exerciseRunDao.queryBuilder();
        qb.where().eq("exercise_id", exercise.getId()).and().eq("date", date);
        //qb.where().eq("exercise_id", exercise.getId()).and().eq("date", date);

        try {
            ExerciseRun run = qb.queryForFirst();
            Log.d("demo", run.toString());
            UpdateBuilder<ExerciseRun, Integer> ub = exerciseRunDao.updateBuilder();
            ub.where().eq("id", run.getId());
            ub.updateColumnValue("count", count);
            ub.update();
        }catch (NullPointerException e){
            ExerciseRun run = new ExerciseRun(exercise, count, date);
            exerciseRunDao.create(run);
        }

        OpenHelperManager.releaseHelper();
    }

    public Date getCurrentDateWithoutTime() throws ParseException {
        DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        return formatter.parse(formatter.format(new Date()));
    }
}

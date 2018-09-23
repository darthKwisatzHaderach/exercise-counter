package com.exercisesdiary;

import android.content.Intent;
import android.os.Bundle;
import android.sax.TextElementListener;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.exercisesdiary.model.Exercise;
import com.exercisesdiary.model.ExerciseRun;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.List;

public class ExerciseDetailsActivity extends AppCompatActivity {
    public static final String EXERCISE_OLD_NAME = "com.exercisediary.EXERCISE_OLD_NAME";
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_details);
        Intent intent = getIntent();
        name = intent.getStringExtra(MainActivity.EXERCISE_NAME);
        Toolbar toolbar = (Toolbar) findViewById(R.id.exercise_details_menu);
        toolbar.setTitle(name);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView today = (TextView) findViewById(R.id.today);
        TextView lastWeek = (TextView) findViewById(R.id.lastWeek);
        TextView lastMonth = (TextView) findViewById(R.id.lastMonth);
        TextView lastQuarter = (TextView) findViewById(R.id.lastQuarter);
        TextView lastYear = (TextView) findViewById(R.id.lastYear);

        try {
            today.setText(getCount(1).toString());
            lastWeek.setText(getCount(7).toString());
            lastMonth.setText(getCount(30).toString());
            lastQuarter.setText(getCount(90).toString());
            lastYear.setText(getCount(365).toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater mMenuInflater = getMenuInflater();
        mMenuInflater.inflate(R.menu.exercise_details_menu, menu);
        return true;
    }

    public void delete(MenuItem item) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();

        DeleteBuilder<Exercise, Integer> deleteBuilder = exerciseDao.deleteBuilder();
        deleteBuilder.where().eq("name", name);
        deleteBuilder.delete();

        OpenHelperManager.releaseHelper();
        finish();
    }

    public Integer getCount(Integer days) throws SQLException {
        DBHelper dbHelper = OpenHelperManager.getHelper(this, DBHelper.class);
        RuntimeExceptionDao<Exercise, Integer> exerciseDao = dbHelper.getExerciseRuntimeDao();
        RuntimeExceptionDao<ExerciseRun, Integer> exerciseRunDao = dbHelper.getExerciseRunRuntimeDao();

        QueryBuilder<Exercise, Integer> qb = exerciseDao.queryBuilder();
        qb.where().eq("name", name);
        Exercise exercise = qb.queryForFirst();

        QueryBuilder<ExerciseRun, Integer> queryBuilder = exerciseRunDao.queryBuilder();

        queryBuilder.where().eq("exercise_id", exercise.getId());
        queryBuilder.limit((long) days);
        queryBuilder.orderBy("date", false);
        List<ExerciseRun> runs = queryBuilder.query();

        Integer count = 0;

        for (ExerciseRun run : runs) {
            Log.d("demo", run.toString());
            count = count + run.getCount();
        }

        OpenHelperManager.releaseHelper();

        return count;
    }

    public void editExercise(MenuItem item) {
        Intent intent = new Intent(this, EditExerciseActivity.class);
        intent.putExtra(EXERCISE_OLD_NAME, MainActivity.EXERCISE_NAME);
        startActivity(intent);
    }
}

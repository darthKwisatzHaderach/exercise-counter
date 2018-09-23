package com.exercisesdiary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.exercisesdiary.model.Exercise;
import com.exercisesdiary.model.ExerciseRun;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

public class DBHelper extends OrmLiteSqliteOpenHelper  {
    private static final String DATABASE_NAME = "exercises.db";
    private static final int DATABASE_VERSION = 5;

    private Dao<Exercise, Integer> exerciseDao = null;
    private Dao<ExerciseRun, Integer> exerciseRunDao = null;
    private RuntimeExceptionDao<Exercise, Integer> exerciseRuntimeDao = null;
    private RuntimeExceptionDao<ExerciseRun, Integer> exerciseRunRuntimeDao = null;

    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Exercise.class);
            TableUtils.createTable(connectionSource, ExerciseRun.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, Exercise.class, true);
            TableUtils.dropTable(connectionSource, ExerciseRun.class, true);
            onCreate(database, connectionSource);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Dao<Exercise, Integer> getExerciseDao() throws SQLException {
        if (exerciseDao == null){
            exerciseDao = getDao(Exercise.class);
        }
        return exerciseDao;
    }

    public Dao<ExerciseRun, Integer> getExerciseRunDao() throws SQLException {
        if (exerciseRunDao == null){
            exerciseRunDao = getDao(ExerciseRun.class);
        }
        return exerciseRunDao;
    }

    public RuntimeExceptionDao<Exercise, Integer> getExerciseRuntimeDao() {
        if (exerciseRuntimeDao == null){
            exerciseRuntimeDao = getRuntimeExceptionDao(Exercise.class);
        }
        return exerciseRuntimeDao;
    }

    public RuntimeExceptionDao<ExerciseRun, Integer> getExerciseRunRuntimeDao() {
        if (exerciseRunRuntimeDao == null){
            exerciseRunRuntimeDao = getRuntimeExceptionDao(ExerciseRun.class);
        }
        return exerciseRunRuntimeDao;
    }
}

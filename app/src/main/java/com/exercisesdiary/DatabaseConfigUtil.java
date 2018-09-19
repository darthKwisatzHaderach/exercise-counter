package com.exercisesdiary;

import com.exercisesdiary.model.Exercise;
import com.exercisesdiary.model.ExerciseRun;
import com.j256.ormlite.android.apptools.OrmLiteConfigUtil;

import java.io.IOException;
import java.sql.SQLException;

public class DatabaseConfigUtil extends OrmLiteConfigUtil {

    private static final Class<?>[] classes = new Class[]{Exercise.class, ExerciseRun.class};

    public static void main(String[] args) throws IOException, SQLException {
        writeConfigFile("ormlite_config.txt", classes);
    }
}

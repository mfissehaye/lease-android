package zewd.com.learnamharic.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySqliteHelper extends SQLiteOpenHelper {

    public static final String TABLE_EXAMS = "exams";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_QUESTIONS_COUNT = "questions_count";
    public static final String COLUMN_DURATION = "duration";
    public static final String COLUMN_INSTRUCTIONS = "instructions";
    public static final String COLUMN_JSON_FILE_PATH = "json_file_path";
    public static final String COLUMN_ENCRYPTION_KEY = "encryption_key";
    public static final String COLUMN_DATE_CREATED = "date_created";
    public static final String COLUMN_FILE_SIZE = "file_size";
    public static final String COLUMN_ICON_FILE_PATH = "icon_file_path";

    public static final String DATABASE_NAME = "exams.db";
    public static final int DATABASE_VERSION = 1;

    public static final String DATABASE_CREATE = "create table "
            + TABLE_EXAMS + "(" + COLUMN_ID
            + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NAME
            + " TEXT NOT NULL, " + COLUMN_QUESTIONS_COUNT
            + " INTEGER NOT NULL, " + COLUMN_DURATION
            + " INTEGER NOT NULL, " + COLUMN_INSTRUCTIONS
            + " TEXT NOT NULL, " + COLUMN_JSON_FILE_PATH
            + " TEXT NOT NULL, " + COLUMN_ENCRYPTION_KEY
            + " TEXT, " + COLUMN_DATE_CREATED
            + " TEXT, " + COLUMN_FILE_SIZE
            + " REAL, " + COLUMN_ICON_FILE_PATH
            + " TEXT);";

    public MySqliteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {

        Log.w(MySqliteHelper.class.getSimpleName(), "Upgrading database from version " + oldVersion + " to " + newVersion + ", which will destroy old data");

        database.execSQL("DROP TABLE IF EXISTS " + TABLE_EXAMS);

        onCreate(database);
    }
}

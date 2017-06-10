package helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "escolaX.db";
    private static final int VERSION = 42;

    private static final String ALUMN_TABLE = "Alumn";
    private static final String NAME_ALUMN = "[nameAlumn]";
    public static final String ALUMN_ID = "[IDAlumn]";
    private static final String REGISTRY_ALUMN = "[registryAlumn]";

    private static final String PARENT_TABLE = "Parent";
    private static final String NAME_PARENT = "[nameParent]";
    private static final String PHONE_PARENT = "[phoneParent]";
    private static final String PARENT_ID = "[IDParent]";

    private static final String STRIKE_TABLE = "Strike";
    private static final String STRIKE_ID = "[IDStrike]";
    private static final String DESCRIPTION_STRIKE = "[descriptionStrike]";
    private static final String DATE_STRIKE = "[dateStrike]";

    private static final String CREATE_ALUMN = "CREATE TABLE IF NOT EXISTS " + ALUMN_TABLE + " (" +
            ALUMN_ID + " INTEGER PRIMARY KEY NOT NULL," +
            NAME_ALUMN + " VARCHAR(64) NOT NULL, " +
            REGISTRY_ALUMN + " VARCHAR(6) NOT NULL );";

    private static final String CREATE_PARENT = "CREATE TABLE IF NOT EXISTS " + PARENT_TABLE + " (" +
            PARENT_ID + " INTEGER PRIMARY KEY NOT NULL," +
            NAME_PARENT + " VARCHAR(64) NOT NULL, " +
            PHONE_PARENT + " VARCHAR(13) NOT NULL, " +
            ALUMN_ID + " INTEGER, " +
            "FOREIGN KEY ("+ALUMN_ID+") REFERENCES "+ ALUMN_TABLE + "("+ALUMN_ID+"));";

    private static final String CREATE_STRIKE = "CREATE TABLE IF NOT EXISTS " + STRIKE_TABLE + " (" +
            STRIKE_ID + " INTEGER PRIMARY KEY NOT NULL, " +
            DESCRIPTION_STRIKE + " VARCHAR(150) NOT NULL, " +
            DATE_STRIKE + "VARCHAR(10) NOT NULL, " +
            ALUMN_ID + " INTEGER, " +
            "FOREIGN KEY ("+ALUMN_ID+") REFERENCES "+ ALUMN_TABLE + "("+ALUMN_ID+"));";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ALUMN);
        database.execSQL(CREATE_PARENT);
        database.execSQL(CREATE_STRIKE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
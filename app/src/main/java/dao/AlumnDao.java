package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import model.Alumn;

public class AlumnDao extends SQLiteOpenHelper{

    private static final String DATABASE_NAME = "treeHouse";
    private static final int VERSION = 42;
    private static AlumnDao instance = null;

    public static final String ALUMN_TABLE = "Alumn";
    private static final String DROP_TABLE_ALUMN = "DROP TABLE IF EXISTS " + ALUMN_TABLE;

    private static final String NAME_ALUMN = "[nameAlumn]";
    private static final String PHONE_ALUMN = "[phoneAlumn]";
    public static final String ALUMN_ID = "[IDAlumn]";
    private static final String REGISTRY_ALUMN = "[registry_alumn]";

    public AlumnDao(Context context) {
        super(context, DATABASE_NAME,null,VERSION);
    }

    public static AlumnDao getInstance(Context context){
        if(AlumnDao.instance != null){

        }else{
            AlumnDao.instance = new AlumnDao(context);
        }
        return AlumnDao.instance;
    }

    private static final String CREATE_ALUMN = "CREATE TABLE IF NOT EXISTS " + ALUMN_TABLE+ " (" +
            ALUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            NAME_ALUMN + " VARCHAR(64) NOT NULL, "+
            PHONE_ALUMN + " VARCHAR(13) NOT NULL, " +
            REGISTRY_ALUMN + "INTEGER NOT NULL ) ; ";

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_ALUMN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean insertAlumn (Alumn alumn) {

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_ALUMN, alumn.getName());
        contentValues.put(PHONE_ALUMN, alumn.getPhone());
        contentValues.put(REGISTRY_ALUMN, alumn.getRegistry());

        long result = database.insert(ALUMN_TABLE,null,contentValues);
        database.close();
        if(result == -1){
            return false;
        }else {
            return true;
        }
    }

    public List<Alumn> getAllAlumns() {

        List<Alumn> alumnList = new ArrayList<Alumn>();

        SQLiteDatabase database = this.getWritableDatabase();
        Cursor cursor = database.rawQuery("SELECT * FROM " + ALUMN_TABLE,null);

        while(cursor.moveToNext()) {

            Alumn alumn = new Alumn();

            alumn.setIdAlumn(cursor.getInt(cursor.getColumnIndex("IDAlumn")));
            alumn.setName(cursor.getString(cursor.getColumnIndex("nameAlumn")));
            alumn.setPhone(cursor.getString(cursor.getColumnIndex("phoneAlumn")));
            alumn.setRegistry(cursor.getInt(cursor.getColumnIndex("registry_alumn")));

            alumnList.add(alumn);
        }

        return alumnList;
    }
}
package konnov.commr.vk.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by ilya on 09/12/2017.
 */

public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "notesDB";
    private static final String TABLE_NAME = "notestable";
    private static final int DB_VERSION = 1;
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NOTE = "note";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }



    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTE + " MEDIUMTEXT);") ;
    }



    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }



    public void insertData(String notesString){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NOTE, notesString);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
    }



    public ArrayList dbToList(){
        ArrayList<String> arrayList = new ArrayList<String>();
        String dbString;
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE 1";

        Cursor c = sqLiteDatabase.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()){
            if(c.getString(c.getColumnIndex(COLUMN_NOTE)) != null){
                dbString = c.getString(c.getColumnIndex(COLUMN_NOTE));
                arrayList.add(dbString);
            }
            c.moveToNext();
        }
        sqLiteDatabase.close();
        c.close();
        return arrayList;
    }



    public String dbNoteItemToString(int id){
        id++; //incrementing it since database id starts with 1, not with 0
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_ID + " = " + id;
        Cursor cursor = sqLiteDatabase.rawQuery(query, null);
        if(cursor.moveToFirst()){
            return cursor.getString(cursor.getColumnIndex(COLUMN_NOTE));
        }
        cursor.close();
        return null;
    }



    public void changeNote(int id, String note){
        id++; //incrementing it since database id starts with 1, not with 0
        note = "\'"+note+"\'";
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE " + TABLE_NAME + " SET " + COLUMN_NOTE + " = " + note + " WHERE " + COLUMN_ID + " = " + id);
//        Cursor cursor = db.rawQuery(query, null);
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(COLUMN_NOTE, note);
//        db.update(TABLE_NAME, contentValues, COLUMN_ID, new String[] {String.valueOf(id)});

 //       cursor.close();
    }


    public void deleteItemFromDB(int index){
        SQLiteDatabase database = getWritableDatabase();
        index++;
        String stringId = String.valueOf(index);
        database.delete(TABLE_NAME, COLUMN_ID + "=" + stringId, null);

        long lastID = DatabaseUtils.longForQuery(database, "SELECT MAX(_id) FROM " + TABLE_NAME, null);
        if (lastID > index)
            database.execSQL("UPDATE " + TABLE_NAME + " SET _id = " + index + " WHERE _id = " + lastID);
        if(lastID == 0)
            deleteDB();

    }

    public void deleteDB(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }



}

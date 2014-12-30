package com.example.victor.payback;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Victor on 12/22/2014.
 */
public class DatabaseHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "profileManager",
    TABLE_PROFILES = "debtProfiles",
    KEY_ID = "id",
    KEY_NAME = "name",
    KEY_AMOUNT = "amount",
    KEY_DEBT = "debt",
    KEY_PHONE = "phone",
    KEY_DESCRIPTION = "description"
    ;

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_PROFILES + "( " + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+ KEY_NAME + " TEXT, "+ KEY_AMOUNT + " DOUBLE, "+ KEY_DEBT + " TEXT, " + KEY_PHONE + " TEXT, "+ KEY_DESCRIPTION + " TEXT )");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_PROFILES);

        onCreate(db);

    }

    public void createDebtProfile(DebtProfile profile){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, profile.getName());
        values.put(KEY_AMOUNT, profile.getAmount());
        values.put(KEY_DEBT, profile.getDebtType());
        values.put(KEY_PHONE, profile.getPhone());
        values.put(KEY_DESCRIPTION, profile.getDescription());

        db.insert(TABLE_PROFILES, null, values);
        db.close();

    }

    public DebtProfile getDebtProfile(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_PROFILES, new String[]{KEY_ID, KEY_NAME, KEY_AMOUNT, KEY_DEBT, KEY_PHONE, KEY_DESCRIPTION }, KEY_ID + "=? ", new String[]{ String.valueOf(id)},null,null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        DebtProfile profile = new DebtProfile(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5));
        return profile;

    }

    public void deleteDebtProfile(DebtProfile profile){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_PROFILES, KEY_ID + "=?", new String[]{String.valueOf(profile.getID())});
        db.close();

    }

    public int getProfileCount() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_PROFILES, null);
        int count = cursor.getCount();
        db.close();
        cursor.close();
        return count;

    }

    public int updateDebtProfiles(DebtProfile profile){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, profile.getName());
        values.put(KEY_AMOUNT, profile.getAmount());
        values.put(KEY_DEBT, profile.getDebtType());
        values.put(KEY_PHONE, profile.getPhone());
        values.put(KEY_DESCRIPTION, profile.getDescription());
        int rowsAffected = db.update(TABLE_PROFILES, values, KEY_ID + "=?", new String[] { String.valueOf(profile.getID())});
        db.close();
        return rowsAffected;
    }

    public List<DebtProfile> getAllProfiles(){
        List<DebtProfile> profiles = new ArrayList<DebtProfile>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_PROFILES, null);

        if(cursor.moveToFirst()){
            do {
                profiles.add(new DebtProfile(Integer.parseInt(cursor.getString(0)), cursor.getString(1), Double.parseDouble(cursor.getString(2)), cursor.getString(3), cursor.getString(4), cursor.getString(5)));
            }
            while (cursor.moveToNext());

        }
        cursor.close();
        db.close();
        return profiles;

    }

}

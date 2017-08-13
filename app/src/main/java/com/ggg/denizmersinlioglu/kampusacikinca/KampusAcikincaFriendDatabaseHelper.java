package com.ggg.denizmersinlioglu.kampusacikinca;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by DenizMersinlioglu on 12/08/2017.
 */

public class KampusAcikincaFriendDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "starbuzz"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    KampusAcikincaFriendDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updateMyDatabase(db, 0, DB_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 1) {
            db.execSQL("CREATE TABLE FRIENDS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "EMAIL TEXT, ");
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE FRIENDS ADD COLUMN FAVORITE NUMERIC;");
        }
    }

    private static void insertUser(SQLiteDatabase db, String name,
                                    String email) {
        ContentValues friendValues = new ContentValues();
        friendValues.put("NAME", name);
        friendValues.put("DESCRIPTION", email);
        db.insert("FRIENDS", null, friendValues);
    }
}

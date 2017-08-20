package com.ggg.denizmersinlioglu.kampusacikinca;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by DenizMersinlioglu on 16/08/2017.
 */

public class KampusAcikincaUserDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "myUser"; // the name of our database
    private static final int DB_VERSION = 2; // the version of the database

    KampusAcikincaUserDatabaseHelper(Context context) {
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
            db.execSQL("CREATE TABLE MY_USER (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "NAME TEXT, "
                    + "EMAIL TEXT, "
                    + "CAMPUS TEXT,"
                    + "DESCRIPTION TEXT,"
                    + "IMAGE_RESOURCE_ID INTEGER);");
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        insertUser(db,user.getDisplayName(),user.getEmail(),"-","-",R.drawable.burgerking);

    }

    private static void insertUser(SQLiteDatabase db, String name,
                                   String email, String campus, String description, int resourceId) {
        ContentValues userValues = new ContentValues();
        userValues.put("NAME", name);
        userValues.put("EMAIL", email);
        userValues.put("CAMPUS", campus);
        userValues.put("DESCRIPTION", description);
        userValues.put("IMAGE_RESOURCE_ID", resourceId);
        db.insert("MY_USER", null, userValues);
    }
}

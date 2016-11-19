package com.example.kringlan.sololev.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.kringlan.sololev.model.User;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DB_HELPER";

    private static final String USER_TABLE = "users";
    private static final String USER_USERNAME = "username";
    private static final String USER_PASSWORD = "password";

    private static final int USER_USERNAME_COL = 1;
    private static final int USER_PASSWORD_COL = 2;

    private static final String CREATE_USERS_TABLE = "CREATE TABLE " + USER_TABLE +
            " (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            USER_USERNAME + " VARCHAR(25) NOT NULL," +
            USER_PASSWORD + " VARCHAR(25) NOT NULL" +
            ");";

    private static final String ORDER_TABLE = "orders";
    private static final String ORDER_ID = "_id";
    private static final String ORDER_DATE = "order_date";
    private static final String ORDER_CUSTOMER = "costumer";
    private static final String ORDER_ISDELIVERED = "is_delivered";
    private static final String ORDER_DELIVEREDDATE = "delivered_date";
    private static final String ORDER_DELIVEREDLONG = "delivered_long";
    private static final String ORDER_DELIVEREDLAT = "delivered_lat";

    private static final int ORDER_ID_COL = 0;
    private static final int ORDER_DATE_COL = 1;
    private static final int ORDER_CUSTOMER_COL = 2;
    private static final int ORDER_ISDELIVERED_COL = 3;
    private static final int ORDER_DELIVEREDDATE_COL = 4;
    private static final int ORDER_DELIVEREDLONG_COL = 5;
    private static final int ORDER_DELIVEREDLAT_COL = 6;

    private static final String CREATE_ORDERS_TABLE = "CREATE TABLE " + ORDER_TABLE + " (" +
            ORDER_ID + " INTEGER PRIMARY KEY," +
            ORDER_DATE + " REAL NOT NULL," +
            ORDER_CUSTOMER + " INTEGER NOT NULL," +
            ORDER_ISDELIVERED + " NUMERIC NOT NULL," +
            ORDER_DELIVEREDDATE + " REAL NOT NULL," +
            ORDER_DELIVEREDLONG + " REAL NOT NULL," +
            ORDER_DELIVEREDLAT + " REAL NOT NULL," +
            ");";


    public DBHelper(Context context) {
        super(context, "LevAppDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addUser(String username, String password) {
        if(findUser(username) == null) {
            SQLiteDatabase db = getWritableDatabase();

            ContentValues cvs = new ContentValues();
            cvs.put(USER_USERNAME, username);
            cvs.put(USER_PASSWORD, password);

            long id = db.insert(USER_TABLE, null, cvs);
            Log.d(TAG, "Inserted new user: " + id);

            db.close();
            return true;
        } else return false;
    }

    public User findUser(String name) {
        SQLiteDatabase db = getReadableDatabase();

        String selection = USER_USERNAME + " =?";
        String[] selectionArgs = {name};

        Cursor c = db.query(USER_TABLE, null, selection, selectionArgs, null, null, null, null);

        User user;

        c.moveToFirst();

        if(c.getCount() > 0) {
            user = new User(c.getString(USER_USERNAME_COL), c.getString(USER_PASSWORD_COL));
        } else {
            user = null;
        }

        db.close();
        c.close();
        return user;
    }
}

package com.example.kringlan.sololev.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.example.kringlan.sololev.model.Customer;
import com.example.kringlan.sololev.model.Order;
import com.example.kringlan.sololev.model.User;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = "DB_HELPER";

    // -----------------------------------------------------------------------------------------

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

    // -----------------------------------------------------------------------------------------

    private static final String CUSTOMER_TABLE = "customers";

    private static final String CUSTOMER_ID = "_id";
    private static final String CUSTOMER_NAME = "name";
    private static final String CUSTOMER_PHONE = "phone";
    private static final String CUSTOMER_ADDRESS = "address";
    private static final String CUSTOMER_CREATED = "created";

    private static final int CUSTOMER_ID_COL = 0;
    private static final int CUSTOMER_NAME_COL = 1;
    private static final int CUSTOMER_PHONE_COL = 2;
    private static final int CUSTOMER_ADDRESS_COL = 3;
    private static final int CUSTOMER_CREATED_COL = 4;

    private static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE " + CUSTOMER_TABLE + " (" +
            CUSTOMER_ID + " INTEGER PRIMARY KEY," +
            CUSTOMER_NAME + " VARCHAR(25) NOT NULL," +
            CUSTOMER_PHONE + " VARCHAR(15) NOT NULL," +
            CUSTOMER_ADDRESS + " VARCHAR(50) NOT NULL," +
            CUSTOMER_CREATED + " REAL NOT NULL" +
            ");";

    // -----------------------------------------------------------------------------------------

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
            ORDER_DELIVEREDDATE + " REAL," +
            ORDER_DELIVEREDLONG + " REAL," +
            ORDER_DELIVEREDLAT + " REAL" +
            ");";

    // -----------------------------------------------------------------------------------------

    public DBHelper(Context context) {
        super(context, "LevAppDB", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_ORDERS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public boolean addUser(String username, String password) {
        if(findUser(username) == null) {
            ContentValues cvs = new ContentValues();

            cvs.put(USER_USERNAME, username);
            cvs.put(USER_PASSWORD, password);

            writeToDB(USER_TABLE, cvs);

            return true;
        } else return false;
    }

    public void addCustomer(Customer customer) {
        ContentValues cvs = new ContentValues();

        cvs.put(CUSTOMER_ID, customer.getId());
        cvs.put(CUSTOMER_NAME, customer.getName());
        cvs.put(CUSTOMER_PHONE, customer.getPhoneNumber());
        cvs.put(CUSTOMER_ADDRESS, customer.getAddress());
        cvs.put(CUSTOMER_CREATED, customer.getCreatedDate());

        writeToDB(CUSTOMER_TABLE, cvs);
    }

    public void addOrder(Order order) {
        Customer customer = order.getCustomer();

        if(customer != null) {
            if(findCustomer(customer.getId()) == null) {
                addCustomer(customer);
            }
        }
        else {
            customer = new Customer(null, null, null);
        }

        ContentValues cvs = new ContentValues();

        cvs.put(ORDER_ID, order.getOrderID());
        cvs.put(ORDER_DATE, order.getDeliveredDate());
        cvs.put(ORDER_CUSTOMER, customer.getId());
        cvs.put(ORDER_ISDELIVERED, order.isDelivered());
        cvs.put(ORDER_DELIVEREDDATE, order.getDeliveredDate());
        cvs.put(ORDER_DELIVEREDLONG, order.getDeliveredLong());
        cvs.put(ORDER_DELIVEREDLAT, order.getDeliveredLat());

        writeToDB(ORDER_TABLE, cvs);
    }

    public User findUser(String name) {
        String table = USER_TABLE;
        String selection = USER_USERNAME + " =?";
        String[] selectionArgs = {name};

        Cursor c = readFromDB(table, selection, selectionArgs);

        User user;

        c.moveToFirst();

        if(c.getCount() > 0) {
            user = new User(c.getString(USER_USERNAME_COL),
                            c.getString(USER_PASSWORD_COL));
        } else {
            user = null;
        }

        c.close();
        return user;
    }

    public Customer findCustomer(int id) {
        String table = CUSTOMER_TABLE;
        String selection = CUSTOMER_ID + " =?";
        String[] selectionArgs = {String.valueOf(id)};

        Cursor c = readFromDB(table, selection, selectionArgs);

        c.moveToFirst();
        Customer customer;
        if(c.getCount() > 0) {
            customer = new Customer(c.getInt(CUSTOMER_ID_COL),
                    c.getString(CUSTOMER_NAME_COL),
                    c.getString(CUSTOMER_PHONE_COL),
                    c.getString(CUSTOMER_ADDRESS_COL),
                    c.getLong(CUSTOMER_CREATED_COL));
        } else {
            customer = null;
        }

        c.close();
        return customer;
    }

    public Order[] getUndeliveredOrders() {
        String table = ORDER_TABLE;
        String selection = ORDER_ISDELIVERED + " =?";
        String[] selectionArgs = {"0"};

        Cursor c = readFromDB(table, selection, selectionArgs);

        Order[] orders = new Order[c.getCount()];

        if(c.moveToFirst()) {
            do {
                orders[c.getPosition()] = new Order(c.getInt(ORDER_ID_COL),
                                                    c.getLong(ORDER_DATE_COL),
                                                    findCustomer(c.getInt(ORDER_CUSTOMER_COL)),
                                                    c.getInt(ORDER_ISDELIVERED_COL) == 1,
                                                    c.getLong(ORDER_DELIVEREDDATE_COL),
                                                    c.getLong(ORDER_DELIVEREDLONG_COL),
                                                    c.getLong(ORDER_DELIVEREDLAT_COL));
            } while (c.moveToNext());
        }

        c.close();
        return orders;
    }

    private void writeToDB(String table, ContentValues cvs) {
        SQLiteDatabase writableDatabase = getWritableDatabase();
        long id = writableDatabase.insert(table, null, cvs);
        Log.d(TAG, "Inserted new row in " + table + ": " + id);
        writableDatabase.close();
    }

    private Cursor readFromDB(String table, String selection, String[] selectionArgs) {
        SQLiteDatabase readableDatabase = getReadableDatabase();
        Cursor c = readableDatabase.query(table, null, selection, selectionArgs, null, null, null);
        return c;
    }
}

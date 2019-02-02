package com.bitehunter.bitehunter.Others;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bitehunter.bitehunter.Model.Meal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Jevin on 15-Jan-18.
 */

public class LocalDatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "bitehunter";

    // Contacts table name
    private static final String TABLE_MENU = CommonTags.TAG_SELECTED_MENU;

    // Contacts Table Columns names
    private static final String KEY_ID = CommonTags.TAG_MEAL_ITEM_ID;
    private static final String KEY_NAME = CommonTags.TAG_MEAL_NAME;
    private static final String KEY_PRICE = CommonTags.TAG_MEAL_PRICE;
    private static final String KEY_IMAGE = CommonTags.TAG_MEAL_IMAGE;
    private static final String KEY_QUANTITY = CommonTags.TAG_MEAL_QUANTITY;


    public LocalDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_SELECTED_MENU_TABLE = "CREATE TABLE " + TABLE_MENU + "("
                + KEY_ID + " TEXT PRIMARY KEY,"
                + KEY_NAME + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_QUANTITY + " INTEGER,"
                + KEY_PRICE + " TEXT" + ")";

        db.execSQL(CREATE_SELECTED_MENU_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MENU);

        // Create tables again
        onCreate(db);
    }

    // Adding new meal
    public void addMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID, meal.getItemID()); // meal id
        values.put(KEY_NAME, meal.getName()); // meal Name
        values.put(KEY_IMAGE, meal.getImage()); // meal image
        values.put(KEY_PRICE, meal.getPrice()); // meal price
        values.put(KEY_QUANTITY, 1); // meal quantity

        // Inserting Row
        db.insert(TABLE_MENU, null, values);
        db.close(); // Closing database connection
    }


    // Getting All meals
    public ArrayList<Meal> getAllMeals() {

        ArrayList<Meal> mealList = new ArrayList<Meal>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Meal meal = new Meal();
                meal.setItemID(cursor.getString(0));
                meal.setName(cursor.getString(1));
                meal.setImage(cursor.getString(2));
                meal.setPrice(cursor.getString(4));

                // Adding contact to list
                mealList.add(meal);
            } while (cursor.moveToNext());
        }

        // return contact list
        return mealList;
    }


    // Deleting single meal
    public void deleteMeal(Meal meal) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_MENU, KEY_ID + " = ?",
                new String[]{String.valueOf(meal.getItemID())});
        db.close();
    }


/*
    // Getting All meals
    public List<String> getAllMealIds() {

        List<String> mealIdList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_ID + " FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
               String mealId = cursor.getString(0);

                // Adding meal to list
                mealIdList.add(mealId);
            } while (cursor.moveToNext());
        }

        // return contact list
        return mealIdList;
    }

    // Getting All meals
    public List<String> getAllMealNames() {

        List<String> mealNameList = new ArrayList<String>();
        // Select All Query
        String selectQuery = "SELECT " + KEY_NAME + " FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String mealName = cursor.getString(1);

                // Adding meal to list
                mealNameList.add(mealName);
            } while (cursor.moveToNext());
        }

        // return meal list
        return mealNameList;
    }*/


    // Getting All meals
    public HashMap<String, String> getAllMealIdAndName() {

        HashMap<String, String> mealIdList = new HashMap<String, String>();
        // Select All Query
        String selectQuery = "SELECT " +
                KEY_ID + " , "+
                KEY_NAME +
                " FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String mealId = cursor.getString(0);
                String mealName = cursor.getString(1);

                // Adding meal to list
                mealIdList.put(mealId, mealName);
            } while (cursor.moveToNext());
        }

        // return contact list
        return mealIdList;
    }

    // Getting All meals
    public HashMap<String, Double> getAllMealIdAndPrice() {

        HashMap<String, Double> mealNameList = new HashMap<String, Double>();
        // Select All Query
        String selectQuery = "SELECT " +
                KEY_ID + " , " +
                KEY_PRICE +
                " FROM " + TABLE_MENU;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                String mealId = cursor.getString(0);
                Double mealPrice = Double.parseDouble(cursor.getString(1));
                //String mealPrice = cursor.getString(1);

                // Adding meal to list
                mealNameList.put(mealId, mealPrice);
            } while (cursor.moveToNext());
        }

        // return meal list
        return mealNameList;
    }
}
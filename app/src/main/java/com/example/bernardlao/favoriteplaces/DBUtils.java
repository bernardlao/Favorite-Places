package com.example.bernardlao.favoriteplaces;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Bernard Lao on 10/20/2017.
 */

public class DBUtils extends SQLiteOpenHelper {
    private static int version = 16844060;
    private static String dbName = "placesToGo";

    private String tblUser = "tblUser";
    private String userID = "userID";
    private String fullname = "fullname";
    private String username = "username";
    private String password = "password";

    private String tblLocation = "tblLocation";
    private String locationID = "locationID";
    private String locUserID = "userID";
    private String placeName = "placeName";
    private String rate = "rate";
    private String lati = "lati";
    private String longi = "longi";
    private String addressName = "addressName";
    private String type = "type";

    private Context context;
    SQLiteDatabase db;

    public DBUtils(Context appContext) {
        super(appContext, dbName, null, version);
        this.context = appContext;
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String user = "CREATE TABLE " + tblUser + " ( " +
                userID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                fullname + " TEXT," +
                username + " TEXT," +
                password + " TEXT);";
        sqLiteDatabase.execSQL(user);
        String location = "CREATE TABLE " + tblLocation + " ( " +
                locationID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                locUserID + " INTEGER," +
                placeName + " TEXT," +
                rate + " DOUBLE," +
                lati + " DOUBLE," +
                longi + " DOUBLE," +
                addressName + " TEXT," +
                type + " TEXT);";
        sqLiteDatabase.execSQL(location);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        String query = "DROP TABLE " + tblUser;
        sqLiteDatabase.execSQL(query);
        query = "DROP TABLE " + tblLocation;
        sqLiteDatabase.execSQL(query);
        onCreate(sqLiteDatabase);
    }
    public void openConnection() {
        db = this.getWritableDatabase();
    }

    public void closeConnection() {
        this.close();
    }

    public boolean isUserExist(String uname){
        openConnection();
        String query = "SELECT * FROM " + tblUser + " WHERE " + username + "='" + uname + "'";
        Cursor c = db.rawQuery(query,null);
        boolean retVal = false;
        if(c.moveToFirst())
            retVal = true;
        else
            retVal = false;
        closeConnection();
        return retVal;
    }
    public boolean isLocationExist(String pName){
        openConnection();
        String query = "SELECT * FROM " + tblLocation + " WHERE " + placeName + "='" + pName + "' AND " +
                locUserID + "=" + LoginActivity.loggedInUser;
        Cursor c = db.rawQuery(query,null);
        boolean retVal = false;
        if(c.moveToFirst())
            retVal = true;
        else
            retVal = false;
        closeConnection();
        return retVal;
    }
    public boolean isLocationExistOnUpdate(String pName, String locID){
        openConnection();
        String query = "SELECT * FROM " + tblLocation + " WHERE " + placeName + "='" + pName + "' AND " +
                locUserID + "=" + LoginActivity.loggedInUser + " AND " + locationID + "!=" + locID;
        Cursor c = db.rawQuery(query,null);
        boolean retVal = false;
        if(c.moveToFirst())
            retVal = true;
        else
            retVal = false;
        closeConnection();
        return retVal;
    }
    public long registerUser(String fname, String uname, String pass){
        openConnection();
        ContentValues values = new ContentValues();
        values.put(fullname,fname);
        values.put(username,uname);
        values.put(password,pass);
        long success = db.insert(tblUser,null,values);
        closeConnection();
        return success;
    }
    public long addLocation(String uID,String pName, double star, double la, double lo, String addName, String type){
        openConnection();
        ContentValues values = new ContentValues();
        values.put(locUserID,uID);
        values.put(placeName,pName);
        values.put(rate,star);
        values.put(lati,la);
        values.put(longi,lo);
        values.put(addressName,addName);
        values.put(this.type,type);
        long success = db.insert(tblLocation,null,values);
        closeConnection();
        return success;
    }
    public long updateLocation(int locID,String pName, double star, double la, double lo, String addName, String type){
        openConnection();
        ContentValues values = new ContentValues();
        values.put(placeName,pName);
        values.put(rate,star);
        values.put(lati,la);
        values.put(longi,lo);
        values.put(addressName,addName);
        values.put(this.type,type);
        long success = db.update(tblLocation,values,locationID + "=?",new String[]{String.valueOf(locID)});
        closeConnection();
        return success;
    }
    public long deleteLocation(int locID){
        openConnection();
        long success = db.delete(tblLocation,locationID+"=?",new String[]{String.valueOf(locID)});
        closeConnection();
        return success;
    }
    public String getUserIDIfExist(String uname, String pass){
        openConnection();
        String uID ="";
        String query = "SELECT * FROM " + tblUser + " WHERE " + username + "='" + uname + "' LIMIT 1";
        Cursor c = db.rawQuery(query,null);
        if(c.moveToFirst()){
            String oUname = c.getString(c.getColumnIndex(username));
            String oPass = c.getString(c.getColumnIndex(password));
            if(oUname.equals(uname) && oPass.equals(pass)){
                uID = c.getString(c.getColumnIndex(userID));
            }
        }
        closeConnection();
        return uID;
    }
    public Cursor selectCursor(String query){
        openConnection();
        Cursor c = db.rawQuery(query,null);
        return c;
    }
}

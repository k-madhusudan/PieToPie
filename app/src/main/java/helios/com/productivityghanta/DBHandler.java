package helios.com.productivityghanta;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ProdGhanta";
    private static final int DATABASE_VERSION = 3;
    private static String TABLE_ACT = "TimeActivity";
    private static String TABLE_GOAL = "Goal";
    private static String TABLE_LIST = "ActivityList";
    String ALTER_GOAL = "ALTER TABLE `Goal` ADD COLUMN timeToTake INTEGER DEFAULT 0";
    String CREATE_GOAL = "CREATE TABLE `Goal` (ID INTEGER PRIMARY KEY , `GoalName`\tTEXT,`Activity`\tTEXT,`FromDate`\tTEXT,`ToDate`\tTEXT,`isCurrent`\tTEXT)";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE `TimeActivity` (ID INTEGER PRIMARY KEY,  `Activity`  TEXT,  `Minutes`  INTEGER,  `DateOfAct`  TEXT,  `Productive`  INTEGER)");
        db.execSQL("CREATE TABLE ActivityList (ID INTEGER PRIMARY KEY, Name TEXT, isProductive TEXT) ");
        db.execSQL(this.CREATE_GOAL);
        db.execSQL(this.ALTER_GOAL);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {
            case 1:
               // db.execSQL(this.CREATE_GOAL);
                break;
            case 2:
                break;
            default:
                return;
        }
        //db.execSQL(this.ALTER_GOAL);
    }

    public void insertTrackedActivity(String activity, String minutes, String date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Activity", activity);
        cv.put("Minutes", minutes);
        cv.put("DateOfAct", date);
        db.insert(TABLE_ACT, null, cv);
        db.close();
    }

    public List<String> getAllRecords() {
        String query = "Select  * FROM " + TABLE_ACT;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        List activities = new ArrayList();
        if (c.moveToFirst()) {
            do {
                activities.add(c.getString(1) + "\n" + c.getString(2) + "\n" + c.getString(3));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }

    public List<String> getAllRecords(String fromDate, String toDate) {
        String query = "Select  * FROM " + TABLE_ACT + " where DateOfAct >= Date('" + fromDate + "') and DateOfAct<  Date('" + toDate + "')";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        List activities = new ArrayList();
        if (c.moveToFirst()) {
            do {
                activities.add(c.getString(1) + "\n" + c.getString(2) + "\n" + c.getString(3));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }

    public List<String> getAllRecords(String currentDate) {
        String query = "Select  * FROM " + TABLE_ACT + " where DateOfAct= '" + currentDate + "'";
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        List activities = new ArrayList();
        if (c.moveToFirst()) {
            do {
                activities.add(c.getString(1) + "\n" + c.getString(2) + "\n" + c.getString(3));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }

    public List<String> export() {
        String query = "Select  * FROM " + TABLE_ACT;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        List activities = new ArrayList();
        if (c.moveToFirst()) {
            do {
                activities.add(c.getString(1) + "," + c.getString(2) + "," + c.getString(3) + "\n");
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }

    public void insertActivityType(String activityName, String isProductive) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", activityName);
        cv.put("isProductive", isProductive);
        db.insert(TABLE_LIST, null, cv);
        db.close();
    }

    public List getAllActivityTypes() {
        String query = "Select  * FROM " + TABLE_LIST;
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery(query, null);
        List activities = new ArrayList();
        if (c.moveToFirst()) {
            do {
                activities.add(c.getString(1));
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return activities;
    }

    public boolean isMinutesGreaterForADay(String minutes, String date) {
        int sum;
        Cursor c = getReadableDatabase().rawQuery("select sum(minutes) from " + TABLE_ACT + " where DateOfAct ='" + date + "';", null);
        String actData = "0";
        if (c.moveToFirst()) {
            do {
                actData = c.getString(0);
            } while (c.moveToNext());
        }
        if (actData == null || Integer.parseInt(actData) == 0) {
            sum = 0 + Integer.parseInt(minutes);
            Log.e("values2", "jhgfd");
        } else {
            sum = Integer.parseInt(actData) + Integer.parseInt(minutes);
            Log.e("values3", "dgjkujl");
        }
        if (actData != null) {
            Log.e("values", String.valueOf(sum));
        }
        if (sum <= 1440) {
            return false;
        }
        Log.e("values4", String.valueOf(sum));
        return true;
    }

    public void addNewGoal(String goalName, String activityName, String minutes, String fromDate, String toDate, String isCurrent) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("GoalName", goalName);
        cv.put("Activity", activityName);
        cv.put("timeToTake", minutes);
        cv.put("FromDate", fromDate);
        cv.put("ToDate", toDate);
        cv.put("isCurrent", isCurrent);
        db.insert(TABLE_GOAL, null, cv);
        db.close();
    }

    public Cursor getCurrentGoals() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return getReadableDatabase().rawQuery("Select rowid _id,* FROM " + TABLE_GOAL + " where FromDate <= Date('" + today + "') and ToDate >= DATE('" + today + "')", null);
    }

    public Cursor getArchivedGoals() {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        return getReadableDatabase().rawQuery("Select rowid _id,* FROM " + TABLE_GOAL + " where FromDate< Date('" + today + "') and ToDate < DATE('" + today + "')", null);
    }

    public boolean deleteCurrentGoal(long id) {
        String today = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
        String query = "delete FROM " + TABLE_GOAL + " where ID=" + id;
        SQLiteDatabase db = getWritableDatabase();
        Log.e("querydelete", query);
        db.execSQL(query);
        return true;
    }

    public boolean deleteActivity(String activity, String mins) {
        Calendar cal = Calendar.getInstance();
        String query = "delete FROM " + TABLE_ACT + " where Activity='" + activity + "' and Minutes=" + mins;
        SQLiteDatabase db = getWritableDatabase();
        Log.e("querydelete", query);
        db.execSQL(query);
        return true;
    }

    public GoalBean getGoalDate(long id) {
        Cursor c = getReadableDatabase().rawQuery("select * FROM " + TABLE_GOAL + " where ID=" + id, null);
        GoalBean bean = new GoalBean();
        if (c.moveToFirst()) {
            do {
                bean.setGoalName(c.getString(1));
                bean.setActivityName(c.getString(2));
                bean.setFrom(c.getString(3));
                bean.setTo(c.getString(4));
                bean.setMinutes(c.getString(6));
            } while (c.moveToNext());
        }
        return bean;
    }

    public String getGoalDataByTime(String activity, String fromDate, String toDate) {
        Cursor c = getReadableDatabase().rawQuery("Select  sum(minutes) as total_sum FROM " + TABLE_ACT + " where DateOfAct >= Date('" + fromDate + "') and DateOfAct<=  Date('" + toDate + "') and Activity='" + activity + "'", null);
        String res = BuildConfigMine.FLAVOR;
        if (c.moveToFirst()) {
            do {
                res = c.getString(0);
            } while (c.moveToNext());
        }
        return res;
    }

    public boolean deleteAllActivity(){
        String query = "delete FROM " + TABLE_ACT;
        SQLiteDatabase db = getWritableDatabase();
        Log.e("querydelete", query);
        db.execSQL(query);
        db.close();
        return true;
    }
}

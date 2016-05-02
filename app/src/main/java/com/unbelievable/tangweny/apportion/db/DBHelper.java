package com.unbelievable.tangweny.apportion.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.unbelievable.tangweny.apportion.entity.Check;
import com.unbelievable.tangweny.apportion.entity.Participant;
import com.unbelievable.tangweny.apportion.util.LogUtil;
import com.unbelievable.tangweny.apportion.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    public static String sqlite = "participant";
    public static int factory = 1;
    private Context mcontext;
    //参与者表名
    public static final String PARTICIPANT = "participant_table";
    public static final String CHECK = "check_table";
    private SQLiteDatabase mDefaultWritableDatabase = null;

    public DBHelper(Context context) {
        super(context, sqlite, null, factory);
        this.mcontext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        this.mDefaultWritableDatabase = db;
        /*创建参与者表,这里在第一次创建时，涉及多线程调用db*/
        createParticipantTable();
        createCheckTable();
    }

    /**
     * 添加合伙人，时间，姓名
     */
    public void insertParticipant(String time, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("parTime", time);
        cv.put("parName", name);
        long result = db.insert(PARTICIPANT, null, cv);
        LogUtil.d("insert result ", result);
    }

    /*
     * 通用查询方法
     */
    public Cursor query(String sql, String[] args) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql, args);
        return cursor;
    }

    public List<Participant> queryParticipants() {

        List<Participant> pars = new ArrayList<>();
        String sql = "select * from " + PARTICIPANT;
        Cursor cursor = query(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getCount();
            if (count == 0)
                return null;
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int parId = cursor.getInt(0);
                String parName = cursor.getString(1);
                String parTime = cursor.getString(2);

                Participant par = new Participant();
                par.setParId(parId);
                par.setParTime(parTime);
                par.setParName(parName);
                pars.add(par);
            }
        }
        return pars;
    }

    public List<Check> queryChecks() {

        List<Check> checks = new ArrayList<>();
        String sql = "select * from " + CHECK;
        Cursor cursor = query(sql, null);
        if (cursor.moveToNext()) {
            int count = cursor.getCount();
            if (count == 0)
                return null;
            for (int i = 0; i < count; i++) {
                cursor.moveToPosition(i);
                int parId = cursor.getInt(0);
                String parName = cursor.getString(1);
                String parTime = cursor.getString(2);
                double consume = cursor.getDouble(3);
                String remark = cursor.getString(4);
                int isCheck = cursor.getInt(5);
                Check check = new Check();
                check.setParId(parId);
                check.setParTime(parTime);
                check.setParName(parName);
                check.setConsume(consume);
                check.setRemark(remark);
                check.setIsCheck(isCheck);
                checks.add(check);
            }
        }
        return checks;
    }


    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.mDefaultWritableDatabase = db;
    }

    /**
     * isCheck 0 表示已结账单，1 表示未结账单
     */
    public void createParticipantTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ;
        StringBuffer sqlname = new StringBuffer();
        sqlname.append("create table if not exists ");
        sqlname.append(" ");
        sqlname.append(PARTICIPANT);
        sqlname.append(
                "(participantId    INTEGER PRIMARY KEY," +
                        "parName   string," +
                        "parTime   string);");
        String b = sqlname.toString();
        try {
            db.execSQL(b);
            LogUtil.e("创建参与者表", "" + PARTICIPANT);
        } catch (Exception e) {
            LogUtil.e("创建参与者表", "" + e.getMessage());
        }
    }

    public void createCheckTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ;
        StringBuffer sqlname = new StringBuffer();
        sqlname.append("create table if not exists ");
        sqlname.append(" ");
        sqlname.append(CHECK);
        sqlname.append(
                "(checkId    INTEGER PRIMARY KEY," +
                        "parName   string," +
                        "parTime   string," +
                        "consume   double," +
                        "remark   string," +
                        "isCheck    INTEGER);");
        String b = sqlname.toString();
        try {
            db.execSQL(b);
            LogUtil.e("创建账单表", "" + CHECK);
        } catch (Exception e) {
            LogUtil.e("创建账单表", "" + e.getMessage());
        }
    }

    /*
    批量更新数据，开启事务
     */
    public void bulkInsert(List<Check> checks) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        for (Check val : checks) {
            ContentValues cv = new ContentValues();
            cv.put("consume", val.getConsume());
            cv.put("parName", val.getParName());
            cv.put("parTime", TimeUtil.getDateTime(System.currentTimeMillis()));
            cv.put("isCheck", val.getIsCheck());
            cv.put("remark", val.getRemark());

            long rowId = db.insert(CHECK, null, cv);
            LogUtil.d("check info :", val.getParName() + ":" + val.getConsume() + ":" + val.getIsCheck());
            LogUtil.d("effectRow :", rowId + "");
        }
        db.setTransactionSuccessful();
        db.endTransaction();
//        db.close();
    }

    public void bulkInsert2(List<Check> checks) {
        String sql = "insert into " + CHECK + " (parTime,parName,consume,isCheck,remark) values(?,?,?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(sql);
        db.beginTransaction();
        for (Check val : checks) {
            statement.bindString(1, TimeUtil.getDateTime(System.currentTimeMillis()));
            statement.bindString(2, val.getParName());
            statement.bindDouble(3, val.getConsume());
            statement.bindLong(4, val.getIsCheck());
            statement.bindString(5, val.getRemark());
            statement.execute();
        }
        db.setTransactionSuccessful();
        ;
        db.endTransaction();
//        db.close();
    }

    public void deleteTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        ;
        try {
            db.execSQL("drop table " + PARTICIPANT);
        } catch (Exception e) {
            LogUtil.e("更改表数据 ", e.getMessage());
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        final SQLiteDatabase db;
        if (mDefaultWritableDatabase != null) {
            db = mDefaultWritableDatabase;
        } else {
            db = super.getWritableDatabase();
        }
        return db;
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        this.mDefaultWritableDatabase = db;
    }
}

package com.unbelievable.tangweny.apportion.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.unbelievable.tangweny.apportion.util.LogUtil;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {
	public static String sqlite = "participant";
	public static int factory = 1;
	private Context mcontext;
	//参与者表名
	public static final String PARTICIPANT = "participant_table";
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
	}

	/**
	 * 插入合伙人，时间，姓名，账单，是否结账
	 */
	public void insertTalbe(String time, String name, double consume,
			int isCheck) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put("parTime", time);
		cv.put("parName", name);
		cv.put("consume", consume);
		cv.put("isCheck", isCheck);
		long result = db.insert(PARTICIPANT, null, cv);
		LogUtil.d("insert result ",result);
	}
	/*
	 * 通用查询方法
	 */
	public Cursor query(String sql, String[] args) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, args);
		return cursor;
	}


	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		this.mDefaultWritableDatabase = db;
	}

	public void createParticipantTable() {
		SQLiteDatabase db = this.getWritableDatabase();;
		StringBuffer sqlname = new StringBuffer();
		sqlname.append("create table if not exists ");
		sqlname.append(" ");
		sqlname.append(PARTICIPANT);
		sqlname.append(
				"(participantId    INTEGER PRIMARY KEY," +
						"parName   string," +
						"parTime   string," +
						"consume   double," +
						"isCheck    INTEGER);");
		String b = sqlname.toString();
		try {
			db.execSQL(b);
			LogUtil.e("创建参与者表", "" + PARTICIPANT);
		} catch (Exception e) {
			LogUtil.e("创建参与者表", "" + e.getMessage());
		}
	}
	/*
	批量插入数据，开启事务
	 */
	public void bulkInsert(String tableName,
						   ArrayList<ContentValues> valuesArr){
		SQLiteDatabase db = this.getWritableDatabase();;
		db.beginTransaction();
		for (ContentValues val : valuesArr){
			db.insert(tableName,null,val);
		}
		db.setTransactionSuccessful();
		db.endTransaction();
		db.close();
	}

	public void deleteTable() {
		SQLiteDatabase db = this.getWritableDatabase();;
		try {
			db.execSQL("drop table " + PARTICIPANT);
		} catch (Exception e) {
			LogUtil.e("更改表数据 ", e.getMessage());
		}
	}

	@Override
	public SQLiteDatabase getWritableDatabase() {
		final SQLiteDatabase db;
		if(mDefaultWritableDatabase != null){
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

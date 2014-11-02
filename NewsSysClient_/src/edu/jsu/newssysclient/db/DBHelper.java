package edu.jsu.newssysclient.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 数据库Helper，用于创建数据库
 * @author zuo
 *
 */
public class DBHelper extends SQLiteOpenHelper{
	public static final String DBNAME = "NewsSysDB.db";

	//需要context参数和数据库路径
	public DBHelper(Context context) {
		super(context, DBNAME, null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String tabNewsItems = "create table NewsItems(id varchar(50) not null,title varchar(50) not null,imageurl varchar(300),descr varchar(100) not null)";
		db.execSQL(tabNewsItems);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

}

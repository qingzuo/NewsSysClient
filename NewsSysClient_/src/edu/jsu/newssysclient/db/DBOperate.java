package edu.jsu.newssysclient.db;

import java.util.ArrayList;
import java.util.List;

import edu.jsu.newssysclient.MyApplication;
import edu.jsu.newssysclient.bean.Item;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class DBOperate {
	
	/**
	 * 获取所有缓存的数据
	 * @param max
	 * @param index
	 * @return
	 */
	public static List<Item> getNewsDescrList(int max, int index){
		List<Item> list = new ArrayList<Item>();

		DBHelper helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from NewsItems", null);

		while (cursor.moveToNext()) {
			Item n = new Item();

			String id = cursor.getString(cursor.getColumnIndex("id"));
			String title = cursor.getString(cursor.getColumnIndex("title"));
			String imageurl = cursor.getString(cursor.getColumnIndex("imageurl"));
			String descr = cursor.getString(cursor.getColumnIndex("descr"));
			

			n.setId(id);
			n.setTitle(title);
			n.setImageurl(imageurl);
			n.setDescr(descr);

			list.add(n);
		}

		cursor.close();
		db.close();
		
		return list;
	}
	
	/**
	 * 插入一条新闻简介
	 * @param id
	 * @param title
	 * @param imageurl
	 * @param descr
	 */
	public static void insertNewsDescr(String id, String title, String imageurl, String descr){
		DBHelper helper = new DBHelper(MyApplication.getInstance());
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL("insert into NewsItems values('"+id+"', '"+title+"', '"+imageurl+"', '"+descr+"')");
	}
	
	/**
	 * 插入多条新闻简介
	 * @param context
	 * @param list
	 */
	public static void insertNewsDescrList(List<Item> list){
		for (Item n : list) {
			insertNewsDescr(n.getId(), n.getTitle(), n.getId(), n.getDescr());
		}
	}
	
	/*public static void insertUserInfo2DB(Context context,String name, String pwd, String imageName){
		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		db.execSQL("insert into user_info(name, password, image) values('"+name+"', '"+pwd+"', '"+imageName+"')");
		
	}
	*//**
	 * 根据关键字keyword 得到数据库  分类食物表 的相关内容
	 * 
	 * @param context
	 * @return
	 *//*
	public static ArrayList<FoodInfo> getTypeFoodDataFromDB(Context context, String keyWord) {
		ArrayList<FoodInfo> data = new ArrayList<FoodInfo>();

		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from food_infos as A join (select fid from food_with_type where tid=(select _id from food_types where name= '"+keyWord+"')) as B on A.[_id]=B.fid", null);

		while (cursor.moveToNext()) {
			FoodInfo food = new FoodInfo();

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String iconpath = cursor.getString(cursor
					.getColumnIndex("iconpath"));
			float price = cursor.getFloat(cursor.getColumnIndex("price"));
			String characteristic = cursor.getString(cursor
					.getColumnIndex("characteristic"));
			String source = cursor.getString(cursor.getColumnIndex("source"));
			String brief_introduction = cursor.getString(cursor
					.getColumnIndex("brief_introduction"));
			String detailed_introduction = cursor.getString(cursor
					.getColumnIndex("detailed_introduction"));
			Float start = cursor.getFloat(cursor.getColumnIndex("start"));
			int good_reputation = cursor.getInt(cursor
					.getColumnIndex("good_reputation"));
			int middle_reputation = cursor.getInt(cursor
					.getColumnIndex("middle_reputation"));
			int bad_reputation = cursor.getInt(cursor
					.getColumnIndex("bad_reputation"));
			String making = cursor.getString(cursor.getColumnIndex("making"));
			int need_time = cursor.getInt(cursor.getColumnIndex("need_time"));
			int order_time = cursor.getInt(cursor.getColumnIndex("order_time"));

			food.set_id(id);
			food.setName(name);
			food.setIconPath(iconpath);
			food.setPrice(price);
			food.setCharacteristic(characteristic);
			food.setSource(source);
			food.setBrief_introduction(brief_introduction);
			food.setDetailed_introduction(detailed_introduction);
			food.setStart(start);
			food.setGood_reputation(good_reputation);
			food.setMiddle_reputation(middle_reputation);
			food.setBad_reputation(bad_reputation);
			food.setMaking(making);
			food.setOrder_time(order_time);

			data.add(food);
		}

		cursor.close();
		db.close();
		return data;
	}
	
	*//**
	 * 检查用户名和密码
	 * @param context
	 * @param name
	 * @param password
	 * @return
	 *//*
	public static String checkUserInfo(Context context, String name, String password){
		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		String sql = "select * from user_info where name='"+name+"' and password='"+password+"'";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor.moveToNext()){
			return cursor.getString(cursor.getColumnIndex("image"));	//登陆成功，返回用户图片名字
		}
		
		return null;
	}
	
	*//**
	 * 得到数据库food_infos表的所有内容
	 * @param context
	 * @return
	 *//*
	public static ArrayList<FoodInfo> getDataFromDB(Context context) {
		ArrayList<FoodInfo> data = new ArrayList<FoodInfo>();

		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from food_infos order by start desc", null);

		while (cursor.moveToNext()) {
			FoodInfo food = new FoodInfo();

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String iconpath = cursor.getString(cursor
					.getColumnIndex("iconpath"));
			float price = cursor.getFloat(cursor.getColumnIndex("price"));
			String characteristic = cursor.getString(cursor
					.getColumnIndex("characteristic"));
			String source = cursor.getString(cursor.getColumnIndex("source"));
			String brief_introduction = cursor.getString(cursor
					.getColumnIndex("brief_introduction"));
			String detailed_introduction = cursor.getString(cursor
					.getColumnIndex("detailed_introduction"));
			Float start = cursor.getFloat(cursor.getColumnIndex("start"));
			int good_reputation = cursor.getInt(cursor
					.getColumnIndex("good_reputation"));
			int middle_reputation = cursor.getInt(cursor
					.getColumnIndex("middle_reputation"));
			int bad_reputation = cursor.getInt(cursor
					.getColumnIndex("bad_reputation"));
			String making = cursor.getString(cursor.getColumnIndex("making"));
			int need_time = cursor.getInt(cursor.getColumnIndex("need_time"));
			int order_time = cursor.getInt(cursor.getColumnIndex("order_time"));

			food.set_id(id);
			food.setName(name);
			food.setIconPath(iconpath);
			food.setPrice(price);
			food.setCharacteristic(characteristic);
			food.setSource(source);
			food.setBrief_introduction(brief_introduction);
			food.setDetailed_introduction(detailed_introduction);
			food.setStart(start);
			food.setGood_reputation(good_reputation);
			food.setMiddle_reputation(middle_reputation);
			food.setBad_reputation(bad_reputation);
			food.setMaking(making);
			food.setOrder_time(order_time);

			data.add(food);
		}

		cursor.close();
		db.close();
		return data;
	}

	*//**
	 * 根据关键字keyword 得到数据库food_infos表的相关内容
	 * 
	 * @param context
	 * @return
	 *//*
	public static ArrayList<FoodInfo> getQueryDataFromDB(Context context, String keyWord) {
		ArrayList<FoodInfo> data = new ArrayList<FoodInfo>();

		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery("select * from food_infos where name like '%"+keyWord+"%'", null);

		while (cursor.moveToNext()) {
			FoodInfo food = new FoodInfo();

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String iconpath = cursor.getString(cursor
					.getColumnIndex("iconpath"));
			float price = cursor.getFloat(cursor.getColumnIndex("price"));
			String characteristic = cursor.getString(cursor
					.getColumnIndex("characteristic"));
			String source = cursor.getString(cursor.getColumnIndex("source"));
			String brief_introduction = cursor.getString(cursor
					.getColumnIndex("brief_introduction"));
			String detailed_introduction = cursor.getString(cursor
					.getColumnIndex("detailed_introduction"));
			Float start = cursor.getFloat(cursor.getColumnIndex("start"));
			int good_reputation = cursor.getInt(cursor
					.getColumnIndex("good_reputation"));
			int middle_reputation = cursor.getInt(cursor
					.getColumnIndex("middle_reputation"));
			int bad_reputation = cursor.getInt(cursor
					.getColumnIndex("bad_reputation"));
			String making = cursor.getString(cursor.getColumnIndex("making"));
			int need_time = cursor.getInt(cursor.getColumnIndex("need_time"));
			int order_time = cursor.getInt(cursor.getColumnIndex("order_time"));

			food.set_id(id);
			food.setName(name);
			food.setIconPath(iconpath);
			food.setPrice(price);
			food.setCharacteristic(characteristic);
			food.setSource(source);
			food.setBrief_introduction(brief_introduction);
			food.setDetailed_introduction(detailed_introduction);
			food.setStart(start);
			food.setGood_reputation(good_reputation);
			food.setMiddle_reputation(middle_reputation);
			food.setBad_reputation(bad_reputation);
			food.setMaking(making);
			food.setOrder_time(order_time);

			data.add(food);
		}

		cursor.close();
		db.close();
		return data;
	}

	*//**
	 * 把网络上得到的数据保存到数据库
	 * 
	 * @param context
	 * @param data
	 * @return
	 * @throws Exception 
	 *//*
	public static void insertData2DB(Context context) throws Exception {
		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		
		//清除表之前的数据
		String clearSql = "drop table food_infos";
		String createSql = "create table food_infos(_id integer PRIMARY KEY,name varchar(50),iconpath varchar(50),price float,characteristic varchar(50),source varchar(50),brief_introduction varchar(50),detailed_introduction varchar(100),start float,good_reputation int,middle_reputation int,bad_reputation int,making varchar(50),need_time int,order_time int)";
		//用事务进行清除
		db.beginTransaction();
		try{
			db.execSQL(clearSql);
			db.execSQL(createSql);
			db.setTransactionSuccessful();
		}finally{
			db.endTransaction();
		}
		
		//插入新的数据
		String sql = "insert into food_infos(_id, name, iconpath, price, characteristic, source, brief_introduction, detailed_introduction, start, good_reputation, middle_reputation, bad_reputation, making, need_time, order_time) values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		ArrayList<FoodInfo> data = GetFoodInfoFromXml.getFoodInfos(context, "");
		for (FoodInfo food : data) {
			Object[] bindArgs = {food.get_id(), food.getName(), food.getIconPath(), food.getPrice(), food.getCharacteristic(), food.getSource(), food.getBrief_introduction(), food.getDetailed_introduction(), food.getStart(), food.getGood_reputation(), food.getMiddle_reputation(), food.getBad_reputation(), food.getMaking(), food.getNeed_time(), food.getOrder_time() };
			db.execSQL(sql, bindArgs);
		}
		
		db.close();
	}

	*//**
	 * 得到数据库food_infos表的按照订购次数order_time降序排列后的所有内容
	 * 
	 * @param context
	 * @return
	 *//*
	public static ArrayList<FoodInfo> getSortDataFromDB(Context context) {
		ArrayList<FoodInfo> data = new ArrayList<FoodInfo>();

		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from food_infos order by order_time desc", null);

		while (cursor.moveToNext()) {
			FoodInfo food = new FoodInfo();

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String iconpath = cursor.getString(cursor
					.getColumnIndex("iconpath"));
			float price = cursor.getFloat(cursor.getColumnIndex("price"));
			String characteristic = cursor.getString(cursor
					.getColumnIndex("characteristic"));
			String source = cursor.getString(cursor.getColumnIndex("source"));
			String brief_introduction = cursor.getString(cursor
					.getColumnIndex("brief_introduction"));
			String detailed_introduction = cursor.getString(cursor
					.getColumnIndex("detailed_introduction"));
			Float start = cursor.getFloat(cursor.getColumnIndex("start"));
			int good_reputation = cursor.getInt(cursor
					.getColumnIndex("good_reputation"));
			int middle_reputation = cursor.getInt(cursor
					.getColumnIndex("middle_reputation"));
			int bad_reputation = cursor.getInt(cursor
					.getColumnIndex("bad_reputation"));
			String making = cursor.getString(cursor.getColumnIndex("making"));
			int need_time = cursor.getInt(cursor.getColumnIndex("need_time"));
			int order_time = cursor.getInt(cursor.getColumnIndex("order_time"));

			food.set_id(id);
			food.setName(name);
			food.setIconPath(iconpath);
			food.setPrice(price);
			food.setCharacteristic(characteristic);
			food.setSource(source);
			food.setBrief_introduction(brief_introduction);
			food.setDetailed_introduction(detailed_introduction);
			food.setStart(start);
			food.setGood_reputation(good_reputation);
			food.setMiddle_reputation(middle_reputation);
			food.setBad_reputation(bad_reputation);
			food.setMaking(making);
			food.setOrder_time(order_time);

			data.add(food);
		}

		cursor.close();
		db.close();
		return data;
	}

	*//**
	 * 得到数据库特价的所有内容
	 * @param context
	 * @return
	 *//*
	public static ArrayList<FoodInfo> getSpecialFoodFromDB(Context context) {
		ArrayList<FoodInfo> data = new ArrayList<FoodInfo>();

		String dbPath = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/CampusOrderMeal/db/MyDB.db";
		DBHelper helper = new DBHelper(context, dbPath);
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor cursor = db.rawQuery(
				"select * from food_infos join special_foods on food_infos._id=special_foods._id", null);

		while (cursor.moveToNext()) {
			FoodInfo food = new FoodInfo();

			int id = cursor.getInt(cursor.getColumnIndex("_id"));
			String name = cursor.getString(cursor.getColumnIndex("name"));
			String iconpath = cursor.getString(cursor
					.getColumnIndex("iconpath"));
			float price = cursor.getFloat(cursor.getColumnIndex("price"));
			String characteristic = cursor.getString(cursor
					.getColumnIndex("characteristic"));
			String source = cursor.getString(cursor.getColumnIndex("source"));
			String brief_introduction = cursor.getString(cursor
					.getColumnIndex("brief_introduction"));
			String detailed_introduction = cursor.getString(cursor
					.getColumnIndex("detailed_introduction"));
			Float start = cursor.getFloat(cursor.getColumnIndex("start"));
			int good_reputation = cursor.getInt(cursor
					.getColumnIndex("good_reputation"));
			int middle_reputation = cursor.getInt(cursor
					.getColumnIndex("middle_reputation"));
			int bad_reputation = cursor.getInt(cursor
					.getColumnIndex("bad_reputation"));
			String making = cursor.getString(cursor.getColumnIndex("making"));
			int need_time = cursor.getInt(cursor.getColumnIndex("need_time"));
			int order_time = cursor.getInt(cursor.getColumnIndex("order_time"));
			float special_offer = cursor.getFloat(cursor.getColumnIndex("special_offer"));

			food.set_id(id);
			food.setName(name);
			food.setIconPath(iconpath);
			food.setPrice(price);
			food.setCharacteristic(characteristic);
			food.setSource(source);
			food.setBrief_introduction(brief_introduction);
			food.setDetailed_introduction(detailed_introduction);
			food.setStart(start);
			food.setGood_reputation(good_reputation);
			food.setMiddle_reputation(middle_reputation);
			food.setBad_reputation(bad_reputation);
			food.setMaking(making);
			food.setOrder_time(order_time);
			food.setSpecial_offer(special_offer);

			data.add(food);
		}

		cursor.close();
		db.close();
		return data;
	}*/
}

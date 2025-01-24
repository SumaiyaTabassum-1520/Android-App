package com.example.eventmanagementapp;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class EventDB extends SQLiteOpenHelper {


	private final Context context;

	public  EventDB(Context context) {

		super(context, "EventDB.db", null, 1);
		this.context = context;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		System.out.println("DB@OnCreate");
		String sql = "CREATE TABLE events  ("
				+ "ID TEXT PRIMARY KEY,"
				+ "Title TEXT NOT NULL,"
				+ "Place TEXT NOT NULL,"
				+ "DateTime INTEGER,"
				+ "Capacity INTEGER,"
				+ "Budget REAL,"
				+ "Email TEXT,"
				+ "Phone TEXT,"
				+ "Description TEXT,"
				+"EventType TEXT"
				+ ")";
		db.execSQL(sql);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		System.out.println("Write code to modify database schema here");
		//db.execSQL("");
	}

	public void insertEvent(String ID, String title, String place, long datetime, int capacity, double budget,String email,String phone,String des,String eventType) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("ID", ID);
		cols.put("Title", title);
		cols.put("Place",place);
		cols.put("DateTime",datetime);
		cols.put("Capacity",capacity);
		cols.put("Budget",budget);
		cols.put("Email",email);
		cols.put("Phone",phone);
		cols.put("Description", des);
		cols.put("EventType",eventType);
		long result = db.insert("events", null ,  cols);
		if(result==-1){
			Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(context,"Insert SuccessFull",Toast.LENGTH_SHORT).show();
		}
		db.close();
	}
	public void updateEvent(String ID, String title, String place, long datetime, int capacity, double budget, String email,String phone,String des,String eventType) {
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues cols = new ContentValues();
		cols.put("ID", ID);
		cols.put("Title",title);
		cols.put("Place",place);
		cols.put("DateTime",datetime);
		cols.put("Capacity",capacity);
		cols.put("Budget",budget);
		cols.put("Email",email);
		cols.put("Phone",phone);
		cols.put("description", des);
		cols.put("EventType",eventType);
		db.update("events", cols, "ID=?", new String[ ] {ID} );
		db.close();
	}
	public void deleteEvent(String ID) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete("events", "ID=?", new String[ ] {ID} );
		db.close();
	}
	Cursor readAllData(){
		String q="Select * from events";
		SQLiteDatabase db=this.getReadableDatabase();
		Cursor cursor=null;
		if(db!=null){
			cursor=db.rawQuery(q,null);
		}
		return cursor;
	}
	public Cursor selectEvents(String query) {
		SQLiteDatabase db = this.getWritableDatabase();
		Cursor res = null;
		try {
			res = db.rawQuery(query, null);
		} catch (Exception e){
			e.printStackTrace();
		}
		return res;
	}
}
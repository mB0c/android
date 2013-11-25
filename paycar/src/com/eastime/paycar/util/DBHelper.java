package com.eastime.paycar.util;


import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper{

	private static DatabaseHelper mDbHelper;
	public static SQLiteDatabase mDb;
	//private static SQLiteDatabase mDh;
	private static final String DATABASE_NAME = "gps_sqlite";
	private static final int DATABASE_VERSION = 1;
	private Context mCtx;
	public static DBHelper db;
	
	public static DBHelper getInstance(){
		if(null == db){
			db = new DBHelper();
		}
		
		return db;
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper{
		
		
		
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//�������ṹ
			String gpsCreateSql ="CREATE TABLE "+Contants.table1+" (id integer primary key autoincrement,username varchar,a1 varchar,a2 varchar,a3 varchar,result varchar,writetime varchar,number varchar)";
			String danhaoCreateSql = "CREATE TABLE "+Contants.table2+" (id integer primary key autoincrement,number varchar,imgpath varchar,succinfo varchar,failinfo varchar,isdel integer,issucc integer,isupload integer,paytime varchar,createtime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";
			String jizhanCreateSql = "CREATE TABLE "+Contants.table3+"(id integer primary key autoincrement,username varchar,mcc varchar,mnc varchar,nid varchar,sid varchar,cid varchar,bid varchar,lac varchar,createtime varchar,number varchar)";
/*			if(!isTableExist(Contants.table1)){
				db.execSQL(gpsCreateSql);
			}
			if(!isTableExist(Contants.table2)){
				db.execSQL(danhaoCreateSql);
			}
			if(!isTableExist(Contants.table3)){
				db.execSQL(jizhanCreateSql);
			}*/
			db.execSQL(gpsCreateSql);
			db.execSQL(danhaoCreateSql);
			db.execSQL(jizhanCreateSql);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			
		}
	}
	//��װ��Ĺ���
/*	public DBHelper(Context ctx){
		//this.mCtx = ctx;
		this.mCtx = getApplicationContext();
		
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();  //�ɶ�д
		//mDh = mDbHelper.getReadableDatabase();  //ֻ����
	}*/
	private DBHelper(){
		//this.mCtx = ctx;
		this.mCtx = AppContext.getInstance();
		
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();  //�ɶ�д
		//mDh = mDbHelper.getReadableDatabase();  //ֻ����
	}
	
	//�ر�����Դ
	public void closeConnection(){
		if(mDb != null && mDb.isOpen()){
			mDb.close();
		}
		if(mDbHelper != null ){
			mDbHelper.close();
		}
	}
	
	/**
	 * ��������
	 * @param tableName  ����
	 * @param initValues ��Ҫ����ı�ֵ
	 * @return  �����²�����к� ���ʧ�� ����-1
	 */

	public long insert(String tableName,ContentValues initValues){
		
		return mDb.insert(tableName, null, initValues);
	}
	
	/**
	 * ɾ������
	 * @param tableName ����
	 * @param whereClause ����
	 * @param whereArgs ������Ӧ��ֵ�����deleteCondition���С������ţ�
	 * ���ô������е�ֵ�滻��һһ��Ӧ��
	 * @return 
	 */
	//the number of rows affected if a whereClause is passed in, 0 otherwise. 
	//To remove all rows and get a count pass "1" as the whereClause.
	public boolean delete(String tableName,String whereClause,String[] whereArgs){
		return mDb.delete(tableName, whereClause, whereArgs) > 0;
	}
	
	/**
	 * ��������
	 * @param tableName ����
	 * @param initialValues Ҫ���µ���
	 * @param whereClause ���µ�����
	 * @param whereArgs ���������еġ�������Ӧ��ֵ 
	 * @return
	 */
	public boolean update(String tableName,ContentValues values,String whereClause,String[] whereArgs){
		return mDb.update(tableName, values, whereClause, whereArgs) > 0;
	}
	
	/**
	 * ��ѯ���ݿ� ����һ���б�
	 * @param distinct �Ƿ�ȥ�ظ�
	 * @param tableName ����
	 * @param columns Ҫ���ص���
	 * @param selection ����
	 * @param selectionArgs �����С������Ĳ���ֵ
	 * @param groupBy ����
	 * @param having �����������
	 * @param orderBy ����
	 * @param limit �����õ�
	 * @return 
	 */
	public Cursor findList(boolean distinct, String tableName){
		return findList(distinct, tableName, null, null, null, null, null, null, null);
	}
	public Cursor findList(boolean distinct, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit){
		return mDb.query(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
	}
	
	/**
	 * ��ȡһ������
	 * @param distinct �Ƿ�ȥ�ظ�
	 * @param tableName ����
	 * @param columns Ҫ���ص���
	 * @param selection ����
	 * @param selectionArgs �����С������Ĳ���ֵ
	 * @param groupBy ����
	 * @param having �����������
	 * @param orderBy ����
	 * @param limit �����õ�
	 * @return
	 * @throws SQLException
	 */
	public Cursor findOne(boolean distinct, String tableName, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws SQLException{
		
		Cursor mCursor = findList(distinct, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		if(mCursor != null){
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	/**
	 * ִ�д�������sql���
	 * @param sql  sql��� �������  ?
	 * @param args  �滻 sql�����  ��
	 */
	public void execSQL(String sql,Object[] args){
		mDb.execSQL(sql,args);
	}
	
	/**
	 * ִ��sql���
	 * @param sql
	 */
	public void execSQL(String sql){
		mDb.execSQL(sql);
	}
	
	
	/**
	 * ��ѯ�ñ��Ƿ����
	 * @param tableName
	 * @return
	 */
	public static boolean isTableExist(String tableName){
		
		boolean result = false;
		
		if(tableName == null){
			return result;
		}
		
		try {
			Cursor cursor;
			String sql = "select count(1) from sqlite_master where type='table' and name='"+ tableName.trim() +"'";
			cursor = mDb.rawQuery(sql, null);
			
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				if(count > 0){
					result = true;
				}
			}
			
			cursor.close();
			
		} catch (Exception e) {
			//TODO
			e.printStackTrace();
		}

		return result;
		
	}
	
	
	/**
	 * ��ѯ�����Ƿ���ĳ�ֶ�
	 * @param tableName
	 * @param columnName  �ֶ���
	 * @return
	 */
	public boolean isColumnExist(String tableName,String columnName){
		
		boolean result = false;
		
		//�����������������false
		if(!isTableExist(tableName)){
			return false;
		}
		
		try {
			Cursor cursor;
			String sql = "select count(1) from sqlite_master where type = 'table' and name = '" +tableName.trim()+"' and sql like '%"+columnName.trim() +"%'";
			cursor = mDb.rawQuery(sql, null);
			if(cursor.moveToNext()){
				int count = cursor.getInt(0);
				if(count > 0){
					result = true;
				}
			}
			cursor.close();
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		return result;
	}
	
}
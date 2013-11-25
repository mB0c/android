package com.eastime.paycar.activitys;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;

import android.os.Bundle;
import android.view.View;
import android.view.Window;

import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.eastime.paycar.R;
import com.eastime.paycar.adapters.SelectListViewAdapter;
import com.eastime.paycar.bean.YunDanItem;
import com.eastime.paycar.util.Contants;
import com.eastime.paycar.util.DBHelper;

public class SelectActivity extends Activity{

	private SelectListViewAdapter adapter;
	private List<YunDanItem> dataMap = new ArrayList<YunDanItem>();
	private ListView listView;
	private DBHelper db;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.cactivity);
		
		listView = (ListView) findViewById(R.id.lv2);
		listView.setOnItemLongClickListener(onItemLongClick);
		listView.setOnItemClickListener(onItemClickListener);
		adapter = new SelectListViewAdapter(this, dataMap);
		listView.setAdapter(adapter);
		//��ȡ����
		getData();
		//�������
		showData();
	}

	//���������¼�
	private AdapterView.OnItemLongClickListener onItemLongClick = new AdapterView.OnItemLongClickListener() {

		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,int position, long arg3) {
			// TODO Auto-generated method stub
			
			final int position1 = position;
			
			AlertDialog.Builder dialog = new AlertDialog.Builder(SelectActivity.this);

			dialog.setTitle("�Ƿ�ɾ��?"); 
			
			dialog.setPositiveButton("ȷ��", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					//���ݿ�ɾ��
					deleteDanHao(position1);
					//������ʾ��������
					showData();
					dialog.dismiss();
					Toast.makeText(SelectActivity.this, "ɾ���ɹ���", Toast.LENGTH_LONG).show();
				}



			});
			
			dialog.setNegativeButton("ȡ��", new android.content.DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
					
				}
			});
			
			dialog.create().show();
			
			
			return false;
		}
	};
	
	
	//�����¼�
	private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			//����󵯳���ϸ��Ϣ����
			//��ȡ������Ŀ�ı��
			int id = dataMap.get(position).getId();
			//��ת��detail����
			Intent intent = new Intent();
			intent.setClass(SelectActivity.this, DetailInfoActivity.class);
			intent.putExtra("id", id);
			startActivity(intent);			
		}
	};
	
	private void showData() {
		adapter.notifyDataSetChanged();
	}

	private void getData() {
		List<YunDanItem> item0 = new ArrayList<YunDanItem>();
		List<YunDanItem> item1 = new ArrayList<YunDanItem>();
		List<YunDanItem> item2 = new ArrayList<YunDanItem>();
		//���dataMap
		dataMap.clear();
		db = DBHelper.getInstance();
		Cursor cursor = db.findList(false, Contants.table2);
		while(cursor.moveToNext()){
			YunDanItem item = new YunDanItem();
			
			item.setId(cursor.getInt(0));
			item.setNumber(cursor.getString(1));
			item.setImgpath(cursor.getString(2));
			item.setSuccinfo(cursor.getString(3));
			item.setFailinfo(cursor.getString(4));
			item.setIsdel(Integer.parseInt(cursor.getString(5)));
			try{
				item.setIssucc(Integer.parseInt(cursor.getString(6)));
			}catch(Exception e){
				item.setIssucc(-1);
			}
			item.setIsUpload(Integer.parseInt(cursor.getString(7)));
			item.setPayTime(cursor.getString(8));
			item.setCreateTime(cursor.getString(9));
			int issucc = item.getIssucc();
			int isupload = item.getIsUpload();
			if(2 != issucc && 0 == isupload){
				//������� δ�ϴ��� 
				item1.add(item);
			}else if(2 != issucc && 1 == isupload){
				//������ɲ��ϴ�
				item2.add(item);
			}else{
				//����δ���
				item0.add(item);
			}
		}
		dataMap.addAll(item0);
		dataMap.addAll(item1);
		dataMap.addAll(item2);
		cursor.close();
		
	}
	
    //ɾ������
	private void deleteDanHao(int position1) {
		db = DBHelper.getInstance();
		String deleteData = dataMap.get(position1).getNumber();
		String whereClause = "number = ?";
		String [] whereArgs = {deleteData};
		if(db.delete(Contants.table2, whereClause, whereArgs)){
			dataMap.remove(position1);
		}else{
			Toast.makeText(this, "����ɾ��ʧ�ܣ�", Toast.LENGTH_LONG).show();
			return;
		}
	}

	@Override
	protected void onResume() {
		
		getData();
		showData();
		
		super.onResume();
	}
	
	
	
	
}
package com.ldm.familybill;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.ldm.db.DBHelper;
import com.ldm.excel.ExcelUtils;
import com.ldm.object.BillObject;

@SuppressLint("SimpleDateFormat")
public class MainActivity extends Activity implements OnClickListener {
	private EditText mFoodEdt;
	private EditText mArticlesEdt;
	private EditText mTrafficEdt;
	private EditText mTravelEdt;

	private Button export_bill, import_bill;
	private File file;
	private String[] title = { "日期", "吃", "穿", "住", "行" };
	private String[] saveData;
	private DBHelper mDbHelper;
	private ArrayList<ArrayList<String>> bill2List;
	private ListView bill_listview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		findViewsById();
		mDbHelper = new DBHelper(this);
		mDbHelper.open();
		bill2List = new ArrayList<ArrayList<String>>();
	}

	private void findViewsById() {
		mFoodEdt = (EditText) findViewById(R.id.family_bill_food_edt);
		mArticlesEdt = (EditText) findViewById(R.id.family_bill_articles_edt);
		mTrafficEdt = (EditText) findViewById(R.id.family_bill_traffic_edt);
		mTravelEdt = (EditText) findViewById(R.id.family_bill_travel_edt);
		bill_listview = (ListView) findViewById(R.id.bill_listview);
		export_bill = (Button) findViewById(R.id.export_bill);
		import_bill = (Button) findViewById(R.id.import_bill);
		export_bill.setOnClickListener(this);
		import_bill.setOnClickListener(this);
		View contentHeader = LayoutInflater.from(this).inflate(
				R.layout.listview_header, null);
		bill_listview.addHeaderView(contentHeader);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.export_bill:

			saveData = new String[] {
					new SimpleDateFormat("yyyy-MM-dd").format(new Date()),
					mFoodEdt.getText().toString().trim(),
					mArticlesEdt.getText().toString().trim(),
					mTrafficEdt.getText().toString().trim(),
					mTravelEdt.getText().toString().trim() };
			if (canSave(saveData)) {
				ContentValues values = new ContentValues();
				values.put("time",
						new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
				values.put("food", mFoodEdt.getText().toString());
				values.put("use", mArticlesEdt.getText().toString());
				values.put("traffic", mTrafficEdt.getText().toString());
				values.put("travel", mTravelEdt.getText().toString());

				long insert = mDbHelper.insert("family_bill", values);
				if (insert > 0) {
					initData();
				}
			} else {
				Toast.makeText(this, "请填写任意一项内容", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.import_bill:
			ArrayList<BillObject> billList = (ArrayList<BillObject>) ExcelUtils
					.read2DB(new File(getSDPath() + "/Family/bill.xls"), this);
			bill_listview.setAdapter(new BillAdapter(this, billList));
			break;
		}
	}

	@SuppressLint("SimpleDateFormat")
	public void initData() {
		file = new File(getSDPath() + "/Family");
		makeDir(file);
		ExcelUtils.initExcel(file.toString() + "/bill.xls", title);
		ExcelUtils.writeObjListToExcel(getBillData(), getSDPath()
				+ "/Family/bill.xls", this);
	}

	private ArrayList<ArrayList<String>> getBillData() {
		Cursor mCrusor = mDbHelper.exeSql("select * from family_bill");
		while (mCrusor.moveToNext()) {
			ArrayList<String> beanList = new ArrayList<String>();
			beanList.add(mCrusor.getString(1));
			beanList.add(mCrusor.getString(2));
			beanList.add(mCrusor.getString(3));
			beanList.add(mCrusor.getString(4));
			beanList.add(mCrusor.getString(5));

			bill2List.add(beanList);
		}
		mCrusor.close();
		return bill2List;
	}

	public static void makeDir(File dir) {
		if (!dir.getParentFile().exists()) {
			makeDir(dir.getParentFile());
		}
		dir.mkdir();
	}

	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED);
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();
		}
		String dir = sdDir.toString();
		return dir;

	}

	private boolean canSave(String[] data) {
		boolean isOk = false;
		for (int i = 0; i < data.length; i++) {
			if (i > 0 && i < data.length) {
				if (!TextUtils.isEmpty(data[i])) {
					isOk = true;
				}
			}
		}
		return isOk;
	}
}

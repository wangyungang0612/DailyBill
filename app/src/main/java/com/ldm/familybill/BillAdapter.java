package com.ldm.familybill;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ldm.object.BillObject;

public class BillAdapter extends BaseAdapter {

	private List<BillObject> list;
	private LayoutInflater inflater;
	// private String year,month;
	

	public BillAdapter(Context context, List<BillObject> list) {
		inflater = LayoutInflater.from(context);
		this.list = list;
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public void changeList(List<BillObject> list) {
		this.list = list;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.bill_items, null);
			holder.food = (TextView) convertView.findViewById(R.id.food);
			holder.clothes = (TextView) convertView.findViewById(R.id.clothes);
			holder.house = (TextView) convertView.findViewById(R.id.house);
			holder.vehicle = (TextView) convertView.findViewById(R.id.vehicle);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		BillObject billObject = list.get(position);
		holder.food.setText(billObject.getFood());
		holder.clothes.setText(billObject.getClothes());
		holder.house.setText(billObject.getHouse());
		holder.vehicle.setText(billObject.getVehicle());
		return convertView;
	}

	public class ViewHolder {
		ImageView icon;
		TextView food,clothes,house,vehicle;
	}
}

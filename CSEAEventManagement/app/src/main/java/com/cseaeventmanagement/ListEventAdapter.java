package com.cseaeventmanagement;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListEventAdapter extends RecyclerView.Adapter<ListEventAdapter.ViewHolder> {

	private List_Event_Data_POJO[] listdata;

	// RecyclerView recyclerView;
	public ListEventAdapter(List_Event_Data_POJO[] listdata) {
		this.listdata = listdata;
	}
	public void setData(List_Event_Data_POJO[] listdata) { this.listdata = listdata; }
	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
		View listItem = layoutInflater.inflate(R.layout.list_events_part, parent, false);
		return new ViewHolder(listItem);
	}

	@Override
	public void onBindViewHolder(ViewHolder holder, int position) {
		final List_Event_Data_POJO List_Event_Data_POJO = listdata[position];
		holder.name.setText(listdata[position].eventName);
		holder.date.setText(listdata[position].eventDate);
		holder.desc.setText(listdata[position].eventDesc);
		holder.relativeLayout.setTag(listdata[position].eventFav);
		holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				// Avneet I changed this intent to go to view event details
				//List_Event_Data_POJO.eventName
				Intent intent = new Intent(view.getContext(), EventViewActivity.class);
				intent.putExtra("event_id",(String)view.getTag());
				view.getContext().startActivity(intent);
			}
		});
	}

	@Override
	public int getItemCount() {
		if(listdata == null)
			return 0;
		return listdata.length;
	}

	public static class ViewHolder extends RecyclerView.ViewHolder {
		public TextView name;
		public TextView date;
		public TextView desc;
		public RelativeLayout relativeLayout;

		public ViewHolder(View itemView) {
			super(itemView);
			this.name = itemView.findViewById(R.id.name);
			this.desc = itemView.findViewById(R.id.desc);
			this.date = itemView.findViewById(R.id.date);
			relativeLayout = itemView.findViewById(R.id.relativeLayout);
		}
	}
}

package com.cseaeventmanagement;

public class List_Event_Data_POJO {
	public String eventName;
	public String eventDate;
	public String eventDesc;
	public String eventFav;

	public List_Event_Data_POJO(String eventName, String eventDate, String eventDesc, String eventFav) {
		this.eventName = eventName;
		this.eventDate = eventDate;
		this.eventDesc = eventDesc;
		this.eventFav = eventFav;
	}
}
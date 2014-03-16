package com.zikesjan.planespotting.model;

public class Flight {
	public String id;
	public double lon;
	public double lat;
	public String type;
	public String registartion;
	public String from;
	public String to;
	public String fl;
	
	public Flight(String id, double lon, double lat, String type,
			String registartion, String from, String to, String fl) {
		super();
		this.id = id;
		this.lon = lon;
		this.lat = lat;
		this.type = type;
		this.registartion = registartion;
		this.from = from;
		this.to = to;
		this.fl = fl;
	}
	
}

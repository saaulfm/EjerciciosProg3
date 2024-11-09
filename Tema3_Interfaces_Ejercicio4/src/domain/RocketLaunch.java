package domain;

import java.time.LocalDateTime;

public class RocketLaunch {
	
	public enum Status {
		SUCCESS,
		FAILURE
	}

	private String company;
	private LocalDateTime launchDate;
	private String location;
	private String detail;
	private float cost;
	private Status status;
	
	public RocketLaunch(String company, LocalDateTime launchDate, String location, String detail, float cost, boolean status) {
		this.company = company;
		this.launchDate = launchDate;
		this.location = location;
		this.detail = detail;
		this.cost = cost;
		this.status = status ? Status.SUCCESS : Status.FAILURE; 
	}
	
	public String getCompany() {
		return company;
	}
	
	public LocalDateTime getLaunchDate() {
		return launchDate;
	}

	public String getLocation() {
		return location;
	}

	public String getDetail() {
		return detail;
	}

	public float getCost() {
		return cost;
	}

	public Status getStatus() {
		return status;
	}

	@Override
	public String toString() {
		return "RocketLaunch [company=" + company + ", launchDate=" + launchDate + ", location=" + location
				+ ", status=" + status + "]";
	}
}
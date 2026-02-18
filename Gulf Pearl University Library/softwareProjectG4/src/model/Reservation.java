package model;

import java.time.LocalDate;

public class Reservation {
	public enum Status {
		RESERVED, CANCELLED, FULFILLED
	}

	private String reservationId;
	private String membershipId;
	private String callNumber;
	private LocalDate reservedOn;
	private LocalDate reservedFor;
	private Status status;

	public Reservation(String reservationId, String membershipId, String callNumber, LocalDate reservedOn,
			LocalDate reservedFor, Status status) {
		this.reservationId = reservationId;
		this.membershipId = membershipId;
		this.callNumber = callNumber;
		this.reservedOn = reservedOn;
		this.reservedFor = reservedFor;
		this.status = status;
	}

	public String getReservationId() {
		return reservationId;
	}

	public String getMembershipId() {
		return membershipId;
	}

	public String getCallNumber() {
		return callNumber;
	}

	public LocalDate getReservedOn() {
		return reservedOn;
	}

	public LocalDate getReservedFor() {
		return reservedFor;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status s) {
		this.status = s;
	}
}

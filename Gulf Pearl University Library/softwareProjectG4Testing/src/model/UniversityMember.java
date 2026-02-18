package model;

public class UniversityMember {
	private String fullName;
	private String address;
	private String universityID;
	private String financeID;
	private double amount;
	private String membershipStatus;
	private String position;

	public UniversityMember(String financeID, String fullName, String address, String universityID, double amount,
			String membershipStatus, String position) {
		this.financeID = financeID;
		this.fullName = fullName;
		this.address = address;
		this.universityID = universityID;
		this.amount = amount;
		this.membershipStatus = membershipStatus;
		this.position = position;
	}

	public String getFullName() {
		return fullName;
	}

	public String getAddress() {
		return address;
	}

	public String getUniversityID() {
		return universityID;
	}

	public String getFinanceID() {
		return financeID;
	}

	public double getAmount() {
		return amount;
	}

	public String getMembershipStatus() {
		return membershipStatus;
	}

	public String getPosition() {
		return position;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public void setUniversityID(String universityID) {
		this.universityID = universityID;
	}

	public void setFinanceID(String financeID) {
		this.financeID = financeID;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setMembershipStatus(String status) {
		this.membershipStatus = status;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public boolean deduct(double fee) {
		if (amount >= fee) {
			amount -= fee;
			return true;
		}
		return false;
	}
}

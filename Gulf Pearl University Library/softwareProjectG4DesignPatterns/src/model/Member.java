package model;

public class Member {
	private String name;
	private String address;
	private String universityID;
	private String financeID;
	private String membershipID;
	private int totalReserved;
	private boolean libraryMember;

	public Member(String name, String address, String universityID, String financeID) {
		this.name = name;
		this.address = address;
		this.universityID = universityID;
		this.financeID = financeID;
		this.totalReserved = 0;
		this.libraryMember = false;
	}

	public String getName() {
		return name;
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

	public String getMembershipID() {
		return membershipID;
	}

	public int getTotalReserved() {
		return totalReserved;
	}

	public boolean isLibraryMember() {
		return libraryMember;
	}

	public void setMembershipID(String id) {
		this.membershipID = id;
	}

	public void setTotalReserved(int tr) {
		this.totalReserved = tr;
	}

	public void setLibraryMember(boolean b) {
		this.libraryMember = b;
	}
}

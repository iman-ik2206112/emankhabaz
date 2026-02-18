package factoryPattern;

public abstract class ItemP {
	protected String callNumber;
	protected String type;
	protected String title;
	protected int publicationYear;
	protected String itemStatus;
	protected boolean isReserved;
	protected boolean isBorrowed;
	protected String membershipID;

	public ItemP(String callNumber, String type, String title, int publicationYear, String itemStatus,
			boolean isReserved, boolean isBorrowed) {
		this.callNumber = callNumber;
		this.type = type;
		this.title = title;
		this.publicationYear = publicationYear;
		this.itemStatus = itemStatus;
		this.isReserved = isReserved;
		this.isBorrowed = isBorrowed;
		this.membershipID = "";
	}

	public String getCallNumber() {
		return callNumber;
	}

	public String getType() {
		return type;
	}

	public String getTitle() {
		return title;
	}

	public int getPublicationYear() {
		return publicationYear;
	}

	public String getItemStatus() {
		return itemStatus;
	}

	public boolean isReserved() {
		return isReserved;
	}

	public boolean isBorrowed() {
		return isBorrowed;
	}

	public String getMembershipID() {
		return membershipID;
	}

	public void setReserved(boolean reserved) {
		isReserved = reserved;
	}

	public void setBorrowed(boolean borrowed) {
		isBorrowed = borrowed;
	}

	public void setItemStatus(String itemStatus) {
		this.itemStatus = itemStatus;
	}

	public void setMembershipID(String membershipID) {
		this.membershipID = membershipID;
	}
}

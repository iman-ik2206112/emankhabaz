package factoryPattern;

public class Magazine extends ItemP {

	public Magazine(String callNumber, String type, String title, int publicationYear, String itemStatus,
			boolean isReserved, boolean isBorrowed) {
		super(callNumber, type, title, publicationYear, itemStatus, isReserved, isBorrowed);
	}
}

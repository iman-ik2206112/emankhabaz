package factoryPattern;

public class Book extends ItemP {

	public Book(String callNumber, String type, String title, int publicationYear, String itemStatus,
			boolean isReserved, boolean isBorrowed) {
		super(callNumber, type, title, publicationYear, itemStatus, isReserved, isBorrowed);
	}
}

package factoryPattern;

public class DigitalMedia extends ItemP {

	public DigitalMedia(String callNumber, String type, String title, int publicationYear, String itemStatus,
			boolean isReserved, boolean isBorrowed) {
		super(callNumber, type, title, publicationYear, itemStatus, isReserved, isBorrowed);
	}
}

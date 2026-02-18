package factoryPattern;

public class ItemFactory {

	public static ItemP createItem(String type, String callNumber, String title, int publicationYear, String itemStatus,
			boolean isReserved, boolean isBorrowed) {

		if (type == null)
			return null;

		switch (type.toLowerCase()) {
		case "book":
			return new Book(callNumber, type, title, publicationYear, itemStatus, isReserved, isBorrowed);

		case "magazine":
			return new Magazine(callNumber, type, title, publicationYear, itemStatus, isReserved, isBorrowed);

		case "digitalmedia":
		case "digital media":
			return new DigitalMedia(callNumber, type, title, publicationYear, itemStatus, isReserved, isBorrowed);

		default:
			throw new IllegalArgumentException("Unknown item type: " + type);
		}
	}
}

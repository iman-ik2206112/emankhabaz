package factoryPattern;

public class ItemFactoryDemo {
	public static void main(String[] args) {

		ItemP book = ItemFactory.createItem("book", "100011", "Software engineering", 2000, "Available", false, false);

		ItemP magazine = ItemFactory.createItem("magazine", "100012", "National Geographic", 2024, "Available", false,
				false);

		ItemP digital = ItemFactory.createItem("digitalmedia", "100013", "Java Programming e-book", 2021, "Available",
				false, false);

		System.out.println(book.getTitle() + " (" + book.getType() + ")");
		System.out.println(magazine.getTitle() + " (" + magazine.getType() + ")");
		System.out.println(digital.getTitle() + " (" + digital.getType() + ")");
	}
}

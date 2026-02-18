package facadePattern;

import model.DataManager;
import model.Item;
import model.Member;
import model.UniversityMember;

public class FacadePatternDemo {

	public static void main(String[] args) {

		DataManager.loadAllData();

		Member m = new Member("Ruaa", "Old Airport", "UNI1002", "ACC1002");
		m.setMembershipID("M1002");
		m.setTotalReserved(0);
		DataManager.updateMember(m);

		Item item = new Item("100008", "Book", "Database Systems", 2017, "Available", false, false);
		DataManager.updateItem(item);

		UniversityMember um = new UniversityMember("ACC30001", "Lina Saleh", "West Bay", "UNI3001", 2500.0, "No",
				"student");
		DataManager.updateUniversityMember(um);

		LibraryFacade facade = new LibraryFacade();

		System.out.println("TEST 1: APPLY FOR MEMBERSHIP");
		boolean applyResult = facade.applyForMembership("Lina Saleh", "student", "West Bay", "UNI3001", "ACC30001");
		System.out.println("ApplyForMembership Result: " + applyResult);

		System.out.println("\nTEST 2: FIND MEMBER BY NAME ");
		Member m1 = facade.findMemberByName("Ruaa");
		System.out.println(m1 != null ? m1.getName() + " found" : "NOT FOUND!");

		System.out.println("\nTEST 3: FIND MEMBER BY ID");
		Member m2 = facade.findMemberByID("M1002");
		System.out.println(m2 != null ? m2.getName() + " found" : "NOT FOUND!");

		System.out.println("\nTEST 4: RESERVE ITEM");
		boolean r = facade.reserveItem("M1001", "100008");
		System.out.println("Reserve Result = " + r);

		System.out.println("\nTEST 5: GET ITEM TYPE");
		String type = facade.getItemType("100008");
		System.out.println("Item Type = " + type);
	}
}

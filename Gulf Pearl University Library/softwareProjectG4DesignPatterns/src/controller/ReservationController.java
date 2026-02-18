package controller;

import java.util.List;

import model.DataManager;
import model.Item;
import model.Member;

public class ReservationController {
	private List<Item> itemsList;

	public ReservationController() {
		this.itemsList = DataManager.getItemsList();
	}

	public boolean reserveItem(String membershipID, String callNumber) {
		if (membershipID == null || callNumber == null)
			return false;

		Member member = DataManager.getMembersList().stream().filter(m -> membershipID.equals(m.getMembershipID()))
				.findFirst().orElse(null);

		if (member == null || !member.isLibraryMember())
			return false;
		if (member.getTotalReserved() >= 3)
			return false; // max 3 items

		Item item = DataManager.getItemsList().stream().filter(i -> i.getCallNumber().equals(callNumber)).findFirst()
				.orElse(null);

		if (item == null || item.isReserved() || item.isBorrowed())
			return false;

		item.setReserved(true);
		item.setMembershipID(member.getMembershipID());
		item.setItemStatus("Reserved");
		DataManager.updateItem(item);

		member.setTotalReserved(member.getTotalReserved() + 1);
		DataManager.updateMember(member);

		return true;
	}

	public String getItemType(String callNumber) {
		Item item = DataManager.getItemsList().stream().filter(i -> i.getCallNumber().equals(callNumber)).findFirst()
				.orElse(null);
		return item != null ? item.getType() : null;
	}

	public Item findItem(String callNumber) {
		if (callNumber == null)
			return null;
		return itemsList.stream().filter(i -> i.getCallNumber().equalsIgnoreCase(callNumber)).findFirst().orElse(null);
	}
}

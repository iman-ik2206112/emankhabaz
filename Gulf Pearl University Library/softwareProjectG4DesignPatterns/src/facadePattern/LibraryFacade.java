package facadePattern;

import controller.MembershipController;
import controller.ReservationController;
import model.Item;
import model.Member;

public class LibraryFacade {

	private MembershipController membershipController;
	private ReservationController reservationController;

	public LibraryFacade() {
		this.membershipController = new MembershipController();
		this.reservationController = new ReservationController();

	}

	public boolean applyForMembership(String name, String status, String address, String universityID,
			String financeID) {

		return membershipController.applyMembership(name, status, address, universityID, financeID);
	}

	public Member findMemberByName(String name) {
		return membershipController.findMemberByName(name);
	}

	public Member findMemberByID(String membershipID) {
		return membershipController.findMemberByMembershipID(membershipID);
	}

	public boolean reserveItem(String membershipID, String callNumber) {
		return reservationController.reserveItem(membershipID, callNumber);
	}

	public String getItemType(String callNumber) {
		Item item = reservationController.findItem(callNumber);
		return (item != null) ? item.getType() : null;
	}

}
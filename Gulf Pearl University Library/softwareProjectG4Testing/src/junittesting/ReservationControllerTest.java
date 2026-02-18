package junittesting;

import controller.ReservationController;
import model.DataManager;
import model.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ReservationControllerTest {

	private ReservationController reservationController;

	private final String VALID_RESERVATION_MEMBER_ID = "M1001";
	private final String RESERVATION_LIMIT_MEMBER_ID = "M1002";

	private final String VALID_CALL_NUMBER_1 = "100001";
	private final String VALID_CALL_NUMBER_2 = "100008";
	private final String VALID_CALL_NUMBER_3 = "100010";
	private final String AVAILABLE_CALL_NUMBER_4 = "100006";
	private final String ITEM_ALREADY_RESERVED_CALL_NUMBER = "100002";

	private final String NON_EXISTENT_CALL_NUMBER = "999999";
	private final String NON_EXISTENT_MEMBER_ID = "M9999";

	@BeforeEach
	void setUp() {
		DataManager.loadAllData();
		reservationController = new ReservationController();
	}

	@Test
	void testSuccessfulItemReservation() {

		Member member = DataManager.getMembersList().stream()
				.filter(m -> m.getMembershipID().equals(VALID_RESERVATION_MEMBER_ID)).findFirst().orElseThrow();
		int initialReservedCount = member.getTotalReserved();

		boolean result = reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_1);

		assertTrue(result, "Reservation should succeed for an available item and eligible member.");
		assertEquals(initialReservedCount + 1, member.getTotalReserved(), "Member's reserved count should increase.");
	}

	@Test
	void testFailedReservationLimitReached() {

		Member member = DataManager.getMembersList().stream()
				.filter(m -> m.getMembershipID().equals(RESERVATION_LIMIT_MEMBER_ID)).findFirst().orElseThrow();
		int initialReservedCount = member.getTotalReserved(); // Should be 3

		boolean result = reservationController.reserveItem(RESERVATION_LIMIT_MEMBER_ID, VALID_CALL_NUMBER_2);

		assertFalse(result, "Reservation should fail as the limit (3) has been reached.");
		assertEquals(initialReservedCount, member.getTotalReserved(), "Member's reserved count should remain 3.");
	}

	@Test
	void testFailedReservationItemAlreadyReserved() {

		boolean result = reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID,
				ITEM_ALREADY_RESERVED_CALL_NUMBER);
		assertFalse(result, "Reservation should fail as the item is already reserved.");
	}

	@Test
	void testFailedReservationInvalidMemberId() {

		boolean result = reservationController.reserveItem(NON_EXISTENT_MEMBER_ID, VALID_CALL_NUMBER_1);
		assertFalse(result, "Reservation should fail because the member ID does not exist.");
	}

	@Test
	void testFailedReservationInvalidCallNumber() {

		boolean result = reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, NON_EXISTENT_CALL_NUMBER);
		assertFalse(result, "Reservation should fail because the item call number does not exist.");
	}

	@Test
	void testSequentialReservationsAndCounterIntegrity() {

		Member member = DataManager.getMembersList().stream()
				.filter(m -> m.getMembershipID().equals(VALID_RESERVATION_MEMBER_ID)).findFirst().orElseThrow();

		assertTrue(reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_1),
				"Reservation 1 should succeed.");
		assertTrue(reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_2),
				"Reservation 2 should succeed.");
		assertTrue(reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_3),
				"Reservation 3 should succeed.");

		assertEquals(3, member.getTotalReserved(), "Count should be 3 after 3 successful reservations.");
	}

	@Test
	void testBoundaryLimitEnforcement() {

		Member member = DataManager.getMembersList().stream()
				.filter(m -> m.getMembershipID().equals(VALID_RESERVATION_MEMBER_ID)).findFirst().orElseThrow();

		reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_1);
		reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_2);
		reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, VALID_CALL_NUMBER_3);

		boolean result = reservationController.reserveItem(VALID_RESERVATION_MEMBER_ID, AVAILABLE_CALL_NUMBER_4);

		assertFalse(result, "Reservation 4 should fail as the limit of 3 is enforced.");
		assertEquals(3, member.getTotalReserved(), "Count must remain 3 after boundary failure.");
	}
}
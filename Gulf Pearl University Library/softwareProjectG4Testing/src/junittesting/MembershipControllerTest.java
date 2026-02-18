package junittesting;

import controller.MembershipController;
import model.DataManager;
import model.UniversityMember;
import model.FinancialAccount;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MembershipControllerTest {

	private MembershipController membershipController;

	private final String VALID_FIN_ID = "ACC20001";
	private final String VALID_UNI_ID = "UNI2001";
	private final String VALID_NAME = "Dr. Omar";
	private final String VALID_STATUS = "Staff";

	private final String INSUFFICIENT_FIN_ID = "ACC30003";
	private final String INSUFFICIENT_UNI_ID = "UNI3003";
	private final String INSUFFICIENT_NAME = "Eman Ali";
	private final String INSUFFICIENT_STATUS = "Student";

	private final String ALREADY_MEMBER_FIN_ID = "ACC30004";
	private final String ALREADY_MEMBER_UNI_ID = "UNI3005";
	private final String ALREADY_MEMBER_NAME = "Ruaa";
	private final String ALREADY_MEMBER_STATUS = "Student";

	@BeforeEach
	void setUp() {

		DataManager.loadAllData();

		membershipController = new MembershipController();
	}

	@Test
	void testSuccessfulMembershipApplication() {

		int initialMemberCount = DataManager.getMembersList().size();
		UniversityMember umBefore = DataManager.getUniversityMembersList().stream()
				.filter(u -> u.getFinanceID().equals(VALID_FIN_ID)).findFirst().orElseThrow();
		double initialAmount = umBefore.getAmount();

		boolean result = membershipController.applyMembership(VALID_NAME, VALID_STATUS, "Al Sadd", VALID_UNI_ID,
				VALID_FIN_ID);

		assertTrue(result, "Registration should succeed for a valid applicant with funds.");
		assertEquals(initialMemberCount + 1, DataManager.getMembersList().size(), "Member count should increase.");

		UniversityMember umAfter = DataManager.getUniversityMembersList().stream()
				.filter(u -> u.getFinanceID().equals(VALID_FIN_ID)).findFirst().orElseThrow();

		double expectedAmount = initialAmount - FinancialAccount.getMembershipFee();
		assertEquals(expectedAmount, umAfter.getAmount(), 0.001, "Fee should be deducted from account.");
		assertEquals("Yes", umAfter.getMembershipStatus(), "UniversityMember status should be updated to 'Yes'.");
	}

	@Test
	void testFailedApplicationInsufficientFunds() {

		UniversityMember um = DataManager.getUniversityMembersList().stream()
				.filter(u -> u.getFinanceID().equals(INSUFFICIENT_FIN_ID)).findFirst().orElseThrow();
		double originalAmount = um.getAmount();

		boolean result = membershipController.applyMembership(INSUFFICIENT_NAME, INSUFFICIENT_STATUS, "Gharrafa",
				INSUFFICIENT_UNI_ID, INSUFFICIENT_FIN_ID);

		assertFalse(result, "Registration should fail due to insufficient funds (800 < 1500).");

		UniversityMember umAfter = DataManager.getUniversityMembersList().stream()
				.filter(u -> u.getFinanceID().equals(INSUFFICIENT_FIN_ID)).findFirst().orElseThrow();
		assertEquals(originalAmount, umAfter.getAmount(), 0.001,
				"Funds should remain unchanged after failed transaction.");
		assertEquals("No", umAfter.getMembershipStatus(), "UniversityMember status should remain 'No'.");
	}

	@Test
	void testFailedApplicationAlreadyMember() {

		int initialMemberCount = DataManager.getMembersList().size();

		boolean result = membershipController.applyMembership(ALREADY_MEMBER_NAME, ALREADY_MEMBER_STATUS, "Airport",
				ALREADY_MEMBER_UNI_ID, ALREADY_MEMBER_FIN_ID);

		assertFalse(result, "Registration should fail as the user is already a library member.");
		assertEquals(initialMemberCount, DataManager.getMembersList().size(), "Member count should not change.");
	}

	@Test
	void testFailedApplicationExternalStatus() {

		boolean result = membershipController.applyMembership(VALID_NAME, "External", "Al Sadd", VALID_UNI_ID,
				VALID_FIN_ID);
		assertFalse(result, "Registration should fail for 'External' status.");
	}

	@Test
	void testFailedApplicationInvalidUniversityMember() {

		boolean result = membershipController.applyMembership("Fake User", "Student", "Fake Address", "UNI9999",
				"ACC9999");
		assertFalse(result, "Registration should fail if the user is not found in the university system.");
	}
}
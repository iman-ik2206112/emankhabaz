package junittesting;

import model.DataManager;
import model.UniversityMember;
import model.AdminstrativeStaff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ModelLogicTest {

	private UniversityMember testUm;
	private AdminstrativeStaff pearlSys;

	private final String SUFFICIENT_FIN_ID = "ACC20001";

	@BeforeEach
	void setUp() {

		DataManager.loadAllData();

		testUm = DataManager.getUniversityMembersList().stream().filter(u -> u.getFinanceID().equals(SUFFICIENT_FIN_ID))
				.findFirst().orElseThrow();

		pearlSys = new AdminstrativeStaff();

	}

	@Test
	void testSuccessfulDeduction() {
		double initialAmount = testUm.getAmount();
		double fee = 1500.0;

		boolean result = testUm.deduct(fee);

		assertTrue(result, "Deduction should be successful when funds are sufficient.");
		assertEquals(initialAmount - fee, testUm.getAmount(), 0.01, "Amount should be reduced by 1500.");
	}

	@Test
	void testFailedDeductionInsufficientFunds() {
		double initialAmount = testUm.getAmount();
		double fee = 5001.0;

		boolean result = testUm.deduct(fee);

		assertFalse(result, "Deduction should fail due to insufficient funds.");
		assertEquals(initialAmount, testUm.getAmount(), 0.01, "Amount should remain unchanged.");
	}

	@Test
	void testIsNewUniversityId_True() {
		String newId = "UNI9999";

		assertTrue(pearlSys.isNewUniversityId(newId), "UNI9999 should be a new ID.");
	}

	@Test
	void testIsNewUniversityId_False() {

		String existingId = "UNI3004";

		assertFalse(pearlSys.isNewUniversityId(existingId), "UNI3004 should NOT be a new ID.");
	}

	@Test
	void testRegisterUniversityId() {
		String newId = "UNI7777";

		assertTrue(pearlSys.isNewUniversityId(newId), "Pre-check: Should be new.");
		pearlSys.registerUniversityId(newId);

		assertFalse(pearlSys.isNewUniversityId(newId), "Post-check: Should no longer be new.");
		assertTrue(pearlSys.isValidUniversityMember(newId), "Should now be considered a valid university member.");
	}
}
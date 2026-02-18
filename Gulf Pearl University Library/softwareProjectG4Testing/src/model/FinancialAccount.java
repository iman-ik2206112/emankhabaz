package model;

public class FinancialAccount {

	private static final double MEMBERSHIP_FEE = 1500.0;

	public static double getMembershipFee() {
		return MEMBERSHIP_FEE;
	}

	public static boolean chargeFee(UniversityMember um) {
		double fee = getMembershipFee();

		if (um == null || um.getAmount() < fee) {
			return false;
		}

		double newBalance = um.getAmount() - fee;
		um.setAmount(newBalance);

		DataManager.updateUniversityMember(um);

		return true;
	}
}
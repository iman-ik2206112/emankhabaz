package controller;

import model.DataManager;
import model.FinancialAccount;
import model.Member;
import model.UniversityMember;

import java.util.List;

public class MembershipController {

	private List<Member> membersList;
	private List<UniversityMember> universityMembers;

	public MembershipController() {

		this.membersList = DataManager.getMembersList();
		this.universityMembers = DataManager.getUniversityMembersList();
	}

	public boolean applyMembership(String name, String status, String address, String universityID, String financeID) {

		if (name == null || name.trim().isEmpty())
			return false;
		if (status == null || status.equalsIgnoreCase("External"))
			return false;
		if (financeID == null || financeID.trim().isEmpty())
			return false;

		this.membersList = DataManager.getMembersList();
		this.universityMembers = DataManager.getUniversityMembersList();
		UniversityMember um = universityMembers.stream().filter(x -> financeID.trim().equalsIgnoreCase(x.getFinanceID())
				&& universityID.trim().equalsIgnoreCase(x.getUniversityID())).findFirst().orElse(null);
		if (um == null) {
			return false;
		}

		if ("Yes".equalsIgnoreCase(um.getMembershipStatus())) {
			return false;
		}

		boolean already = membersList.stream()
				.anyMatch(m -> (m.getUniversityID() != null && m.getUniversityID().equalsIgnoreCase(universityID))
						|| (m.getFinanceID() != null && m.getFinanceID().equalsIgnoreCase(financeID)));
		if (already) {
			return false;
		}

		if (!FinancialAccount.chargeFee(um)) {
			return false;
		}

		um.setMembershipStatus("Yes");
		DataManager.updateUniversityMember(um);

		String membershipId = "M" + (membersList.size() + 1001);
		Member newMember = new Member(name, address, universityID, financeID);
		newMember.setMembershipID(membershipId);
		newMember.setLibraryMember(true);
		newMember.setTotalReserved(0);

		DataManager.updateMember(newMember);

		return true;
	}

	public Member findMemberByName(String name) {
		return DataManager.getMembersList().stream()
				.filter(m -> m.getName() != null && m.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
	}

	public Member findMemberByMembershipID(String membershipID) {
		return DataManager.getMembersList().stream()
				.filter(m -> m.getMembershipID() != null && m.getMembershipID().equalsIgnoreCase(membershipID))
				.findFirst().orElse(null);
	}

	public void updateMember(Member member) {
		DataManager.updateMember(member);
	}
}

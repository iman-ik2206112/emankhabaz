package model;

import java.util.HashSet;
import java.util.Set;

public class AdminstrativeStaff {

	private Set<String> validUniversityIds = new HashSet<>();

	public AdminstrativeStaff() {

		syncWithDataManager();
	}

	public boolean isNewUniversityId(String universityId) {
		if (universityId == null || universityId.trim().isEmpty())
			return false;
		return !validUniversityIds.contains(universityId.trim());
	}

	public void registerUniversityId(String universityId) {
		if (universityId != null && !universityId.trim().isEmpty()) {
			validUniversityIds.add(universityId.trim());
		}
	}

	public boolean isValidUniversityMember(String universityId) {
		if (universityId == null || universityId.trim().isEmpty())
			return false;
		return validUniversityIds.contains(universityId.trim());
	}

	public String getApplicantPosition(String uniID) {
		if (uniID == null || uniID.trim().isEmpty()) {
			return null;
		}

		UniversityMember um = DataManager.getUniversityMemberByUniId(uniID);

		if (um != null) {
			String position = um.getPosition().trim();
			if (position.equalsIgnoreCase("student") || position.equalsIgnoreCase("staff")) {

				return position.substring(0, 1).toUpperCase() + position.substring(1).toLowerCase();
			}
		}
		return null;
	}

	public void syncWithDataManager() {
		validUniversityIds.clear();
		for (Member m : DataManager.getMembersList()) {
			if (m.getUniversityID() != null && !m.getUniversityID().trim().isEmpty()) {
				validUniversityIds.add(m.getUniversityID().trim());
			}
		}
	}
}
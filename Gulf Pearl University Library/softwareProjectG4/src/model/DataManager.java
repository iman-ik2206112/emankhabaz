package model;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

	public static final String MEMBERS_FILE = "Membership.txt";
	public static final String ITEMS_FILE = "Items.txt";
	public static final String UNIVERSITY_MEMBERS_FILE = "universityMembers.txt";

	private static List<Member> membersList = new ArrayList<>();
	private static List<Item> itemsList = new ArrayList<>();
	private static List<UniversityMember> universityMembersList = new ArrayList<>();

	public static void ensureDataFiles() {
		try {
			File m = new File(MEMBERS_FILE);
			if (!m.exists()) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(m))) {
					bw.write("Name,Status,Address,UniversityID,FinanceID,TotalReserved,LibraryMember,MembershipID\n");
					bw.write("Mona,Student,Airport,UNI3004,ACC30002,0,Yes,M1001\n");
					bw.write("Ruaa,Student,Airport,UNI3005,ACC30004,3,Yes,M1002\n");
					bw.write("Ali Ahmed,Student,Doha,UNI1001,ACC10001,0,Yes,M1003\n");
					bw.write("Sara Hassan,Student,Al Rayyan,UNI1002,ACC10002,0,Yes,M1006\n");
				}
			}

			File it = new File(ITEMS_FILE);
			if (!it.exists()) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(it))) {
					bw.write("CallNumber,Type,Title,PublicationYear,ItemStatus,IsReserved,IsBorrowed,MembershipID\n");
					bw.write("100001,Book,Introduction to Java,2020,Available,No,No,\n");
					bw.write("100002,Book,Data Structures in C,2018,Reserved,Yes,No,M1002\n");
					bw.write("100003,Book,Algorithms Unlocked,2019,Reserved,Yes,No,\n");
					bw.write("100005,Digital Media,Machine Learning Lecture,2022,Reserved,Yes,No,M1002\n");
					bw.write("100006,Magazine,National Geographic March 2023,2023,Available,No,No,\n");
					bw.write("100007,Magazine,Science Today April 2023,2023,Reserved,Yes,Yes,M1002\n");
					bw.write("100008,Book,Database Systems,2017,Available,No,No,\n");
					bw.write("100010,Book,Computer Networks,2016,Available,No,No,\n");

				}
			}

			File u = new File(UNIVERSITY_MEMBERS_FILE);
			if (!u.exists()) {
				try (BufferedWriter bw = new BufferedWriter(new FileWriter(u))) {
					bw.write("FinanceID,FullName,Address,UniversityID,Amount,isMember,Position\n");
					bw.write("ACC10001,Ali Ahmed,Doha,UNI1001,2000,Yes,student\n");
					bw.write("ACC10002,Sara Hassan,Al Rayyan,UNI1002,800,Yes,student\n");
					bw.write("ACC20001,Dr. Omar,Al Sadd,UNI2001,5000,No,staff\n");
					bw.write("ACC30001,Lina Saleh,West Bay,UNI3001,3000,No,student\n");
					bw.write("ACC30009,Mohammed Ali,Education City,UNI3002,1500,No,student\n");
					bw.write("ACC30003,Eman Ali,Gharrafa,UNI3003,800,No,student\n");
					bw.write("ACC30002,Mona,Airport,UNI3004,2000,Yes,student\n");
					bw.write("ACC30004,Ruaa,Airport,UNI3005,1000,Yes,student\n");
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void loadAllData() {
		membersList = loadMembers();
		itemsList = loadItems();
		universityMembersList = loadUniversityMembers();
	}

	public static List<Member> loadMembers() {
		List<Member> list = new ArrayList<>();
		File f = new File(MEMBERS_FILE);
		if (!f.exists())
			return list;

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				String[] p = line.split(",", -1);
				String name = p.length > 0 ? p[0] : "";

				String address = p.length > 2 ? p[2] : "";
				String uni = p.length > 3 ? p[3] : "";
				String fin = p.length > 4 ? p[4] : "";
				int totalReserved = 0;
				try {
					totalReserved = Integer.parseInt(p.length > 5 ? p[5] : "0");
				} catch (Exception ignored) {
				}
				boolean lib = p.length > 6 && p[6].equalsIgnoreCase("Yes");
				String memid = p.length > 7 ? p[7] : null;

				Member m = new Member(name, address, uni, fin);
				m.setTotalReserved(totalReserved);
				m.setLibraryMember(lib);
				if (memid != null && !memid.isEmpty())
					m.setMembershipID(memid);
				list.add(m);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void updateMember(Member member) {
		boolean found = false;
		for (int i = 0; i < membersList.size(); i++) {
			Member m = membersList.get(i);
			if (m.getMembershipID() != null && m.getMembershipID().equals(member.getMembershipID())) {
				membersList.set(i, member);
				found = true;
				break;
			}
		}
		if (!found)
			membersList.add(member);
	}

	private static void saveMembersToFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(MEMBERS_FILE))) {
			bw.write("Name,Status,Address,UniversityID,FinanceID,TotalReserved,LibraryMember,MembershipID\n");
			for (Member m : membersList) {
				String status = "Student";
				String fin = m.getFinanceID() == null ? "NUL" : m.getFinanceID();
				bw.write(String.join(",", m.getName(), status, m.getAddress(),
						m.getUniversityID() == null ? "N/A" : m.getUniversityID(), fin,
						String.valueOf(m.getTotalReserved()), m.isLibraryMember() ? "Yes" : "No",
						m.getMembershipID() == null ? "" : m.getMembershipID()));
				bw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<Member> getMembersList() {
		return membersList;
	}

	public static List<Item> loadItems() {
		List<Item> list = new ArrayList<>();
		File f = new File(ITEMS_FILE);
		if (!f.exists())
			return list;

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				String[] p = line.split(",", -1);
				String call = p.length > 0 ? p[0].trim() : "";
				String type = p.length > 1 ? p[1] : "";
				String title = p.length > 2 ? p[2] : "";
				int year = 0;
				try {
					year = Integer.parseInt(p.length > 3 ? p[3] : "0");
				} catch (Exception ignored) {
				}
				String status = p.length > 4 ? p[4] : "";
				boolean isReserved = p.length > 5 && p[5].equalsIgnoreCase("Yes");
				boolean isBorrowed = p.length > 6 && p[6].equalsIgnoreCase("Yes");
				String memID = p.length > 7 ? p[7] : "";

				Item item = new Item(call, type, title, year, status, isReserved, isBorrowed);
				item.setMembershipID(memID);
				list.add(item);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private static void saveItemsToFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(ITEMS_FILE))) {
			bw.write("CallNumber,Type,Title,PublicationYear,ItemStatus,IsReserved,IsBorrowed,MembershipID\n");
			for (Item i : itemsList) {
				bw.write(String.join(",", i.getCallNumber(), i.getType(), i.getTitle(),
						String.valueOf(i.getPublicationYear()), i.getItemStatus(), i.isReserved() ? "Yes" : "No",
						i.isBorrowed() ? "Yes" : "No", i.getMembershipID() == null ? "" : i.getMembershipID()));
				bw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void updateItem(Item item) {
		boolean found = false;
		for (int i = 0; i < itemsList.size(); i++) {
			if (itemsList.get(i).getCallNumber().equals(item.getCallNumber().trim())) {
				itemsList.set(i, item);
				found = true;
				break;
			}
		}
		if (!found)
			itemsList.add(item);
	}

	public static List<Item> getItemsList() {
		return itemsList;
	}

	public static List<UniversityMember> loadUniversityMembers() {
		List<UniversityMember> list = new ArrayList<>();
		File f = new File(UNIVERSITY_MEMBERS_FILE);
		if (!f.exists())
			return list;

		try (BufferedReader br = new BufferedReader(new FileReader(f))) {
			String line = br.readLine();
			while ((line = br.readLine()) != null) {
				if (line.trim().isEmpty())
					continue;
				String[] p = line.split(",", -1);

				String financeID = p.length > 0 ? p[0].trim() : "";
				String fullName = p.length > 1 ? p[1].trim() : "";
				String address = p.length > 2 ? p[2].trim() : "";
				String universityID = p.length > 3 ? p[3].trim() : "";
				double amount = 0.0;
				try {
					amount = Double.parseDouble(p.length > 4 ? p[4] : "0");
				} catch (Exception ignored) {
				}
				String membershipStatus = p.length > 5 ? p[5].trim() : "No";
				String position = p.length > 6 ? p[6].trim() : "member";

				list.add(new UniversityMember(financeID, fullName, address, universityID, amount, membershipStatus,
						position));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	public static void updateUniversityMember(UniversityMember um) {
		boolean found = false;
		for (int i = 0; i < universityMembersList.size(); i++) {
			if (universityMembersList.get(i).getFinanceID().equalsIgnoreCase(um.getFinanceID())) {
				universityMembersList.set(i, um);
				found = true;
				break;
			}
		}
		if (!found)
			universityMembersList.add(um);
	}

	public static UniversityMember getUniversityMemberByUniId(String uniID) {
		if (uniID == null) {
			return null;
		}

		for (UniversityMember um : universityMembersList) {
			if (um.getUniversityID() != null && um.getUniversityID().equalsIgnoreCase(uniID.trim())) {
				return um;
			}
		}
		return null;
	}

	private static void saveUniversityMembersToFile() {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(UNIVERSITY_MEMBERS_FILE))) {

			bw.write("FinanceID,FullName,Address,UniversityID,Amount,isMember,Position\n");
			for (UniversityMember u : universityMembersList) {
				bw.write(String.join(",", u.getFinanceID(), u.getFullName(), u.getAddress(), u.getUniversityID(),
						String.valueOf(u.getAmount()), u.getMembershipStatus(), u.getPosition()));
				bw.write("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static List<UniversityMember> getUniversityMembersList() {
		return universityMembersList;
	}

	public static void saveAllToFile() {
		saveMembersToFile();
		saveItemsToFile();
		saveUniversityMembersToFile();
	}
}

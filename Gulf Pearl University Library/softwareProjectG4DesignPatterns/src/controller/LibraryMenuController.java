package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import model.DataManager;
import model.Item;
import model.Member;
import model.UniversityMember;
import model.AdminstrativeStaff;
import model.FinancialAccount;

import java.io.IOException;

public class LibraryMenuController {

	@FXML
	private ChoiceBox<String> statusChoice;
	@FXML
	private TextField nameField, addressField, universityIDField, financeIDField, membershipIDField, callNumberField;
	@FXML
	private Button applyButton, reserveButton, backButton;
	private Stage currentStage;

	private MembershipController membershipController;
	private ReservationController reservationController;

	private static final int MAX_RESERVATIONS = 3;

	public void initControllers(MembershipController membershipController,
			ReservationController reservationController) {
		this.membershipController = membershipController;
		this.reservationController = reservationController;
	}

	public void setCurrentStage(Stage stage) {
		this.currentStage = stage;
	}

	@FXML
	private void initialize() {

		if (statusChoice != null) {
			statusChoice.getItems().clear();
			statusChoice.getItems().addAll("Student", "Staff", "External");
		}
	}

	@FXML
	private void applyMembership() {
		String name = nameField.getText().trim();
		String address = addressField.getText().trim();
		String status = statusChoice.getValue();
		String uniID = universityIDField.getText().trim();
		String finID = financeIDField.getText().trim();
		String verifiedStatus = new AdminstrativeStaff().getApplicantPosition(uniID);

		final double MEMBERSHIP_FEE = FinancialAccount.getMembershipFee();

		if (name.isEmpty() || address.isEmpty() || status == null || finID.isEmpty() || uniID.isEmpty()) {
			showAlert("Error", "Please fill in all required membership fields.");
			return;
		}
		if (!isValidName(name)) {
			showAlert("Error", "Invalid Name. Name should only contain letters, spaces, hyphens, or apostrophes.");
			return;
		}

		if (address.length() < 3 || !address.matches(".*[a-zA-Z0-9]+.*")) {
			showAlert("Error", "Invalid Address. Address must only contain numbers and letters.");
			return;
		}

		UniversityMember um = DataManager.getUniversityMembersList().stream()
				.filter(u -> u.getFinanceID().equalsIgnoreCase(finID)).findFirst().orElse(null);

		if (um == null) {
			showAlert("Error", "You are not registered in the university system.");
			return;
		}
		if (!name.equalsIgnoreCase(um.getFullName())) {
			showAlert("Error", "Application rejected: The Name entered does not match university records for this ID.");
			return;
		}

		if (!address.equalsIgnoreCase(um.getAddress())) {
			showAlert("Error",
					"Application rejected: The Address entered does not match university records for this ID.");
			return;
		}
		if (status.equalsIgnoreCase("External")) {
			showAlert("Error", "An External Can not be a Library Member!");
			return;
		}
		if (verifiedStatus == null || !status.equalsIgnoreCase(verifiedStatus)) {

			showAlert("Error",
					"Application rejected: Your claimed status (" + status + ") does not match your verified status ("
							+ (verifiedStatus != null ? verifiedStatus : "Unverified")
							+ ") in the university records.");
			return;
		}

		if ("Yes".equalsIgnoreCase(um.getMembershipStatus())) {
			showAlert("Info", "You are already a library member.");
			return;
		}

		if (um.getAmount() < MEMBERSHIP_FEE) {
			showAlert("Error", "Insufficient balance. Minimum " + MEMBERSHIP_FEE + " QAR required.");
			return;
		}

		boolean alreadyMember = DataManager.getMembersList().stream()
				.anyMatch(m -> m.getFinanceID().equalsIgnoreCase(finID) || m.getUniversityID().equalsIgnoreCase(uniID));

		if (alreadyMember) {
			showAlert("Error", "You are already registered as a library member.");
			return;
		}

		if (!FinancialAccount.chargeFee(um)) {

			showAlert("Error", "Transaction failed during processing.");
			return;
		}

		um.setMembershipStatus("Yes");

		String memID = "M" + (DataManager.getMembersList().size() + 1001);
		Member newMember = new Member(name, address, uniID, finID);

		newMember.setMembershipID(memID);
		newMember.setLibraryMember(true);
		newMember.setTotalReserved(0);
		DataManager.updateMember(newMember);

		showAlert("Success", "Membership applied successfully!\nYour Membership ID: " + memID + "\n" + MEMBERSHIP_FEE
				+ " QAR has been deducted from your account.");
		clearFields();
	}

	private boolean isValidName(String name) {
		if (name == null || name.trim().isEmpty()) {
			return false;
		}
		return name.matches("[\\p{L} .'-]+");
	}

	@FXML
	private void reserveItem() {
		String membershipID = membershipIDField.getText().trim();
		String callNumber = callNumberField.getText().trim();

		if (membershipID.isEmpty() || callNumber.isEmpty()) {
			showAlert("Error", "Membership ID and Call Number are required.");
			return;
		}

		Member member = DataManager.getMembersList().stream().filter(m -> membershipID.equals(m.getMembershipID()))
				.findFirst().orElse(null);

		if (member == null) {
			showAlert("Error", "Member not found!");
			return;
		}

		if (member.getTotalReserved() >= MAX_RESERVATIONS) {
			showAlert("Error", "You have reached the maximum number of reservations (" + MAX_RESERVATIONS + ").");
			return;
		}

		Item item = DataManager.getItemsList().stream().filter(i -> callNumber.equals(i.getCallNumber())).findFirst()
				.orElse(null);

		if (item == null) {
			showAlert("Error", "Item not found!");
			return;
		}

		if (item.isReserved()) {
			showAlert("Error", "Item is not available for reservation.");
			return;
		}

		item.setReserved(true);
		item.setMembershipID(member.getMembershipID());
		member.setTotalReserved(member.getTotalReserved() + 1);

		DataManager.updateItem(item);
		DataManager.updateMember(member);

		showAlert("Success", "You've successfully reserved a " + item.getType() + ".\nYour total reservations: "
				+ member.getTotalReserved() + "/" + MAX_RESERVATIONS);

		clearFields();
	}

	@FXML
	private void goBack() {
		if (currentStage != null)
			currentStage.close();
	}

	@FXML
	private void openMembership() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/MembershipView.fxml"));
			Parent root = loader.load();

			LibraryMenuController controller = loader.getController();
			controller.setCurrentStage(new Stage());
			controller.initControllers(membershipController, reservationController);

			Stage stage = controller.currentStage;
			stage.setTitle("Apply for Membership");
			stage.setScene(new Scene(root));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to open Membership window.");
		}
	}

	@FXML
	private void openReservation() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ReservationView.fxml"));
			Parent root = loader.load();

			LibraryMenuController controller = loader.getController();
			controller.setCurrentStage(new Stage());
			controller.initControllers(membershipController, reservationController);

			Stage stage = controller.currentStage;
			stage.setTitle("Reserve an Item");
			stage.setScene(new Scene(root));
			stage.show();

		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Failed to open Reservation window.");
		}
	}

	@FXML
	private void exitApp() {
		DataManager.saveAllToFile();
		System.exit(0);
	}

	@FXML
	private void showAlert(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

	private void clearFields() {
		if (nameField != null)
			nameField.clear();
		if (addressField != null)
			addressField.clear();
		if (universityIDField != null)
			universityIDField.clear();
		if (financeIDField != null)
			financeIDField.clear();
		if (callNumberField != null)
			callNumberField.clear();
		if (membershipIDField != null)
			membershipIDField.clear();
		if (statusChoice != null)
			statusChoice.getSelectionModel().clearSelection();
	}

}
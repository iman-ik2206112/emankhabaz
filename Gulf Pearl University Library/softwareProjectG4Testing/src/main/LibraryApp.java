package main;

import controller.LibraryMenuController;
import controller.MembershipController;
import controller.ReservationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.DataManager;
import model.AdminstrativeStaff;

public class LibraryApp extends Application {

	public static AdminstrativeStaff pearlSys;

	static {
		DataManager.ensureDataFiles();
		DataManager.loadAllData();
	}

	@Override
	public void start(Stage stage) throws Exception {

		pearlSys = new AdminstrativeStaff();

		MembershipController membershipController = new MembershipController();
		ReservationController reservationController = new ReservationController();

		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/LibraryMenuView.fxml"));
		Scene scene = new Scene(loader.load());

		LibraryMenuController mainMenuController = loader.getController();
		mainMenuController.initControllers(membershipController, reservationController);

		stage.setScene(scene);
		stage.setTitle("Gulf Pearl University Library System");
		stage.show();
	}

	@Override
	public void stop() throws Exception {
		DataManager.saveAllToFile();
		super.stop();
	}

	public static void main(String[] args) {
		launch(args);
	}
}

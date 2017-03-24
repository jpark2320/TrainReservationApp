package application;
	
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.StringTokenizer;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


public class Main extends Application {
	
	private Stage window;
	private Scene loginScene;
	private Scene registerScene;
	private Scene chooseFuncScene;
	private Scene addSchoolInfoScene;
	private Scene viewTrainScheduleScene;
	private Scene scheduleScene;
	private Scene searchTrainScene;
	private Scene selectDepartureScene;
	private Scene extraInfoScene;
	private Scene makeReservationScene;
	private Scene paymentInformationScene;
	private Scene confirmationScene;
	private Scene updateReservationScene;
	private Scene reservationChangeScene;
	private Scene giveReviewScene;
	private Scene viewReviewScene;
	private Scene reviewTrainScene;
	private Scene updateSubmitScene;
	private Scene updateSubmitResultScene;
	private Scene chooseFuncManagerScene;
	private Scene viewRevenueReportScene;
	private Scene viewPopularRouteReportScene;
	private Scene cancelReservationScene;
	private Scene cancelRevNextScene;
	private Scene cancelRevNext2Scene;
	
	private String userID;
	private String studEmail;
	private String selectedPrice;
	private String selectedClass;
	private int numBaggage;
	private String passName;
	private String trainNum;
	private int reviewNum = 0;
	private double totalCost = 0.0;
	private double changeFee = 50.0;
	private double revPrice = 0.0;
	private double TCR = 0.0;
	private String DC;
	private double AR = 0.0;
	private double discount = 0.0;
	private String cancelID;
	private String cancelTrain;
	private Connection connectionToCs4400;
	private ArrayList<String> tempA;
	private ArrayList<String> tempB;
	private ArrayList<String> tempC;
	private ArrayList<String> tempD;
	private ArrayList<String> tempE;
	private ArrayList<String> tempF;
	private ArrayList<String> tempG;
	private ArrayList<String> tempH;
	private String departsFrom;
	private String arrivesAt;
	private LocalDate datePicked;
	private int revID;
	private String uRevID;
	private String revTrainNum;;
	private String updateRevDate;
	private LocalDate today = LocalDate.now();
	private ObservableList<String> e0 = FXCollections.observableArrayList();
	private ObservableList<String> e1 = FXCollections.observableArrayList();
	private ObservableList<String> e2 = FXCollections.observableArrayList();
	private ObservableList<String> e3 = FXCollections.observableArrayList();
	private ObservableList<String> e4 = FXCollections.observableArrayList();
	private ObservableList<String> e5 = FXCollections.observableArrayList();
	private ObservableList<String> e6 = FXCollections.observableArrayList();
	private ObservableList<String> e7 = FXCollections.observableArrayList();
	private int i = 0;
	private Label labelRemove = new Label("Remove");
	private VBox e8 = new VBox(10, labelRemove);
	private TextField typeCostMake;
	private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	
	
	
	@Override
	public void start(Stage stage) {
		try {
			connectSql();
			window = stage;
			loginScene();
			stage.setScene(loginScene);
			stage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void warning(String str) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(str);
		alert.showAndWait();
	}
	
	private void connectSql() {
		final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
		final String DB_URL = "jdbc:mysql://academic-mysql.cc.gatech.edu/cs4400_Team_3";

	    final String USER = "cs4400_Team_3";	// db user
	    final String PASS = "3DPkl65B";		// db pw

	    try {
			Class.forName(JDBC_DRIVER);
			connectionToCs4400 = DriverManager.getConnection(DB_URL, USER, PASS);
	    } catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void closeSql() {
		try {
			connectionToCs4400.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkCredential(String idIn, String pwIn) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT Username, Password FROM User";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    while (rs.next()) {
			    String id = rs.getString("Username").trim();
			    String pw = rs.getString("Password").trim();
			
			    if (idIn.equals(id) && pwIn.equals(pw)) {
				    return true;
			    }		  
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void loginScene() {
		Label labelLogin = new Label("Login");
		labelLogin.setStyle("-fx-font-size: 30;");
		
		Label labelID = new Label("Username");
		TextField typeID = new TextField();
		HBox hBoxID = new HBox(10, labelID, typeID);
		hBoxID.setAlignment(Pos.CENTER);
				
		Label labelPW = new Label("Password");
		TextField typePW = new TextField();
		HBox hBoxPW = new HBox(10, labelPW, typePW);
		hBoxPW.setAlignment(Pos.CENTER);
		
		Button btnLogin = new Button("Login");
		btnLogin.setOnAction(e -> {
			userID = typeID.getText().trim();
			String pwIn = typePW.getText().trim();
			if (checkCredential(userID, pwIn)) {
				for (int i = 0; i < 5; i++) {
					if (userID.equals("master" + i)) {
						chooseFuncManager();
						window.setScene(chooseFuncManagerScene);
						window.centerOnScreen();
						return;
					}
				}
				chooseFunc();
				window.setScene(chooseFuncScene);
				window.centerOnScreen();
			} else {
		    	warning("Wrong ID or Password. Try Again.");
		    }
		});
		
		Button btnQuit = new Button("Quit");
		btnQuit.setOnAction(e -> {
			closeSql();
			System.exit(0);
		});
		
		Button btnRegister = new Button("Register");
		btnRegister.setOnAction(e-> {
			registerScene();
			window.setScene(registerScene);
		});
		
		HBox hBoxBtn = new HBox(10, btnQuit, btnLogin);
		hBoxBtn.setAlignment(Pos.CENTER);
		
		VBox vBoxLogin = new VBox(20, labelLogin, hBoxID, hBoxPW, hBoxBtn, btnRegister);
		vBoxLogin.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxLogin);
		loginScene = new Scene(stackPane,600,400);
	}
	
	private boolean checkUsername(String idIn, String mailIn) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sqlCheck = "SELECT Username, Email FROM User Join Customer USING(Username)";
		    ResultSet rs = stmt.executeQuery(sqlCheck);
		    
		    while (rs.next()) {
			    String id = rs.getString("Username").trim();
			    String email = rs.getString("Email").trim();
			    if (idIn.equals(id) || mailIn.equals(email)) {
				    return true;
			    }		  
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void register(String idIn, String mailIn, String pwIn, String pwIn2) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
		    
		    if (studEmail.substring(studEmail.length() -3).equals("edu")) {
		    	String sql = "INSERT INTO User values('" + idIn + "', '" + pwIn + "')";
				String sql2 = "INSERT INTO Customer values('" + idIn + "', '" + mailIn + "', 1)";
				stmt.executeUpdate(sql);
				stmt.executeUpdate(sql2);
			} else {
				String sql = "INSERT INTO User values('" + idIn + "', '" + pwIn + "')";
				String sql2 = "INSERT INTO Customer values('" + idIn + "', '" + mailIn + "', 0)";
				stmt.executeUpdate(sql);
				stmt.executeUpdate(sql2);
			}  
	    	
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		} 
	}
	
	private void registerScene() {
		Label labelRegister = new Label("New User Registration");
		labelRegister.setStyle("-fx-font-size: 30;");
		
		Label labelID = new Label("Username");
		TextField typeID = new TextField();
		HBox hBoxID = new HBox(10, labelID, typeID);
		hBoxID.setAlignment(Pos.CENTER);
		
		Label labelMail = new Label("Email Address");
		TextField typeMail = new TextField();
		HBox hBoxMail = new HBox(10, labelMail, typeMail);
		hBoxMail.setAlignment(Pos.CENTER);
		
		Label labelPW = new Label("Password");	
		TextField typePW = new TextField();
		HBox hBoxPW = new HBox(10, labelPW, typePW);
		hBoxPW.setAlignment(Pos.CENTER);
		
		Label labelRePW = new Label("Confirm Password");
		TextField typeRePW = new TextField();
		HBox hBoxRePW = new HBox(10, labelRePW, typeRePW);
		hBoxRePW.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(loginScene);
		});
		Button btnCreate = new Button("Create");
		btnCreate.setOnAction(e -> {
			
		studEmail = typeMail.getText();	
			
		if ((!typeID.getText().isEmpty() && !typeMail.getText().isEmpty()) 
				&& (!typePW.getText().isEmpty() && !typeRePW.getText().isEmpty())) {
			if (!checkUsername(typeID.getText(), typeMail.getText())) {
				if (typeMail.getText().contains("@") && typeMail.getText().contains(".")) {
					if (typePW.getText().equals(typeRePW.getText())) {
						register(typeID.getText(), typeMail.getText(), typePW.getText(), typeRePW.getText());
						window.setScene(loginScene);
						warning("New Account was created.");
					} else {
						warning("Password and confirmation password must be same.");
					}
				} else {
					warning("The email address should have the format [ (id)@(domain).??? ]");
				}
			} else {
				warning("The username and email already exist.");
			}
		} else {
			warning("You should fill in all the fields.");
		} 
			
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnCreate);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxLogin = new VBox(30, labelRegister, hBoxID, hBoxMail, hBoxPW, hBoxRePW, hBoxBtns);
		vBoxLogin.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxLogin);
		registerScene = new Scene(stackPane,600,400);
	}
	
	private void chooseFunc() {
		Label labelFunc = new Label("Choose Functionality");
		labelFunc.setStyle("-fx-font-size: 30;");
		
		Button btnView = new Button("View Train Schedule");
		btnView.setOnAction(e -> {
			viewTrainSchedule();
			window.setScene(viewTrainScheduleScene);
			
		});
	
		Button btnMake = new Button("Make a new reservation");
		btnMake.setOnAction(e -> {
			searchTrain();
			window.setScene(searchTrainScene);
		});
		
		Button btnUpdate = new Button("Update a reservation");
		btnUpdate.setOnAction(e -> {
			updateReservation();
			window.setScene(updateReservationScene);
		});
		
		Button btnCancel = new Button("Cancel a reservation");
		btnCancel.setOnAction(e -> {
			cancelReservation();
			window.setScene(cancelReservationScene);
		});
		
		Button btnGive = new Button("Give Review");
		btnGive.setOnAction(e -> {
			giveReview();
			window.setScene(giveReviewScene);
		});
		
		Button btnReview = new Button("View Review");
		btnReview.setOnAction(e -> {
			viewReview();
			window.setScene(viewReviewScene);
		});
		
		Button btnAdd = new Button("Add school information(student discount)");
		btnAdd.setOnAction(e -> {
			addSchoolInfo();
			window.setScene(addSchoolInfoScene);			
		});
		
		Button btnLogout = new Button("Log out");
		btnLogout.setOnAction(e -> {
			discount = 0;
			window.setScene(loginScene);
		});
		
		VBox vBoxFunc = new VBox(10, labelFunc, btnView, btnMake, btnUpdate, btnCancel, btnGive, btnReview, btnAdd, btnLogout);
		vBoxFunc.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxFunc);
		chooseFuncScene = new Scene(stackPane, 600, 400);
	}
	
	private void addSchoolInfo() {
		Label labelAdd = new Label("Add School Info");
		labelAdd.setStyle("-fx-font-size: 30;");
		
		Label labelMail = new Label("School Email Address");
		TextField typeMail = new TextField();
		HBox hBoxMail = new HBox(10, labelMail, typeMail);
		hBoxMail.setAlignment(Pos.CENTER);
		
		VBox vBoxMail = new VBox(10, labelAdd, hBoxMail);
		vBoxMail.setAlignment(Pos.CENTER);
		
		Label warning = new Label("Your school email address ends with .edu");
		warning.setStyle("-fx-font-size: 10;");
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
		});
		Button btnSubmit = new Button("Submit");
		btnSubmit.setOnAction(e -> {
			if (!typeMail.getText().isEmpty()) {
				String mail = typeMail.getText();
				if (mail.substring(mail.length() - 3, mail.length()).equals("edu")) {
					discount = 0.8;
					warning("Student discount is applied.");
				} else {
					warning("The email cannot apply student discount.");
				}
			} else {
				warning("You should fill in the field");
			}
		});
		
		HBox hBoxBtns = new HBox(30, btnBack, btnSubmit);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxMail2 = new VBox(vBoxMail, warning, hBoxBtns);
		vBoxMail2.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxMail2);
		addSchoolInfoScene = new Scene(stackPane, 600, 400);
	}
	
	private boolean checkTrainNumber(String numberIn) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT TrainNumber FROM TrainRoute";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    while (rs.next()) {
			    String number = rs.getString("TrainNumber").trim();
			    
			    if (numberIn.equals(number)) {
				    return true;
			    } 
		    }
		    rs.close();
		    stmt.close();
		     
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void viewTrainSchedule() {
		Label labelView = new Label("View Train Schedule");
		labelView.setStyle("-fx-font-size: 30;");
		
		Label labelTrain = new Label("Train Number");
		TextField typeTrain = new TextField();
		HBox hBoxTrain = new HBox(10, labelTrain, typeTrain);
		hBoxTrain.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
		});
		Button btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			if(checkTrainNumber(typeTrain.getText())) {
				getTrainSchedule(typeTrain.getText());
				schedule();
				window.setScene(scheduleScene);
				window.centerOnScreen();
			} else {
		    	warning("Wrong Train Number. Try Again.");
		    }
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnSearch);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxTrain = new VBox(30, labelView, hBoxTrain, hBoxBtns);
		vBoxTrain.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxTrain);
		viewTrainScheduleScene = new Scene(stackPane, 600, 400);
	}
	
	private void getTrainSchedule(String train) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM TrainRouteStopStation WHERE TrainNumber = '" 
					+ train + "' ORDER BY ArrivalTime";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    
		    while (rs.next()) {
		    	String tNum = rs.getString("TrainNumber").trim();
			    String aTime = rs.getString("ArrivalTime").trim();
			    String dTime = rs.getString("DepartureTime").trim();
			    String sName = rs.getString("StationName").trim();
			    tempA.add(tNum);
			    tempB.add(aTime);
			    tempC.add(dTime);
			    tempD.add(sName);
		    }
		    rs.close();
		    stmt.close();
		     
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void schedule() {
		
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> tNumbers = new ListView<>(tA);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> depTime = new ListView<>(tB);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> arrTime = new ListView<>(tC);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> staName = new ListView<>(tD);
		
		Label labelView = new Label("View Train Schedule");
		labelView.setStyle("-fx-font-size: 30;");
		
		Label labelTrain = new Label("Train number");
		VBox vBoxTrain = new VBox(30, labelTrain, tNumbers);
		vBoxTrain.setAlignment(Pos.CENTER);
		
		Label arrivalTime = new Label("Arrival Time");
		VBox vBoxDeparture = new VBox(30, arrivalTime, depTime);
		vBoxDeparture.setAlignment(Pos.CENTER);
		
		Label departureTime = new Label("Departure Time");
		VBox vBoxArrival = new VBox(30, departureTime, arrTime);
		vBoxArrival.setAlignment(Pos.CENTER);
		
		Label stationName = new Label("Station");
		VBox vBoxStation = new VBox(30, stationName, staName);
		vBoxStation.setAlignment(Pos.CENTER);
		
		HBox hBoxView = new HBox(20, vBoxTrain, vBoxDeparture, vBoxArrival, vBoxStation);
		hBoxView.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(viewTrainScheduleScene);
		});
		
		VBox vBoxView = new VBox(30, labelView, hBoxView, btnBack);
		vBoxView.setAlignment(Pos.BASELINE_CENTER);
		
		StackPane stackPane = new StackPane(vBoxView);
		scheduleScene = new Scene(stackPane, 700, 600);
	}
	
	private void getStation() {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT Name, Location FROM Station";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    
		    while (rs.next()) {
			    String name = rs.getString("Name").trim();
			    String location = rs.getString("Location").trim();
			    tempA.add(name);
			    tempB.add(location);
		    }
		    rs.close();
		    stmt.close();
		     
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void searchTrain() {
		getStation();
		
		Label labelView = new Label("Search Train");
		labelView.setStyle("-fx-font-size: 30;");
		
		Label labelDepart = new Label("Depart From");
		ChoiceBox<String> departs = new ChoiceBox<>();
		departs.getItems().addAll(tempA);
		HBox hBoxDepart = new HBox(10, labelDepart, departs);
		hBoxDepart.setAlignment(Pos.CENTER);
		
		Label labelArrive = new Label("Arrives At");
		ChoiceBox<String> arrives = new ChoiceBox<>();
		arrives.getItems().addAll(tempA);
		HBox hBoxArrive = new HBox(10, labelArrive, arrives);
		hBoxArrive.setAlignment(Pos.CENTER);
		
		Label labelDate = new Label("Departure Date");
		DatePicker dPick = new DatePicker();
		HBox hBoxDate = new HBox(10, labelDate, dPick);
		hBoxDate.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
		});
		Button btnFind = new Button("Find Trains");
		btnFind.setOnAction(e -> {
			if ((departs.getValue() != null && arrives.getValue() != null) 
					&& dPick.getValue() != null) {
				if (!(departs.getValue().equals(arrives.getValue()))) {
					departsFrom = departs.getValue();
					arrivesAt = arrives.getValue();
					datePicked = dPick.getValue();
					if (datePicked.isAfter(today)) {
						getSelectedDeparture(departsFrom, arrivesAt, datePicked);
						selectDeparture();
						window.setScene(selectDepartureScene);
						window.centerOnScreen();
					} else {
						warning("The date should be in the future.");
					}
				} else {
					warning("You cannnot choose the same location.");
				}
			} else {
				warning("You should fill in all the fields.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnFind);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxSearch = new VBox(10, labelView, hBoxDepart, hBoxArrive, hBoxDate, hBoxBtns);
		vBoxSearch.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxSearch);
		searchTrainScene = new Scene(stackPane, 600, 400);
	}
	
	private void getSelectedDeparture(String dLoc, String aLoc, LocalDate dDate) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM TrainRoute JOIN TrainRouteStopStation " 
						+ "USING (TrainNumber) WHERE StationName = '" + dLoc + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    
		    while (rs.next()) {
		    	String t1 = rs.getString("TrainNumber").trim();
		    	String t2 = rs.getString("DepartureTime").trim();
		    	String t3 = rs.getString("FirstClassPrice").trim();
			    String t4 = rs.getString("SecondClassPrice").trim();
			    tempA.add(t1);
			    tempB.add(t2);
			    tempC.add(t3);
			    tempD.add(t4);
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
//	private void findTrainNumber(String price) {	
//		try {
//			Statement stmt = connectionToCs4400.createStatement();
//			String sql = "SELECT TrainNumber FROM TrainRoute WHERE FirstClassPrice = '" + price 
//							+ "' or SecondClassPrice = '" + price + "'";
//		    ResultSet rs = stmt.executeQuery(sql);
//		    while (rs.next()) {
//		    	trainNum = rs.getString("TrainNumber").trim();
//		    }
//		    rs.close();
//		    stmt.close();
//		} catch (SQLException e) {
//			e.printStackTrace();
//		}
//	}
	
	private void selectDeparture() {
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> List1 = new ListView<>(tA);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> List2 = new ListView<>(tB);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> List3 = new ListView<>(tC);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> List4 = new ListView<>(tD);
		
		Label labelDeparture = new Label("Select Departure");
		labelDeparture.setStyle("-fx-font-size: 30;");
		
		Label labelTrain = new Label("Train number");
		VBox vBoxTrain = new VBox(30, labelTrain, List1);
		vBoxTrain.setAlignment(Pos.CENTER);
		
		Label labelFirst = new Label("1st Class Price");
		VBox vBoxFirst = new VBox(30, labelFirst, List3);
		vBoxTrain.setAlignment(Pos.CENTER);
		
		
		Label labelSecond = new Label("2nd Class Price");
		VBox vBoxSecond = new VBox(30, labelSecond, List4);
		vBoxTrain.setAlignment(Pos.CENTER);
		
		HBox hBoxView = new HBox(50, vBoxTrain, vBoxFirst, vBoxSecond);
		hBoxView.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(searchTrainScene);
		});
		Button btnNext = new Button("Next");
		btnNext.setOnAction(e -> {
			String tempTrnNum = List1.getSelectionModel().getSelectedItem();
			String fstPrice = List3.getSelectionModel().getSelectedItem();
			String sndPrice = List4.getSelectionModel().getSelectedItem();
			if (!e1.contains(tempTrnNum)) {
				if (fstPrice != null 
						|| sndPrice != null) {
					if (sndPrice == null) {
						selectedPrice = fstPrice;
						selectedClass = "1st Class";
					} else {
						selectedPrice = sndPrice;
						selectedClass = "2st Class";
					}
					trainNum = tempTrnNum;
//					findTrainNumber(selectedPrice);
					extraInfo();
					window.setScene(extraInfoScene);
				} else {
					warning("You should choose at least one of the prices");
				}
			} else {
				warning("You already have the train [" + tempTrnNum + "].");
			}
			
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnNext);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxSelect = new VBox(30, labelDeparture, hBoxView, hBoxBtns);
		vBoxSelect.setAlignment(Pos.BASELINE_CENTER);
		
		StackPane stackPane = new StackPane(vBoxSelect);
		selectDepartureScene = new Scene(stackPane, 800, 600);
	}
	
	private void extraInfo() {
		Label labelExtra = new Label("Travel Extras & Passenger Info");
		labelExtra.setStyle("-fx-font-size: 30;");
		
		Label labelBaggage = new Label("Number of Baggage");
		ChoiceBox<Integer> numBag = new ChoiceBox<>();
		numBag.getItems().addAll(1, 2, 3, 4);
		HBox hBoxBaggage = new HBox(10, labelBaggage, numBag);
		hBoxBaggage.setAlignment(Pos.CENTER);
		
		Label warning = new Label("Every passanger can bring up to 4 baggage. 2 Free of charge, 2 for $30 per bag");
		warning.setStyle("-fx-font-size: 10;");
		
		Label labelName = new Label("Passanger Name");
		TextField typeName = new TextField();
		HBox hBoxName = new HBox(10, labelName, typeName);
		hBoxName.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(selectDepartureScene);
			window.centerOnScreen();
		});
		Button btnNext = new Button("Next");
		btnNext.setOnAction(e -> {
			if ((numBag.getValue() != null && typeName.getText() != null) && !typeName.getText().isEmpty()) {
				numBaggage = numBag.getValue();
				passName = typeName.getText();
				makeReservation();
				++i;
				window.setScene(makeReservationScene);
				window.centerOnScreen();
			} else {
				warning("You should fill in all the fields.");
			}
			
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnNext);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxExtra = new VBox(30, labelExtra, hBoxBaggage, warning, hBoxName, hBoxBtns);
		vBoxExtra.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxExtra);
		extraInfoScene = new Scene(stackPane, 600, 400);
	}
	
	private void cardInfo(String idIn) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM PaymentInfo WHERE C_Username = '" + idIn + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    
		    while (rs.next()) {
		    	String t1 = rs.getString("CardNumber").trim();
		    	tempA.add(t1);
			}
		    rs.close();
		    stmt.close();
		     
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkRevID(int rev) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT ReservationID FROM Reservation";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    while (rs.next()) {
			    String id = rs.getString("ReservationID").trim();
			
			    if (Integer.toString(rev).equals(id)) {
				    return true;
			    }		  
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void make(String trainNum, String passName, String cls
					, LocalDate dDate, String dFrom, String aAt, int numBag, String cardNum) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			
			String sql = "INSERT INTO Reservation(IsCancelled, CardNumber, Username, TotalCost) "
					+ "VALUES(0, '" + cardNum + "', '" + userID + "', " + totalCost + ")";
	
			stmt.executeUpdate(sql);
			
			String sql2 = "SELECT MAX(ReservationID) FROM Reservation";
			ResultSet rs = stmt.executeQuery(sql2);
			while (rs.next()) {
				revID = rs.getInt("MAX(ReservationID)");
			}
			
			for (int i = 0; i < e1.size(); i++) {
				if (e1.get(i) != " ") {
					String sql3 = "INSERT INTO ReservationReservesTrainRoute "
							+ "VALUES('" + revID + "', '" + e1.get(i) + "', '" + e7.get(i) + "', '" + e4.get(i) 
							+ "', '" + e0.get(i) + "', '" + e2.get(i) + "', '" + e3.get(i) + "', " + e6.get(i) + ")";
					System.out.println(sql3);
					stmt.executeUpdate(sql3);
				}
			}
			rs.close();
			stmt.close();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void makeReservation() {
		
		double eachCost = 0;
		if (numBaggage == 3) {
			eachCost += 30;
			totalCost += 30;
		} else if (numBaggage == 4) {
			eachCost += 60;
			totalCost += 60;
		}
		eachCost += Double.parseDouble(selectedPrice);
		totalCost += Double.parseDouble(selectedPrice);
		Label labelDiscount;
		if (discount == 0) {
			labelDiscount = new Label("Student Discount didn't Apply.");
		} else {
			totalCost = totalCost * discount;
			labelDiscount = new Label("Student Discount Applied.");
		}
		
		Double hei = 100.0;
		Double wid = 170.0;
		Label labelMake = new Label("Make Reservation");
		labelMake.setStyle("-fx-font-size: 30;");
		
		Label labelSelected = new Label("The below is currently selected.");
		labelSelected.setStyle("-fx-font-size: 10;");
		
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); 
		e0.addAll(formatter.format(datePicked)); // doesn't need to show on the screen.
		
		Label labelTrain = new Label("Train Number");
		e1.addAll(trainNum);
		ListView<String> v1 = new ListView<>(e1);
		v1.setMaxHeight(hei);
		v1.setMaxWidth(wid);
		VBox vBoxTrain = new VBox(10, labelTrain, v1);
		vBoxTrain.setAlignment(Pos.CENTER);
		
		Label labelDepart = new Label("Departs From");
		e2.addAll(departsFrom);
		ListView<String> v2 = new ListView<>(e2);
		v2.setMaxHeight(hei);
		v2.setMaxWidth(wid);
		VBox vBoxDepart = new VBox(10, labelDepart, v2);
		vBoxDepart.setAlignment(Pos.CENTER);
		
		Label labelArrive = new Label("Arrives At");
		e3.addAll(arrivesAt);
		ListView<String> v3 = new ListView<>(e3);
		v3.setMaxHeight(hei);
		v3.setMaxWidth(wid);
		VBox vBoxArrive = new VBox(10, labelArrive, v3);
		vBoxArrive.setAlignment(Pos.CENTER);
		
		Label labelClass = new Label("Class");
		e4.addAll(selectedClass);
		ListView<String> v4 = new ListView<>(e4);
		v4.setMaxHeight(hei);
		v4.setMaxWidth(wid);
		VBox vBoxClass = new VBox(10, labelClass, v4);
		vBoxClass.setAlignment(Pos.CENTER);
		
		Label labelPrice = new Label("Price");
		e5.addAll(Double.toString(eachCost));
		ListView<String> v5 = new ListView<>(e5);
		v5.setMaxHeight(hei);
		v5.setMaxWidth(wid);
		VBox vBoxPrice = new VBox(10, labelPrice, v5);
		vBoxPrice.setAlignment(Pos.CENTER);
		
		Label labelBaggage = new Label("# of Baggages");
		e6.addAll(Integer.toString(numBaggage));
		ListView<String> v6 = new ListView<>(e6);
		v6.setMaxHeight(hei);
		v6.setMaxWidth(wid);
		VBox vBoxBaggage = new VBox(10, labelBaggage, v6);
		vBoxBaggage.setAlignment(Pos.CENTER);
		
		Label labelName = new Label("Passenger Name");
		e7.addAll(passName);
		ListView<String> v7 = new ListView<>(e7);
		v7.setMaxHeight(hei);
		v7.setMaxWidth(wid);
		VBox vBoxName = new VBox(10, labelName, v7);
		vBoxName.setAlignment(Pos.CENTER);
		
		Button btnRemove = new Button("Remove " + i);
		btnRemove.setOnAction(e -> {
			String s = btnRemove.getText();
			StringTokenizer token = new StringTokenizer(s);
			String tmp = new String();
			while (token.hasMoreTokens()) {
				tmp = token.nextToken();
			}
			int index = Integer.parseInt(tmp);
			e1.set(index, " ");
			e2.set(index, " ");
			e3.set(index, " ");
			e4.set(index, " ");
			totalCost -= Double.parseDouble(e5.get(index));
			e5.set(index, " ");
			e6.set(index, " ");
			e7.set(index, " "); // Cost doesn't minimize..
			
			typeCostMake.clear();
			typeCostMake.appendText(Double.toString(totalCost));
			
			window.setScene(makeReservationScene);
			window.centerOnScreen();
		});
		e8.getChildren().addAll(btnRemove);
		
		HBox hBoxMake = new HBox(vBoxTrain, vBoxDepart
				, vBoxArrive, vBoxClass, vBoxPrice, vBoxBaggage, vBoxName, e8);
		hBoxMake.setAlignment(Pos.CENTER);
		
		Label labelCost = new Label("Total Cost");
		typeCostMake = new TextField(Double.toString(totalCost));
		HBox hBoxCost = new HBox(10, labelCost, typeCostMake);
		hBoxCost.setAlignment(Pos.CENTER);
		
		cardInfo(userID);
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		Label labelCard = new Label("Use Card");
		ChoiceBox<String> choiceCard = new ChoiceBox<>(tA);
		Button btnAddCard = new Button("Add Card");
		btnAddCard.setOnAction(e -> {
			paymentInformation();
			window.setScene(paymentInformationScene);
			window.centerOnScreen();
		});
		HBox hBoxCard = new HBox(10, labelCard, choiceCard, btnAddCard);
		hBoxCard.setAlignment(Pos.CENTER);
		
		Button btnAdd = new Button("Continue adding a train");
		btnAdd.setOnAction(e -> {
			window.setScene(searchTrainScene);
			window.centerOnScreen();
		});
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(extraInfoScene);
			window.centerOnScreen();
		});
		Button btnSubmit = new Button("Submit");
		btnSubmit.setOnAction(e -> {
			if (choiceCard.getValue() != null) {
				make(trainNum, passName, selectedClass, datePicked, departsFrom
						, arrivesAt, numBaggage, choiceCard.getValue());
				confirmation();
				window.setScene(confirmationScene);
				window.centerOnScreen();
				warning("You made a reservation successfully");
			} else {
				warning("You should choose a card to pay for the reservation.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnSubmit);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxMake = new VBox(10, labelMake, labelSelected, hBoxMake, 
						labelDiscount, hBoxCost, hBoxCard, btnAdd, hBoxBtns);
		vBoxMake.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxMake);
		makeReservationScene = new Scene(stackPane, 1400, 1000);
	}
	
	private void addCard(String cNum, String cName, LocalDate exp, String CVV) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "INSERT INTO PaymentInfo VALUES('" + cNum + "', '" + cName 
					+ "', '" + exp + "', '" + CVV + "', '" + userID +"')";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void deleteCard(String cNum) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "DELETE FROM PaymentInfo WHERE CardNumber LIKE '%" + cNum + "'";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void paymentInformation() {
		Label labelPayment = new Label("Payment Information");
		labelPayment.setStyle("-fx-font-size: 30;");
		
		Label labelAdd = new Label("Add Card");
		
		Label labelName = new Label("Name on Card");
		TextField typeName = new TextField();
		HBox hBoxName = new HBox(10, labelName, typeName);
				
		Label labelCard = new Label("Card Number");
		TextField typeCard = new TextField();
		HBox hBoxCard = new HBox(10, labelCard, typeCard);
		
		Label labelCVV = new Label("CVV");
		TextField typeCVV = new TextField();
		HBox hBoxCVV = new HBox(10, labelCVV, typeCVV);
		
		Label labelEXP = new Label("Expiration Date");
		DatePicker dPick = new DatePicker();
		HBox hBoxEXP = new HBox(10, labelEXP, dPick);
		
		Button btnAddSubmit = new Button("Add Submit");
		btnAddSubmit.setOnAction(e -> {
			if ((typeName.getText() != "" && typeCard.getText() != "") 
					&& (typeCVV != null && dPick.getValue() != null)) {
				if (dPick.getValue().isAfter(today)) {
					addCard(typeCard.getText(), typeName.getText(), dPick.getValue(), typeCVV.getText());
					makeReservation();
					window.setScene(makeReservationScene);
					window.centerOnScreen();
					warning("The new card was issued successfully.");
				} else {
					warning("The day should be in the future.");
				}
			} else {
				warning("You should fill in all the fields.");
			}
		});
		btnAddSubmit.setAlignment(Pos.CENTER);
		
		VBox vBoxAdd = new VBox(30, labelAdd, hBoxName, hBoxCard, hBoxCVV, hBoxEXP, btnAddSubmit);
		vBoxAdd.setAlignment(Pos.BASELINE_CENTER);
		
		Label labelDelete = new Label("Delete Card");
		labelDelete.setAlignment(Pos.CENTER);
		
		cardInfo(userID);
		ObservableList<String> tB = FXCollections.observableArrayList(tempA);
		Label labelNumber = new Label("Card Number");
		ChoiceBox<String> choiceNumber = new ChoiceBox<>(tB);
		HBox hBoxNumber = new HBox(10, labelNumber, choiceNumber);
		hBoxNumber.setAlignment(Pos.CENTER);
		
		Button btnDeleteSubmit = new Button("Delete Submit");
		btnDeleteSubmit.setOnAction(e -> {
			if (choiceNumber.getValue() != "") {
				deleteCard(choiceNumber.getValue());
				makeReservation();
				window.setScene(makeReservationScene);
				window.centerOnScreen();
				warning("The card was deleted successfully.");
			} else {
				warning("You should fill in the card number");
			}
			
		});
		btnDeleteSubmit.setAlignment(Pos.CENTER);
		
		VBox vBoxDelete = new VBox(30, labelDelete, hBoxNumber, btnDeleteSubmit);
		vBoxDelete.setAlignment(Pos.BASELINE_CENTER);
		
		HBox hBoxAD = new HBox(30, vBoxAdd, vBoxDelete);
		hBoxAD.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(makeReservationScene);
			window.centerOnScreen();
		});
		btnBack.setAlignment(Pos.CENTER);
		
		VBox vBoxTotal = new VBox(30, labelPayment, hBoxAD, btnBack);
		vBoxTotal.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxTotal);
		paymentInformationScene = new Scene(stackPane, 600, 600);
	}
	
	private void confirmation() {
		Label labelConfirm = new Label("Confirmation");
		labelConfirm.setStyle("-fx-font-size: 30;");
		
		Label labelID = new Label("Reservation ID");
		TextField typeID = new TextField(Integer.toString(revID));
		HBox hBoxID = new HBox(10, labelID, typeID);
		hBoxID.setAlignment(Pos.CENTER);
		
		Label confirm = new Label("Thank you for purchase! Please save reservation ID for your records.");
		confirm.setStyle("-fx-font-size: 10;");
		
		Button btnBack = new Button("Go Back to Choose Functionality");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
			window.centerOnScreen();
		});
		
		VBox vBoxConfirm = new VBox(30, labelConfirm, hBoxID, confirm, btnBack);
		vBoxConfirm.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxConfirm);
		
		confirmationScene = new Scene(stackPane, 600, 400);	
	}
	
	private void updateReservation() {
		Label labelReservation = new Label("Update Reservation");
		labelReservation.setStyle("-fx-font-size: 30;");
		
		Label labelID = new Label("Reservation ID");
		TextField typeID = new TextField();
		HBox hBoxID = new HBox(10, labelID, typeID);
		hBoxID.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
			window.centerOnScreen();
		});
		Button btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			if (!typeID.getText().isEmpty()) {
				if (checkRevID(Integer.parseInt(typeID.getText()))) {
					findReservation(Integer.parseInt(typeID.getText()));
					uRevID = typeID.getText();
					reservationChange();
					window.setScene(reservationChangeScene);
					window.centerOnScreen();
				} else {
					warning("The reservation ID was not found. Try again.");
				}
			} else {
				warning("You should fill in the field.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnSearch);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxUpdate = new VBox(30, labelReservation, hBoxID, hBoxBtns);
		vBoxUpdate.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxUpdate);
		updateReservationScene = new Scene(stackPane, 600, 400);
	}
	
	private void findReservation(int rev) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM ReservationReservesTrainRoute WHERE ReservationID = '" + rev + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    tempE = new ArrayList<>();
		    tempF = new ArrayList<>();
		    tempG = new ArrayList<>();
		    tempH = new ArrayList<>();		    
		    
		    while (rs.next()) {
		    	String c1 = rs.getString("ReservationID").trim();
		    	String c2 = rs.getString("TrainNumber").trim();
			    String c3 = rs.getString("PassengerName").trim();
			    String c4 = rs.getString("Class").trim();
			    String c5 = rs.getString("DepartureDate").trim();
			    String c6 = rs.getString("DepartsFrom").trim();
			    String c7 = rs.getString("ArrivesAt").trim();
			    String c8 = rs.getString("NumberOfBaggages").trim();
			 
		    	tempA.add(c1);
			    tempB.add(c2);
			    tempC.add(c3);
			    tempD.add(c4);
			    tempE.add(c5);
			    tempF.add(c6);
			    tempG.add(c7);
			    tempH.add(c8);
		    }
		    
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void reservationChange() {
		Double hei = 100.0;
		Double wid = 170.0;
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> t1 = new ListView<>(tA);
		t1.setMaxHeight(hei);
		t1.setMaxWidth(wid);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> t2 = new ListView<>(tB);
		t2.setMaxHeight(hei);
		t2.setMaxWidth(wid);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> t3 = new ListView<>(tC);
		t3.setMaxHeight(hei);
		t3.setMaxWidth(wid);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> t4 = new ListView<>(tD);
		t4.setMaxHeight(hei);
		t4.setMaxWidth(wid);
		ObservableList<String> tE = FXCollections.observableArrayList(tempE);
		ListView<String> t5 = new ListView<>(tE);
		t5.setMaxHeight(hei);
		t5.setMaxWidth(wid);
		ObservableList<String> tF = FXCollections.observableArrayList(tempF);
		ListView<String> t6 = new ListView<>(tF);
		t6.setMaxHeight(hei);
		t6.setMaxWidth(wid);
		ObservableList<String> tG = FXCollections.observableArrayList(tempG);
		ListView<String> t7 = new ListView<>(tG);
		t7.setMaxHeight(hei);
		t7.setMaxWidth(wid);
		ObservableList<String> tH = FXCollections.observableArrayList(tempH);
		ListView<String> t8 = new ListView<>(tH);
		t8.setMaxHeight(hei);
		t8.setMaxWidth(wid);
		
		Label labelReservation = new Label("Update Reservation");
		labelReservation.setStyle("-fx-font-size: 30;");
		
		Label l0 = new Label("Choose the train number that you want to update.");
		l0.setStyle("-fx-font-size: 15;");
		
		Label l1 = new Label("Reservation ID");
		VBox v1 = new VBox(l1, t1);
		
		Label l2 = new Label("Train number");
		VBox v2 = new VBox(l2, t2);
		
		Label l3 = new Label("Passenger Name");
		VBox v3 = new VBox(l3, t3);
		
		Label l4 = new Label("Class");
		VBox v4 = new VBox(l4, t4);
		
		Label l5 = new Label("Departure Date");
		VBox v5 = new VBox(l5, t5);
		
		Label l6 = new Label("Departs From");
		VBox v6 = new VBox(l6, t6);
		
		Label l7 = new Label("Arrives At");
		VBox v7 = new VBox(l7, t7);
		
		Label l8 = new Label("Number Of Baggages");
		VBox v8 = new VBox(l8, t8);
		
		HBox hBoxInfo = new HBox(v1, v2, v3, v4, v5, v6, v7, v8);
		hBoxInfo.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(updateReservationScene);
			window.centerOnScreen();
		});
		Button btnNext = new Button("Next");
		btnNext.setOnAction(e -> {
			if (t2.getSelectionModel().getSelectedItem() != null) {
				revTrainNum = t2.getSelectionModel().getSelectedItem();
				chooseReservation(revTrainNum);
				updateSubmit();
				window.setScene(updateSubmitScene);
				window.centerOnScreen();
			} else {
				warning("You should choose the train number.");
			}
			
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnNext);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxInfo = new VBox(10, labelReservation, l0, hBoxInfo, hBoxBtns);
		vBoxInfo.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxInfo);
		reservationChangeScene = new Scene(stackPane, 1400, 400);
	}
	
	private void chooseReservation(String tNum) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM ReservationReservesTrainRoute WHERE TrainNumber = '" 
						+ tNum + "' and ReservationID = '" + uRevID + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    tempE = new ArrayList<>();
		    tempF = new ArrayList<>();
		    tempG = new ArrayList<>();
		    tempH = new ArrayList<>();		    
		    
		    while (rs.next()) {
		    	String c1 = rs.getString("ReservationID").trim();
		    	String c2 = rs.getString("TrainNumber").trim();
			    String c3 = rs.getString("PassengerName").trim();
			    String c4 = rs.getString("Class").trim();
			    String c5 = rs.getString("DepartureDate").trim();
			    String c6 = rs.getString("DepartsFrom").trim();
			    String c7 = rs.getString("ArrivesAt").trim();
			    String c8 = rs.getString("NumberOfBaggages").trim();
			 
		    	tempA.add(c1);
			    tempB.add(c2);
			    tempC.add(c3);
			    tempD.add(c4);
			    tempE.add(c5);
			    tempF.add(c6);
			    tempG.add(c7);
			    tempH.add(c8);
		    }
		    
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void changeRev(String date) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM ReservationReservesTrainRoute WHERE TrainNumber = '" 
						+ revTrainNum + "' and ReservationID = '" + uRevID + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    tempE = new ArrayList<>();
		    tempF = new ArrayList<>();
		    tempG = new ArrayList<>();
		    tempH = new ArrayList<>();		    
		    
		    while (rs.next()) {
		    	String c1 = rs.getString("ReservationID").trim();
		    	String c2 = rs.getString("TrainNumber").trim();
			    String c3 = rs.getString("PassengerName").trim();
			    String c4 = rs.getString("Class").trim();
			    String c5 = date;
			    String c6 = rs.getString("DepartsFrom").trim();
			    String c7 = rs.getString("ArrivesAt").trim();
			    String c8 = rs.getString("NumberOfBaggages").trim();
			 
		    	tempA.add(c1);
			    tempB.add(c2);
			    tempC.add(c3);
			    tempD.add(c4);
			    tempE.add(c5);
			    tempF.add(c6);
			    tempG.add(c7);
			    tempH.add(c8);
		    }
		    
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateSubmit() {
		Double hei = 100.0;
		Double wid = 170.0;
		
		// First
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> t1 = new ListView<>(tA);
		t1.setMaxHeight(hei);
		t1.setMaxWidth(wid);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> t2 = new ListView<>(tB);
		t2.setMaxHeight(hei);
		t2.setMaxWidth(wid);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> t3 = new ListView<>(tC);
		t3.setMaxHeight(hei);
		t3.setMaxWidth(wid);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> t4 = new ListView<>(tD);
		t4.setMaxHeight(hei);
		t4.setMaxWidth(wid);
		ObservableList<String> tE = FXCollections.observableArrayList(tempE);
		ListView<String> t5 = new ListView<>(tE);
		t5.setMaxHeight(hei);
		t5.setMaxWidth(wid);
		ObservableList<String> tF = FXCollections.observableArrayList(tempF);
		ListView<String> t6 = new ListView<>(tF);
		t6.setMaxHeight(hei);
		t6.setMaxWidth(wid);
		ObservableList<String> tG = FXCollections.observableArrayList(tempG);
		ListView<String> t7 = new ListView<>(tG);
		t7.setMaxHeight(hei);
		t7.setMaxWidth(wid);
		ObservableList<String> tH = FXCollections.observableArrayList(tempH);
		ListView<String> t8 = new ListView<>(tH);
		t8.setMaxHeight(hei);
		t8.setMaxWidth(wid);
		
		Label labelReservation = new Label("Update Reservation");
		labelReservation.setStyle("-fx-font-size: 30;");
		
		Label l0 = new Label("Current Train Ticket");
		l0.setStyle("-fx-font-size: 20;");
		
		Label l1 = new Label("Reservation ID");
		VBox v1 = new VBox(l1, t1);
		
		Label l2 = new Label("Train number");
		VBox v2 = new VBox(l2, t2);
		
		Label l3 = new Label("Passenger Name");
		VBox v3 = new VBox(l3, t3);
		
		Label l4 = new Label("Class");
		VBox v4 = new VBox(l4, t4);
		
		Label l5 = new Label("Departure Date");
		VBox v5 = new VBox(l5, t5);
		
		Label l6 = new Label("Departs From");
		VBox v6 = new VBox(l6, t6);
		
		Label l7 = new Label("Arrives At");
		VBox v7 = new VBox(l7, t7);
		
		Label l8 = new Label("Number Of Baggages");
		VBox v8 = new VBox(l8, t8);
		
		HBox hBoxInfo = new HBox(v1, v2, v3, v4, v5, v6, v7, v8);
		hBoxInfo.setAlignment(Pos.CENTER);
		
		
		Label labelNewD = new Label("New Departure Date");
		DatePicker dPick = new DatePicker();
		HBox hBoxNew = new HBox(10, labelNewD, dPick);
		hBoxNew.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(reservationChangeScene);
			window.centerOnScreen();
		});
		Button btnUpdate = new Button("Update");
		btnUpdate.setOnAction(e -> {
			if (dPick.getValue() != null) {
				if (dPick.getValue().isAfter(today)) {
					
					updateNewDate(uRevID, revTrainNum, dPick.getValue());
					changeRev(updateRevDate);
					updateSubmitResult();
					getUpdatePrice();
					window.setScene(updateSubmitResultScene);
					window.centerOnScreen();
					warning("The new date was updated.");
				} else {
					warning("An update should be made at least 1 day eariler than the departure date.");
				}
			} else {
				warning("You should choose the date.");
			}
			
		});
		HBox hBoxBtns = new HBox(10, btnBack, btnUpdate);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxInfo = new VBox(10, labelReservation, l0, hBoxInfo, hBoxNew, hBoxBtns);
		vBoxInfo.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxInfo);
		updateSubmitScene = new Scene(stackPane, 1400, 400);
	}
	
	private void getUpdatePrice() {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT TotalCost FROM ReservationReservesTrainRoute JOIN Reservation USING(ReservationID) "
					+ "WHERE ReservationID = " + uRevID + " AND TrainNumber = '" + revTrainNum + "'";
			ResultSet rs = stmt.executeQuery(sql);
			String rlt = null;
			while (rs.next()) {
				rlt = rs.getString("TotalCost").trim();
			}
			revPrice = Double.parseDouble(rlt);
			rs.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void updateSubmitResult() {
		Double hei = 100.0;
		Double wid = 170.0;
		Label labelReservation = new Label("Update Reservation");
		labelReservation.setStyle("-fx-font-size: 30;");
		
		ObservableList<String> cA = FXCollections.observableArrayList(tempA); 
		ObservableList<String> cB = FXCollections.observableArrayList(tempB);
		ObservableList<String> cC = FXCollections.observableArrayList(tempC);
		ObservableList<String> cD = FXCollections.observableArrayList(tempD);
		ObservableList<String> cE = FXCollections.observableArrayList(tempE);
		ObservableList<String> cF = FXCollections.observableArrayList(tempF);
		ObservableList<String> cG = FXCollections.observableArrayList(tempG);
		ObservableList<String> cH = FXCollections.observableArrayList(tempH);
		
		ListView<String> a1 = new ListView<>(cA);
		a1.setMaxHeight(hei);
		a1.setMaxWidth(wid);
		
		ListView<String> a2 = new ListView<>(cB);
		a2.setMaxHeight(hei);
		a2.setMaxWidth(wid);
		
		ListView<String> a3 = new ListView<>(cC);
		a3.setMaxHeight(hei);
		a3.setMaxWidth(wid);
		
		ListView<String> a4 = new ListView<>(cD);
		a4.setMaxHeight(hei);
		a4.setMaxWidth(wid);
		
		ListView<String> a5 = new ListView<>(cE);
		a5.setMaxHeight(hei);
		a5.setMaxWidth(wid);
		
		ListView<String> a6 = new ListView<>(cF);
		a6.setMaxHeight(hei);
		a6.setMaxWidth(wid);
		
		ListView<String> a7 = new ListView<>(cG);
		a7.setMaxHeight(hei);
		a7.setMaxWidth(wid);
		
		ListView<String> a8 = new ListView<>(cH);
		a8.setMaxHeight(hei);
		a8.setMaxWidth(wid);
		
		Label lb = new Label("Updated Train Ticket");
		lb.setStyle("-fx-font-size: 20;");
		
		Label b1 = new Label("Reservation ID");
		VBox z1 = new VBox(b1, a1);
		
		Label b2 = new Label("Train number");
		VBox z2 = new VBox(b2, a2);
		
		Label b3 = new Label("Passenger Name");
		VBox z3 = new VBox(b3, a3);
		
		Label b4 = new Label("Class");
		VBox z4 = new VBox(b4, a4);
		
		Label b5 = new Label("Departure Date");
		VBox z5 = new VBox(b5, a5);
		
		Label b6 = new Label("Departs From");
		VBox z6 = new VBox(b6, a6);
		
		Label b7 = new Label("Arrives At");
		VBox z7 = new VBox(b7, a7);
		
		Label b8 = new Label("Number Of Baggages");
		VBox z8 = new VBox(b8, a8);
		
		HBox hBoxInfo = new HBox(z1, z2, z3, z4, z5, z6, z7, z8);
		hBoxInfo.setAlignment(Pos.CENTER);
		
		Label labelCF = new Label("Change Fee");
		TextField typeCF = new TextField(Double.toString(changeFee));
		HBox hBoxCF = new HBox(10, labelCF, typeCF);
		hBoxCF.setAlignment(Pos.CENTER);
		
		Label labelUTC = new Label("Updated Total Cost");
		TextField typeUTC = new TextField("TotalCost + 50");		
		HBox hBoxUTC = new HBox(10, labelUTC, typeUTC);
		hBoxUTC.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(updateSubmitScene);
			window.centerOnScreen();
		});
		Button btnNext = new Button("Submit");
		btnNext.setOnAction(e -> {
			window.setScene(chooseFuncScene);
			window.centerOnScreen();
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnNext);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxInfo = new VBox(10, labelReservation, hBoxInfo, lb, hBoxCF, hBoxUTC, hBoxBtns);
		vBoxInfo.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxInfo);
		updateSubmitResultScene = new Scene(stackPane, 1400, 400);
	}
	
	
	
	private void updateNewDate(String revID, String revTrain, LocalDate date) {
		try {
			Statement stmt = connectionToCs4400.createStatement(); 
			
			updateRevDate = formatter.format(date);
			String sql = "UPDATE ReservationReservesTrainRoute "
					+ "SET DepartureDate = '" + updateRevDate 
					+ "' WHERE ReservationID = '" + revID + "' AND TrainNumber = '" + revTrain + "'";
		    
			stmt.executeUpdate(sql);
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void cancelReservation() {
		Label labelCancel = new Label("Cancel Reservation");
		labelCancel.setStyle("-fx-font-size: 30;");
		
		Label labelRevID = new Label("Reservation ID");
		TextField typeRevID = new TextField();
		HBox hBoxRevID = new HBox(10, labelRevID, typeRevID);
		hBoxRevID.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
			window.centerOnScreen();
		});
		Button btnSearch = new Button("Search");
		btnSearch.setOnAction(e -> {
			cancelID = typeRevID.getText();
			if (!typeRevID.getText().isEmpty()) {
				cancel(typeRevID.getText());
				cancelRevNext();
				window.setScene(cancelRevNextScene);
				window.centerOnScreen();
			} else {
				warning("You should fill in the filed.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnSearch);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxCancel = new VBox(30, labelCancel, hBoxRevID, hBoxBtns);
		vBoxCancel.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxCancel);
		cancelReservationScene = new Scene(stackPane, 600, 400);
	}
	
	private void cancel(String revID) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT TrainNumber, DepartsFrom, ArrivesAt, Class, TotalCost, "
					+ "NumberOfBaggages, PassengerName, DepartureDate FROM ReservationReservesTrainRoute "
					+ "JOIN Reservation USING(ReservationID) WHERE ReservationID = '" 
						+ revID + "' AND IsCancelled = 0";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    tempE = new ArrayList<>();
		    tempF = new ArrayList<>();
		    tempG = new ArrayList<>();		    
		    
		    while (rs.next()) {
		    	String c1 = rs.getString("TrainNumber").trim();
		    	String c2 = rs.getString("DepartsFrom").trim();
			    String c3 = rs.getString("ArrivesAt").trim();
			    String c4 = rs.getString("Class").trim();
			    String c5 = rs.getString("TotalCost").trim();
			    TCR = Double.parseDouble(rs.getString("TotalCost").trim());
			    String c6 = rs.getString("NumberOfBaggages").trim();
			    String c7 = rs.getString("PassengerName").trim();
			    DC = rs.getString("DepartureDate").trim();
		    	tempA.add(c1);
			    tempB.add(c2);
			    tempC.add(c3);
			    tempD.add(c4);
			    tempE.add(c5);
			    tempF.add(c6);
			    tempG.add(c7);
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void cancelRevNext() {
		Double hei = 100.0;
		Double wid = 170.0;
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> t1 = new ListView<>(tA);
		t1.setMaxHeight(hei);
		t1.setMaxWidth(wid);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> t2 = new ListView<>(tB);
		t2.setMaxHeight(hei);
		t2.setMaxWidth(wid);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> t3 = new ListView<>(tC);
		t3.setMaxHeight(hei);
		t3.setMaxWidth(wid);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> t4 = new ListView<>(tD);
		t4.setMaxHeight(hei);
		t4.setMaxWidth(wid);
		ObservableList<String> tE = FXCollections.observableArrayList(tempE);
		ListView<String> t5 = new ListView<>(tE);
		t5.setMaxHeight(hei);
		t5.setMaxWidth(wid);
		ObservableList<String> tF = FXCollections.observableArrayList(tempF);
		ListView<String> t6 = new ListView<>(tF);
		t6.setMaxHeight(hei);
		t6.setMaxWidth(wid);
		ObservableList<String> tG = FXCollections.observableArrayList(tempG);
		ListView<String> t7 = new ListView<>(tG);
		t7.setMaxHeight(hei);
		t7.setMaxWidth(wid);
		
		Label labelReservation = new Label("Cancel Reservation");
		labelReservation.setStyle("-fx-font-size: 30;");
		
		Label labelWarn = new Label("Choose the train number that you want to cancel.");
		labelWarn.setStyle("-fx-font-size: 15;");
		
		Label l1 = new Label("Train Number");
		VBox v1 = new VBox(l1, t1);
		
		Label l2 = new Label("Departs From");
		VBox v2 = new VBox(l2, t2);
		
		Label l3 = new Label("Arrives At");
		VBox v3 = new VBox(l3, t3);
		
		Label l4 = new Label("Class");
		VBox v4 = new VBox(l4, t4);
		
		Label l5 = new Label("Price");
		VBox v5 = new VBox(l5, t5);
		
		Label l6 = new Label("# of Baggages");
		VBox v6 = new VBox(l6, t6);
		
		Label l7 = new Label("Passenger Name");
		VBox v7 = new VBox(l7, t7);
		
		HBox hBoxInfo = new HBox(v1, v2, v3, v4, v5, v6, v7);
		hBoxInfo.setAlignment(Pos.CENTER);
				
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(cancelReservationScene);
			window.centerOnScreen();
		});
		Button btnNext = new Button("Next");
		btnNext.setOnAction(e -> {
			cancelTrain = t1.getSelectionModel().getSelectedItem();
			if (cancelTrain != null) {
				cancelRevNext2();
				window.setScene(cancelRevNext2Scene);
				window.centerOnScreen();
			} else {
				warning("You should choose the train number to cancel.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnNext);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxInfo = new VBox(10, labelReservation, labelWarn, hBoxInfo, hBoxBtns);
		vBoxInfo.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxInfo);
		cancelRevNextScene = new Scene(stackPane, 1400, 400);
	}
	
	private void cancelRevNext2() {
		Double hei = 100.0;
		Double wid = 170.0;
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> t1 = new ListView<>(tA);
		t1.setMaxHeight(hei);
		t1.setMaxWidth(wid);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> t2 = new ListView<>(tB);
		t2.setMaxHeight(hei);
		t2.setMaxWidth(wid);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> t3 = new ListView<>(tC);
		t3.setMaxHeight(hei);
		t3.setMaxWidth(wid);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> t4 = new ListView<>(tD);
		t4.setMaxHeight(hei);
		t4.setMaxWidth(wid);
		ObservableList<String> tE = FXCollections.observableArrayList(tempE);
		ListView<String> t5 = new ListView<>(tE);
		t5.setMaxHeight(hei);
		t5.setMaxWidth(wid);
		ObservableList<String> tF = FXCollections.observableArrayList(tempF);
		ListView<String> t6 = new ListView<>(tF);
		t6.setMaxHeight(hei);
		t6.setMaxWidth(wid);
		ObservableList<String> tG = FXCollections.observableArrayList(tempG);
		ListView<String> t7 = new ListView<>(tG);
		t7.setMaxHeight(hei);
		t7.setMaxWidth(wid);
		
		Label labelReservation = new Label("Cancel Reservation");
		labelReservation.setStyle("-fx-font-size: 30;");
		
		Label labelWarn = new Label("Choose the train number that you want to cancel.");
		labelWarn.setStyle("-fx-font-size: 15;");
		
		Label l1 = new Label("Train Number");
		VBox v1 = new VBox(l1, t1);
		
		Label l2 = new Label("Departs From");
		VBox v2 = new VBox(l2, t2);
		
		Label l3 = new Label("Arrives At");
		VBox v3 = new VBox(l3, t3);
		
		Label l4 = new Label("Class");
		VBox v4 = new VBox(l4, t4);
		
		Label l5 = new Label("Price");
		VBox v5 = new VBox(l5, t5);
		
		Label l6 = new Label("# of Baggages");
		VBox v6 = new VBox(l6, t6);
		
		Label l7 = new Label("Passenger Name");
		VBox v7 = new VBox(l7, t7);
		
		HBox hBoxInfo = new HBox(v1, v2, v3, v4, v5, v6, v7);
		hBoxInfo.setAlignment(Pos.CENTER);
		
		Label labelTCR = new Label("Total Cost of Reservation");
		TextField typeTCR = new TextField(Double.toString(TCR));
		HBox hBoxTCR = new HBox(10, labelTCR, typeTCR);
		hBoxTCR.setAlignment(Pos.CENTER);
		
		Label labelDC = new Label("Date of Cancellation");
		TextField typeDC = new TextField(formatter.format(today));
		HBox hBoxDC= new HBox(10, labelDC, typeDC);
		hBoxDC.setAlignment(Pos.CENTER);
		
		Label labelAR = new Label("Amount to be Refunded");
		AR = (TCR * 0.8) - 50.0;
		TextField typeAR;
		if (AR > 0) {
			typeAR = new TextField(Double.toString(AR));
		} else {
			typeAR = new TextField(Double.toString(0.0));
		}
		HBox hBoxAR = new HBox(10, labelAR, typeAR);
		hBoxAR.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(cancelReservationScene);
			window.centerOnScreen();
		});
		Button btnNext = new Button("Submit");
		btnNext.setOnAction(e -> {
			changeCancel(cancelID);
			warning("You cancelled the reservation [" + cancelID + "] sucessfully.");
			window.setScene(chooseFuncScene);
			window.centerOnScreen();
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnNext);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxInfo = new VBox(10, labelReservation, labelWarn, hBoxInfo, hBoxTCR, hBoxDC, hBoxAR, hBoxBtns);
		vBoxInfo.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxInfo);
		cancelRevNext2Scene = new Scene(stackPane, 1400, 400);
	}
	
	private void changeCancel(String cID) {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "UPDATE Reservation JOIN ReservationReservesTrainRoute "
					+ "USING(ReservationID) SET IsCancelled = 1 WHERE ReservationID = " + cID;
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void give(String commIn, String ratingIn, String trainIn) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM Review";
			ResultSet rs = stmt.executeQuery(sql);
			reviewNum = 0;
			while (rs.next()) {
				reviewNum++;
		    }
		    rs.close();
			
			String sql2 = "INSERT INTO Review values('" + ++reviewNum + "', '" + commIn 
							+ "', '" + ratingIn + "', '" + userID + "', '" + trainIn +"')";
			stmt.executeUpdate(sql2);
			
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void giveReview() {
		Label labelGive = new Label("Give Review");
		labelGive.setStyle("-fx-font-size: 30;");
		
		Label labelTrain = new Label("Train Number");
		TextField typeTrain = new TextField();
		HBox hBoxTrain = new HBox(10, labelTrain, typeTrain);
		hBoxTrain.setAlignment(Pos.CENTER);
		
		Label labelRate = new Label("Rating");
		ChoiceBox<String> chooseRating = new ChoiceBox<>();
		chooseRating.getItems().addAll("VERY GOOD", "GOOD", "NEUTRAL", "BAD", "VERY BAD");
		HBox hBoxRate = new HBox(10, labelRate, chooseRating);
		hBoxRate.setAlignment(Pos.CENTER);
		
		Label labelComm = new Label("Commnet");
		TextField typeComm = new TextField();
		HBox hBoxComm = new HBox(10, labelComm, typeComm);
		hBoxComm.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
		});
		Button btnSubmit = new Button("Submit");
		btnSubmit.setOnAction(e -> {
			if (!typeTrain.getText().isEmpty() && chooseRating.getValue() != null) {
				if (checkTrain(typeTrain.getText())) {
					give(typeComm.getText(), chooseRating.getValue(), typeTrain.getText());
					warning("You submitted a new review successfully.");
					window.setScene(chooseFuncScene);
					window.centerOnScreen();
				} else {
					warning("Train [" + typeTrain.getText() + "] cannot be found. Try again.");
				}
			} else {
				warning("You should fill in all the fields except for comment.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnSubmit);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxReview = new VBox(30, labelGive, hBoxTrain, hBoxRate, hBoxComm, hBoxBtns);
		vBoxReview.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxReview);
		giveReviewScene = new Scene(stackPane, 600, 400);
	}
	
	private void viewReview() {
		Label labelView = new Label("View Review");
		labelView.setStyle("-fx-font-size: 30;");
		
		Label labelTrain = new Label("Train Number");
		TextField typeTrain = new TextField();
		
		HBox hBoxTrain = new HBox(10, labelTrain, typeTrain);
		hBoxTrain.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncScene);
			window.centerOnScreen();
		});
		Button btnSubmit = new Button("Submit");
		btnSubmit.setOnAction(e -> {
			trainNum = typeTrain.getText();
			if (!trainNum.isEmpty()) {
				if (checkTrain(trainNum)) {
					view(trainNum);
					reviewTrain();
					window.setScene(reviewTrainScene);
					window.centerOnScreen();
				} else {
					warning("Train [" + trainNum + "] cannot be found. Try again.");
				}
			} else {
				warning ("You should fill in the field.");
			}
		});
		HBox hBoxBtns = new HBox(30, btnBack, btnSubmit);
		hBoxBtns.setAlignment(Pos.CENTER);
		
		VBox vBoxReview = new VBox(30, labelView, hBoxTrain, hBoxBtns);
		vBoxReview.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxReview);
		viewReviewScene = new Scene(stackPane, 600, 400);
	}

	private void view(String trainIn) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM Review WHERE TrainNumber = '" + trainIn + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    tempD = new ArrayList<>();
		    
		    while (rs.next()) {
		    	String checkTrain = rs.getString("TrainNumber").trim();
		    	String c1 = rs.getString("ReviewNumber").trim();
			    String c2 = rs.getString("Comment");
			    String c3 = rs.getString("Rating").trim();
			    String c4 = rs.getString("C_Username").trim();
			    
			    if (trainIn.equals(checkTrain)) {
			    	tempA.add(c1);
				    tempB.add(c2);
				    tempC.add(c3);
				    tempD.add(c4);
			    }
		    }
		    rs.close();
		    stmt.close();
		     
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private boolean checkTrain(String trainIn) {	
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT * FROM TrainRoute WHERE TrainNumber = '" + trainIn + "'";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    while (rs.next()) {
		    	String checkTrain = rs.getString("TrainNumber").trim();
			    if (trainIn.equals(checkTrain)) {
			    	return true;
			    }
		    }
		    rs.close();
		    stmt.close();
		     
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	private void reviewTrain() {
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> t1 = new ListView<>(tA);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> t2 = new ListView<>(tB);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> t3 = new ListView<>(tC);
		ObservableList<String> tD = FXCollections.observableArrayList(tempD);
		ListView<String> t4 = new ListView<>(tD);
		
		Label labelReview = new Label("Review of Train : " + trainNum);
		labelReview.setStyle("-fx-font-size: 30;");
		
		Label labelNum = new Label("Review Number");
		VBox vBoxNum = new VBox(10, labelNum, t1);
		vBoxNum.setAlignment(Pos.CENTER);
		
		Label labelComm = new Label("Comment");
		VBox vBoxComm = new VBox(10, labelComm, t2);
		vBoxComm.setAlignment(Pos.CENTER);
		
		Label labelRating = new Label("Rating");
		VBox vBoxRating = new VBox(10, labelRating, t3);
		vBoxRating.setAlignment(Pos.CENTER);
		
		Label labelID = new Label("Username");
		VBox vBoxID = new VBox(10, labelID, t4);
		vBoxID.setAlignment(Pos.CENTER);
		
		HBox hBoxReview = new HBox(10, vBoxNum, vBoxComm, vBoxRating, vBoxID);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(viewReviewScene);
		});
		
		VBox vBoxReview = new VBox(30, labelReview, hBoxReview, btnBack);
		vBoxReview.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxReview);
		reviewTrainScene = new Scene(stackPane, 800, 600);
	}
	
	private void chooseFuncManager() {
		Label labelFunc = new Label("Choose Functionality");
		labelFunc.setStyle("-fx-font-size: 30;");
		
		Button btnViewRevenue = new Button("View Revenue Report");
		btnViewRevenue.setOnAction(e -> {
			viewR();
			viewRevenueReport();
			window.setScene(viewRevenueReportScene);
			window.centerOnScreen();
		});
		
		Button btnViewRoute = new Button("View Popular Route Report");
		btnViewRoute.setOnAction(e -> {
			viewPP();
			viewPopularRouteReport();
			window.setScene(viewPopularRouteReportScene);
		});
		
		Button btnLogout = new Button("Log out");
		btnLogout.setOnAction(e -> {
			window.setScene(loginScene);
			window.centerOnScreen();
		});
		
		VBox vBoxFunc = new VBox(30, labelFunc, btnViewRevenue, btnViewRoute, btnLogout);
		vBoxFunc.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxFunc);
		chooseFuncManagerScene = new Scene(stackPane, 600, 400);
	}
	
	private void viewR() {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT MONTH(DepartureDate) as MONTH, SUM(TotalCost) as REVENUE "
					+ "from ReservationReservesTrainRoute JOIN Reservation USING(ReservationID) "
					+ "group by MONTH having MONTH >= 1 AND MONTH <= 3";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    
		    while (rs.next()) {
			    String a = rs.getString("MONTH").trim();
			    String b = rs.getString("REVENUE").trim();
			    
			    tempA.add(a);
			    tempB.add(b);
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void viewRevenueReport() {
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> ttA = new ListView<>(tA);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> ttB = new ListView<>(tB);
		
		Label labelReport = new Label("View Revenue Report");
		labelReport.setStyle("-fx-font-size: 30;");
		
		Label labelMonth = new Label("Month");
		VBox vBoxMonth = new VBox(10, labelMonth, ttA);
		
		Label labelRevenue = new Label("Revenue");
		VBox vBoxRevenue = new VBox(10, labelRevenue, ttB);
		
		HBox hBoxMR = new HBox(20,vBoxMonth, vBoxRevenue);
		hBoxMR.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncManagerScene);
			window.centerOnScreen();
		});
		
		VBox vBoxAll = new VBox(30, labelReport, hBoxMR, btnBack);
		vBoxAll.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxAll);
		viewRevenueReportScene = new Scene(stackPane, 600, 400);
	}
	
	private void viewPP() {
		try {
			Statement stmt = connectionToCs4400.createStatement();
			String sql = "SELECT MONTH(DepartureDate) as MONTH, TrainNumber, "
					+ "COUNT(TrainNumber) as NumOfReservation from ReservationReservesTrainRoute "
					+ "JOIN Reservation USING(ReservationID) group by MONT TrainNumber "
					+ "having MONTH >= 1 AND MONTH <= 5 ORDER BY COUNT(TrainNumber) DSC";
		    ResultSet rs = stmt.executeQuery(sql);
		    
		    tempA = new ArrayList<>();
		    tempB = new ArrayList<>();
		    tempC = new ArrayList<>();
		    
		    while (rs.next()) {
			    String a = rs.getString("MONTH").trim();
			    String b = rs.getString("TrainNumber").trim();
			    String c = rs.getString("NumOfReservation").trim();
			    
			    tempA.add(a);
			    tempB.add(b);
			    tempC.add(c);
		    }
		    rs.close();
		    stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private void viewPopularRouteReport() {
		ObservableList<String> tA = FXCollections.observableArrayList(tempA);
		ListView<String> ttA = new ListView<>(tA);
		ObservableList<String> tB = FXCollections.observableArrayList(tempB);
		ListView<String> ttB = new ListView<>(tB);
		ObservableList<String> tC = FXCollections.observableArrayList(tempC);
		ListView<String> ttC = new ListView<>(tC);
		
		Label labelReport = new Label("View Popular Route Report");
		labelReport.setStyle("-fx-font-size: 30;");
		
		Label labelMonth = new Label("Month");
		VBox vBoxMonth = new VBox(10, labelMonth, ttA);
		
		Label labelTrain = new Label("Train Number");
		VBox vBoxTrain = new VBox(10, labelTrain, ttB);
		
		Label labelNumOfRev = new Label("Number Of Reservation");
		VBox vBoxNumOfRev = new VBox(10, labelNumOfRev, ttC);
		
		HBox hBoxAll = new HBox(20, vBoxMonth, vBoxTrain, vBoxNumOfRev);
		hBoxAll.setAlignment(Pos.CENTER);
		
		Button btnBack = new Button("Back");
		btnBack.setOnAction(e -> {
			window.setScene(chooseFuncManagerScene);
			window.centerOnScreen();
		});
		
		VBox vBoxAll = new VBox(30, labelReport, hBoxAll, btnBack);
		vBoxAll.setAlignment(Pos.CENTER);
		
		StackPane stackPane = new StackPane(vBoxAll);
		viewPopularRouteReportScene = new Scene(stackPane, 600, 400);
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
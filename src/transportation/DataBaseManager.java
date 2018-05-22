package Transportation;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Scanner;

import org.sqlite.SQLiteConfig;

public class DataBaseManager{
	public static final String DB_URL = "jdbc:sqlite:database.db";  
	public static final String DRIVER = "org.sqlite.JDBC";  
	public void createNewDatabase(String fileName) {

		try (Connection conn = DriverManager.getConnection(DB_URL)) {
			if (conn != null) {
			//	System.out.println("A new database has been created.");
			}

		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
	}

	public static Connection getConnection() throws ClassNotFoundException {
		Connection conn = null;
		try {
			// db parameters
			Class.forName(DRIVER);  
			SQLiteConfig config = new SQLiteConfig();  
	        config.enforceForeignKeys(true); 
			// create a connection to the database
			conn = DriverManager.getConnection(DB_URL,config.toProperties());

		//	System.out.println("Connection to SQLite has been established.");

		} catch (SQLException e) {
			//System.out.println(e.getMessage());
		} 
		
		return conn;
	}
	
	public void createTables() {
		createTrucksTable();
		createDriversTable();
		createTransportaionsTable();
		createTransportDestinationsTable();
		createDestinationsTable();
		createSourcesTable();
		createReservationDocumentsTable();
		
	}
	
	private void createReservationDocumentsTable() {
		   // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS RESERVATION_DOCUMENTS (\n"
                + "	ID integer PRIMARY KEY,\n"
                + "	TRANSPORT_ID integer NOT NULL, \n"
                + " FOREIGN KEY(TRANSPORT_ID) REFERENCES TRANSPORTAIONS(ID) \n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            // create a new table

            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
		
	}

	private void createSourcesTable() {
		   String sql = "CREATE TABLE IF NOT EXISTS SOURCES (\n"
				    + "	ID integer PRIMARY KEY,\n"
	                + "	ADDRESS text NOT NULL,\n"
	                + "	CONTACT_NAME text NOT NULL, \n"
	                + "	CONTACT_PHONE integer NOT NULL\n"
	                + ");";
	        
	        try (Connection conn = DriverManager.getConnection(DB_URL);
	                Statement stmt = conn.createStatement()) {
	            // create a new table

	            stmt.execute(sql);
	        } catch (SQLException e) {
	        }
		
	}
	
	private void createDestinationsTable() {
		 String sql = "CREATE TABLE IF NOT EXISTS DESTINATIONS (\n"
	                + "	ID integer PRIMARY KEY,\n"
	                + "	ADDRESS text NOT NULL,\n"
	                + "	CONTACT_NAME text NOT NULL, \n"
	                + "	CONTACT_PHONE integer NOT NULL, \n"
	                + "	DELIVERY_AREA text NOT NULL \n"
	                + ");";
	        
	        try (Connection conn = DriverManager.getConnection(DB_URL);
	                Statement stmt = conn.createStatement()) {
	            // create a new table

	            stmt.execute(sql);
	        } catch (SQLException e) {
	        }
		
	}

	public void createTransportaionsTable() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS TRANSPORTAIONS (\n"
                + "	ID integer PRIMARY KEY,\n"
                + "	TRUCK_ID integer NOT NULL,\n"
                + "	DRIVER_ID integer NOT NULL,\n"
                + "	DEAPARTURE_TIME text NOT NULL, \n"
                + "	DEAPARTURE_DATE text NOT NULL, \n"
                + " FOREIGN KEY(DRIVER_ID) REFERENCES DRIVERS(ID), \n"
                + " FOREIGN KEY(TRUCK_ID) REFERENCES TRUCKS(ID) \n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            // create a new table

            stmt.execute(sql);
        } catch (SQLException e) {
        }

    }
	
	public void createTrucksTable() {
   
        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS TRUCKS (\n"
                + "	ID integer PRIMARY KEY,\n"
                + "	MODEL text NOT NULL, \n"
                + "	COLOR text NOT NULL, \n"
                + "	NETO_WEIGHT integer NOT NULL,\n"
                + "	MAX_WEIGHT integer NOT NULL \n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            // create a new table

            stmt.execute(sql);
        } catch (SQLException e) {
        }

    }
	
	public void createTransportDestinationsTable() {

        // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS TRANSPORT_DESTINATIONS (\n"
                + "	TRANSPORT_ID integer NOT NULL,\n"
                + "	SOURCE_ID integer NOT NULL, \n"
                + "	DESTINATION_ID integer NOT NULL, \n"
                + " FOREIGN KEY(SOURCE_ID) REFERENCES SOURCES(ID), \n"
                + " FOREIGN KEY(DESTINATION_ID) REFERENCES DESTINATIONS(ID), \n"
                + " FOREIGN KEY(TRANSPORT_ID) REFERENCES TRANSPORTAIONS(ID) \n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            // create a new table

            stmt.execute(sql);
        } catch (SQLException e) {
        }

    }

	private void createDriversTable() {
		   // SQL statement for creating a new table
        String sql = "CREATE TABLE IF NOT EXISTS DRIVERS (\n"
                + "	ID integer PRIMARY KEY,\n"
                + "	LICENCE_KIND integer NOT NULL, \n"
                + "	FIRST_NAME integer NOT NULL, \n"
                + "	LAST_NAME integer NOT NULL \n"
                + ");";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
                Statement stmt = conn.createStatement()) {
            // create a new table

            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

		
	}
	

	
	
	public void insertTransport(boolean scanInput , Transport t) throws ClassNotFoundException {
		Transport transport;
		ReservationDocument reservationDocument = null;
		if(scanInput) {
		Scanner sc = new Scanner(System.in);
		
		
		LinkedList<Destination> DestinationList = new LinkedList<Destination>();
		
		System.out.println("Please insert transportation id:");
		int iD = sc.nextInt();

		System.out.println("Truck id:");
		int Truck_ID = sc.nextInt();

		System.out.println("Driver id:");
		int Driver_ID = sc.nextInt();

		boolean check = false;
		
			check = checkForConstraint(Truck_ID , Driver_ID);
		
		if(!check) {
			System.out.println("Error happend:"
					+ " \n maybe The driver licence not matched to the specefied truck ,"
					+ " \n or there is no driver id as specefied ,"
					+ " \n or  there is no truck id as specefied.");
			return;
		}
		
		System.out.println("Please enter Source:");
		
		System.out.println("Source id:");
		int SourceId = sc.nextInt();
		
		System.out.println("Address:");
		String SourceAddress = sc.next();

		System.out.println("Contact Name:");
		String SourceContactName = sc.next();
		
		System.out.println("Phone Number:");
		int SourcePhoneNumber = sc.nextInt();
		

		Source source = new Source(SourceId ,SourceAddress, SourcePhoneNumber, SourceContactName);
		insertSource(source);

		System.out.println("Please insert Departure date:");

		System.out.println("Year(YYYY): ");
		String year = sc.next();

		System.out.println("Month(MM): ");
		String month = sc.next();

		System.out.println("Day(DD): ");
		String day = sc.next();

		String Departure_Date = day + "/" + month + "/" + year;

		System.out.println("Please insert Departure time:");

		System.out.println("Hour(HH): ");
		String hour = sc.next();

		System.out.println("Minuts(MM): ");
		String minute = sc.next();

		System.out.println("Second(SS): ");
		String second = sc.next();

		String Departure_Time = hour + ":" + minute + ":" + second;

		System.out.println("How many destinations do you want?: ");
		int numOfIterations = sc.nextInt();
		for (int i = 0; i < numOfIterations; i++) {
			System.out.println("Destination number " + (i + 1) + " :");
			System.out.println("Destination id:");
			int DestinationId = sc.nextInt();
			System.out.println("Address:");
			String Address = sc.next();
			System.out.println("Contact Name:");
			String ContactName = sc.next();
			System.out.println("Phone Number:");
			int PhoneNumber = sc.nextInt();
			System.out.println("Delivery Area: ");
			String zone = sc.next();
			Destination destination = new Destination(DestinationId,Address, PhoneNumber, ContactName, zone);
			System.out.println("Reservation Document ID of the Destination: ");
			int RESERVATION_ID = sc.nextInt();
		    reservationDocument = new ReservationDocument(RESERVATION_ID, iD);
			
			insertDestination(destination);
			DestinationList.add(destination);
		}
		 transport = new Transport(iD, Truck_ID, Driver_ID, Departure_Date, Departure_Time, source, DestinationList);
		}
		else {
			transport=t;
		}
		 String sql = "INSERT INTO TRANSPORTAIONS(ID , TRUCK_ID, DRIVER_ID , DEAPARTURE_TIME , DEAPARTURE_DATE) VALUES(? , ? , ? , ? , ?)";
		 
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, transport.getID());
	            pstmt.setInt(2, transport.getTruck_ID());
	            pstmt.setInt(3, transport.getDriver_ID());
	            pstmt.setString(4, transport.getDepartureTime());
	            pstmt.setString(5, transport.getDepartureDate());
	            pstmt.executeUpdate();
	            System.out.println("New Transport has been added!");
	        } catch (SQLException e) {
	        }
	        if(scanInput) {
	        insertReservationDocument(reservationDocument);
	        }
	}

	public void insertTruck(boolean scanInput , Truck t) throws ClassNotFoundException {
		Truck truck;
	
		if(scanInput) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter truck:");

		System.out.println("ID:");
		int truckId = sc.nextInt();
		
		System.out.println("MODEL:");
		String MODEL = sc.next();

		System.out.println("COLOR: ");
		String COLOR = sc.next();

		System.out.println("NETO WEIGHT: ");
		int NETO_WEIGHT = sc.nextInt();
		
		System.out.println("MAX WEIGHT: ");
		int MAX_WEIGHT = sc.nextInt();
		
		 truck = new Truck(truckId ,MODEL, COLOR, NETO_WEIGHT , MAX_WEIGHT);
		}else {
			truck=t;
		}
		
		 String sql = "INSERT INTO TRUCKS(ID , MODEL, COLOR , NETO_WEIGHT , MAX_WEIGHT) VALUES(? , ? , ? , ? , ?)";
		 
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, truck.getID());
	            pstmt.setString(2, truck.getModel());
	            pstmt.setString(3, truck.getColor());
	            pstmt.setInt(4, truck.getNeto_Weight());
	            pstmt.setInt(5, truck.getMax_Weight());
	            pstmt.executeUpdate();
	            System.out.println("New Truck has been added!");
	        } catch (SQLException e) {
	        }
		
	}
	
	public void insertDriver(boolean scanInput , Driver d) throws ClassNotFoundException {
		Driver driver;
		if(scanInput) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please enter driver:");

		System.out.println("ID:");
		int driverId = sc.nextInt();
		
		System.out.println("LICENCE KIND:");
		String LICENCE_KIND = sc.next();

		System.out.println("FIRST NAME: ");
		String FIRST_NAME = sc.next();

		System.out.println("LAST NAME: ");
		String LAST_NAME = sc.next();
		
		driver  = new Driver(driverId ,LICENCE_KIND, FIRST_NAME, LAST_NAME);
		}else {
			driver = d;
		}
		 String sql = "INSERT INTO DRIVERS(ID , LICENCE_KIND ,FIRST_NAME , LAST_NAME) VALUES(? , ? , ? , ? )";
		 
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, driver.getID());
	            pstmt.setString(2, driver.getLicence_Kind());
	            pstmt.setString(3, driver.getFirst_Name());
	            pstmt.setString(4, driver.getLast_Name());
	   
	            pstmt.executeUpdate();
	            System.out.println("New driver has been added!");
	        } catch (SQLException e) {
	        }
		
	}
	
	public void insertSource(Source source) throws ClassNotFoundException {
		 String sql = "INSERT INTO SOURCES(ID , ADDRESS , CONTACT_NAME , CONTACT_PHONE) VALUES(?, ? , ? , ? )";
		 
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, source.getID());
	            pstmt.setString(2, source.getAddress());
	            pstmt.setString(3, source.getContactName());
	            pstmt.setInt(4, source.getPhoneNumber());
	
	            
	   
	            pstmt.executeUpdate();
	            System.out.println("New source has been added!");
	        } catch (SQLException e) {
	        }
		
	}

	public void insertDestination(Destination destination) throws ClassNotFoundException {
		 String sql = "INSERT INTO DESTINATIONS(ID , ADDRESS , CONTACT_NAME , CONTACT_PHONE , DELIVERY_AREA) VALUES(?, ? , ? , ? , ? )";
		 
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, destination.getID());
	            pstmt.setString(2, destination.getAddress());
	            pstmt.setString(3, destination.getContactName());
	            pstmt.setInt(4, destination.getPhoneNumber());
	            pstmt.setString(5, destination.getDELIVERY_AREA());
	
	            
	   
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	        }
		
	}
	
	public void insertReservationDocument(ReservationDocument reservationDocument) throws ClassNotFoundException {
		 String sql = "INSERT INTO RESERVATION_DOCUMENTS(ID , TRANSPORT_ID) VALUES(? , ? )";
		 
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	            pstmt.setInt(1, reservationDocument.getID());
	            pstmt.setInt(2, reservationDocument.getTransport_ID());
	   
	            pstmt.executeUpdate();
	           
	        } catch (SQLException e) {
	        }
	}
	
	
	
	
	public void updateTransport() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		String Departure_Date;
		String Departure_Time;
		int Driver_ID;
		int Truck_ID;
		int ID;
		String sql;
		
		
		System.out.println("Please insert transportation id to update:");
		ID = sc.nextInt();

		System.out.println("=========================================");
		System.out.println("|   	  Update menu	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to update:		|");
		System.out.println("|        1. TRUCK ID & DRIVER ID|");
		System.out.println("|        2. DEAPARTURE TIME	|");
		System.out.println("|        3. DEAPARTURE DATE	|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Insert TRUCK ID:");
			Truck_ID = sc.nextInt();
			
			System.out.println("Insert DRIVER ID:");
			Driver_ID = sc.nextInt();
			
			boolean check = false;
			
			check = checkForConstraint(Truck_ID , Driver_ID);
		
		if(!check) {
			System.out.println("Error happend:"
					+ " \n maybe The driver licence not matched to the specefied truck ,"
					+ " \n or there is no driver id as specefied ,"
					+ " \n or  there is no truck id as specefied.");
			return;
		}

			   sql = "UPDATE TRANSPORTAIONS SET DRIVER_ID = ? ,\n" 
					    + "TRUCK_ID = ?" 
		                + "WHERE Id = ?";
		 
			  try (Connection conn = this.getConnection();
		                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		 
		            // set the corresponding param
		        	 pstmt.setInt(1, Driver_ID);
		        	 pstmt.setInt(2, Truck_ID);
		             pstmt.setInt(3, ID);
		            // update 
		            pstmt.executeUpdate();
		        } catch (SQLException e) {
		        }
			break;
		case 3:
			System.out.println("Please insert Departure time:");

			System.out.println("Hour(HH): ");
			String hour = sc.next();

			System.out.println("Minuts(MM): ");
			String minute = sc.next();

			System.out.println("Second(SS): ");
			String second = sc.next();

			Departure_Time = hour + ":" + minute + ":" + second;
			
			   sql = "UPDATE TRANSPORTAIONS SET DEAPARTURE_TIME = ? "
		                + "WHERE Id = ?";
		 
			  try (Connection conn = this.getConnection();
		                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		 
		            // set the corresponding param
		        	 pstmt.setString(1, Departure_Time);
		             pstmt.setInt(2, ID);
		            // update 
		            pstmt.executeUpdate();
		        } catch (SQLException e) {
		        }
			break;
		case 4:
			System.out.println("Please insert Departure date:");

			System.out.println("Year(YYYY): ");
			String year = sc.next();

			System.out.println("Month(MM): ");
			String month = sc.next();

			System.out.println("Day(DD): ");
			String day = sc.next();

			Departure_Date = day + "/" + month + "/" + year;
			
			   sql = "UPDATE TRANSPORTAIONS SET DEAPARTURE_DATE = ? "
		                + "WHERE Id = ?";
		 
			  try (Connection conn = this.getConnection();
		                PreparedStatement pstmt = conn.prepareStatement(sql)) {
		 
		            // set the corresponding param
		        	 pstmt.setString(1, Departure_Date);
		             pstmt.setInt(2, ID);
		            // update 
		            pstmt.executeUpdate();
		        } catch (SQLException e) {
		        }
			break;
			
		}
		System.out.println("Transport has been Updated!");
		
	}

	
	
	
	public void deleteTransport(boolean scanInput , int d) throws ClassNotFoundException {
		int ID;
		String sql;	
		if(scanInput) {
		Scanner sc = new Scanner(System.in);
		System.out.println("Please insert transportation id to delete:");
		ID = sc.nextInt();
		}
		else {
			ID = d;
		}
	    sql = "DELETE FROM TRANSPORTAIONS WHERE id = ?";
	     
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setInt(1, ID);
	            // execute the delete statement
	            pstmt.executeUpdate();
	 
	        } catch (SQLException e) {
	            return;
	        }
	        System.out.println("Transport has been deleted!");
		
	}

	public void deleteTruck(boolean scanInput , int d) throws ClassNotFoundException{
		int ID;
		String sql;	
		if(scanInput) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please insert TRUCK id to delete:");
		ID = sc.nextInt();
		}
		else {
			ID=d;
		}
	    sql = "DELETE FROM TRUCKS WHERE id = ?";
	     
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setInt(1, ID);
	            // execute the delete statement
	            pstmt.executeUpdate();
	 
	        } catch (SQLException e) {
	            return;
	        }
	        System.out.println("Truck has been deleted!");
		
	}
	
	public void deleteDriver(boolean scanInput , int d) throws ClassNotFoundException {
		int ID;
		String sql;	
		if(scanInput) {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Please insert DRIVER id to delete:");
		ID = sc.nextInt();
		}
		else {
			ID = d;
		}
	    sql = "DELETE FROM DRIVERS WHERE id = ?";
	     
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setInt(1, ID);
	            // execute the delete statement
	            pstmt.executeUpdate();
	 
	        } catch (SQLException e) {
	            return;
	        }
	        System.out.println("Driver has been deleted!");
		
	}

	
	
	

	public void getInfoTransport() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		int ID;
		String sql;
		
		
		System.out.println("=========================================");
		System.out.println("|   	  Print info Transportation	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to print:			|");
		System.out.println("|        1. Print by ID			|");
		System.out.println("|        2. Print all			|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Please insert transportation id to show:");
			ID = sc.nextInt();
			sql = "SELECT ID, TRUCK_ID , DRIVER_ID , DEAPARTURE_TIME , DEAPARTURE_DATE FROM TRANSPORTAIONS WHERE Id = ?";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"TRUCK ID: "	+ rs.getInt("TRUCK_ID") +  " ," + 
			        		 			"DRIVER ID: " + rs.getInt("DRIVER_ID") +  " ," + 
			        		 			"DEAPARTURE TIME: " +rs.getString("DEAPARTURE_TIME") +  " ," + 
			                            "DEAPARTURE DATE: " + rs.getString("DEAPARTURE_DATE") 
			);
			     }
			 } catch (SQLException e) {
			 }
			break;
		case 2:
			sql = "SELECT ID, TRUCK_ID , DRIVER_ID , DEAPARTURE_TIME , DEAPARTURE_DATE FROM TRANSPORTAIONS";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"TRUCK ID: "	+ rs.getInt("TRUCK_ID") +  " ," + 
			        		 			"DRIVER ID: " + rs.getInt("DRIVER_ID") +  " ," + 
			        		 			"DEAPARTURE TIME: " +rs.getString("DEAPARTURE_TIME") +  " ," + 
			                            "DEAPARTURE DATE: " + rs.getString("DEAPARTURE_DATE") 
			);
			     }
			 } catch (SQLException e) {
			 }
			break;
		}
		
	}

	public void getInfoTruck() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		int ID;
		String sql;
		
		
		System.out.println("=========================================");
		System.out.println("|   	  Print info Trucks	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to print:			|");
		System.out.println("|        1. Print by ID			|");
		System.out.println("|        2. Print all			|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Please insert Truck id to print:");
			ID = sc.nextInt();
			sql = "SELECT ID, MODEL , COLOR , NETO_WEIGHT , MAX_WEIGHT FROM TRUCKS WHERE Id = ?";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"MODEL: "	+ rs.getString("MODEL") +  " ," + 
			        		 			"COLOR: " + rs.getString("COLOR") +  " ," + 
			        		 			"NETO WEIGHT: " +rs.getInt("NETO_WEIGHT") +  " ," + 
			                            "MAX WEIGHT: " + rs.getInt("MAX_WEIGHT") 
			);
			     }
			 } catch (SQLException e) {
			 }
			break;
		case 2:
			sql = "SELECT ID, MODEL , COLOR , NETO_WEIGHT , MAX_WEIGHT FROM TRUCKS";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	   System.out.println("ID: " + rs.getInt("ID") +  " ," + 
	        		 			"MODEL: "	+ rs.getString("MODEL") +  " ," + 
	        		 			"COLOR: " + rs.getString("COLOR") +  " ," + 
	        		 			"NETO WEIGHT: " +rs.getInt("NETO_WEIGHT") +  " ," + 
	                            "MAX WEIGHT: " + rs.getInt("MAX_WEIGHT") 
	);
			     }
			 } catch (SQLException e) {
			 }
			break;
		}
		
	}

	public void getInfoDriver() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		int ID;
		String sql;
		
		
		System.out.println("=========================================");
		System.out.println("|   	  Print info DRIVERS	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to print:			|");
		System.out.println("|        1. Print by ID			|");
		System.out.println("|        2. Print all			|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Please insert DRIVER id to print:");
			ID = sc.nextInt();
			sql = "SELECT ID, LICENCE_KIND , FIRST_NAME , LAST_NAME FROM DRIVERS WHERE Id = ?";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"LICENCE KIND: "	+ rs.getString("LICENCE_KIND") +  " ," + 
			        		 			"FIRST NAME: " +rs.getString("FIRST_NAME") +  " ," + 
			                            "LAST NAME: " + rs.getString("LAST_NAME") 
			);
			     }
			 } catch (SQLException e) {
			 }
			break;
		case 2:
			sql = "SELECT ID, LICENCE_KIND , FIRST_NAME , LAST_NAME FROM DRIVERS";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	   System.out.println("ID: " + rs.getInt("ID") +  " ," + 
	        		 			"LICENCE KIND: "	+ rs.getString("LICENCE_KIND") +  " ," + 
	        		 			"FIRST NAME: " + rs.getString("FIRST_NAME") +  " ," + 
	        		 			"LAST NAME: " +rs.getString("LAST_NAME")
	);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		}
		
	}
	

	public void getInfoReservation() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		int ID;
		String sql;
		
		
		System.out.println("=========================================");
		System.out.println("|   Print info RESERVATION DOCUMENTS	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to print:			|");
		System.out.println("|        1. Print by ID			|");
		System.out.println("|        2. Print all			|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Please insert RESERVATION DOCUMENTS id to print:");
			ID = sc.nextInt();
			sql = "SELECT ID, TRANSPORT_ID FROM RESERVATION_DOCUMENTS WHERE Id = ?";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"TRANSPORT ID: "	+ rs.getString("TRANSPORT_ID")

			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		case 2:
			sql = "SELECT ID, TRANSPORT_ID FROM RESERVATION_DOCUMENTS";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"TRANSPORT ID: "	+ rs.getString("TRANSPORT_ID")

			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		}
		
	}

	
	public void getInfoSource() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		int ID;
		String sql;
		
		
		System.out.println("=========================================");
		System.out.println("|   	  Print info SOURCES	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to print:			|");
		System.out.println("|        1. Print by ID			|");
		System.out.println("|        2. Print all			|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Please insert SOURCE id to print:");
			ID = sc.nextInt();
			sql = "SELECT ID, ADDRESS , CONTACT_NAME, CONTACT_PHONE FROM SOURCES WHERE Id = ?";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"ADDRESS: "	+ rs.getString("ADDRESS") +  " ," + 
			        		 			"CONTACT NAME: " +rs.getString("CONTACT_NAME") +  " ," + 
			                            "CONTACT PHONE: " + rs.getInt("CONTACT_PHONE") 
			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		case 2:
			sql = "SELECT ID, ADDRESS  , CONTACT_NAME, CONTACT_PHONE FROM SOURCES";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"ADDRESS: "	+ rs.getString("ADDRESS") +  " ," + 
			        		 			"CONTACT NAME: " +rs.getString("CONTACT_NAME") +  " ," + 
			                            "CONTACT PHONE: " + rs.getInt("CONTACT_PHONE") 
			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		}
		
	}

	public void getInfoDestination() throws ClassNotFoundException {
		Scanner sc = new Scanner(System.in);
		int ID;
		String sql;
		
		
		System.out.println("=========================================");
		System.out.println("|   	  Print info DESTINATIONS	     	|");
		System.out.println("=========================================");
		System.out.println("|Options to print:			|");
		System.out.println("|        1. Print by ID			|");
		System.out.println("|        2. Print all			|");
		System.out.println("=========================================");
		
		
		System.out.println("Select option: ");
		int input = sc.nextInt();
		// Switch construct
		switch (input) {
		case 1:
			System.out.println("Please insert DESTINATION id to print:");
			ID = sc.nextInt();
			sql = "SELECT ID , ADDRESS , CONTACT_NAME, CONTACT_PHONE , DELIVERY_AREA FROM DESTINATIONS WHERE Id = ?";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"ADDRESS: "	+ rs.getString("ADDRESS") +  " ," + 
			        		 			"DELIVERY AREA: " + rs.getString("DELIVERY_AREA") +  " ," + 
			        		 			"CONTACT NAME: " +rs.getString("CONTACT_NAME") +  " ," + 
			                            "CONTACT PHONE: " + rs.getInt("CONTACT_PHONE") 
			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		case 2:
			sql = "SELECT ID , ADDRESS , DELIVERY_AREA , CONTACT_NAME, CONTACT_PHONE  FROM DESTINATIONS";
			 
			 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			         System.out.println("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"ADDRESS: "	+ rs.getString("ADDRESS") +  " ," + 
			        		 			"DELIVERY AREA: " + rs.getString("DELIVERY_AREA") +  " ," + 
			        		 			"CONTACT NAME: " +rs.getString("CONTACT_NAME") +  " ," + 
			                            "CONTACT PHONE: " + rs.getInt("CONTACT_PHONE") 
			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			break;
		}
		
	}
	
	
	
	private boolean checkForConstraint(int truck_ID, int driver_ID) throws ClassNotFoundException {

		String sql = "SELECT MODEL FROM TRUCKS WHERE Id = ?";
		 String model = null;
		 String licene = null;
		 try (Connection conn = this.getConnection();
		      PreparedStatement pstmt  = conn.prepareStatement(sql)){
		     
		     // set the value
		     pstmt.setInt(1,truck_ID);
		     //
		     ResultSet rs  = pstmt.executeQuery();
		     model = rs.getString("MODEL");
		     // loop through the result set
		
		 licene = getLicence(driver_ID);
		 } catch (SQLException e) {
			 
		 }
		 
		 
		 if(model==null || licene==null || !model.equals(licene)) {

		return false;
		 }
		 else
			 return true;
		 }
	
	private String getLicence(int driver_ID) throws ClassNotFoundException {
		String sql = "SELECT LICENCE_KIND FROM DRIVERS WHERE Id = ?";
		String licene = null;
		 try (Connection conn = this.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,driver_ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     licene = rs.getString("LICENCE_KIND");
			     // loop through the result set
			  
			 } catch (SQLException e) {
			 
			 }
		 return licene;
	}

	
	public void deleteSource(Source source) throws ClassNotFoundException {
		   String sql = "DELETE FROM SOURCES WHERE id = ?";
		     
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setInt(1, source.getID());
	            // execute the delete statement
	            pstmt.executeUpdate();
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	            return;
	        }
		
	}

	public void deleteDestination(Destination dest) throws ClassNotFoundException {
		   String sql = "DELETE FROM DESTINATIONS WHERE id = ?";
		     
	        try (Connection conn = this.getConnection();
	                PreparedStatement pstmt = conn.prepareStatement(sql)) {
	 
	            // set the corresponding param
	            pstmt.setInt(1, dest.getID());
	            // execute the delete statement
	            pstmt.executeUpdate();
	 
	        } catch (SQLException e) {
	            System.out.println(e.getMessage());
	            return;
	        }
		
	}


	public void deleteReservationDocument(ReservationDocument res) throws ClassNotFoundException {
		 String sql = "DELETE FROM RESERVATION_DOCUMENTS WHERE id = ?";
	     
        try (Connection conn = this.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
 
            // set the corresponding param
            pstmt.setInt(1, res.getID());
            // execute the delete statement
            pstmt.executeUpdate();
 
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }
		
	}
		}
/*
package superLi.transportation;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

public class TEST {

	DataBaseManager manager = new DataBaseManager();
	Transport transport = new Transport(11, 12, 13, "01/01/2015", "20:00:00",
			new Source(14, "Beer-Sheva", 050, "Shmulik"), new LinkedList<Destination>());
	private Truck truck = new Truck(12, "A", "BLACK", 16, 17);
	private Driver driver = new Driver(13, "A", "BenBen", "Bibi");
	private Source source = new Source(14, "Beer-Sheva", 777, "Shmulik");
	private Destination destination = new Destination(15, "Tel-Aviv", 050505050, "Saar", "C");
	private ReservationDocument res = new ReservationDocument(20, 11);
	
	@Before public void initialize() {
		manager.createNewDatabase("database.db");
		manager.createTables();
	    }
	@Test
	public void test_Insert_Transportation() throws ClassNotFoundException, SQLException {
		manager.insertDriver(false, driver);
		manager.insertTruck(false, truck);
		manager.insertTransport(false, transport);
		assertEquals(getInfoOf("Transport", 11) , "ID: 11 ,TRUCK ID: 12 ,DRIVER ID: 13 ,DEAPARTURE TIME: 20:00:00 ,DEAPARTURE DATE: 01/01/2015");
		manager.deleteDriver(false, 13);
		manager.deleteTruck(false, 12);
		manager.deleteTransport(false, 11);
		
	}
	@Test
	public void test_Delete_Transportation() throws ClassNotFoundException, SQLException {
		manager.insertDriver(false, driver);
		manager.insertTruck(false, truck);
		manager.insertTransport(false, transport);
		manager.deleteTransport(false, 11);
		assertEquals(getInfoOf("Transport", 11), null);
		manager.deleteDriver(false, 13);
		manager.deleteTruck(false, 12);
	}
	@Test
	public void test_Insert_Truck() throws ClassNotFoundException, SQLException {
		manager.insertTruck(false, truck);
		assertEquals(getInfoOf("Truck", 12) , "ID: 12 ,MODEL: A ,COLOR: BLACK ,NETO WEIGHT: 16 ,MAX WEIGHT: 17");
		manager.deleteTruck(false, 12);
	}
	@Test
	public void test_Insert_Driver() throws ClassNotFoundException, SQLException {
		manager.insertDriver(false, driver);
		assertEquals(getInfoOf("Driver", 13) , "ID: 13 ,LICENCE KIND: A ,FIRST NAME: BenBen ,LAST NAME: Bibi");
		manager.deleteDriver(false, 13);
		
	}
	@Test
	public void test_Insert_Source() throws ClassNotFoundException, SQLException {
		manager.insertSource(source);
		assertEquals(getInfoOf("Source", 14) , "ID: 14 ,ADDRESS: Beer-Sheva ,CONTACT NAME: Shmulik ,CONTACT PHONE: 777");
		manager.deleteSource(source);
	
	}
	@Test
	public void test_Insert_Destination() throws ClassNotFoundException, SQLException {
		manager.insertDestination(destination);
		assertEquals(getInfoOf("Destination", 15) , "ID: 15 ,ADDRESS: Tel-Aviv ,DELIVERY AREA: C ,CONTACT NAME: Saar ,CONTACT PHONE: 10652200");
		manager.deleteDestination(destination);
	
	}
	@Test
	public void test_Insert_ReservationDocument() throws ClassNotFoundException, SQLException {
		manager.insertDriver(false, driver);
		manager.insertTruck(false, truck);
		manager.insertTransport(false, transport);
		manager.insertReservationDocument(res);
		assertEquals(getInfoOf("Reservation_Doc", 20) , "ID: 20 ,TRANSPORT ID: 11");
		manager.deleteDriver(false, 13);
		manager.deleteTruck(false, 12);
		manager.deleteReservationDocument(res);
		manager.deleteTransport(false, 11);
		
	}
	
	@Test
	public void test_Delete_Truck() throws ClassNotFoundException, SQLException {

		manager.insertTruck(false, truck);
		manager.deleteTruck(false, 12);
		assertEquals(getInfoOf("Truck", 12), null);

		
	}
	@Test
	public void test_Delete_Driver() throws ClassNotFoundException, SQLException {
		manager.insertDriver(false, driver);
		manager.deleteDriver(false, 13);
		assertEquals(getInfoOf("Driver", 13), null);

		
		
	}
	
	public String getInfoOf(String ent , int ID) throws ClassNotFoundException, SQLException {
		String sql;
		String toReturn = null;
	switch (ent) {
	case "Transport":
		sql = "SELECT ID, TRUCK_ID , DRIVER_ID , DEAPARTURE_TIME , DEAPARTURE_DATE FROM TRANSPORTAIONS WHERE Id = ?";
		 
		 try (Connection conn = manager.getConnection();
		      PreparedStatement pstmt  = conn.prepareStatement(sql)){
		     
		     // set the value
		     pstmt.setInt(1,ID);
		     //
		     ResultSet rs  = pstmt.executeQuery();
		     
		     // loop through the result set
		     while (rs.next()) {
		    	 toReturn = ("ID: " + rs.getInt("ID") +  " ," + 
		        		 			"TRUCK ID: "	+ rs.getInt("TRUCK_ID") +  " ," + 
		        		 			"DRIVER ID: " + rs.getInt("DRIVER_ID") +  " ," + 
		        		 			"DEAPARTURE TIME: " +rs.getString("DEAPARTURE_TIME") +  " ," + 
		                            "DEAPARTURE DATE: " + rs.getString("DEAPARTURE_DATE") 
		);
		     }
		 }
		break;
		
		case "Source":
			sql = "SELECT ID, ADDRESS , CONTACT_NAME, CONTACT_PHONE FROM SOURCES WHERE Id = ?";
			 
			 try (Connection conn = manager.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	 toReturn =("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"ADDRESS: "	+ rs.getString("ADDRESS") +  " ," + 
			        		 			"CONTACT NAME: " +rs.getString("CONTACT_NAME") +  " ," + 
			                            "CONTACT PHONE: " + rs.getInt("CONTACT_PHONE") 
			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			
		  		break;
		  		
		case "Truck":
			sql = "SELECT ID, MODEL , COLOR , NETO_WEIGHT , MAX_WEIGHT FROM TRUCKS WHERE Id = ?";
			 
			 try (Connection conn = manager.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	 toReturn = ("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"MODEL: "	+ rs.getString("MODEL") +  " ," + 
			        		 			"COLOR: " + rs.getString("COLOR") +  " ," + 
			        		 			"NETO WEIGHT: " +rs.getInt("NETO_WEIGHT") +  " ," + 
			                            "MAX WEIGHT: " + rs.getInt("MAX_WEIGHT") 
			);
			     }
			 }
			
	  		break;
	  		
		case "Driver":
			sql = "SELECT ID, LICENCE_KIND , FIRST_NAME , LAST_NAME FROM DRIVERS WHERE Id = ?";
			 
			 try (Connection conn = manager.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	 toReturn =("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"LICENCE KIND: "	+ rs.getString("LICENCE_KIND") +  " ," + 
			        		 			"FIRST NAME: " +rs.getString("FIRST_NAME") +  " ," + 
			                            "LAST NAME: " + rs.getString("LAST_NAME") 
			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			
	  		break;
	  		
		case "Destination":
			sql = "SELECT ID , ADDRESS , CONTACT_NAME, CONTACT_PHONE , DELIVERY_AREA FROM DESTINATIONS WHERE Id = ?";
			 
			 try (Connection conn = manager.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	 toReturn =("ID: " + rs.getInt("ID") +  " ," + 
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
	  		
		case "Reservation_Doc":
			sql = "SELECT ID, TRANSPORT_ID FROM RESERVATION_DOCUMENTS WHERE Id = ?";
			 
			 try (Connection conn = manager.getConnection();
			      PreparedStatement pstmt  = conn.prepareStatement(sql)){
			     
			     // set the value
			     pstmt.setInt(1,ID);
			     //
			     ResultSet rs  = pstmt.executeQuery();
			     
			     // loop through the result set
			     while (rs.next()) {
			    	 toReturn =("ID: " + rs.getInt("ID") +  " ," + 
			        		 			"TRANSPORT ID: "	+ rs.getString("TRANSPORT_ID")

			);
			     }
			 } catch (SQLException e) {
			     System.out.println(e.getMessage());
			 }
			
	  		break;

	default:
		break;
	
	}
	return toReturn;	
	}
	
}
*/
package superLi.transportation;
import java.util.LinkedList;

public class Transport {
	private int ID;
	private int Truck_ID;
	private int Driver_ID;
	private String Departure_Date;
	private String Departure_Time;
	private Source Source;
	private LinkedList<Destination> DestinationList;
	
	
	public Transport(int iD, int truck_ID, int driver_ID, String departure_Date, String departure_Time, Source source,
			LinkedList<Destination> destinationList) {
		super();
		ID = iD;
		Truck_ID = truck_ID;
		Driver_ID = driver_ID;
		Departure_Date = departure_Date;
		Departure_Time = departure_Time;
		Source = source;
		DestinationList = destinationList;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getDepartureDate() {
		return Departure_Date;
	}
	public void setDepartureDate(String date) {
		Departure_Date = date;
	}
	public String getDepartureTime() {
		return Departure_Time;
	}
	public void setDepartureTime(String departureTime) {
		Departure_Time = departureTime;
	}

	public int getTruck_ID() {
		return Truck_ID;
	}

	public void setTruck_ID(int truck_ID) {
		Truck_ID = truck_ID;
	}

	public int getDriver_ID() {
		return Driver_ID;
	}
	public void setDriver_ID(int driver_ID) {
		Driver_ID = driver_ID;
	}
	public Source getSource() {
		return Source;
	}
	public void setSource(Source source) {
		Source = source;
	}
	public LinkedList<Destination> getDestinationList() {
		return DestinationList;
	}
	public void setDestinationList(LinkedList<Destination> destinationList) {
		DestinationList = destinationList;
	}
}

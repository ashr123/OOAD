package superLi.transportation;

public class ReservationDocument {
	private int ID;
	private int Transport_ID;
	
	public ReservationDocument(int id,int transpotrt_ID) {
		super();
		ID = id;
		Transport_ID = transpotrt_ID;
	}
	public int getTransport_ID() {
		return Transport_ID;
	}
	public void setTransport_ID(int transport_ID) {
		Transport_ID = transport_ID;
	}
	public int getID() {
		return ID;
	}
	public void setReservationID(int reservationID) {
		ID = reservationID;
	}

	
}

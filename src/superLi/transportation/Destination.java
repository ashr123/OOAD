package superLi.transportation;

public class Destination {
	private int ID;
	

	private String Address;
	private int PhoneNumber;
	private String ContactName;
	private String DELIVERY_AREA;
	public Destination(int id,String address, int phoneNumber, String contactName, String dELIVERY_AREA) {
		super();
		ID = id;
		Address = address;
		PhoneNumber = phoneNumber;
		ContactName = contactName;
		DELIVERY_AREA = dELIVERY_AREA;
	}
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getAddress() {
		return Address;
	}
	public void setAddress(String address) {
		Address = address;
	}
	public int getPhoneNumber() {
		return PhoneNumber;
	}
	public void setPhoneNumber(int phoneNumber) {
		PhoneNumber = phoneNumber;
	}
	public String getContactName() {
		return ContactName;
	}
	public void setContactName(String contactName) {
		ContactName = contactName;
	}

	public String getDELIVERY_AREA() {
		return DELIVERY_AREA;
	}
	public void setDELIVERY_AREA(String dELIVERY_AREA) {
		DELIVERY_AREA = dELIVERY_AREA;
	}

}

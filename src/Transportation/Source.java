package Transportation;

public class Source {
	private int ID;
	
	private String Address;
	private int PhoneNumber;
	private String ContactName;
	public Source(Integer iD,String address, int phoneNumber, String contactName) {
		super();
		ID = iD;
		Address = address;
		PhoneNumber = phoneNumber;
		ContactName = contactName;
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
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	
}

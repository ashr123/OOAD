package Transportation;

public class Driver {
	private int ID;
	private String First_Name;
	private String Last_Name;
	private String Licence_Kind;
	public String getFirst_Name() {
		return First_Name;
	}
	public void setFirst_Name(String first_Name) {
		First_Name = first_Name;
	}
	public String getLast_Name() {
		return Last_Name;
	}
	public void setLast_Name(String last_Name) {
		Last_Name = last_Name;
	}
	
	public int getID() {
		return ID;
	}
	public void setID(int iD) {
		ID = iD;
	}
	public String getLicence_Kind() {
		return Licence_Kind;
	}
	public void setLicence_Kind(String licence_Kind) {
		Licence_Kind = licence_Kind;
	}
	public Driver(int iD,String licence_Kind, String first_Name, String last_Name ) {
		super();
		ID = iD;
		First_Name = first_Name;
		Last_Name = last_Name;
		Licence_Kind = licence_Kind;
	}
	
}

package transportation;

import org.sqlite.SQLiteConfig;
import superLi.employees.Employee;

import java.sql.*;

public class Driver extends Employee
{
	private String licenceKind;

	private Driver(int ID, String firstName, String lastName, Date startingDate, double salary,
	               int bankNum, int bankBranchNum, int bankAccountNum, String licenceKind) throws SQLException
	{
		super(ID, firstName, lastName, startingDate, salary, bankNum, bankBranchNum, bankAccountNum);
		this.licenceKind=licenceKind;
	}

	public Driver getDriver(int ID)
	{
		Employee employee=getEmployee(ID);
		if (employee==null)
			return null;
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT LICENCE_KIND FROM DRIVERS WHERE ID=?;"))
		{
			stmt.setInt(1, ID);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				if (resultSet.next())
					return new Driver(employee.getID(),
					                  employee.getFirstName(),
					                  employee.getLastName(),
					                  employee.getStartingDate(),
					                  employee.getSalary(),
					                  employee.getBankNum(),
					                  employee.getbankBranchNum(),
					                  employee.getBankAccountNum(),
					                  resultSet.getString(0));
				else
					return null;
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error at getting an Driver!!");
			return null;
		}
	}

	public static boolean addDriver(int ID, String licenceKind)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement(
		     		"INSERT INTO DRIVERS (ID, LICENCE_KIND) VALUES (?, ?);"))
		{
			stmt.setInt(1, ID);
			stmt.setString(2, licenceKind.trim());
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at adding an Driver!!");
			return false;
		}
	}

	public String getLicenceKind() {
		return licenceKind;
	}

	public void setLicenceKind(String licenceKind) {
		this.licenceKind=licenceKind;
	}

	private static Connection getConnection() throws SQLException
	{
		SQLiteConfig config=new SQLiteConfig();
		config.enforceForeignKeys(true);
		return DriverManager.getConnection("jdbc:sqlite:mydb.db", config.toProperties());
	}
}

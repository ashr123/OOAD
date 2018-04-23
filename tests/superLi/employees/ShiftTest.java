package superLi.employees;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sqlite.SQLiteConfig;

import java.sql.*;

import static org.junit.Assert.*;

public class ShiftTest
{
	private static Connection getConnection() throws SQLException
	{
		SQLiteConfig config=new SQLiteConfig();
		config.enforceForeignKeys(true);
		return DriverManager.getConnection("jdbc:sqlite:mydb.db", config.toProperties());
	}

	@BeforeClass
	static void populateDB()
	{
		EmployeeTest.populateDBs();
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("INSERT INTO Shifts (ID, date, isMorningShift, job) VALUES (?, ?, ?, ?);"))
		{
			
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
		}
	}

	@AfterClass
	static void unPopulateDB()
	{
		EmployeeTest.unPopulateDBs();
	}
	static

	@Test
	public void getShift()
	{

	}

	@Test
	public void addEmployeeToShift()
	{
	}

	@Test
	public void isShiftExists()
	{
	}

	@Test
	public void showShiftAt()
	{
	}
}
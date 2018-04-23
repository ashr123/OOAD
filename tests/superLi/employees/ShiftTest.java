package superLi.employees;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sqlite.SQLiteConfig;

import java.sql.*;
import java.time.LocalDate;

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
	public static void populateDB()
	{
		EmployeeTest.populateDBs();

		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("INSERT INTO Shifts (ID, date, isMorningShift, job) VALUES (?, ?, ?, ?);"))
		{
			stmt.setInt(1, 123456789);
			stmt.setString(2, LocalDate.now().toString());
			stmt.setBoolean(3, true);
			stmt.setString(4, "job1");
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
		}
	}

	@AfterClass
	public static void unPopulateDB()
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("DELETE FROM Shifts where ID=?;"))
		{
			stmt.setInt(1, 123456789);
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
		}
		EmployeeTest.unPopulateDBs();
	}

	@Test
	public void getShift()
	{
//		assertNotNull(Shift.getShift(LocalDate.now().getDayOfMonth()<10 ?
//		                             "0"+LocalDate.now().getDayOfMonth() :
//		                             LocalDate.now().getDayOfMonth()+"",
//
//		                             LocalDate.now().getMonthValue()<10 ?
//		                             "0"+LocalDate.now().getMonthValue() :
//		                             LocalDate.now().getMonthValue()+"",
//
//		                             LocalDate.now().getYear()+"",
//		                             true));
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
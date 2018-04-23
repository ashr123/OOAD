package superLi.employees;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class EmployeeTest
{
	private static Connection getConnection() throws SQLException
	{
		SQLiteConfig config=new SQLiteConfig();
		config.enforceForeignKeys(true);
		return DriverManager.getConnection("jdbc:sqlite:mydb.db", config.toProperties());
	}

	@BeforeClass
	static void populateDBs()
	{
		Employee.init();
		Shift.init();
		try (Connection conn=getConnection();
		     PreparedStatement stmt1=conn.prepareStatement(
				     "INSERT INTO Employees (ID, firstName, lastName, salary, startingDate, bankNum, bankBranchNum, bankAccountNum) VALUES (?, ?, ?, ?, date('now'), ?, ?, ?), (?, ?, ?, ?, date('now'), ?, ?, ?);");
		     PreparedStatement stmt2=conn.prepareStatement("INSERT INTO jobs (job) VALUES (?), (?), (?);");
		     PreparedStatement stmt3=conn.prepareStatement(
				     "INSERT INTO WorkingHours (ID, date, morningShift, noonShift) VALUES (?, ?, ?, ?), (?, ?, ?, ?);");
		     PreparedStatement stmt4=conn.prepareStatement(
				     "INSERT INTO Qualifications (ID, job) VALUES (?, ?), (?, ?), (?, ?);");
		     //		PreparedStatement stmt5= conn.prepareStatement("INSERT INTO Shifts (ID, date, isMorningShift, job) VALUES (?, ?, ?, ?), (?, ?, ?, ?);")
		)
		{
			stmt1.setInt(1, 123456789);
			stmt1.setString(2, "Roy");
			stmt1.setString(3, "Ash");
			stmt1.setDouble(4, 30000);
			stmt1.setInt(5, 12);
			stmt1.setInt(6, 123);
			stmt1.setInt(7, 123456);

			stmt1.setInt(8, 987654321);
			stmt1.setString(9, "Tommer");
			stmt1.setString(10, "Levy");
			stmt1.setDouble(11, 10000);
			stmt1.setInt(12, 99);
			stmt1.setInt(13, 999);
			stmt1.setInt(14, 999999);
			stmt1.executeUpdate();

			stmt2.setString(1, "job1");
			stmt2.setString(2, "job2");
			stmt2.setString(3, "job3");
			stmt2.executeUpdate();

			stmt3.setInt(1, 123456789);
			stmt3.setString(2, LocalDate.now().toString());
			stmt3.setBoolean(3, true);
			stmt3.setBoolean(4, true);

			stmt3.setInt(5, 987654321);
			stmt3.setString(6, LocalDate.now().toString());
			stmt3.setBoolean(7, false);
			stmt3.setBoolean(8, true);
			stmt3.executeUpdate();

			stmt4.setInt(1, 123456789);
			stmt4.setString(2, "job1");

			stmt4.setInt(3, 123456789);
			stmt4.setString(4, "job2");

			stmt4.setInt(5, 987654321);
			stmt4.setString(6, "job2");
			stmt4.executeUpdate();

			//LocalDate.now().getYear()+"-"+LocalDate.now().getMonthValue()+'-'+LocalDate.now().getDayOfMonth()
			//			stmt5.setInt(1, 123456789);
			//			stmt5.setString(2, LocalDate.now().toString());
			//			stmt5.setBoolean(3, true);
			//			stmt5.setString(4, "job1");
			//
			//			stmt5.setInt(1, 123456789);
			//			stmt5.setString(2, LocalDate.now().toString());
			//			stmt5.setBoolean(3, false);
			//			stmt5.setString(4, "job1");
			System.out.println("Populated!!");
		}
		catch (SQLException e)
		{
			System.err.println(e);
		}
	}

	@AfterClass
	static void unPopulateDBs()
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt1=conn.prepareStatement("DELETE FROM Employees WHERE ID=? OR ID=?;");
		     PreparedStatement stmt2=conn.prepareStatement("DELETE FROM jobs WHERE job=? OR job=? OR job=?;");
		     PreparedStatement stmt3=conn.prepareStatement(
				     "DELETE FROM WorkingHours WHERE ID=? OR ID=?;");
		     PreparedStatement stmt4=conn.prepareStatement(
				     "DELETE FROM Qualifications WHERE ID=? OR ID=? OR ID=?;");
		     //		PreparedStatement stmt5= conn.prepareStatement("INSERT INTO Shifts (ID, date, isMorningShift, job) VALUES (?, ?, ?, ?), (?, ?, ?, ?);")
		)
		{
			stmt1.setInt(1, 123456789);
			stmt1.setInt(2, 987654321);
			stmt1.executeUpdate();

			stmt2.setString(1, "job1");
			stmt2.setString(2, "job2");
			stmt2.setString(3, "job3");
			stmt2.executeUpdate();

			stmt3.setInt(1, 123456789);
			stmt3.setInt(2, 987654321);
			stmt3.executeUpdate();

			stmt4.setInt(1, 123456789);
			stmt4.setInt(2, 123456789);
			stmt4.setInt(3, 987654321);
			stmt4.executeUpdate();
			System.out.println("Unpopulated!!");
		}
		catch (SQLException e)
		{
			System.err.println(e);
		}
	}

	@Test
	public void addEmployee()
	{
		assertTrue(Employee.addEmployee(111111111, "Steve", "Cohen",
		                                3000, 11, 111,
		                                111111));
		assertFalse(Employee.addEmployee(111111111, "Steve", "Cohen",
		                                 3000, 11, 111,
		                                 111111));
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("DELETE FROM Employees WHERE ID=? OR ID=?;");
		)
		{
			stmt.setInt(1, 111111111);
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.err.println(e);
		}
	}


	@Test
	public void getEmployee()
	{
		Employee employee=Employee.getEmployee(123456789);
		assertNotNull(employee);
		assertEquals(123456789, employee.getID());
	}

	@Test
	public void showAvailableEmployeesToShift()
	{
		assertNotNull(Employee.showAvailableEmployeesToShift(LocalDate.now().getDayOfMonth()<10 ?
		                                                     "0"+LocalDate.now().getDayOfMonth() :
		                                                     LocalDate.now().getDayOfMonth()+"",

		                                                     LocalDate.now().getMonthValue()<10 ?
		                                                     "0"+LocalDate.now().getMonthValue() :
		                                                     LocalDate.now().getMonthValue()+"",

		                                                     LocalDate.now().getYear()+"",
		                                                     true, "job1"));
	}

	@Test
	public void addJob()
	{
		assertTrue(Employee.addJob("Job4"));
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("DELETE FROM Jobs WHERE job=?;");
		)
		{
			stmt.setString(1, "Job4");
			stmt.executeUpdate();
		}
		catch (SQLException e)
		{
			System.err.println(e);
		}
	}

	@Test
	public void updateEmployee()
	{
		assertTrue(Employee.getEmployee(123456789).updateEmployee("eee", "qqq", 29000, 12, 333, 111111));
	}

	@Test
	public void addAvailability()
	{
		assertTrue(Employee.addAvailability(987654321,
		                                    LocalDate.now().getDayOfMonth()<10 ?
		                                    "0"+LocalDate.now().getDayOfMonth() :
		                                    LocalDate.now().getDayOfMonth()+"",

		                                    LocalDate.now().getMonthValue()<10 ?
		                                    "0"+LocalDate.now().getMonthValue() :
		                                    LocalDate.now().getMonthValue()+"",

		                                    LocalDate.now().getYear()+"",
		                                    true, true));
	}

	@Test
	public void addQualification()
	{
		assertTrue(Employee.addQualification(987654321, "job3"));
		assertFalse(Employee.addQualification(987654321, "job5"));
	}
}
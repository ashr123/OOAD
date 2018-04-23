package superLi.employees;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.sql.*;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class EmployeeTest
{
	private static final String DB_CON_URL="jdbc:sqlite:mydb.db";

	@BeforeClass
	public void populateDBs()
	{
		Employee.init();
		Shift.init();
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement(
				     "INSERT INTO Employees (ID, firstName, lastName, salary, startingDate, bankNum, bankBrunchNum, bankAccountNum) VALUES (?, ?, ?, ?, date('now'), ?, ?, ?), (?, ?, ?, ?, date('now'), ?, ?, ?);");
		     PreparedStatement stmt2=conn.prepareStatement("INSERT INTO jobs (job) VALUES (?), (?);");
		     PreparedStatement stmt3=conn.prepareStatement(
				     "INSERT INTO WorkingHours (ID, date, morningShift, noonShift) VALUES (?, ?, ?, ?), (?, ?, ?, ?), (?, ?, ?, ?);");)
		{
			stmt.setInt(1, 123456789);
			stmt.setString(2, "Roy");
			stmt.setString(3, "Ash");
			stmt.setDouble(4, 30000);
			stmt.setInt(5, 12);
			stmt.setInt(6, 123);
			stmt.setInt(7, 123456);

			stmt.setInt(8, 987654321);
			stmt.setString(9, "Tommer");
			stmt.setString(10, "Levy");
			stmt.setDouble(11, 10000);
			stmt.setInt(12, 99);
			stmt.setInt(13, 999);
			stmt.setInt(14, 999999);
			stmt.executeUpdate();

			stmt2.setString(1, "job1");
			stmt2.setString(2, "job2");
			stmt2.setString(3, "job3");
			stmt2.executeUpdate();

			stmt3.setInt(1, 123456789);
			stmt3.setString(2, LocalDate.now().toString());
			stmt3.executeUpdate();
		}
		catch (SQLException e)
		{
			System.err.println(e);
		}
	}


	@Test
	public void addEmployee1()
	{
		assertTrue(Employee.addEmployee(111111111, "Steve", "Cohen",
		                                       3000, 11, 111,
		                                       111111));
		assertFalse(Employee.addEmployee(111111111, "Steve", "Cohen",
		                                       3000, 11, 111,
		                                       111111));
	}


	@Test
	public void getEmployee()
	{
		Employee employee=Employee.getEmployee(111111111);
		assertNotNull(employee);
		assertEquals(111111111, employee.getID());
	}

	@Test
	public void showAvailableEmployeesToShift()
	{
	}

	@Test
	public void addJob()
	{
	}

	@Test
	public void updateEmployee()
	{
	}
}
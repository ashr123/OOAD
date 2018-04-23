package superLi.employees;

import org.sqlite.SQLiteConfig;
import superLi.DBTablePrinter;

import java.sql.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Shift
{
	static
	{
		synchronized (Shift.class)
		{
			try (Connection conn=getConnection();
			     Statement stmt=conn.createStatement())
			{
//				stmt.executeUpdate("DROP TABLE IF EXISTS Shifts;");
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Shifts"+
				                   "("+
				                   "ID INTEGER REFERENCES Employees(ID) ON DELETE CASCADE , "+
				                   "date TEXT NOT NULL CHECK (DATE(date)>=DATE('now')), "+
				                   "isMorningShift BOOLEAN NOT NULL, "+
				                   "job TEXT REFERENCES Jobs(job) ON DELETE CASCADE, "+
				                   "PRIMARY KEY(ID, date, isMorningShift, job)"+
				                   ");"
				                  );
			}
			catch (SQLException e)
			{
				System.err.println("Error at building the Shift table!!");
				System.exit(1);
			}
		}
	}

	private Date date;
	private Map<Employee, Collection<String>> employeeToJobsMap=new HashMap<>();
	private boolean isMorningShift;

	private Shift(String day, String month, String year, boolean isMorningShift) throws SQLException
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT ID, job FROM Shifts "+
		                                                  "WHERE "+
		                                                  "DATE(date)=DATE(?) AND "+
		                                                  "isMorningShift is ?;"))
		{
			stmt.setString(1, year+'-'+month+'-'+day);
			stmt.setBoolean(2, isMorningShift);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				if (resultSet.isClosed())
					throw new SQLException("No shift at this date and time");
				else
				{
					date=Date.valueOf(year+'-'+month+'-'+day);
					this.isMorningShift=isMorningShift;
				}
				while (resultSet.next())
				{
					Employee employee=Employee.getEmployee(resultSet.getInt("ID"));
					if (!employeeToJobsMap.containsKey(employee))
						employeeToJobsMap.put(employee, new LinkedList<>());
					if (!employeeToJobsMap.get(employee).contains(resultSet.getString("job")))
						employeeToJobsMap.get(employee).add(resultSet.getString("job"));
				}
			}
		}
	}

	private static Connection getConnection() throws SQLException
	{
		SQLiteConfig config=new SQLiteConfig();
		config.enforceForeignKeys(true);
		return DriverManager.getConnection("jdbc:sqlite:mydb.db", config.toProperties());
	}

	public static Shift getShift(String day, String month, String year, boolean isMorningShift)
	{
		try
		{
			return new Shift(day, month, year, isMorningShift);
		}
		catch (SQLException e)
		{
			System.err.println("Error at getting a shift!!");
			return null;
		}
	}

	public static boolean addEmployeeToShift(int ID, String day, String month, String year, boolean isMorningShift,
	                                      String job)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM WorkingHours WHERE "+
		                                                  "DATE(date)=DATE(?) AND "+
		                                                  "morningShift is ? AND ID=?"))
		{
			stmt.setString(1, year+'-'+month+'-'+day);
			stmt.setBoolean(2, isMorningShift);
			stmt.setInt(3, ID);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				while (resultSet.next())
					try (PreparedStatement stmt2=conn.prepareStatement("INSERT INTO Shifts (ID, "+
					                                                   "date, "+
					                                                   "isMorningShift, "+
					                                                   "job) VALUES (?, ?, ?, ?);"))
					{
						stmt2.setInt(1, ID);
						stmt2.setString(2, year+'-'+month+'-'+day);
						stmt2.setBoolean(3, isMorningShift);
						stmt2.setString(4, job);
						stmt2.executeUpdate();
					}
			}
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at adding an employee to shift");
			return false;
		}
	}

	public static boolean isShiftExists(String day, String month, String year, boolean isMorningShift)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM Shifts WHERE "+
		                                                  "DATE(date)=DATE(?) AND "+
		                                                  "isMorningShift is ?;"))
		{
			stmt.setString(1, year+'-'+month+'-'+day);
			stmt.setBoolean(2, isMorningShift);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				return !resultSet.isClosed();
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error at checking if a shift exists!!");
			return false;
		}
	}

	public static String showShiftAt(String day, String month, String year, boolean isMorningShift)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT Employees.ID, firstName, lastName, "+
		                                                  "job FROM "+
		                                                  "Employees, Shifts "+
		                                                  "WHERE "+
		                                                  "DATE(date)=DATE(?) AND "+
		                                                  "isMorningShift is ? AND Employees.ID=Shifts.ID;"))
		{
			stmt.setString(1, year+'-'+month+'-'+day);
			stmt.setBoolean(2, isMorningShift);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				return DBTablePrinter.printResultSet(resultSet);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e);
			return null;
		}
	}

	public static void init()
	{
	}

	public Date getDate()
	{
		return new Date(date.getTime());
	}

	public Map<Employee, Collection<String>> getEmployeeToJobsMap()
	{
		return new HashMap<>(employeeToJobsMap);
	}

	public boolean isMorningShift()
	{
		return isMorningShift;
	}

	@Override
	public String toString()
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT Employees.ID, firstName, lastName, "+
		                                                  "job FROM "+
		                                                  "Employees, Shifts "+
		                                                  "WHERE "+
		                                                  "DATE(date)=DATE(?) AND "+
		                                                  "isMorningShift is ? AND Employees.ID=Shifts.ID;"))
		{
			stmt.setString(1, date.toString());
			stmt.setBoolean(2, isMorningShift);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				return "Shift on date: "+date+", at "+(isMorningShift ? "morning" : "evening")+" details:\n"+DBTablePrinter.printResultSet(resultSet);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e);
			return null;
		}
	}
}
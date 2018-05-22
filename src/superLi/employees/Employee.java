package superLi.employees;

import org.sqlite.SQLiteConfig;
import superLi.DBTablePrinter;

import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Scanner;

/**
 * Represents an employee in a neighborhood grocery store.
 * Also immutable object.
 */
public class Employee
{
	//Initiates the DB for the first time if not exists.
	static
	{
		synchronized (Employee.class)
		{
			try (Connection conn=getConnection();
			     Statement stmt=conn.createStatement())
			{
				stmt.execute("PRAGMA foreign_keys=ON");
				//				stmt.executeUpdate("DROP TABLE IF EXISTS Employees;");
				//				stmt.executeUpdate("DROP TABLE IF EXISTS WorkingHours;");
				//				stmt.executeUpdate("DROP TABLE IF EXISTS jobs;");
				//				stmt.executeUpdate("DROP TABLE IF EXISTS Qualifications;");
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Employees"+
				                   '('+
				                   "ID INTEGER PRIMARY KEY CHECK (ID BETWEEN 100000000 AND 999999999), "+
				                   "firstName VARCHAR(20) NOT NULL, "+
				                   "lastName VARCHAR(20) NOT NULL, "+
				                   "salary REAL NOT NULL CHECK (salary>=0), "+
				                   "startingDate TEXT NOT NULL, "+
				                   "bankNum INTEGER NOT NULL CHECK (bankNum BETWEEN 10 AND 99), "+
				                   "bankBranchNum INTEGER NOT NULL CHECK (bankBranchNum BETWEEN 100 AND 999), "+
				                   "bankAccountNum INTEGER NOT NULL CHECK (bankAccountNum BETWEEN 100000 AND 999999)"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS WorkingHours"+
				                   "("+
				                   "ID INTEGER REFERENCES Employees(ID) ON DELETE CASCADE, "+
				                   "date TEXT NOT NULL CHECK (DATE(date)>=DATE('now')), "+
				                   "morningShift BOOLEAN NOT NULL, "+
				                   "noonShift BOOLEAN NOT NULL,"+
				                   "PRIMARY KEY(ID, date, morningShift, noonShift),"+
				                   "CONSTRAINT bbb CHECK (morningShift OR noonShift)"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Jobs"+
				                   "("+
				                   "job TEXT PRIMARY KEY"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Qualifications"+
				                   "("+
				                   "ID INTEGER REFERENCES Employees(ID) ON DELETE CASCADE, "+
				                   "job TEXT REFERENCES Jobs(job) ON DELETE CASCADE, "+
				                   "PRIMARY KEY (ID, job)"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DRIVERS ("
				                   +"	ID integer PRIMARY KEY REFERENCES Employees(ID),"
				                   +"	LICENCE_KIND integer NOT NULL"
				                   +");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TRUCKS ("
				                   +"	ID integer PRIMARY KEY,"
				                   +"	MODEL text NOT NULL, "
				                   +"	COLOR text NOT NULL, "
				                   +"	NETO_WEIGHT integer NOT NULL,"
				                   +"	MAX_WEIGHT integer NOT NULL "
				                   +");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TRANSPORTAIONS ("
				                   +"	ID integer PRIMARY KEY,"
				                   +"	TRUCK_ID integer NOT NULL,"
				                   +"	DRIVER_ID integer NOT NULL,"
				                   +"	DEAPARTURE_TIME text NOT NULL, "
				                   +"	DEAPARTURE_DATE text NOT NULL, "
				                   +" FOREIGN KEY(DRIVER_ID) REFERENCES DRIVERS(ID), "
				                   +" FOREIGN KEY(TRUCK_ID) REFERENCES TRUCKS(ID)"
				                   +");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS TRANSPORT_DESTINATIONS ("
				                   +"	TRANSPORT_ID integer NOT NULL,"
				                   +"	SOURCE_ID integer NOT NULL, "
				                   +"	DESTINATION_ID integer NOT NULL, "
				                   +" FOREIGN KEY(SOURCE_ID) REFERENCES SOURCES(ID), "
				                   +" FOREIGN KEY(DESTINATION_ID) REFERENCES DESTINATIONS(ID), "
				                   +" FOREIGN KEY(TRANSPORT_ID) REFERENCES TRANSPORTAIONS(ID) "
				                   +");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS DESTINATIONS ("
				                   +"	ID integer PRIMARY KEY,"
				                   +"	ADDRESS text NOT NULL,"
				                   +"	CONTACT_NAME text NOT NULL, "
				                   +"	CONTACT_PHONE integer NOT NULL, "
				                   +"	DELIVERY_AREA text NOT NULL"
				                   +");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS SOURCES ("
				                   +"	ID integer PRIMARY KEY,"
				                   +"	ADDRESS text NOT NULL,"
				                   +"	CONTACT_NAME text NOT NULL, "
				                   +"	CONTACT_PHONE integer NOT NULL"
				                   +");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS RESERVATION_DOCUMENTS ("
				                   +"	ID integer PRIMARY KEY,"
				                   +"	TRANSPORT_ID integer NOT NULL, "
				                   +" FOREIGN KEY(TRANSPORT_ID) REFERENCES TRANSPORTAIONS(ID)"
				                   +");"
				                  );
			}
			catch (SQLException e)
			{
				System.err.println("Error building DB!!");
				System.exit(1);
			}
		}
	}

	private final int ID;
	private String firstName, lastName;
	private Date startingDate;
	private double salary;
	private int bankNum, bankBranchNum, bankAccountNum;
	private Collection<Availability> availabilities=new LinkedList<>();
	private Collection<String> qualifications=new LinkedList<>();

	/**
	 * Constructor<br>
	 * Private because it's not allowed to create an independent {@link Employee} outside the DB.
	 *
	 * @param ID             the Identity number of the employee
	 * @param firstName      the first name of the employee
	 * @param lastName       the last name of the employee
	 * @param startingDate   date of commencement of employing an employee
	 * @param salary         the salary of the employee
	 * @param bankNum        number of the employee's bank
	 * @param bankBranchNum  number of the employee's bank's brunch
	 * @param bankAccountNum number of the employee's bank account
	 */
	protected Employee(int ID, String firstName, String lastName, Date startingDate, double salary,
	                   int bankNum, int bankBranchNum, int bankAccountNum) throws SQLException
	{
		this.ID=ID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.startingDate=startingDate;
		this.salary=salary;
		this.bankNum=bankNum;
		this.bankBranchNum=bankBranchNum;
		this.bankAccountNum=bankAccountNum;
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement(
				     "SELECT date, morningShift, noonShift FROM WorkingHours WHERE ID=?;");
		     PreparedStatement stmt2=conn.prepareStatement("SELECT job FROM Qualifications WHERE ID=?"))
		{
			stmt.setInt(1, ID);
			stmt2.setInt(1, ID);
			try (ResultSet resultSet=stmt.executeQuery();
			     ResultSet resultSet2=stmt2.executeQuery())
			{
				while (resultSet.next())
					availabilities.add(new Availability(Date.valueOf(
							resultSet.getString("date")),
					                                    resultSet.getBoolean("morningShift"),
					                                    resultSet.getBoolean("noonShift")));
				while (resultSet2.next())
					qualifications.add(resultSet2.getString("job"));
			}
		}
	}

	private static Connection getConnection() throws SQLException
	{
		SQLiteConfig config=new SQLiteConfig();
		config.enforceForeignKeys(true);
		return DriverManager.getConnection("jdbc:sqlite:mydb.db", config.toProperties());
	}

	/**
	 * Adds a new employee to the store
	 *
	 * @param ID             the Identity number of the employee
	 * @param firstName      the first name of the employee
	 * @param lastName       the last name of the employee
	 * @param salary         the salary of the employee
	 * @param bankNum        number of the employee's bank
	 * @param bankBranchNum  number of the employee's bank's brunch
	 * @param bankAccountNum number of the employee's bank account
	 * @return {@code true} if the employee was added successfully to the DB, {@code false} otherwise
	 */
	public static boolean addEmployee(int ID, String firstName, String lastName, double salary, int bankNum,
	                                  int bankBranchNum, int bankAccountNum)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement(
				     "INSERT INTO Employees (ID, firstName, lastName, salary, startingDate, bankNum, bankBranchNum, bankAccountNum) VALUES (?, ?, ?, ?, date('now'), ?, ?, ?);"))
		{
			stmt.setInt(1, ID);
			stmt.setString(2, firstName.trim());
			stmt.setString(3, lastName.trim());
			stmt.setDouble(4, salary);
			stmt.setInt(5, bankNum);
			stmt.setInt(6, bankBranchNum);
			stmt.setInt(7, bankAccountNum);
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at adding an employee!!");
			return false;
		}
	}

	/**
	 * Gets an existing employee from the DB by it's ID
	 *
	 * @param ID the Identity number of the employee
	 * @return an {@link Employee} if it's ID exists in the DB, {@code null} otherwise
	 */
	public static Employee getEmployee(int ID)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM Employees WHERE ID=?;"))
		{
			stmt.setInt(1, ID);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				if (resultSet.next())
					return new Employee(resultSet.getInt("ID"),
					                    resultSet.getString("firstName"),
					                    resultSet.getString("lastName"),
					                    Date.valueOf(resultSet.getString("startingDate")),
					                    resultSet.getDouble("salary"),
					                    resultSet.getInt("bankNum"),
					                    resultSet.getInt("bankBranchNum"),
					                    resultSet.getInt("bankAccountNum")
					);
				else
					return null;
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error at getting an employee!!");
			return null;
		}
	}

	public static boolean addAvailability(int ID, String day, String month, String year, boolean isMorningShift,
	                                      boolean isNoonShift)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement(
				     "INSERT INTO WorkingHours (ID, date, morningShift, noonShift) VALUES (?, ?, ?, ?);"))
		{
			stmt.setInt(1, ID);
			stmt.setString(2, year+'-'+month+'-'+day);
			stmt.setBoolean(3, isMorningShift);
			stmt.setBoolean(4, isNoonShift);
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at adding Availability!!");
			return false;
		}
	}

	public static boolean addQualification(int ID, String job)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("INSERT INTO Qualifications (ID, job) VALUES (?, ?);"))
		{
			stmt.setInt(1, ID);
			stmt.setString(2, job.trim());
			stmt.executeUpdate();
			if (job.equals("Driver"))
				try (PreparedStatement stmt2=conn.prepareStatement(
						"INSERT INTO DRIVERS (ID, LICENCE_KIND) VALUES (?, ?);"))
				{
					stmt2.setInt(1, ID);
					System.out.print("Enter licence kind: ");
					stmt2.setString(2, new Scanner(System.in).nextLine().trim());
					stmt2.executeUpdate();
				}
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at getting Qualification!!");
			return false;
		}
	}

	public static String showAvailableEmployeesToShift(String day, String month, String year, boolean isMorningShift,
	                                                   String job)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement(isMorningShift ?
		                                                  "SELECT Employees.ID, firstName, lastName "+
		                                                  "FROM Employees, WorkingHours, Qualifications "+
		                                                  "WHERE DATE(date)=DATE(?) AND "+
		                                                  "MorningShift AND "+
		                                                  "Qualifications.job=?;" :

		                                                  "SELECT Employees.ID, firstName, lastName "+
		                                                  "FROM Employees, WorkingHours, Qualifications "+
		                                                  "WHERE DATE(date)=DATE(?) AND "+
		                                                  "noonShift AND "+
		                                                  "Qualifications.job=?;"))
		{
			stmt.setString(1, year+'-'+month+'-'+day);
			stmt.setString(2, job);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				return DBTablePrinter.printResultSet(resultSet);
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error at showing Available Employees To Shift!!");
			return null;
		}
	}

	public static boolean addJob(String job)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("INSERT INTO jobs (job) VALUES (?);"))
		{
			stmt.setString(1, job);
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at adding job");
			return false;
		}
	}

	public static void init()
	{
	}

	public int getID()
	{
		return ID;
	}

	public String getFirstName()
	{
		return firstName;
	}

	public String getLastName()
	{
		return lastName;
	}

	public Date getStartingDate()
	{
		return new Date(startingDate.getTime());
	}

	public double getSalary()
	{
		return salary;
	}

	public int getBankNum()
	{
		return bankNum;
	}

	public int getbankBranchNum()
	{
		return bankBranchNum;
	}

	public int getBankAccountNum()
	{
		return bankAccountNum;
	}

	public Collection<Availability> getAvailabilities()
	{
		return new LinkedList<>(availabilities);
	}

	/**
	 * Updates an existing employee
	 *
	 * @param firstName      the new first name of the employee
	 * @param lastName       the new last name of the employee
	 * @param salary         the new salary of the employee
	 * @param bankNum        number of the employee's bank
	 * @param bankBranchNum  number of the employee's bank's brunch
	 * @param bankAccountNum number of the employee's bank account
	 * @return {@code true} if the update was successful, {@code false} otherwise
	 */
	public synchronized boolean updateEmployee(String firstName, String lastName, double salary, int bankNum,
	                                           int bankBranchNum, int bankAccountNum)
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("UPDATE Employees SET firstName=?, "+
		                                                  "lastName=?, "+
		                                                  "salary=?, "+
		                                                  "bankNum=?, "+
		                                                  "bankBranchNum=?, "+
		                                                  "bankAccountNum=? "+
		                                                  "WHERE ID=?;"))
		{
			stmt.setString(1, firstName=firstName.trim());
			stmt.setString(2, lastName=lastName.trim());
			stmt.setDouble(3, salary);
			stmt.setInt(4, bankNum);
			stmt.setInt(5, bankBranchNum);
			stmt.setInt(6, bankAccountNum);
			stmt.setInt(7, getID());
			stmt.executeUpdate();
			this.firstName=firstName;
			this.lastName=lastName;
			this.salary=salary;
			this.bankNum=bankNum;
			this.bankBranchNum=bankBranchNum;
			this.bankAccountNum=bankAccountNum;
			return true;
		}
		catch (SQLException e)
		{
			System.err.println("Error at updating an employee!!");
			return false;
		}
	}

	@Override
	public boolean equals(Object o)
	{
		return (this==o || o!=null) && getClass()==o.getClass() && getID()==((Employee) o).getID();
	}

	@Override
	public synchronized String toString()
	{
		try (Connection conn=getConnection();
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM Employees WHERE ID=?;"))
		{
			stmt.setInt(1, getID());
			try (ResultSet resultSet=stmt.executeQuery())
			{
				return DBTablePrinter.printResultSet(
						resultSet)+"availble at:\n"+availabilities+"\nqualified to:\n"+qualifications;
			}
		}
		catch (SQLException e)
		{
			System.err.println("Error at Employee toString!!");
			return null;
		}
		//		return "Employee{"+
		//		       "ID="+getID()+
		//		       ", First name='"+getFirstName()+'\''+
		//		       ", Last name='"+getLastName()+'\''+
		//		       ", Leaving date="+getLeavingDate()+
		//		       ", Salary="+getSalary()+
		//		       '}';
	}
}
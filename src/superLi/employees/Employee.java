package superLi.employees;

import superLi.DBTablePrinter;

import java.sql.*;

/**
 * Represents an employee in a neighborhood grocery store.
 * Also immutable object.
 */
public class Employee
{
	private static final String DB_CON_URL="jdbc:sqlite:mydb.db";
	private final int ID;
	private String firstName, lastName;
	private Date startingDate;
	private double salary;
	private int bankNum, bankBrunchNum, bankAccountNum;

	//Initiates the DB for the first time if not exists.
	static
	{
		synchronized (Employee.class)
		{
			try (Connection conn=DriverManager.getConnection(DB_CON_URL);
			     Statement stmt=conn.createStatement())
			{
//				stmt.executeUpdate("DROP TABLE IF EXISTS Employees;");
//				stmt.executeUpdate("DROP TABLE IF EXISTS WorkingHours;");
//				stmt.executeUpdate("DROP TABLE IF EXISTS jobs;");
//				stmt.executeUpdate("DROP TABLE IF EXISTS Shifts;");
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Employees"+
				                   '('+
				                   "ID INTEGER PRIMARY KEY CHECK (ID BETWEEN 100000000 AND 999999999), "+
				                   "firstName VARCHAR(20) NOT NULL, "+
				                   "lastName VARCHAR(20) NOT NULL, "+
				                   "salary REAL NOT NULL CHECK (salary>=0), "+
				                   "startingDate TEXT NOT NULL, "+
				                   "bankNum INTEGER NOT NULL CHECK (bankNum BETWEEN 10 AND 99), "+
				                   "bankBrunchNum INTEGER NOT NULL CHECK (bankBrunchNum BETWEEN 100 AND 999), "+
				                   "bankAccountNum INTEGER NOT NULL CHECK (bankAccountNum BETWEEN 100000 AND 999999)"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS WorkingHours" +
				                   "("+
				                   "ID INTEGER REFERENCES Employees(ID) ON DELETE CASCADE, "+
				                   "date TEXT NOT NULL CHECK (DATE(date)>=DATE('now')), "+
				                   "morningShift BOOLEAN NOT NULL, "+
				                   "noonShift BOOLEAN NOT NULL," +
				                   "PRIMARY KEY(ID, date, morningShift, noonShift)," +
				                   "CONSTRAINT bbb CHECK (morningShift OR noonShift)"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Jobs"+
				                   "("+
				                   "job TEXT PRIMARY KEY"+
				                   ");"
				                  );
				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Shifts"+
				                   "("+
				                   "ID INTEGER references Employees(ID) ON DELETE CASCADE, "+
				                   "date TEXT NOT NULL CHECK (DATE(date)>=DATE('now')), "+
				                   "isMorningShift BOOLEAN NOT NULL, "+
				                   "job TEXT references Jobs(job), "+
				                   "PRIMARY KEY(ID, date, isMorningShift, job)"+
				                   ");"
				                  );
//				stmt.executeUpdate("CREATE TABLE IF NOT EXISTS ManagersInShifts"+
//				             "("+
//				             "ID INTEGER PRIMARY KEY REFERENCES Employees(ID) ON DELETE CASCADE" +
//				             ");");
			}
			catch (SQLException e)
			{
				System.err.println(e);
				System.exit(1);
			}
		}
	}

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
	 * @param bankBrunchNum  number of the employee's bank's brunch
	 * @param bankAccountNum number of the employee's bank account
	 */
	private Employee(int ID, String firstName, String lastName, Date startingDate, double salary,
	                 int bankNum, int bankBrunchNum, int bankAccountNum)
	{
		this.ID=ID;
		this.firstName=firstName;
		this.lastName=lastName;
		this.startingDate=startingDate;
		this.salary=salary;
		this.bankNum=bankNum;
		this.bankBrunchNum=bankBrunchNum;
		this.bankAccountNum=bankAccountNum;
	}

	/**
	 * Adds a new employee to the store
	 *
	 * @param ID             the Identity number of the employee
	 * @param firstName      the first name of the employee
	 * @param lastName       the last name of the employee
	 * @param salary         the salary of the employee
	 * @param bankNum        number of the employee's bank
	 * @param bankBrunchNum  number of the employee's bank's brunch
	 * @param bankAccountNum number of the employee's bank account
	 * @return {@code true} if the employee was added successfully to the DB, {@code false} otherwise
	 */
	public static boolean addEmployee(int ID, String firstName, String lastName, double salary, int bankNum,
	                                  int bankBrunchNum, int bankAccountNum)
	{
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement(
				     "INSERT INTO Employees (ID, firstName, lastName, salary, startingDate, bankNum, bankBrunchNum, bankAccountNum) VALUES (?, ?, ?, ?, date('now'), ?, ?, ?);"))
		{
			stmt.setInt(1, ID);
			stmt.setString(2, firstName.trim());
			stmt.setString(3, lastName.trim());
			stmt.setDouble(4, salary);
			stmt.setInt(5, bankNum);
			stmt.setInt(6, bankBrunchNum);
			stmt.setInt(7, bankAccountNum);
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
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
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM Employees WHERE ID=?;"))
		{
			stmt.setInt(1, ID);
			try (ResultSet resultSet=stmt.executeQuery())
			{
				if (resultSet.next())
				{
					return new Employee(resultSet.getInt("ID"),
					                    resultSet.getString("firstName"),
					                    resultSet.getString("lastName"),
					                    Date.valueOf(resultSet.getString("startingDate")),
					                    resultSet.getDouble("salary"),
					                    resultSet.getInt("bankNum"),
					                    resultSet.getInt("bankBrunchNum"),
					                    resultSet.getInt("bankAccountNum")
					);
				}
				else
					return null;
			}
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
			return null;
		}
	}

	public static boolean addJob(String job)
	{
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement("INSERT INTO jobs (job) VALUES (?);"))
		{
			stmt.setString(1, job);
			stmt.executeUpdate();
			return true;
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
			return false;
		}
	}

	public static void addEmployeeToShift(int ID, String day, String month, String year, boolean isMorningShift, String job)
	{
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
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
					try (PreparedStatement stmt2=conn.prepareStatement("INSERT INTO Shifts (ID, " +
					                                                   "date, " +
					                                                   "isMorningShift, " +
					                                                   "job) VALUES (?, ?, ?, ?);"))
					{
						stmt2.setInt(1, ID);
						stmt2.setString(2, year+'-'+month+'-'+day);
						stmt2.setBoolean(3, isMorningShift);
						stmt2.setString(4, job);
						stmt2.executeUpdate();
					}
			}
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
		}
	}

	public static String showShiftAt(String day, String month, String year, boolean isMorningShift)
	{
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement("SELECT Employees.ID, firstName, lastName, " +
		                                                  "job FROM " +
		                                                  "Employees, Shifts " +
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
			System.err.println(e);//TODO: print a nicer message
			return null;
		}
	}

	public static String seeAvailableEmployeesForShift(String day, String month, String year, boolean isMorningShift)
	{
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM WorkingHours WHERE " +
		                                                  "DATE(date)=DATE(?) AND " +
		                                                  "morningShift is ?;"))
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
			System.err.println(e);//TODO: print a nicer message
			return null;
		}
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

	public int getBankBrunchNum()
	{
		return bankBrunchNum;
	}

	public int getBankAccountNum()
	{
		return bankAccountNum;
	}

	/**
	 * Updates an existing employee
	 *
	 * @param firstName      the new first name of the employee
	 * @param lastName       the new last name of the employee
	 * @param salary         the new salary of the employee
	 * @param bankNum        number of the employee's bank
	 * @param bankBrunchNum  number of the employee's bank's brunch
	 * @param bankAccountNum number of the employee's bank account
	 * @return {@code true} if the update was successful, {@code false} otherwise
	 */
	public synchronized boolean updateEmployee(String firstName, String lastName, double salary, int bankNum,
	                                           int bankBrunchNum, int bankAccountNum)
	{
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement("UPDATE Employees SET firstName=?, "+
		                                                  "lastName=?, "+
		                                                  "salary=?, "+
		                                                  "bankNum=?, "+
		                                                  "bankBrunchNum=?, "+
		                                                  "bankAccountNum=? "+
		                                                  "WHERE ID=?;"))
		{
			stmt.setString(1, firstName=firstName.trim());
			stmt.setString(2, lastName=lastName.trim());
			stmt.setDouble(3, salary);
			stmt.setInt(4, bankNum);
			stmt.setInt(5, bankBrunchNum);
			stmt.setInt(6, bankAccountNum);
			stmt.setInt(7, getID());
			stmt.executeUpdate();
			//			this.startingDate=Date.valueOf(LocalDate.now());
			this.firstName=firstName;
			this.lastName=lastName;
			this.salary=salary;
			this.bankNum=bankNum;
			this.bankBrunchNum=bankBrunchNum;
			this.bankAccountNum=bankAccountNum;
			return true;
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
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
		try (Connection conn=DriverManager.getConnection(DB_CON_URL);
		     PreparedStatement stmt=conn.prepareStatement("SELECT * FROM Employees WHERE ID=?;"))
		{
			stmt.setInt(1, getID());
			try (ResultSet resultSet=stmt.executeQuery())
			{
				return DBTablePrinter.printResultSet(resultSet);
			}
		}
		catch (SQLException e)
		{
			System.err.println(e);//TODO: print a nicer message
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
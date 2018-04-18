package superLi;

import superLi.employees.Employee;

import java.util.Scanner;

public class Main
{
	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args)
	{
		Scanner sc=new Scanner(System.in);
		boolean exit=false;
		while (!exit)
		{
			int id;
			double salary;
			String fname, lname;
			boolean isLeaving=false;
			System.out.println("Enter command: 'add'/'update'/'get'/'exit':");
			String comm=sc.nextLine();

			switch (comm)
			{
				case "add":
				{
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.print("First name: ");
					fname=sc.nextLine();
					System.out.print("Last name: ");
					lname=sc.nextLine();
					System.out.print("Salary: ");
					salary=Double.parseDouble(sc.nextLine());
					if (Employee.addEmployee(id, fname, lname, salary))
						System.out.println("success");
					break;
				}
				case "update":
				{
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					Employee emp=Employee.getEmployee(id);
					if (emp==null)
					{
						System.out.println("No such employee exists");
						break;
					}
					System.out.print("Fist name: ");
					fname=sc.nextLine();
					System.out.print("Last name: ");
					lname=sc.nextLine();
					System.out.print("Salary: ");
					salary=Double.parseDouble(sc.nextLine());
					System.out.print("Is leaving(Y/N): ");
					if (sc.nextLine().equals("Y"))
						isLeaving=true;
					emp.updateEmployee(fname, lname, isLeaving, salary);
					break;
				}
				case "get":
				{
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					Employee employee=Employee.getEmployee(id);
					System.out.println(Employee.getEmployee(id));
					break;
				}
				case "exit":
				{
					exit=true;
					break;
				}
				default:
				{
					System.out.println("Illegal command");
					break;
				}
			}
		}
	}
}
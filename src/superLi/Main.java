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
		while (true)
		{
			int id, bankNum, bankBrunchNum, bankAccountNum;
			double salary;
			String fname, lname, year, month, day, cIsMorningShift;
			boolean isMorningShift=false;
			System.out.println("Enter command: 'add'/'update'/'get'/'addworking'/'addjob'/'addshift'/'showshift'/'exit':");
			String comm=sc.nextLine().trim();

			switch (comm)
			{
				case "add":
					System.out.print("Id: ");
					id=sc.nextInt();
					System.out.print("First name: ");
					fname=sc.nextLine();
					System.out.print("Last name: ");
					lname=sc.nextLine();
					System.out.print("Salary: ");
					salary=sc.nextDouble();
					System.out.print("Bank number: ");
					bankNum=sc.nextInt();
					System.out.print("Bank brunch number: ");
					bankBrunchNum=sc.nextInt();
					System.out.print("Bank account number: ");
					bankAccountNum=sc.nextInt();
					if (Employee.addEmployee(id, fname, lname, salary, bankNum, bankBrunchNum, bankAccountNum))
						System.out.println("success");
					break;
				case "update":
					System.out.print("Id: ");
					id=sc.nextInt();
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
					salary=sc.nextDouble();
					System.out.print("Bank number: ");
					bankNum=sc.nextInt();
					System.out.print("Bank brunch number: ");
					bankBrunchNum=sc.nextInt();
					System.out.print("Bank account number: ");
					bankAccountNum=Integer.parseInt(sc.nextLine());
					emp.updateEmployee(fname, lname, salary, bankNum, bankBrunchNum, bankAccountNum);
					break;
				case "get":
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.println(Employee.getEmployee(id));
					break;
				case "addworking":
					System.out.print("Enter year (yyyy): ");
					year=sc.nextLine();
					System.out.print("Enter month (mm): ");
					month=sc.nextLine();
					System.out.print("Enter day (dd): ");
					day=sc.nextLine();
					System.out.print("Is it a morning shift? (y/n): ");
					cIsMorningShift=sc.nextLine();
					isMorningShift=cIsMorningShift.equals("y");
					System.out.println(Employee.seeAvailableEmployeesForShift(day, month, year, isMorningShift));
					break;
				case "addjob":
					System.out.print("Enter job: ");
					Employee.addJob(sc.nextLine());
					break;
				case "addshift":
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.print("Enter year (yyyy): ");
					year=sc.nextLine();
					System.out.print("Enter month (mm): ");
					month=sc.nextLine();
					System.out.print("Enter day (dd): ");
					day=sc.nextLine();
					System.out.println("Enter job: ");
					String job=sc.nextLine();
					System.out.print("Is it a morning shift? (y/n): ");
					cIsMorningShift=sc.nextLine();
					isMorningShift=cIsMorningShift.equals("y");
					Employee.addEmployeeToShift(id, day, month, year, isMorningShift, job);
					break;
				case "showshift":
					System.out.print("Enter year (yyyy): ");
					year=sc.nextLine();
					System.out.print("Enter month (mm): ");
					month=sc.nextLine();
					System.out.print("Enter day (dd): ");
					day=sc.nextLine();
					System.out.print("Is it a morning shift? (y/n): ");
					cIsMorningShift=sc.nextLine();
					isMorningShift=cIsMorningShift.equals("y");
					System.out.println(Employee.showShiftAt(day, month, year, isMorningShift));
					break;
				case "q":
				case "exit":
					return;
				default:
					System.out.println("Illegal command");
			}
		}
	}
}
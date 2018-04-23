package superLi;

import superLi.employees.Employee;
import superLi.employees.Shift;

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
			int id=0, bankNum, bankBranchNum, bankAccountNum;
			double salary;
			String fname, lname, year, month, day, cIsMorningShift;
			boolean isMorningShift=false;
			System.out.println("Enter command: 'add'/'update'/'get'/'addworking'/'addqualification'/'addjob'/'addshift'/'showshift" +
			                   "'/'exit':");
			String comm=sc.nextLine().trim();

			switch (comm)
			{
				case "add":
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.print("First name: ");
					fname=sc.nextLine();
					System.out.print("Last name: ");
					lname=sc.nextLine();
					System.out.print("Salary: ");
					salary=sc.nextDouble();
					System.out.print("Bank number: ");
					bankNum=Integer.parseInt(sc.nextLine());
					System.out.print("Bank brunch number: ");
					bankBranchNum=Integer.parseInt(sc.nextLine());
					System.out.print("Bank account number: ");
					bankAccountNum=Integer.parseInt(sc.nextLine());
					if (Employee.addEmployee(id, fname, lname, salary, bankNum, bankBranchNum, bankAccountNum))
						System.out.println("success");
					break;
				case "update":
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
					salary=sc.nextDouble();
					System.out.print("Bank number: ");
					bankNum=Integer.parseInt(sc.nextLine());
					System.out.print("Bank brunch number: ");
					bankBranchNum=Integer.parseInt(sc.nextLine());
					System.out.print("Bank account number: ");
					bankAccountNum=Integer.parseInt(sc.nextLine());
					emp.updateEmployee(fname, lname, salary, bankNum, bankBranchNum, bankAccountNum);
					break;
				case "get":
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.println(Employee.getEmployee(id));
					break;
				case "addqualification":
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.print("Enter job: ");
					Employee.addQualification(id, sc.nextLine());
					break;
				case "addworking":
					System.out.print("Id: ");
					id=Integer.parseInt(sc.nextLine());
					System.out.print("Enter year (yyyy): ");
					year=sc.nextLine();
					System.out.print("Enter month (mm): ");
					month=sc.nextLine();
					System.out.print("Enter day (dd): ");
					day=sc.nextLine();
					System.out.print("Is it a morning shift? (y/n): ");
					cIsMorningShift=sc.nextLine();
					isMorningShift=cIsMorningShift.equals("y");
					System.out.print("Is it a noon shift? (y/n): ");
					cIsMorningShift=sc.nextLine();
					boolean isNoonShift=cIsMorningShift.equals("y");
					Employee.addAvailability(id, day, month, year, isMorningShift, isNoonShift);
					break;
				case "addjob":
					System.out.print("Enter job: ");
					Employee.addJob(sc.nextLine());
					break;
				case "addshift":
//					System.out.print("Id: ");
//					id=Integer.parseInt(sc.nextLine());
					System.out.print("Enter year (yyyy): ");
					year=sc.nextLine();
					System.out.print("Enter month (mm): ");
					month=sc.nextLine();
					System.out.print("Enter day (dd): ");
					day=sc.nextLine();
					System.out.print("Is it a morning shift? (y/n): ");
					cIsMorningShift=sc.nextLine();
					isMorningShift=cIsMorningShift.equals("y");
					if (Shift.isShiftExists(day, month, year,isMorningShift))
					{
						System.out.println("Available managers for this shift:\n"+
						                   Employee.showAvailableEmployeesToShift(day,
						                                                          month,
						                                                          year,
						                                                          isMorningShift,
						                                                          "Manager"));
						System.out.print("Enter manager ID: ");
						id=Integer.parseInt(sc.nextLine());
						Shift.addEmployeeToShift(id, day, month, year, isMorningShift, "Manager");
					}
					System.out.print("Enter job (q to stop): ");
					String job=sc.nextLine();
					while (!job.equals("q"))
					{
						System.out.println("Available employees for this job:\n"+
						                   Employee.showAvailableEmployeesToShift(day,
						                                                          month,
						                                                          year,
						                                                          isMorningShift,
						                                                          job));
						System.out.print("Enter employee ID: ");
						id=Integer.parseInt(sc.nextLine());
						Shift.addEmployeeToShift(id, day, month, year, isMorningShift, job);
						System.out.print("Enter job (q to stop): ");
						job=sc.nextLine();
					}
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
					System.out.println(Shift.showShiftAt(day, month, year, isMorningShift));
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
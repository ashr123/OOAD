package superLi.transportation;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

	public static void main() {

		DataBaseManager manager = new DataBaseManager();
		int input = 0;
//		manager.createNewDatabase("database.db");
//		manager.createTables();
		Scanner sc = new Scanner(System.in);
		while (input != 12) {
			// Display menu graphics
			System.out.println("=========================================");
			System.out.println("|   	  transportation Moudle	     	|");
			System.out.println("=========================================");
			System.out.println("|Options:				|");
			System.out.println("|        1. Insert transportation	|");
			System.out.println("|        2. Update transportation	|");
			System.out.println("|        3. Delete transportation	|");
			System.out.println("|        4. Insert Truck		|");
			System.out.println("|        5. Delete Truck		|");
			System.out.println("|        6. Get transportation info	|");
			System.out.println("|        7. Get Truck info		|");
			System.out.println("|        8. Get Driver info		|");
			System.out.println("|        9. Get Destination info	|");
			System.out.println("|        10. Get Source info		|");
			System.out.println("|        11. Get Reservation info	|");
			System.out.println("|        12. Go back to Main Menu		|");
			System.out.println("=========================================");
			System.out.println("Select option: ");
			try
			{
				input=sc.nextInt();
			}
			catch (InputMismatchException ignored)
			{
				continue;
			}


		/*
		 * TRANSPORTATION
		 */

		if (input == 1) {

			manager.insertTransport(true , null);

		}
		if (input == 2) {
			manager.updateTransport();

		}
		
		if (input == 3) {
			manager.deleteTransport(true , 0);

		}
		
		

		/*
		 * TRUCK
		 */
		
		if (input == 4) {

			
			manager.insertTruck(true, null);

		}

		if (input == 5) {
			manager.deleteTruck(true , 0);

		}
		
		/*
		 * INFO
		 */
		
		if (input == 6) {
			manager.getInfoTransport();
		}
		
		if (input == 7) {

			manager.getInfoTruck();
		}
		if (input == 8) {

			manager.getInfoDriver();
		}
		if (input == 9) {

			manager.getInfoDestination();
		}
		if (input == 10) {

			manager.getInfoSource();
		}
		if (input == 11) {

			manager.getInfoReservation();
		}
		


	}
	}

}

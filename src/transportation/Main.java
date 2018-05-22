package Transportation;

import java.util.Scanner;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {

		DataBaseManager manager = new DataBaseManager();
		int input = 0;
		manager.createNewDatabase("database.db");
		manager.createTables();
		Scanner sc = new Scanner(System.in);
		while (input != 14) {
			// Display menu graphics
			System.out.println("=========================================");
			System.out.println("|   	  Transportation Moudle	     	|");
			System.out.println("=========================================");
			System.out.println("|Options:				|");
			System.out.println("|        1. Insert Transportation	|");
			System.out.println("|        2. Update Transportation	|");
			System.out.println("|        3. Delete Transportation	|");
			System.out.println("|        4. Insert Truck		|");
			System.out.println("|        5. Delete Truck		|");
			System.out.println("|        6. Insert Driver		|");
			System.out.println("|        7. Delete Driver		|");
			System.out.println("|        8. Get Transportation info	|");
			System.out.println("|        9. Get Truck info		|");
			System.out.println("|        10. Get Driver info		|");
			System.out.println("|        11. Get Destination info	|");
			System.out.println("|        12. Get Source info		|");
			System.out.println("|        13. Get Reservation info	|");
			System.out.println("|        14. Exit			|");
			System.out.println("=========================================");
			System.out.println("Select option: ");
			input = sc.nextInt();
	

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
		 * DRIVER
		 */
		
		if (input == 6) {

			
			manager.insertDriver(true, null);

		}


		if (input == 7) {
			manager.deleteDriver(true , 0);

		}
		
		/*
		 * INFO
		 */
		
		if (input == 8) {
			manager.getInfoTransport();
		}
		
		if (input == 9) {

			manager.getInfoTruck();
		}
		if (input == 10) {

			manager.getInfoDriver();
		}
		if (input == 11) {

			manager.getInfoDestination();
		}
		if (input == 12) {

			manager.getInfoSource();
		}
		if (input == 13) {

			manager.getInfoReservation();
		}
		


	}
	}

}

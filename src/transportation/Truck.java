package Transportation;
import java.util.Scanner;

public class Truck {
	private int ID;
	private String Model;
	private String Color;
	private int Neto_Weight;
	private int Max_Weight;
	


	public Truck(int iD, String model, String color, int neto_Weight, int max_Weight) {
		super();
		ID = iD;
		Model = model;
		Color = color;
		Neto_Weight = neto_Weight;
		Max_Weight = max_Weight;
	}

	public int getID() {
		return ID;
	}

	public void setID(int truck_ID) {
		ID = truck_ID;
	}


	public String getModel() {
		return Model;
	}

	public void setModel(String model) {
		Model = model;
	}

	public String getColor() {
		return Color;
	}

	public void setColor(String color) {
		Color = color;
	}

	public int getNeto_Weight() {
		return Neto_Weight;
	}

	public void setNeto_Weight(int neto_Weight) {
		Neto_Weight = neto_Weight;
	}

	public int getMax_Weight() {
		return Max_Weight;
	}

	public void setMax_Weight(int max_Weight) {
		Max_Weight = max_Weight;
	}
	
}

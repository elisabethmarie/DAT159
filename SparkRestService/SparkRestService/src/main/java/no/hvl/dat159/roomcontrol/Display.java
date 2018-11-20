package no.hvl.dat159.roomcontrol;

import no.hvl.dat159.dweet.DweetClientTemperature;

public class Display {


	public void write(String message) {
		System.out.println("DISPLAY: " + message);
		System.out.println();
	}

	public static void main(String[] args) {
		DweetClientTemperature dct = new DweetClientTemperature();
		Display display = new Display();

		try {
			Thread t1 = new Thread();
			t1.start();
			t1.join();
		} catch (Exception e) {
			System.out.println("Display: " + e.getMessage());
			e.printStackTrace();
		}
	}

}

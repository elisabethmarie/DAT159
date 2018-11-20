package no.hvl.dat159.roomcontrol;

import java.io.IOException;

import no.hvl.dat159.dweet.DweetClientTemperature;

public class TemperatureSensor implements Runnable {

	private Room room;

	public TemperatureSensor(Room room) {

		this.room = room;
	}

	public double read() {

		return room.sense();
	}

	@Override
	public void run() {
		
		DweetClientTemperature dct = new DweetClientTemperature();
		while (true) {
			double temp = read();
			try {
				dct.publish((int) temp);
			} catch (IOException e) {
				
				e.printStackTrace();
			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

}

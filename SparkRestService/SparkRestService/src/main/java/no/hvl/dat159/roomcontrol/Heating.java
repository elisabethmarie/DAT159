package no.hvl.dat159.roomcontrol;

import java.io.IOException;

import no.hvl.dat159.dweet.DweetClientHeating;

public class Heating implements Runnable {

	private Room room;
	private DweetClientHeating dch;

	public Heating(Room room) {
		this.room = room;
		dch = new DweetClientHeating();
	}

	public void write(boolean newvalue) {
		room.actuate(newvalue);
	}

	@Override
	public void run() {
		while (true) {
			try {
				String status = dch.get();
				if (status.equals("1")) {
					write(true);
				} else {
					write(false);
				}

			} catch (

			IOException e) {
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

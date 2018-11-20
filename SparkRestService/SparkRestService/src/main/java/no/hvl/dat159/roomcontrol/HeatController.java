package no.hvl.dat159.roomcontrol;

import java.io.IOException;

import no.hvl.dat159.dweet.DweetClientHeating;
import no.hvl.dat159.dweet.DweetClientTemperature;

public class HeatController implements Runnable {

	public void getAndPublish() throws NumberFormatException, IOException {
		DweetClientHeating pubHeat = new DweetClientHeating();

		DweetClientTemperature subTemp = new DweetClientTemperature();
		double temp = Double.parseDouble(subTemp.get());
		if (temp >= 10) {
			pubHeat.publish(0);
		} else {
			pubHeat.publish(1);
		}

	}

	@Override
	public void run() {
		while (true) {
			try {
				getAndPublish();
			} catch (NumberFormatException e) {

				e.printStackTrace();
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

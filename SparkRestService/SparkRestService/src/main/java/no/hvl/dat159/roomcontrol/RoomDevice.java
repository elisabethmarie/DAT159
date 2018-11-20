package no.hvl.dat159.roomcontrol;

public class RoomDevice {

	public static void main(String[] args) {
		
		Room room = new Room(9);

		try {
			TemperatureSensor sensor = new TemperatureSensor(room);
			HeatController hc = new HeatController();
			Heating heat = new Heating(room);
			
			Thread tempPublisher = new Thread(sensor);
			Thread hcPublisher = new Thread(hc);
			Thread heatThread = new Thread(heat);

			tempPublisher.start();
			hcPublisher.start();
			heatThread.start();

			tempPublisher.join();
			hcPublisher.join();
			heatThread.join();
		} catch (Exception ex) {
			
			System.out.println("RoomDevice: " + ex.getMessage());
			ex.printStackTrace();
		}
		


	}

}

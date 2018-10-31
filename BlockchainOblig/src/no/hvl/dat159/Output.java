package no.hvl.dat159;

public class Output {

	// Simplified compared to Bitcoin - The address should be a script
	private long value;
	private String address;

	public Output(long value, String address) {
		this.value = value;
		this.address = address; // the address of the recipient
	}

	@Override
	public String toString() {
		return "Output{" + "Value=" + getValue() + ", Recipient=" + getAddress() + "}";
	}

	public long getValue() {
		return value;
	}

	public String getAddress() {
		return address;
	}
}

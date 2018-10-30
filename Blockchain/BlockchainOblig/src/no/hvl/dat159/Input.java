package no.hvl.dat159;

public class Input {

	// Simplified compared to Bitcoin
	// The signature is moved to Transaction, see comment there.
	private String prevTxHash;
	private int prevOutputIndex;

	public Input(String prevTxHash, int prevOutputIndex) {
		this.prevTxHash = prevTxHash;
		this.prevOutputIndex = prevOutputIndex;
	}

	@Override
	public String toString() {
		return "Input{PrevTxHash=" + prevTxHash + ", prevOutputIndex=" + prevOutputIndex + "}";
	}

	public String getPrevTxHash() {
		return prevTxHash;
	}

	public int getPrevOutputIndex() {
		return prevOutputIndex;
	}
}

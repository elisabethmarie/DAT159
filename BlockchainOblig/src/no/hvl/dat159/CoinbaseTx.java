package no.hvl.dat159;

public class CoinbaseTx {

	// Simplified compared to Bitcoin (nothing significant missing)

	private String coinbase; // message
	private Output output;
	private String txHash;

	// The first transaction (and only the first) is a coin base transaction

	public CoinbaseTx(String coinbase, int value, String address) {
		this.coinbase = coinbase;
		output = new Output(value, address);
		txHash = HashUtil.base64Encode(HashUtil.sha256Hash(this.toString()));

	}

	@Override
	public String toString() {
		return "CoinbaseTx{coinbase=" + coinbase + ", Output=" + output.toString() + ", txHash=" + txHash + "}";
	}

	public String getCoinbase() {
		return coinbase;
	}

	public Output getOutput() {
		return output;
	}

	public String getTxHash() {
		return txHash;
	}

}

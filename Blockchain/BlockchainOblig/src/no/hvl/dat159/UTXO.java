package no.hvl.dat159;

import java.util.HashMap;
import java.util.Map;

public class UTXO {

	// Why is this a Map and not a Set?
	// The values in this map are the UTXOs (unspent Outputs)
	// When removing UTXOs, we need to identify which to remove.
	// Since the Inputs are references to UTXOs, we can use those
	// as keys.
	private Map<Input, Output> map = new HashMap<>();

	public void printUTXO() {
		map.forEach((key, value) -> System.out.println(key + "\n" + value + "\n"));
	}

	public void addOutputFrom(CoinbaseTx ctx) {
		Input inputValue = new Input(ctx.getTxHash(), 0);
		Output outputValue = ctx.getOutput();
		map.put(inputValue, outputValue);
	}

	public void addAndRemoveOutputsFrom(Transaction tx) {
		tx.getOutputs().forEach(output -> map.put(new Input(tx.getTxHash(), tx.getOutputs().indexOf(output)), output));
		tx.getInputs().forEach(input -> map.remove(input));
	}

	public Map<Input, Output> getMap() {
		return map;
	}

}

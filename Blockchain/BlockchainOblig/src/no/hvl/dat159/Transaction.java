package no.hvl.dat159;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Transaction {

	// Simplified compared to Bitcoin
	private List<Input> inputs = new ArrayList<>();
	private List<Output> outputs = new ArrayList<>();

	// If we make the assumption that all the inputs belong to the
	// same key, we can have one signature for the entire transaction,
	// and not one for each input. This simplifies things a lot
	// (more than you think)!

	private PublicKey senderPublicKey;
	private byte[] signature;

	private String txHash;

	public Transaction(PublicKey senderPublicKey) {
		this.senderPublicKey = senderPublicKey;
	}

	public void addInput(Input input) {
		if (inputs.contains(input))
			try {
				throw new Exception("Inputs already exists");
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			inputs.add(input);
	}

	public void addOutput(Output output) {
		if (outputs.contains(output))
			try {
				throw new Exception("Outputs already exists");
			} catch (Exception e) {
				e.printStackTrace();
			}
		else
			outputs.add(output);
	}

	@Override
	public String toString() {
		return "-Transaction-" + "\nInputs: " + inputs() + "\nOutputs: " + outputs() + "\nSender public key: "
				+ senderPublicKey + "\nSignature: " + Arrays.toString(signature);
	}

	public void signTxUsing(PrivateKey privateKey) {
		signature = DSAUtil.signWithDSA(privateKey, inputs() + outputs());
	}

	public void calculateTxHash() { // In Bitcoin, a private key is a 256-bit number.
		txHash = HashUtil.base64Encode(HashUtil.sha256Hash(this.txHash));
	}

	public boolean isValid(Map<Input, Output> map) {
		boolean isValid = true;

		// Check that no variables are empty
		if (!isNotEmpty() || senderPublicKey == null || signature == null || signature.length == 0 || txHash == null) {
			isValid = false;

			// Check for repeating inputs
		} else if (inputs.stream().noneMatch(input -> inputs.indexOf(input) == 1)) {
			isValid = false;

			// Check that output has valid value
		} else if (outputs.stream().allMatch(output -> output.getValue() < 0 || output.getValue() > 21000000)) {
			isValid = false;

			// Check if transaction is signed by sender
		} else if (!DSAUtil.verifyWithDSA(senderPublicKey, inputs() + outputs(), signature)) {
			isValid = false;

			// Verify that Output is unspent
		} else if (!isUnspentOutput(map)) {
			isValid = false;

			// Check if sum of inputs and outputs are equal
		} else if (!inputsAndOutputsSumIsEqual(map)) {
			isValid = false;

			// Check if inputs belong to sender
		} else if (inputs.stream()
				.anyMatch(input -> input.getPrevTxHash().equals(HashUtil.addressFromPublicKey(senderPublicKey)))) {
			isValid = false;

			// Check if transaction hash is correct
		} else if (txHash.equals(HashUtil.base64Encode(HashUtil.sha256Hash(this.toString())))) {
			isValid = false;

		}
		return isValid;
	}

	public boolean isUnspentOutput(Map<Input, Output> map) {
		for (Output o : outputs) {
			for (Map.Entry<Input, Output> i : map.entrySet()) {
				if (i.getKey().getPrevTxHash().equals(o.getAddress())) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean isNotEmpty() {
		return (!(outputs.isEmpty() || inputs.isEmpty()));
	}

	public boolean inputsAndOutputsSumIsEqual(Map<Input, Output> map) {
		String hash = inputs.get(0).getPrevTxHash();
		long sumInputs = 0;
		for (Map.Entry<Input, Output> k : map.entrySet()) {
			if (k.getValue().getAddress().equals(hash)) {
				sumInputs += k.getValue().getValue();
			}
		}
		long sumValues = outputs.stream().mapToLong(Output::getValue).sum();
		return sumInputs == sumValues;
	}

	public List<Input> getInputs() {
		return inputs;
	}

	public List<Output> getOutputs() {
		return outputs;
	}

	public PublicKey getSenderPublicKey() {
		return senderPublicKey;
	}

	public byte[] getSignature() {
		return signature;
	}

	public String getTxHash() {
		return txHash;
	}

	private String inputs() {
		return inputs.stream().map(Input::toString).collect(Collectors.joining("\n\t\t"));
	}

	private String outputs() {
		return outputs.stream().map(Output::toString).collect(Collectors.joining("\n\t\t"));
	}

}

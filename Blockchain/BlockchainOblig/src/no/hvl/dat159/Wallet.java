package no.hvl.dat159;

import java.security.KeyPair;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class Wallet {

	private String id;
	private KeyPair keyPair;

	// A refererence to the "global" complete utxo-set
	private Map<Input, Output> utxoMap;

	public Wallet(String id, UTXO utxo) {
		this.id = id;
		this.utxoMap = utxo.getMap();
		this.keyPair = DSAUtil.generateRandomDSAKeyPair();
	}

	public String getAddress() {
		return HashUtil.addressFromPublicKey(getPublicKey());
	}

	public PublicKey getPublicKey() {
		return keyPair.getPublic();
	}

	public Transaction createTransaction(long value, String address) throws Exception {
		// TODO - This is a big one

		// 1. Collect all UTXO for this wallet and calculate balance
		Map<Input, Output> myUtxo = collectMyUtxo();
		
		long balance = calculateBalance(myUtxo.values());

		// 2. Check if there are sufficient funds --- Exception?
		if (balance < value)
			throw new Exception("Insufficient funds.");

		// 3. Choose a number of UTXO to be spent --- Strategy?
		List<Input> UtxoToSend = new ArrayList<Input>();
		Iterator<Entry<Input, Output>> iterator = myUtxo.entrySet().iterator();
		long collected = 0;
		while (collected < value && iterator.hasNext()) {
			Map.Entry<Input, Output> pair = iterator.next();
			collected += pair.getValue().getValue();
			UtxoToSend.add(pair.getKey());
		}

		// 4. Calculate change
		long change = balance - value;

		// 5. Create an "empty" transaction
		Transaction tx = new Transaction(getPublicKey());

		// 6. Add chosen inputs
		UtxoToSend.forEach(input -> {
			try {
				tx.addInput(input);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});

		// 3. Choose a number of UTXO to be spent --- Strategy?
		Output valueSpent = new Output(value, address);
		tx.addOutput(valueSpent);

		// 7. Add 1 or 2 outputs, depending on change
		if (change > 0) {
			tx.addOutput(new Output(change, getAddress()));
		}

		// 8. Sign the transaction
		tx.signTxUsing(keyPair.getPrivate());

		// 9. Calculate the hash for the transaction
		tx.calculateTxHash();

		// 10. return
		return tx;

		// PS! We have not updated the UTXO yet. That is normally done
		// when appending the block to the blockchain, and not here!
		// Do that manually from the Application-main.
	}

	@Override
	public String toString() {
		return "ID: " + id + "\nAddress: " + getAddress() + "\nBalance: " + getBalance();
	}

	public long getBalance() {
		return calculateBalance(collectMyUtxo().values());
	}

	private long calculateBalance(Collection<Output> outputs) {
		return outputs.stream().mapToLong(Output::getValue).sum();
	}

	private Map<Input, Output> collectMyUtxo() {
		return utxoMap.entrySet().stream()
				.filter(map -> map.getValue().getAddress().equals(this.getAddress()))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

}
package no.hvl.dat159;

public class Application {

	private static UTXO utxo = new UTXO();

	public static void main(String[] args) throws Exception {

		/*
		 * In this assignment, we are going to look at how to represent and record
		 * monetary transactions. We will use Bitcoin as the basis for the assignment,
		 * but there will be some simplifications.
		 */

		Wallet miner = new Wallet(HashUtil.base64Encode(HashUtil.sha256Hash("minerWallet")), utxo);
		Wallet wallet = new Wallet(HashUtil.base64Encode(HashUtil.sha256Hash("myWallet")), utxo);

		CoinbaseTx genesisBlock = new CoinbaseTx("Genesis", 200, miner.getAddress());
		utxo.addOutputFrom(genesisBlock);

		CoinbaseTx coinbaseTx = new CoinbaseTx("Regular transaction", 200, miner.getAddress());

		System.out.println("Block 1 = " + genesisBlock.toString() + "\n");

		utxo.printUTXO();

		try {
			Transaction regTx = miner.createTransaction(40, wallet.getAddress());
			if (!regTx.isValid(utxo.getMap()))
				throw new Exception("Not a valid transaction");

			utxo.addAndRemoveOutputsFrom(regTx);
			System.out.println("\nBlock 2 = " + coinbaseTx.toString() + "\n" + regTx.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}

		utxo.addOutputFrom(coinbaseTx);

		coinbaseTx = new CoinbaseTx("new transaction", 200, miner.getAddress());

		try {
			Transaction regTx = miner.createTransaction(40, wallet.getAddress());
			if (!regTx.isValid(utxo.getMap()))
				throw new Exception("Not a valid transaction");

			utxo.addAndRemoveOutputsFrom(regTx);
			System.out.println("\nBlock3= " + coinbaseTx.toString() + "\n" + regTx.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

		utxo.addOutputFrom(coinbaseTx);

		System.out.println("UTXO:");
		utxo.printUTXO();
		System.out.println("\nMiners Wallet: \n" + miner.toString());
		System.out.println("\nMy Wallet: " + "\n" + wallet.toString());

		// Update the UTXO-set ...

		// 4. Make a nice print-out of all that has happened, as well as the end status.
		//
		// for each of the "block"s (rounds), print
		// "block" number
		// the coinbase transaction
		// hash, message
		// output
		// the regular transaction(s), if any
		// hash
		// inputs
		// outputs
		// End status: the set of unspent outputs
		// End status: for each of the wallets, print
		// wallet id, address, balance
	}
}

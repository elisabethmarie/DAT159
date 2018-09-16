package hvl;

import java.util.Enumeration;
import java.util.Vector;

public class Customer {
	private String _name;
	private Vector _rentals = new Vector();

	public Customer(String name) {
		_name = name;
	}

	public String statement() {
		double totalAmount = 0;
		int frequentRenterPoints = 0;
		Enumeration rentals = _rentals.elements();
		String result = "Rental Record for " + getName() + "\n";
		while (rentals.hasMoreElements()) {
			Rental each = (Rental) rentals.nextElement();

			Movie movie = each.getMovie();
			int priceCode = movie.getPriceCode();
			int daysRented = each.getDaysRented();
			double thisAmount = movie.determineAmount(daysRented);

			frequentRenterPoints += movie.getFrequentRenterPoints(frequentRenterPoints, daysRented);

			String title = movie.getTitle();
			result += "\t" + title + "\t" + String.valueOf(thisAmount) + "\n";
			totalAmount += thisAmount;
		}
		
		result = addFooterLines(totalAmount, frequentRenterPoints, result);

		return result;
	}

	private String addFooterLines(double totalAmount, int frequentRenterPoints, String result) {
		result += "Amount owed is " + String.valueOf(totalAmount) + "\n";
		result += "You earned " + String.valueOf(frequentRenterPoints) + " frequent renter points";
		return result;
	}

	public void addRental(Rental arg) {
		_rentals.addElement(arg);
	}

	public String getName() {
		return _name;
	}
}

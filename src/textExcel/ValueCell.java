package textExcel;

public class ValueCell extends RealCell {
	String initial;
	String print;
	double value;

	public ValueCell(String in) {
		super(in);
		initial = in;
		try {
			value = Double.parseDouble(in);
			print = Double.toString(value);
		} catch (NumberFormatException e) {
			print = in;
			value = Double.POSITIVE_INFINITY;
		}
	}

	@Override
	public String abbreviatedCellText() {
		// TODO Auto-generated method stub
		if (print.length() >= 10) {
			return print.substring(0, 10);
		}
		return String.format("%-10s", print);
	}

	@Override
	public String fullCellText() {
		// TODO Auto-generated method stub
		return initial;
	}

	@Override
	public double getDoubleValue() {
		// TODO Auto-generated method stub
		return value;
	}

}

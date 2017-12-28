package textExcel;

public class PercentCell extends RealCell implements Cell {
	String print;
	String content;

	public PercentCell(String in) {
		super(in);
		String format = in.substring(0, in.length() - 1);
		if (format.contains(".")) {
			print = format.substring(0, format.indexOf('.')) + "%";
		} else {
			print = in;
		}
		content = "";
		content += (Double.parseDouble(format) * 0.01);
	}

	@Override
	public String abbreviatedCellText() {
		// TODO Auto-generated method stub
		return String.format("%-10s", print);
	}

	@Override
	public String fullCellText() {
		// TODO Auto-generated method stub
		return content;
	}

	public double getDoubleValue() {
		return Double.parseDouble(content);
	}
}

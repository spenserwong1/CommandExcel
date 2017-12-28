package textExcel;

import java.util.ArrayList;
import java.util.Arrays;

public class FormulaCell extends RealCell {
	String print;
	Spreadsheet sheet;

	public FormulaCell(String in, Spreadsheet sheet) {
		super(in);
		print = in;
		this.sheet = sheet;

	}

	@Override
	public String abbreviatedCellText() {
		// TODO Auto-generated method stub
		if (Double.isInfinite(getDoubleValue())) {
			return String.format("%-10.10s", "#ERROR");
		}
		return String.format("%-10.10s", Double.toString(getDoubleValue()));
	}

	@Override
	public String fullCellText() {
		// TODO Auto-generated method stub
		return print;
	}

	@Override
	public double getDoubleValue() {
		// TODO Auto-generated method stub
		try {
			String[] format = print.split(" ");
			if (format[1].toLowerCase().equals("sum")) {
				String[] range = format[2].split("-");
				
				SpreadsheetLocation start = new SpreadsheetLocation(range[0].toUpperCase());
				SpreadsheetLocation end = new SpreadsheetLocation(range[1].toUpperCase());
				double sum = 0;
				for (int i = start.getRow(); i <= end.getRow(); i++) {
					for (int j = start.getCol(); j <= end.getCol(); j++) {
						sum += ((RealCell) sheet.sheet[i][j]).getDoubleValue();
					}
				}
				return sum;
			} else if (format[1].toLowerCase().equals("avg")) {
				String[] range = format[2].split("-");
				SpreadsheetLocation start = new SpreadsheetLocation(range[0].toUpperCase());
				SpreadsheetLocation end = new SpreadsheetLocation(range[1].toUpperCase());
				double sum = 0;
				double count = 0;
				for (int i = start.getRow(); i <= end.getRow(); i++) {
					for (int j = start.getCol(); j <= end.getCol(); j++) {
						sum += ((RealCell) sheet.sheet[i][j]).getDoubleValue();
						count++;
					}
				}
				return sum / count;
			} else {
				ArrayList<String> formatlist = new ArrayList<String>(Arrays.asList(format));
				formatlist.remove(0);
				formatlist.remove(formatlist.size() - 1);
				for (int i = 0; i < formatlist.size(); i++) {
					if (formatlist.get(i).toLowerCase().matches("[a-z]\\d+")) {
						SpreadsheetLocation s = new SpreadsheetLocation(formatlist.get(i).toUpperCase());
						if(sheet.getCell(s).abbreviatedCellText().equals("#ERROR")){
							return Double.POSITIVE_INFINITY;
						}
						formatlist.set(i, Double.toString(((RealCell) sheet.getCell(s)).getDoubleValue()));

					}
				}
				multdiv(formatlist);
				addsub(formatlist);
				return Double.parseDouble(formatlist.get(0));
			}
		} catch (ClassCastException e) {
			return Double.POSITIVE_INFINITY;
		}

	}

	public void multdiv(ArrayList<String> in) {
		for (int i = 0; i < in.size(); i++) {
			if (in.get(i).equals("*")) {
				Double mult = Double.parseDouble(in.get(i - 1)) * Double.parseDouble(in.get(i + 1));
				in.set(i - 1, String.valueOf(mult));
				in.remove(i);
				in.remove(i);
				i--;
			}
			if (in.get(i).equals("/")) {
				Double div = Double.parseDouble(in.get(i - 1)) / Double.parseDouble(in.get(i + 1));
				in.set(i - 1, String.valueOf(div));
				in.remove(i);
				in.remove(i);
				i--;
			}
		}
	}

	public void addsub(ArrayList<String> in) {
		for (int i = 0; i < in.size(); i++) {
			if (in.get(i).equals("+")) {
				Double sum = Double.parseDouble(in.get(i - 1)) + Double.parseDouble(in.get(i + 1));
				in.set(i - 1, String.valueOf(sum));
				in.remove(i);
				in.remove(i);
				i--;
			}
			if (in.get(i).equals("-")) {
				Double minus = Double.parseDouble(in.get(i - 1)) - Double.parseDouble(in.get(i + 1));
				in.set(i - 1, String.valueOf(minus));
				in.remove(i);
				in.remove(i);
				i--;
			}
		}
	}

}

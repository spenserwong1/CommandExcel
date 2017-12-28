package textExcel;

public class TextCell implements Cell {
	String content;
	String print;

	public TextCell(String in) {
		content = in;
		print = content.substring(1, content.length() - 1);
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
		return content;
	}

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof Cell)) {
			throw new IllegalArgumentException();
		}
		if (o instanceof EmptyCell) {
			return 1;
		}

		if (o instanceof RealCell) {
			return -1;
		}
		return content.compareTo(((TextCell) o).content);

	}

}

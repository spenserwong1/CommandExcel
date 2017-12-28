package textExcel;

public class EmptyCell implements Cell {

	@Override
	public String abbreviatedCellText() {
		// TODO Auto-generated method stub
		return "          ";
	}

	@Override
	public String fullCellText() {
		// TODO Auto-generated method stub
		return "";
	}

	@Override
	public int compareTo(Object o) {
		if (!(o instanceof Cell)) {
			throw new IllegalArgumentException();
		}
		// TODO Auto-generated method stub
		return -1;
	}

}

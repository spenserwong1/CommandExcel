package textExcel;

public abstract class RealCell implements Cell {

	public RealCell(String in) {
		// This constructor only exists to satisfy constructor requirements
		// for the subclasses.
	}

	public abstract double getDoubleValue();

	@Override
	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		if (!(o instanceof Cell)) {
			throw new IllegalArgumentException();
		}
		if (o instanceof TextCell || o instanceof EmptyCell) {
			return 1;
		}
		if (getDoubleValue() > ((RealCell) o).getDoubleValue()) {
			return 1;
		} else if (getDoubleValue() == ((RealCell) o).getDoubleValue()) {
			return 0;
		} else {
			return -1;
		}

	}
}

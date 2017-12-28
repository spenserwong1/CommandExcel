package textExcel;

//Update this file with your own code.

public class SpreadsheetLocation implements Location {
	int row;
	int col;

	@Override
	public int getRow() {
		// TODO Auto-generated method stub
		return row;
	}

	@Override
	public int getCol() {
		// TODO Auto-generated method stub
		return col;
	}

	public SpreadsheetLocation(String cellName) {
		char colletter = cellName.charAt(0);
		col = ((int) colletter) - 'A';
		row = Integer.parseInt(cellName.substring(1))-1;
	}

}

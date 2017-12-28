package textExcel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;

// Update this file with your own code.

public class Spreadsheet implements Grid {
	Cell[][] sheet;
	ArrayList<String> history;
	private int lim;

	public Spreadsheet() {
		sheet = new Cell[20][12];
		for (int i = 0; i < 20; i++) {
			for (int j = 0; j < 12; j++) {
				sheet[i][j] = new EmptyCell();
			}
		}
	}

	public Spreadsheet(Cell[][] sheet) {
		this.sheet = sheet;
	}

	@Override
	public String processCommand(String command) {
		// TODO Auto-generated method stub
		try {
			if (history != null) {
				if (history.size() == lim && command.indexOf("history") == -1) {
					history.remove(0);
					history.add(command);
				} else if (command.indexOf("history") == -1) {
					history.add(command);
				}

			}
			if (command.toLowerCase().indexOf("history") != -1) {
				if (command.toLowerCase().indexOf("start") != -1) {
					String[] commandarr = command.split(" ");
					history = new ArrayList<String>(Integer.parseInt(commandarr[2]));
					lim = Integer.parseInt(commandarr[2]);
				}
				if (command.toLowerCase().indexOf("display") != -1) {
					if (history.size() == 0) {
						return "";
					}
					String ret = history.get(0);
					for (int i = 1; i < history.size(); i++) {
						ret = history.get(i) + "\n" + ret;
					}
					return ret;
				}
				if (command.toLowerCase().indexOf("clear") != -1) {
					String[] commandarr = command.split(" ");
					if (Integer.parseInt(commandarr[2]) > history.size() - 1) {
						for (int i = 0; i < history.size(); i++) {
							history.set(i, "");
						}
					} else {
						for (int i = 0; i < Integer.parseInt(commandarr[2]); i++) {
							history.remove(0);
						}
					}
				}
				if (command.toLowerCase().indexOf("stop") != -1) {
					history = null;
				}
			} else if (command.toLowerCase().matches("[a-z]\\d+")) {
				SpreadsheetLocation s = new SpreadsheetLocation(command.toUpperCase());
				Cell store = getCell(s);
				return store.fullCellText();
			} else if (command.indexOf("=") != -1) {
				String[] commandarr = command.split(" ", 3);
				SpreadsheetLocation s = new SpreadsheetLocation(commandarr[0].toUpperCase());
				if (command.contains("\"")) {
					sheet[s.getRow()][s.getCol()] = new TextCell(commandarr[2]);
				} else if (commandarr[2].charAt(commandarr[2].length() - 1) == '%') {
					sheet[s.getRow()][s.getCol()] = new PercentCell(commandarr[2]);
				} else if (command.contains("(")) {
					Spreadsheet copy = new Spreadsheet(sheet);
					sheet[s.getRow()][s.getCol()] = new FormulaCell(commandarr[2], copy);
				} else {
					sheet[s.getRow()][s.getCol()] = new ValueCell(commandarr[2]);
				}
				return getGridText();
			} else if (command.toLowerCase().matches("clear")) {
				for (int i = 0; i < 20; i++) {
					for (int j = 0; j < 12; j++) {
						sheet[i][j] = new EmptyCell();
					}
				}
				return getGridText();
			} else if (command.toLowerCase().matches("clear [a-z]\\d+")) {
				String[] commandarr = command.split(" ");
				SpreadsheetLocation s = new SpreadsheetLocation(commandarr[1].toUpperCase());
				sheet[s.getRow()][s.getCol()] = new EmptyCell();
				return getGridText();
			} else if (command.toLowerCase().matches("save \\S+")) {
				String[] commandarr = command.split(" ");
				File f = new File(commandarr[1]);
				PrintStream outputFile;
				try {
					outputFile = new PrintStream(f);
					for (int i = 0; i < 20; i++) {
						for (int j = 0; j < 12; j++) {
							if (!(sheet[i][j] instanceof EmptyCell)) {
								char c = (char) (65 + j);
								String s = Character.toString(c);
								outputFile.println(s + (i + 1) + ","
										+ sheet[i][j].getClass().getName()
												.substring(sheet[i][j].getClass().getName().indexOf(".") + 1)
										+ "," + sheet[i][j].fullCellText());
							}
						}
					}
					outputFile.close();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (command.toLowerCase().matches("open \\S+")) {
				String[] commandarr = command.split(" ");
				Scanner s;
				try {
					s = new Scanner(new File(commandarr[1]));
					while (s.hasNextLine()) {
						String[] temp = (s.nextLine()).split(",");
						char colletter = temp[0].charAt(0);
						int col = ((int) colletter) - 'A';
						int row = Integer.parseInt(temp[0].substring(1)) - 1;
						if (temp[1].toLowerCase().equals("textcell")) {
							sheet[row][col] = new TextCell(temp[2]);
						}
						if (temp[1].toLowerCase().equals("valuecell")) {
							sheet[row][col] = new ValueCell(temp[2]);
						}
						if (temp[1].toLowerCase().equals("formulacell")) {
							Spreadsheet copy = new Spreadsheet(sheet);
							sheet[row][col] = new FormulaCell(temp[2], copy);
						}
						if (temp[1].toLowerCase().equals("percentcell")) {
							sheet[row][col] = new PercentCell(Double.parseDouble(temp[2]) * 100 + "%");
						}
					}
					return getGridText();
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			else if ((command.toLowerCase()).matches("sorta .+") || (command.toLowerCase()).matches("sortd .+")) {
				String[] commandarr = command.split(" ");
				String[] range = commandarr[1].split("-");
				SpreadsheetLocation start = new SpreadsheetLocation(range[0].toUpperCase());
				SpreadsheetLocation end = new SpreadsheetLocation(range[1].toUpperCase());
				ArrayList<Cell> store = new ArrayList<Cell>();
				for (int i = start.getRow(); i <= end.getRow(); i++) {
					for (int j = start.getCol(); j <= end.getCol(); j++) {
						store.add(sheet[i][j]);
					}
				}
				for (int i = 0; i < store.size(); i++) {
					for (int j = i; j < store.size(); j++) {
						Cell big = store.get(i);
						Cell small = store.get(i);
						int idx = i;
						if ((command.toLowerCase()).matches("sorta .+")) {
							if (store.get(j).compareTo(small) < 0) {
								small = store.get(j);
								idx = j;
							}
						}
						if ((command.toLowerCase()).matches("sortd .+")) {
							if (store.get(j).compareTo(small) > 0) {
								small = store.get(j);
								idx = j;
							}
						}
						store.set(i, small);
						store.set(idx, big);
					}
				}
				for (int i = start.getRow(); i <= end.getRow(); i++) {
					for (int j = start.getCol(); j <= end.getCol(); j++) {
						sheet[i][j] = store.get(0);
						store.remove(0);
					}
				}
				return getGridText();
			}
			if (command.matches("\\S+")) {
				return "ERROR: Invalid command.";

			}
		} catch (NumberFormatException e) {
			return "ERROR: Invalid command.";
		} catch (StringIndexOutOfBoundsException e) {
			return "ERROR: Invalid command.";
		} catch (ArrayIndexOutOfBoundsException e) {
			return "ERROR: Invalid command.";
		}
		return "";
	}

	@Override
	public int getRows() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public int getCols() {
		// TODO Auto-generated method stub
		return 12;
	}

	@Override
	public Cell getCell(Location loc) {
		// TODO Auto-generated method stub
		return sheet[loc.getRow()][loc.getCol()];
	}

	@Override
	public String getGridText() {
		// TODO Auto-generated method stub
		String text = "";
		text += String.format("%3s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|%-10s|\n", "", "A",
				"B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L");
		for (int i = 0; i < 20; i++) {
			text += String.format("%-3d", i + 1);
			for (int j = 0; j < 12; j++) {
				int charconv = j + 65;
				char store = (char) charconv;
				String location = store + String.valueOf(i + 1);
				SpreadsheetLocation s = new SpreadsheetLocation(location);
				text += String.format("|%-10s", getCell(s).abbreviatedCellText());

			}
			text += "|\n";
		}
		return text;
	}

}

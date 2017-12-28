package textExcel;

import java.io.FileNotFoundException;
import java.util.Scanner;

// Update this file with your own code.

public class TextExcel {

	public static void main(String[] args) {
		Spreadsheet sheet = new Spreadsheet();
		System.out.println(sheet.getGridText());
		boolean run = true;
		Scanner s = new Scanner(System.in);
		while (run) {
			String command = s.nextLine();
			if (command.equals("quit")) {
				run = false;
			}
			System.out.println(sheet.processCommand(command));

		}
	}

}

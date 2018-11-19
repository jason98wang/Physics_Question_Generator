import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Home {

	// init vars
	public static void main(String[] args) {
		fileIO();
	}

	public static void fileIO() {
		try {
			File f = new File("record.txt");

			// input
			Scanner sc = new Scanner(f);

			String s = "";
			while (sc.hasNext()) {
				s += sc.next();
			}
			sc.close();
			System.out.println(s);

			// output
			PrintWriter pw = new PrintWriter(new FileWriter(f, true));
			pw.println("asdfaaaa");

			pw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

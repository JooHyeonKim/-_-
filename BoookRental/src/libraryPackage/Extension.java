package libraryPackage;

import java.util.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Extension.class
 * 
 * 대여된 책을 연장 -> rent.txt 수정하기(반납일자, 연장유무)
 * 
 * @author JHL
 *
 */

public class Extension {
	static String bookname;
	static String book_num = "";
	static String userId;
	static String rentFile = "rent.txt";
	static String bookFile;
	static String userFile;
	static int line_count;

	static String due_date;


	static boolean available() throws IOException {

		/* file open to compare a rent list */
		BufferedReader inputrent = null;
		try {
			inputrent = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String line = ""; // read lines
		String[] rent_splited = null;
		int found = 0; 
		line_count = 0; 
		while (line != null) {
			line = inputrent.readLine();
			rent_splited = line.split("\t");
			
			if (rent_splited[1].equals(userId) && rent_splited[2].equals(book_num)) {
				
				if (rent_splited[5].equals("0")) {
					
					System.out.println("가능.");
					due_date = rent_splited[4];
					return true;
				} else {
					
					System.out.println("불가능.");
					return false;
				}
			}
			line_count++;
		}
		System.out.println("불가능.");
		return false;
	}

}

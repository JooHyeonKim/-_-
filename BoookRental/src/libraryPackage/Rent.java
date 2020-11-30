package libraryPackage;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;

/**
 * 책 대여 탭
 *  * 
 * @author JHL
 *
 */
public class Rent {
	static String bookname;
	static String book_num;
	static String userId; // 변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile; //
	static String userFile = "user.txt";
	static int line_count;
	
	static String lib_name;
	
	static String due_date;
	
	private static void library() {
		/*
		 * lib_name 에 따라 bookFile의 이름을 결정합니다. (1.txt, 2.txt, 3.txt, 4.txt, 5.txt - 임시로
		 * 각 class 시작 전 함수 실행하여 도서관 정하는 과정
		 */
		bookFile = lib_name + "books.txt";

	}
	static boolean available() throws IOException {
		/**
		 * available() :user file에서 사용자의 대여 권수 확인, rent file에서 이미 대여된 책인지
		 */
		String[] userSplit;
		String temp = "";

		/* file open to check user list */
		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

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
		line_count = 0; // rent file에 데이터 수를 count - 새로 데이터를 쓸 때 순서 배정하기
		int found = 0; // 찾았다면 1, 없으면 0 - 없을 경우 바로 대여 가능하도록 이동
		do {
			line = inputrent.readLine(); // throws IOException
			if (line == null)
				break; // 문서 마지막까지 읽었을 경우 break
			rent_splited = line.split("\t");
			
			if (rent_splited[2].equals(book_num) && rent_splited[6].equals(lib_name)) {
				// find the book
				// then compare
				found = 1;
			}
			
			line_count++;
		} while (line != null);

		inputrent.close();

		// System.out.println("found : " + found);
		if (found == 0) {
			// 대여하기
			System.out.println("현재 대여 가능한 책입니다.");
			do {
				temp = inputStream.readLine(); // userfile throws IOException
				if (temp == null)
					break; // 문서 마지막까지 읽었을 경우 break

				userSplit = temp.split("/");
				if (userSplit[0].equals(userId)) { // 회원을 찾음
					int bookCount;
					bookCount = Integer.valueOf(userSplit[7]);

					/* bookCount대여권수가 3권 이상일 경우 예외처리 */
					if (bookCount < 3)
						return true;
					else {
						System.out.println("대여 가능한 책 수를 초과했습니다. 관리자에게 문의하세요.");
						return false;
					}

				}
				
			} while (temp != null);
			inputStream.close();
			System.out.println("회원 정보에 등록되어 있지 않습니다. 관리자에게 문의하세요.");
			return false;

		} else {
			// ######################
			System.out.println("이미 대여중인 책입니다."); // 가장 빠르게 대여할 수 있는 날짜? -
			return false;

		}
	}

}

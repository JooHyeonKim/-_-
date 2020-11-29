import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
/**
 * 책 반납하는 클래스
 * 
 * user file에서 book_count--, rent file에서 책 목록 없애는 기능 필요
 * 
 * rent file에 없다면? - 해당 도서관 책이 아니다? //오류니까 나중에 생각하기
 * @author JHL
 *
 */

public class Return {
	static String bookname;
	static String book_num;
	static String userId; // 변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile = "books.txt"; //library에 따라 다르게 설정해야 함
	static String userFile = "user.txt";
	static int line_count;
	
	static String lib_name = "삼성도서관";
	
	static String due_date;
	
	private static void library() {
		/*
		 * lib_name 에 따라 bookFile의 이름을 결정합니다. (1.txt, 2.txt, 3.txt, 4.txt, 5.txt - 임시로
		 * 각 class 시작 전 함수 실행하여 도서관 정하는 과정
		 */
		bookFile = lib_name + "books.txt";

	}
	//available 함수가 필요한가?
	private static void deleteLoan(int position) throws IOException, FileNotFoundException {
		File file = new File(rentFile);
		BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // throws
		
		String tempLine;
		String dummy1 = "", dummy2 = "";
		
		// 삭제하고자 하는 position 전까지 이동하며 dummy에 저장
		for (int i = 0; i < position; i++) {
			tempLine = input.readLine(); // 읽으면서 이동
			dummy1 += (tempLine + "\n");
		}
		// 삭제하고자 하는 데이터 건너뛰기
		input.readLine(); //position
		// 삭제하고자 하는 position 이후부터 dummy에 저장하기
		
		String[] splited;
		String newLine = "";
		int num;
		while ((tempLine = input.readLine()) != null) {
			//tempLine의 숫자 하나씩 줄여야함
			splited = tempLine.split("\t");
			num = Integer.valueOf(splited[0]);
			num--;
			splited[0] = Integer.toString(num);
			newLine = "";
			/*끊어진 행 다시 잇기 */
			for (int i = 0; i < 6; i++) {
				newLine += (splited[i] + "\t");
			}
			newLine += (splited[6] + "\n");
			dummy2 += newLine;
		}
		input.close();
		
		PrintWriter output = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));// throws
		/* 수정할 위치 전 */
		output.print(dummy1);

		/* 수정한 위치 후 */
		output.print(dummy2);

		output.close();

		
	}

	
	public static void main(String[] args) throws IOException, FileNotFoundException {
		/*for test*/
		library(); //initialize
		//bookname = "90년생이 온다";
		book_num = "1";
		userId = "2";
		//변수 gui에서 받아오면 될 것 같습니당
		
		/*open rent file to find the book*/
		BufferedReader inputRent = null;
		try {
			inputRent = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String line = ""; // read lines
		String[] rent_splited = null;
		line_count = 0; // Rent file에 데이터 수를 count - 데이터를 수정할 때 순서 배정하기
		
		int found = 0; // 찾은 줄의 숫자, 없으면 0 - 없을 경우 반납할 책이 아님
		do {
			line = inputRent.readLine(); // throws IOException
			if (line == null)
				break; // 문서 마지막까지 읽었을 경우 break
			rent_splited = line.split("\t");
			if (rent_splited[2].equals(book_num) && rent_splited[1].equals(userId)&&rent_splited[6].equals(lib_name)) {
				// find the book

				// then compare
				found = line_count; //found = position으로 argument 사용하기
			}
			line_count++;
		} while (line != null);

		inputRent.close();
		
		if (found != 0) {
			//반납 과정 : Rent file에서 해당 줄 삭제, user file의 bookCount--
			/* 1. file open to delete from loan list */
			//found 줄을 삭제하고 그 뒤로 하나씩 숫자 줄이기
			deleteLoan(found);
			
			/* 2. file open to update user list */
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
			String[] userSplit;
			String temp = "";
			String update = "";
			do {
				temp = inputStream.readLine(); // throws IOException
				if (temp == null)
					break; // 문서 마지막까지 읽었을 경우 break

				userSplit = temp.split("/");
				if (userSplit[0].equals(userId)) {
					/*
					 * for checking System.out.println(temp);
					 */
					int bookCount;
					bookCount = Integer.valueOf(userSplit[7]);
					/* bookCount 내리기 */
					bookCount--;
					userSplit[7] = Integer.toString(bookCount);
					// 쪼개둔 문장 다시 합치기
					for (int i = 0; i < 8; i++) {
						update += userSplit[i];
						update += "/";
					}
					update += userSplit[8];
				}
			} while (temp != null);
			
			/* for checking */
			System.out.println("update : " + update);
			inputStream.close();
			///////////////////////////////////////////////////////////////////
			//file 다시 쓰기
			File file = new File(userFile);
			BufferedReader userInput = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // throws

			String tempLine;
			int position = Integer.valueOf(userId); // 정수형 위해 형변환
			String dummy1 = "", dummy2 = "";

			// 수정하고자 하는 position 전까지 이동하며 dummy에 저장
			for (int i = 0; i < position; i++) {
				tempLine = userInput.readLine(); // 읽으면서 이동
				dummy1 += (tempLine + "\n");
			}
			
			userInput.readLine();// 수정하고자 하는 데이터 건너뛰기
			while ((tempLine = userInput.readLine()) != null) {
				dummy2 += (tempLine + "\n");
			}
			// 삭제하고자 하는 position 이후부터 dummy에 저장하기
			userInput.close();

			PrintWriter userOutput = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));// throws
			/* 수정할 위치 전 */
			userOutput.print(dummy1);

			/* 수정할 위치 */
			userOutput.print(update + "\n");

			/* 수정한 위치 후 */
			userOutput.print(dummy2);

			userOutput.close();
			
		}
		else {
			//해당 도서는 Rent.txt에 없음
			System.out.println("반납 대상이 아닙니다.");
			System.exit(0);
		}
		/*when there is the book in rent.txt */
		
		
	}
}

import java.io.*;
import java.util.*;

public class Search {
	static String bookname;
	static String book_num;
	static String userId; //변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile = "books.txt";
	static int line_count;
	
	static String lib_name = "논현도서관"; //initialize
	
	static String due_date;
	
	private static void library() {
		/*
		 * lib_name 에 따라 bookFile의 이름을 결정합니다. (1.txt, 2.txt, 3.txt, 4.txt, 5.txt - 임시로
		 * 각 class 시작 전 함수 실행하여 도서관 정하는 과정
		 */
		bookFile = lib_name + "books.txt";

	}
	public static void main(String[] args) throws IOException {
		/* variable (로그인할때 설정되는 값) */
		library();
		userId = "1";
		//bookname = "null"; // gui와 연동?
		
		Scanner in = new Scanner(System.in);
		System.out.println("검색할 문구를 입력하십시오 (도서 이름, 저자명, 출판사)");
		String search = null;
		search = in.nextLine(); // 검색할 문구 입력받기
		//////////// 책 이름은 하나만 검색되는데, 출판사나 작가일 경우 원하는 도서를 어떻게 선택할건지 의논 필요
		/* for checking */
		System.out.println(search);

		/* file open to search a book */
		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(bookFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		///////////////////////////////////////////////////////////////////

		String temp = ""; // null값으로 초기화 할 경우 검색 안되는 문제
		String[] searchedList = new String[100];
		int cnt = -1; // 도서 검색이 되었을 경우 count++, 몇 개의 검색결과가 잇는지 - 0개일 경우 "검색결과 없음" 출력
		String[] splited = null;
		do {
			temp = inputStream.readLine(); // throws IOException
			if (temp == null)
				break; // 문서 마지막까지 읽었을 경우 break
			/* for checking */
			if (temp.contains(search)) {
				cnt++;
				// ######################
				/* 출력 형식 어떻게 할 건지 의논 필요 */
				System.out.println(temp); // gui table에 넘기기
				splited = temp.split("/");
				searchedList[cnt] = splited[0]; // 검색결과에 일치하는 값이 포함될 경우 책 번호만 배열에 저장

			}

		} while (temp != null);
		if (cnt < 0) {
			System.out.println("검색 결과가 없습니다."); // 검색결과가 없을 경우 어떻게 나타낼 건지
		} else {
			
			System.out.println("===========================================");
			System.out.println("총 " + (cnt + 1) + " 건의 검색 결과를 찾았습니다.");
		}
		
		//bookname or book number 값 저장 필요
		
		inputStream.close();
		//////////////////////////////////////////////////////////////
	}
}

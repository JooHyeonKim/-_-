import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 가장 빠른 예약 가능일이 언제인지
 *  :
 *  실행 과정 : turn () -> isExist() -> turn()
 * 
 * @author JHL
 *
 */
public class ReservationFinding {
	static String bookname;
	static String book_num;
	static String userId; // 변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile = "books.txt"; // library() 통해 파일 이름 바꾸기
	static String userFile = "user.txt";
	static int line_count;
	
	static String[] library = { "논현도서관", "도곡정보문화도서관", "삼성도서관", "세곡도서관", "역삼도서관"};
	static String lib_name;

	static String due_date, min_date, min_lib = "";

	
	private static void turn() throws IOException, ParseException {
		//books.txt 파일 바꾸며 최소값 갱신하기
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date today = new Date();
		min_date = formatter.format(today);
		//min_date = "21001231"; //임시 숫자

		int index = 0;
		int cnt = 0; //출력된 결과 세기
		for (int i = 0; i < 5; i++) {
			bookFile = library[i] + "books.txt"; //처음부터 5개의 도서관 책 파일 설정
			lib_name = library[i];
			
			index = isExist(bookFile, rentFile);	
			if (index != 0) {
				compareDate(min_date, due_date);
			}

		}
		System.out.println("해당 도서는 " + min_date + " 일부터 대여할 수 있습니다. (" + min_lib + ")");
	}
	
	private static void compareDate(String d1, String d2) throws ParseException {
		//d1 = 현재 최소값, d2 = 비교하려는 값
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		Date c1 = formatter.parse(d1);
		Date c2 = formatter.parse(d2); //string 자료형을 date자료형으로 변환하기
		
		if (c1.compareTo(c2) > 0) {
			min_date = d2;
			min_lib = lib_name + ", "; //반납 예정일과 도서관 이름 수정하기
			
		}
		else if (c1.compareTo(c2) == 0) {
			min_lib += lib_name + ", "; //도서관 이름 추가하기
		}

		
	}
	
	private static int isExist(String file_book, String file_rent) throws IOException, ParseException {
		// input을 파일 이름으로 받기
		/* 도서관 별 파일 이름 - 인풋 값으로 받아서 파일 열기 */
		BufferedReader BOOK = null;
		try {
			BOOK = new BufferedReader(new InputStreamReader(new FileInputStream(file_book), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader RENT = null;
		try {
			RENT = new BufferedReader(new InputStreamReader(new FileInputStream(file_rent), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		String temp = "", rent_temp = "";
		String[] splited_book, splited_rent; // 읽은 문장 쪼개어 저장
		int flag = 0; // 책이 없다면 0, 책이 있고 대여중이라면 1, 책이 있고 대여중이 아니라면 2(바로 대여 가능)
		while (temp != null) {
			temp = BOOK.readLine();
			if (temp == null) break;
			splited_book = temp.split("/");
			
			/*book 찾았을 경우 -> 대여 확인하기 */
			if (splited_book[0].equals(book_num)) {// book number 비교하기
				flag = 2; //rent.txt에 없다면 flag = 2 유지
				
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				Date today = new Date();
				due_date = formatter.format(today); //rent.txt 에 책이 없을 경우 due_date는 오늘 날짜
				
				while (rent_temp != null) {
					rent_temp = RENT.readLine();
					if (rent_temp == null) break;
					splited_rent = rent_temp.split("\t"); // rent.txt에서 한줄씩 읽어 book_num, 반납일자 확인하기
					if (splited_rent[2].equals(book_num)) {
						// find the book in rent.txt - 현재 대여는 불가능
						due_date = splited_rent[4];
						
						flag = 1;
					}
				}
			} // end if
		} // end while

		BOOK.close();
		RENT.close();// close the stream

		return flag;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		book_num = "1";
		userId = "1";
		
		turn();
	}
}

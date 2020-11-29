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
	static String book_num;
	static String userId; // 변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile = "books.txt"; //library() 통해 파일 이름 바꾸기
	static String userFile = "user.txt";
	static int line_count;
	
	static String lib_name = "논현도서관";
	
	static String due_date;
	
	private static void library() {
		/*lib_name 에 따라 bookFile의 이름을 결정합니다. (1.txt, 2.txt, 3.txt, 4.txt, 5.txt - 임시로
		 각 class 시작 전 함수 실행하여 도서관 정하는 과정
		 */
		bookFile = lib_name + "books.txt";
		
	}

	/* available() - rent.txt에 대여된 책이 있는지, 예약되어 있는지,  연장된 적이 있다면 불가능 */
	private static boolean available() throws IOException {

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
		int found = 0; // 찾았다면 1, 없으면 0 - 없을 경우 바로 대여 가능하도록 이동
		line_count = 0; // rent.txt파일에서 위치 찾기 위해
		while (line != null) {
			line = inputrent.readLine();
			rent_splited = line.split("\t");
			// 한 줄씩 읽고 \t 마다 나누기 - [1]사용자 아이디,[2]책 번호,[4]반납예정일, [5]연장 유무
			if (rent_splited[1].equals(userId) && rent_splited[2].equals(book_num) && rent_splited[6].equals(lib_name)) {
				// find the book -> 연장 유무 확인하기
				if (rent_splited[5].equals("0")) {
					//연장한 적 없음 - 예약되어 있는지 확인  (확인 메소드 새로 만들기)
					if (isReserved(rent_splited[0])) {
						System.out.println("예약된 책이므로 연장할 수 없습니다.");
						return false; //예약되어 있다면 연장할 수 없음
					}
					else {
						System.out.println("연장이 가능합니다.");
						due_date = rent_splited[4];
						return true;
					}
				} else {
					// 연장 불가능
					System.out.println("더 이상 연장할 수 없습니다.");
					return false;
				}
			}
			line_count++;
		}
		System.out.println("해당 정보를 찾을 수 없습니다.");
		return false;
	}
	
	private static boolean isReserved(String rentNum) throws IOException {
		/* file open to compare a rent list */
		BufferedReader inputStream = null;
		try {
			inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/* rent.txt에서 같은 책 번호, 같은 도서관이지만 -> 대출 번호가 다르다면 책이 예약되어 있는 상태로 간주 */
		String temp = "";
		String[] splited = null;
		while (temp != null) {
			temp = inputStream.readLine();
			if (temp == null) break; //rent.txt 끝까지 읽으면 중단하기
			splited = temp.split("\t");
			if (splited[2].equals(book_num) && splited[6].equals(lib_name) && !splited[0].equals(rentNum)) return true; //reserved
		}
		//rent.txt 모두 읽은 후에도 조건에 맞는 행 찾지 못하면
		
		return false;
	}
	
	public static void main(String[] args) throws IOException, ParseException {
		/* variable (로그인할때 설정되는 값) */
		userId = "1"; // 로그인할때 값 바꾸기!
		bookname = "90년생이 온다"; // gui에서 값 입력하면 받아오기
		book_num = "1"; // 
		library();
		if (available() == true) {
			// 연장하기 - rent.txt에서 반납일자(+14일), 연장유무 수정(1)하기

			/* 연장 시 반납예정일 계산 */

			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); // 날짜 형식 지정
			Date date = formatter.parse(due_date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date); // 기존 반납예정일

			// cal.setTime(new Date());
			System.out.println("current : " + formatter.format(cal.getTime()));

			cal.add(Calendar.DATE, 14); // 대여기간 2주 - 반납 예정일 계산하기
			System.out.println("after : " + formatter.format(cal.getTime()));
			String rTime = formatter.format(cal.getTime());
			due_date = rTime; // 반납예정일 계산 완료
			
			
			
			/* rent.txt 수정하기 */
			File file = new File(rentFile);
			BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // throws

			String tempLine;
			int position = line_count; // 정수형 위해 형변환
			String dummy1 = "", dummy2 = "";

			//수정할 position 전까지 이동하며 dummy에 저장
			for (int i = 0; i < position; i++) {
				tempLine = inputStream.readLine(); // 읽으면서 이동
				dummy1 += (tempLine + "\n");
			}
			//rent.txt에 저장할 string 다시 작성
			String modify = inputStream.readLine();
			String modified = ""; //수정된 새 문장
			String[] newLine = modify.split("\t"); //\t기준으로 나누기
			newLine[4] = due_date;
			newLine[5] = "1";
			
			for (int i = 0; i < 6; i++) {
				modified += (newLine[i] + "\t");
			}
			modified += newLine[6];
			//수정할 position 이후부터 dummy에 저장하기
			while ((tempLine = inputStream.readLine()) != null) {
				dummy2 += (tempLine + "\n");
			}
			
			inputStream.close();

			
			PrintWriter outputStream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));// throws
			/* 수정할 위치 전 */
			outputStream.print(dummy1);

			/* 수정할 위치 */
			outputStream.print(modified + "\n");

			/* 수정한 위치 후 */
			outputStream.print(dummy2);

			outputStream.close();

		}

		else {
			// 연장할 수 없을 경우 종료
			System.exit(0);
		}

	}

}

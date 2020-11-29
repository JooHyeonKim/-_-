import java.io.*;
import java.util.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**연장할 책이 이미 결정되어 있는 경우!
 * 예약 버튼 누르면 ? 같은 책을 예약하려고 했을 때 중복 체크 기능이 없음 
 */

public class Reservation {
	static String bookname;
	static String book_num;
	static String userId; // 변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile = "books.txt"; // library() 통해 파일 이름 바꾸기
	static String userFile = "user.txt";
	static int line_count;
	
	static String[] library = { "논현도서관", "도곡정보문화도서관", "삼성도서관", "세곡도서관", "역삼도서관"};
	static String lib_name = "삼성도서관";

	static String due_date;

	private static void library() {
		/*
		 * lib_name 에 따라 bookFile의 이름을 결정합니다. (1.txt, 2.txt, 3.txt, 4.txt, 5.txt - 임시로
		 * 각 class 시작 전 함수 실행하여 도서관 정하는 과정
		 */
		bookFile = lib_name + "books.txt";

	}

	private static boolean available() throws IOException {
		/**
		 * available() : 해당 도서관에 있는 책인지,user file에서 사용자의 대여 권수 확인 ,rent file에서 이미 대여된
		 * 책인지
		 * 
		 */
		String[] userSplit;
		String temp = "";

		/* file open to search a book */
		BufferedReader inputBook = null;
		try {
			inputBook = new BufferedReader(new InputStreamReader(new FileInputStream(bookFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		/* file open to check the book number rented */
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

		String line = "", bookLine = ""; // read lines
		String[] rent_splited = null, book_splited = null;
		line_count = 0; // rent file에 데이터 수를 count - 새로 데이터를 쓸 때 순서 배정하기
		int found_r = 0, found_b = 0; // 찾았다면 1, 없으면 0 - 대여 파일에 있을 경우 예약 가능
		/**
		 * first, check if the book is in the library book list
		 */
		do {
			bookLine = inputBook.readLine();
			if (bookLine == null) break;
			book_splited = bookLine.split("/");
			if (book_splited[0].equals(book_num)) {
				// 도서관에 책이 있는 경우
				found_b = 1;
			}

		} while (bookLine != null);
		inputBook.close();
		if (found_b != 1) {
			// 도서관에 책이 있지 않은 경우
			System.out.println("해당 책은 도서관에 구비되어 있지 않습니다.");
			return false;
		} 
		else {
			// 책이 있으니까 다음 확인과정 진행 (대여 리스트에 있는지 - 사용자 대여 권수가 3권이 넘지 않는지)

			do {
				line = inputrent.readLine(); // throws IOException
				if (line == null)
					break; // 문서 마지막까지 읽었을 경우 break
				rent_splited = line.split("\t");
				if (rent_splited[2].equals(book_num) && rent_splited[6].equals(lib_name)) {
					// find the book
					due_date = rent_splited[4];
					// then compare
					found_r = 1;
				}
				line_count++;
			} while (line != null);

			inputrent.close(); // 대여 리스트에 책이 있어야 함 found = 1

			// System.out.println("found : " + found);
			if (found_r == 0) {
				// 대여하기
				System.out.println("현재 대여 가능한 책이므로 예약할 수 없습니다.");
				return false;

			} 
			else {
				// 예약하기
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

			}
		}

	}


	

	public static void main(String[] args) throws IOException, ParseException {
		/* variable (로그인할때 설정되는 값) */
		library();
		userId = "2"; // 로그인할때 값 바꾸기!
		book_num = "1"; // gui와 연동?

		// available - true일 경우만 실행
		if (available() == true) {
			/* file open to read user list - 대여중인 책 개수 수정하기 [5] */
			String temp;
			/* file open to search a book */
			// inputStream = null;
			BufferedReader inputStream = null;
			String[] userSplit;
			// String userID; //main에서 argument로 받아올 건지?
			String update = "";
			try {
				inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
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

					bookCount++;
					userSplit[7] = Integer.toString(bookCount);
					/* file open to write renting information - update information */

					PrintWriter outputStream = null;
					try {
						outputStream = new PrintWriter(new FileOutputStream(rentFile, true));
					} catch (FileNotFoundException e) {
						System.out.println("Error opening the file " + rentFile);
						e.printStackTrace();
					}

					String newInfo = null;
					String date_rent, date_return, extension; // variable 통일 필요
					String line = Integer.toString(line_count);
					// ######################
					/**
					 * long systemTime = System.currentTimeMillis(); SimpleDateFormat formatter =
					 * new SimpleDateFormat("yyyyMMdd", Locale.KOREA); String dTime =
					 * formatter.format(systemTime); System.out.println("Today is " + dTime);
					 * 
					 */
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); // 날짜 형식 지정
					Calendar cal = Calendar.getInstance(); // Calendar 객체 생성
					
					//due_date : 가장 빠른 대출일 찾아서 입력하기  - method ★★★★★★★★★★★★★★★★★★★★
					Date date = formatter.parse(due_date);
					cal.setTime(date); //대출된 책의 기존 반납예정일
			
					System.out.println("current : " + formatter.format(cal.getTime()));
					//String dTime = formatter.format(cal.getTime()); // string 형식으로 변환하기
					// ################수정하기#############
					date_rent = due_date; // 대여일시 저장 (예약 가장 빠른 시간으로) - 함수 필요!!

					cal.add(Calendar.DATE, 14); // 대여기간 2주 - 반납 예정일 계산하기
					System.out.println("after : " + formatter.format(cal.getTime()));
					String rTime = formatter.format(cal.getTime());
					date_return = rTime; // 반납예정일 저장

					// String temp;

					extension = "0";
					newInfo = line + "\t" + userId + "\t" + book_num + "\t" + date_rent + "\t" + date_return + "\t"
							+ extension + "\t" + lib_name;
					/* for checking */
					System.out.println(newInfo);

					outputStream.println(newInfo);

					outputStream.close();

					
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
			/* file open to update user list */
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
			// 삭제하고자 하는 데이터 건너뛰기
			userInput.readLine();
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
		} else {
			// System.out.println("오류 : 대여할 수 없습니다.");
			System.exit(0);
		}
	}
}

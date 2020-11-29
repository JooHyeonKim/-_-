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
	static String bookFile = "books.txt"; //library() 통해 파일 이름 바꾸기
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
	private static boolean available() throws IOException {
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

	public static void main(String[] args) throws IOException {
		/* variable (로그인할때 설정되는 값) */
		library();
		userId = "1"; // 로그인할때 값 바꾸기!
		book_num = "1"; // gui와 연동?
		
		/* file open to search a book */
		BufferedReader inputStream = null;
		// inputStream = null;
		String[] userSplit;
		String temp = "";
		String update = "";
		try {
			inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		/* available에서 true일 경우에만 대여 가능 */
		if (available() == true) {
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
					// bookCount++;

					/* bookCount 올리기 - 대여권수가 3권 이상일 경우 예외처리 */

					bookCount++;
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
					// string을 미리 UTF-8로 인코딩 하는 작업 필요?
					// ######################
					/**
					 * long systemTime = System.currentTimeMillis(); SimpleDateFormat formatter =
					 * new SimpleDateFormat("yyyyMMdd", Locale.KOREA); String dTime =
					 * formatter.format(systemTime); System.out.println("Today is " + dTime);
					 * 
					 * 
					 */

					Calendar cal = Calendar.getInstance(); // Calendar 객체 생성
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); // 날짜 형식 지정

					cal.setTime(new Date());
					System.out.println("current : " + formatter.format(cal.getTime()));
					String dTime = formatter.format(cal.getTime()); // string 형식으로 변환하기
					date_rent = dTime; // 대여일시 저장

					cal.add(Calendar.DATE, 14); // 대여기간 2주 - 반납 예정일 계산하기
					System.out.println("after : " + formatter.format(cal.getTime()));
					String rTime = formatter.format(cal.getTime());
					date_return = rTime; // 반납예정일 저장

					// String temp;

					extension = "0";
					//String lib = lib_name;
					newInfo = line + "\t" + userId + "\t" + book_num + "\t" + date_rent + "\t" + date_return
							+ "\t" + extension + "\t" + lib_name;
					/* for checking */
					System.out.println(newInfo);

					outputStream.println(newInfo);

					outputStream.close();

					userSplit[7] = Integer.toString(bookCount);
					// 쪼개둔 문장 다시 합치기 (user.txt)
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

			// 삭제하고자 하는 position 전까지 이동하며 dummy에 저장
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
		}
		else {
			//System.out.println("오류 : 대여할 수 없습니다.");
			System.exit(0);
		}

	}

}

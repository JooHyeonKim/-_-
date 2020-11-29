import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
/**
 * for test - no need for program
 * 모든 기능 - 테스트 위해서 모아둔 파일입니다.
 * 책을 검색한 후 바로 빌리고 - rent file, user file 업데이트 하는 부분까지
 * 
 * static 변수로 data 가져오면 될 것 같습니당..
 * 
 * @author JHL
 *
 */
public class Total {
	static String bookname;
	static String userId = "2"; //변수 자료형 통일은?
	static String rentFile = "rent.txt";
	static String bookFile = "books.txt";
	static String userFile = "user.txt";
	static int line_count;
	
	public static void main(String[] args) throws IOException {
		

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

		String temp = "추천도서"; // null값으로 초기화 할 경우 검색 안되는 문제
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
				splited = temp.split("\t");
				searchedList[cnt] = splited[0]; // 검색결과에 일치하는 값이 포함될 경우 책 번호만 배열에 저장

			}

		} while (temp != null);
		if (cnt < 0) {
			System.out.println("검색 결과가 없습니다."); // 검색결과가 없을 경우 어떻게 나타낼 건지
		} else {
			
			System.out.println("===========================================");
			System.out.println("총 " + (cnt + 1) + " 건의 검색 결과를 찾았습니다.");
		}

		inputStream.close();
		//////////////////////////////////////////////////////////////
		/*file open to compare a rent list */
		BufferedReader inputrent = null;
		try {
			inputrent = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} 
		String line = null; //read lines
		String[] rent_splited = null;
		String book_num = searchedList[0];
		line_count = 0; // rent file에 데이터 수를 count - 새로 데이터를 쓸 때 순서 배정하기
		int found = 0; //찾았다면 1, 없으면 0 - 없을 경우 바로 대여 가능하도록 이동
		do {
			line = inputrent.readLine();  //throws IOException
			if (line == null) break; //문서 마지막까지 읽었을 경우 break
			rent_splited = line.split("\t");
			if (rent_splited[2].equals(book_num)) {
				//find the book
				
				//then compare 
				found = 1;
			}
			line_count++;
		} while (line != null);
		
		inputrent.close();
		
		//System.out.println("found : " + found);
		if (found == 0) {
			//대여하기
			System.out.println("현재 대여 가능한 책입니다.");
			
		}
		else {
			//######################
			System.out.println("이미 대여중인 책입니다."); //가장 빠르게 대여할 수 있는 날짜? - 
			
		}
		
		
		/////////////////////////////////////////////////////////////
		//
		//이상적인 경우- 검색한 책을 바로 빌리는 step
		
		///////////////////////////////////////////////////
		/*file open to read user list - 대여중인 책 개수 수정하기 [7]*/
		
		/*file open to search a book*/
		//BufferedReader inputStream = null;
		inputStream = null;
		String[] userSplit;
		//String userID; //main에서 argument로 받아올 건지?
		String update = "";
		try {
			inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		do {
			temp = inputStream.readLine();  //throws IOException
			if (temp == null) break; //문서 마지막까지 읽었을 경우 break
			
			userSplit = temp.split("/");
			if (userSplit[0].equals(userId)) {
				/*for checking
				 * System.out.println(temp);
				 */
				int bookCount;
				bookCount = Integer.valueOf(userSplit[7]);
				//bookCount++; 
				
				/*bookCount 올리기 - 대여권수가 3권 이상일 경우 예외처리*/
				if (bookCount < 3) {
					bookCount++;
					/*file open to write renting information - update information*/
					
					PrintWriter outputStream = null;
					try {
						outputStream = new PrintWriter(new FileOutputStream(rentFile, true));
					} catch (FileNotFoundException e) {
						System.out.println("Error opening the file " + rentFile);
						e.printStackTrace();
					}
					
					String newInfo = null;
					String date_rent, date_return, extension; //variable 통일 필요
					String line2 = Integer.toString(line_count);
					//string을 미리 UTF-8로 인코딩 하는 작업 필요?
					
					//######################
					/**
					 * long systemTime = System.currentTimeMillis();
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
					String dTime = formatter.format(systemTime);
					System.out.println("Today is " + dTime);

					 * 
					 */
					
					Calendar cal = Calendar.getInstance(); //Calendar 객체 생성 
					SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd"); //날짜 형식 지정
					
					cal.setTime(new Date());
					System.out.println("current : " + formatter.format(cal.getTime()));
					String dTime = formatter.format(cal.getTime()); //string 형식으로 변환하기
					date_rent = dTime; //대여일시 저장
					
					cal.add(Calendar.DATE, 14); //대여기간 2주 - 반납 예정일 계산하기
					System.out.println("after : " + formatter.format(cal.getTime()));
					String rTime = formatter.format(cal.getTime());
					date_return = rTime; //반납예정일 저장
					
					
					//String temp;
					
					extension = "0";
					newInfo = line2 + "\t" + userId + "\t" + book_num + "\t" + date_rent + "\t" + date_return + "\t" + extension;
					/*for checking*/
					System.out.println(newInfo);
					
					outputStream.println(newInfo); 
					
					outputStream.close();
					
				}
				else {
					System.out.println("대여 가능한 책 수를 초과했습니다. 관리자에게 문의하세요.");
				}
				userSplit[7] = Integer.toString(bookCount);
				//쪼개둔 문장 다시 합치기
				for (int i = 0; i < 8; i++) {
					update += userSplit[i];
					update += "/";
				}
				update += userSplit[8];
			}
			
		} while (temp != null);
		/*for checking */
		System.out.println("update : " + update);
		inputStream.close();
		///////////////////////////////////////////////////////////////////
		/*file open to update user list */
		File file = new File(userFile);
		BufferedReader userInput = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); //throws
		
		
		String tempLine;
		int position = Integer.valueOf(userId); //정수형 위해 형변환
		String dummy1 = "", dummy2 = "";
		
		//삭제하고자 하는 position 전까지 이동하며 dummy에 저장
		for (int i = 0; i < position; i++) {
			tempLine = userInput.readLine(); //읽으면서 이동
			dummy1 += (tempLine + "\n");
		}
		//삭제하고자 하는 데이터 건너뛰기
		userInput.readLine();
		while ((tempLine = userInput.readLine()) != null) {
			dummy2 += (tempLine + "\n");
		}
		//삭제하고자 하는 position 이후부터 dummy에 저장하기
		userInput.close();
		
		PrintWriter userOutput = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));//throws
		/*수정할 위치 전 */
		userOutput.print(dummy1);
		
		/*수정할 위치 */
		userOutput.print(update + "\n");
		
		/*수정한 위치 후*/
		userOutput.print(dummy2);

		userOutput.close();

	}

}

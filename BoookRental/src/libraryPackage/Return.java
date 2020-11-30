package libraryPackage;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
/**
 * 梨� 諛섎궔�븯�뒗 �겢�옒�뒪
 * 
 * user file�뿉�꽌 book_count--, rent file�뿉�꽌 梨� 紐⑸줉 �뾾�븷�뒗 湲곕뒫 �븘�슂
 * 
 * rent file�뿉 �뾾�떎硫�? - �빐�떦 �룄�꽌愿� 梨낆씠 �븘�땲�떎? //�삤瑜섎땲源� �굹以묒뿉 �깮媛곹븯湲�
 * @author JHL
 *
 */

public class Return {
	static String bookname;
	static String book_num;
	static String userId; 
	static String rentFile = "rent.txt";
	static String bookFile;
	static String userFile = "user.txt";
	static int line_count;
	
	static void deleteLoan(int position) throws IOException, FileNotFoundException {
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


}

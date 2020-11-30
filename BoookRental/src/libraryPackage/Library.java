package libraryPackage;
import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;

import libraryPackage.FloydWarshall.liblocation;
import libraryPackage.FloydWarshall.userlocation;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Library extends JFrame {

	private JPanel contentPane;
	private JTextField IDText;
	static JTextArea textPane;
	private JPasswordField PswText;
	private DefaultTableModel model;
	private DefaultTableModel rentModel;
	private JTable table;
	private static JTextField userNumTxt;
	private static JTextField userNameTxt;
	private static JTextField extensionTxt;
	private static JTextField birthTxt;
	private static JTextField phoneNumTxt;
	private static JTextField addressTxt;
	public static String[] info;
	private JTextField searchTxt;
	User user;
	private String bookFile;
	private String rentFile;
	private JTable rentTable;
	private JTextField book1;
	private JTextField book2;
	private JTextField book3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				try {
					Library frame = new Library();
					frame.setVisible(true);
					
		
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	
	private String getBook() {
        return JOptionPane.showInputDialog(
            contentPane, // 다이얼 로그가 frame에 나타내기
            "책 번호를 적으세요:", // 문자열 출력 메시지
            "Library", // 타이틀 명
            JOptionPane.PLAIN_MESSAGE // 아이콘 표시 없음
        );
    }

	/**
	 * Create the frame.
	 */
	public Library() {
		setTitle("LIBRARY");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		JPanel menuPanel = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		/*///////////////////////////////////////////////////////////////////
		 * 최단거리 패널 창-------------------------------------------------------
		////////////////////////////////////////////////////////////////// */
		
		JPanel distancePanel = new JPanel();
		distancePanel.setBounds(0, 0, 584, 361);
		contentPane.add(distancePanel);
		distancePanel.setLayout(null);
		distancePanel.setVisible(false);
		
		
		JButton prvDistance = new JButton("이전");
		prvDistance.setBounds(459, 21, 87, 31);
		distancePanel.add(prvDistance);
		prvDistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				distancePanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		
		
		textPane = new JTextArea();
		textPane.setBounds(39, 74, 507, 222);
		distancePanel.add(textPane);
		
		JScrollPane scrollPane2 = new JScrollPane(textPane);
		scrollPane2.setBounds(39, 74, 507, 222);
		distancePanel.add(scrollPane2);
		
		// 새 창으로 도서관들의 약도 보여주기 --------------------------------------
		JButton map = new JButton("도서관 맵");
		map.setBounds(39, 23, 114, 31);
		distancePanel.add(map);
		map.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new mapMain();
				
			}
		});
		
		// 새 창으로 모든 책 목록을 보여주기 -----------------------------------------
		JButton list = new JButton("책 목록");
		list.setBounds(171, 23, 114, 31);
		distancePanel.add(list);
		list.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				new tableMain();
				
			}
		});
		
		book1 = new JTextField();
		book1.setBounds(39, 319, 116, 21);
		distancePanel.add(book1);
		book1.setColumns(10);
		
		book2 = new JTextField();
		book2.setBounds(171, 319, 116, 21);
		distancePanel.add(book2);
		book2.setColumns(10);
		
		book3 = new JTextField();
		book3.setBounds(304, 319, 116, 21);
		distancePanel.add(book3);
		book3.setColumns(10);
		
		
		// 최단거리 (floydWarshall) 기능 구현 ---------------------------------------------
		JButton searchDistance = new JButton("경로검색");
		searchDistance.setBounds(449, 318, 97, 23);
		distancePanel.add(searchDistance);
		searchDistance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FloydWarshall.bookName1 = book1.getText();
				FloydWarshall.bookName2 = book2.getText();
				FloydWarshall.bookName3 = book3.getText();
				
				try {
					userlocation userxy = new userlocation();
					liblocation libxy = new liblocation();
					
					BufferedReader inputStream = null;
					try {
						inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(FloydWarshall.libFile), "utf-8"));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String line = inputStream.readLine();
					String[] arr = null;
					
					int i=0;
					while ((line = inputStream.readLine())!= null) 
					{
						arr = line.split(",");
						FloydWarshall.libname[i] = arr[0];	
						libxy.x[i] = Double.parseDouble(arr[1]);
						libxy.y[i] = Double.parseDouble(arr[2]);
						i++;
					}
					
					try {
						inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(FloydWarshall.userFile), "utf-8"));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					//Rent rentclass = new Rent();
					int userid = Integer.valueOf(user.no);
					
					line = inputStream.readLine();
					String[] userarr = null;
					String userlo = null;
					String[] userlo2 = null;
					while ((line = inputStream.readLine())!= null) 
					{
						userarr = line.split("/");
						if(Integer.parseInt(userarr[0]) == userid)
						{	
							userlo = userarr[8];
						}
					}
					userlo2 = userlo.split(",");
					userxy.x = Double.parseDouble(userlo2[0]);
					userxy.y = Double.parseDouble(userlo2[1]);
					
					for(int j=0; j<libxy.x.length; j++)
					{
						FloydWarshall.distance[j] = Math.sqrt((Math.pow((userxy.x) - (libxy.x[j]),2) + (Math.pow((userxy.y) - (libxy.y[j]),2))));
					}

					
					FloydWarshall f = new FloydWarshall();
					
					f.floydalgorithm();
						
					
					
				/*	while(click.close)//종료 버튼을 누를때까지
					{
						Scanner sc = new Scanner(System.in);
						
						if(i ==0)
							rentbook.set(0, ~~ );
						else
							rentbook.add(~~ );
							rentval.add(0);
								
						//책 검색 후 선택하는 함수
						//대여책 배열에 저장
					}
				*/

					f.m5(0);
					f.print();
					
					
				}catch (IOException e1) {
					
				}
			}
		});
		
		
		
		
		/*///////////////////////////////////////////////////////////////////
		 * 연장/반납 패널 창-------------------------------------------------------
		////////////////////////////////////////////////////////////////// */	
		
		JPanel extensionPanel = new JPanel();
		extensionPanel.setBounds(0, 0, 584, 361);
		contentPane.add(extensionPanel);
		extensionPanel.setLayout(null);
		extensionPanel.setVisible(false);
		
		String rentHeader[] = {"유저 NO.", "도서 NO.", "대출날짜", "반납날짜"};
		String rentContents[][] = {};
		rentModel = new DefaultTableModel(rentContents, rentHeader) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		rentTable = new JTable(rentModel);
		rentTable.getTableHeader().setReorderingAllowed(false);
		extensionPanel.add(rentTable);
		
		JScrollPane scrollPane1 = new JScrollPane(rentTable);
		scrollPane1.setBounds(44, 71, 501, 130);
		extensionPanel.add(scrollPane1);
		
		JLabel myExtension = new JLabel("내 대출 현황");
		myExtension.setBounds(44, 46, 57, 15);
		extensionPanel.add(myExtension);
		
		JButton prvExtension = new JButton("이전");
		prvExtension.setBounds(459, 20, 86, 29);
		extensionPanel.add(prvExtension);
		prvExtension.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				extensionPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		
		
		// 책 대출 연장 기능---------------------------------------------
		JButton extensionButton = new JButton("연장");
		extensionButton.setBounds(113, 261, 97, 55);
		extensionPanel.add(extensionButton);
		extensionButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					
				Extension.book_num = getBook();
				Extension.userId = user.no;
				
				while(true) {
					if(Extension.book_num != "")
						break;
				}
				
				try {
					if (Extension.available() == true) {

						SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
						Date date = formatter.parse(Extension.due_date);
						Calendar cal = Calendar.getInstance();
						cal.setTime(date); 

						System.out.println("current : " + formatter.format(cal.getTime()));

						cal.add(Calendar.DATE, 14); 
						System.out.println("after : " + formatter.format(cal.getTime()));
						String rTime = formatter.format(cal.getTime());
						Extension.due_date = rTime; 
						
						
						File file = new File(rentFile);
						BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // throws

						String tempLine;
						int position = Extension.line_count; 
						String dummy1 = "", dummy2 = "";


						for (int i = 0; i < position; i++) {
							tempLine = inputStream.readLine(); 
							dummy1 += (tempLine + "\n");
						}

						String modify = inputStream.readLine();
						String modified = ""; 
						String[] newLine = modify.split("\t");
						newLine[4] = Extension.due_date;
						newLine[5] = "1";
						
						for (int i = 0; i < 6; i++) {
							modified += (newLine[i] + "\t");
						}

						while ((tempLine = inputStream.readLine()) != null) {
							dummy2 += (tempLine + "\n");
						}
						
						inputStream.close();

						
						PrintWriter outputStream = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF-8"));// throws
					
						outputStream.print(dummy1);

				
						outputStream.print(modified + "\n");

					
						outputStream.print(dummy2);

						outputStream.close();
						
						rentModel.setNumRows(0);
						BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
						String line = "";
						while((line = bufReader.readLine()) != null) {
							String[] array = line.split("\t");
							String result = array[1] + "\t" + array[2] + "\t" + array[3] + "\t" + array[4];
							array = result.split("\t");
							if(user.no.equals(array[0]))
							rentModel.addRow(array);
							
						}
						JOptionPane.showMessageDialog(extensionPanel, "책 대출 기간이 2주 연장되었습니다.");
						bufReader.close();

					} else {
						JOptionPane.showMessageDialog(extensionPanel, "책 연장이 불가능 합니다.");
					}
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}catch (ParseException e1) {
					e1.printStackTrace();
				}
				
				
				
			}
		});
		
		// 책 반납 기능 ------------------------------------------------------------------------------
		// 유저가 반납하려고 하는 책과 대출목록 데이터에 있는 책이 같은지 비교
		JButton returnButton = new JButton("반납");
		returnButton.setBounds(380, 261, 97, 55);
		extensionPanel.add(returnButton);
		returnButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Return.book_num = getBook();
				Return.userId = user.no;
				String userFile = "user.txt";
				
				while(true) {
					if(Return.book_num != "")
						break;
				}
				
				try {
					
					BufferedReader inputRent = null;
					try {
						inputRent = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile), "utf-8"));
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					String line = ""; 
					String[] rent_splited = null;
					Return.line_count = 0; 
					
					int found = 0;
					do {
						line = inputRent.readLine(); // throws IOException
						if (line == null)
							break; 
						rent_splited = line.split("\t");
						if (rent_splited[2].equals(Return.book_num) && rent_splited[1].equals(Return.userId)/*&&rent_splited[6].equals(Return.lib_num)*/) {
							
							found = Return.line_count; 
						}
						Return.line_count++;
					} while (line != null);

					inputRent.close();
					
					if (found != 0) {
						
						Return.deleteLoan(found);
						
						/* 2. file open to update user list */
						BufferedReader inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
						String[] userSplit;
						String temp = "";
						String update = "";
						do {
							temp = inputStream.readLine(); // throws IOException
							if (temp == null)
								break; 

							userSplit = temp.split("/");
							if (userSplit[0].equals(Return.userId)) {
								/*
								 * for checking System.out.println(temp);
								 */
								int bookCount;
								bookCount = Integer.valueOf(userSplit[7]);
								/* bookCount 내리기 */
								int bookNo = Integer.valueOf(user.extension);
								user.extension = String.valueOf(bookNo - 1);
								bookCount--;
								userSplit[7] = Integer.toString(bookCount);
								
								for (int i = 0; i < 8; i++) {
									update += userSplit[i];
									update += "/";
								}
								update += userSplit[8];
							}
						} while (temp != null);
						
						///////////////////////////////////////////////////////////////////
						//file 다시 쓰기
						File file = new File(userFile);
						BufferedReader userInput = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8")); // throws

						String tempLine;
						int position = Integer.valueOf(Return.userId); // 정수형 위해 형변환
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
						
						rentModel.setNumRows(0);
						BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(rentFile), "utf-8"));
						String line2 = "";
						while((line2 = bufReader.readLine()) != null) {
							String[] array = line2.split("\t");
							String result = array[1] + "\t" + array[2] + "\t" + array[3] + "\t" + array[4];
							array = result.split("\t");
							if(user.no.equals(array[0]))
							rentModel.addRow(array);
							
						}
						
						JOptionPane.showMessageDialog(extensionPanel, "책이 반납 되었습니다.");
						
					}
					
				} catch(IOException e1) {
					
				}
			}
		});
		
		
		
		
		
		
		/*///////////////////////////////////////////////////////////////////
		 * 내 정보창 패널 창---------------------------------------------------------
		////////////////////////////////////////////////////////////////// */	
		
		//로그인시 읽었던 데이터 그리고 프로그램 실행중 업데이트 된 정보 표시
		JPanel myPagePanel = new JPanel();
		myPagePanel.setBounds(0, 0, 584, 361);
		contentPane.add(myPagePanel);
		myPagePanel.setLayout(null);
		myPagePanel.setVisible(false);
		
		JLabel information = new JLabel("고객 정보");
		information.setBounds(62, 31, 57, 15);
		myPagePanel.add(information);
		
		JPanel design = new JPanel();
		design.setBorder(new LineBorder(new Color(0, 153, 255)));
		design.setBounds(53, 56, 482, 223);
		myPagePanel.add(design);
		design.setLayout(null);
		
		JLabel userNum = new JLabel("회원번호");
		userNum.setBounds(12, 10, 57, 15);
		design.add(userNum);
		
		userNumTxt = new JTextField();
		userNumTxt.setBounds(68, 7, 36, 21);
		design.add(userNumTxt);
		userNumTxt.setColumns(10);
		
		JLabel userName = new JLabel("이름");
		userName.setBounds(116, 10, 57, 15);
		design.add(userName);
		
		userNameTxt = new JTextField();
		userNameTxt.setBounds(167, 7, 116, 21);
		design.add(userNameTxt);
		userNameTxt.setColumns(10);
		
		JLabel userExtension = new JLabel("대출권수");
		userExtension.setBounds(320, 10, 57, 15);
		design.add(userExtension);
		
		extensionTxt = new JTextField();
		extensionTxt.setBounds(383, 7, 57, 21);
		design.add(extensionTxt);
		extensionTxt.setColumns(10);
		
		JLabel birth = new JLabel("생년월일");
		birth.setBounds(245, 58, 57, 15);
		design.add(birth);
		
		birthTxt = new JTextField();
		birthTxt.setBounds(315, 55, 125, 21);
		design.add(birthTxt);
		birthTxt.setColumns(10);
		
		JLabel phoneNum = new JLabel("전화번호");
		phoneNum.setBounds(12, 58, 57, 15);
		design.add(phoneNum);
		
		phoneNumTxt = new JTextField();
		phoneNumTxt.setBounds(81, 55, 125, 21);
		design.add(phoneNumTxt);
		phoneNumTxt.setColumns(10);
		
		JLabel address = new JLabel("주소");
		address.setBounds(12, 114, 57, 15);
		design.add(address);
		
		addressTxt = new JTextField();
		addressTxt.setBounds(54, 111, 171, 21);
		design.add(addressTxt);
		addressTxt.setColumns(10);
		
		JButton prevMyPage = new JButton("이전");
		prevMyPage.setBounds(452, 10, 83, 23);
		myPagePanel.add(prevMyPage);
		prevMyPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				myPagePanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		
		
		
		
		/*///////////////////////////////////////////////////////////////////
		 * 검색 패널 창---------------------------------------------------------
		////////////////////////////////////////////////////////////////// */
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(0, 0, 584, 361);
		contentPane.add(searchPanel);
		searchPanel.setLayout(null);
		
		String header[] = {"NO.", "도서명", "저자명", "출판사", "출간일"};
		String contents[][] = {};
		
		//테이블 내용 수정 불가능
		model = new DefaultTableModel(contents, header) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		// 테이블의 도서를 클릭했을 때, 책을 대여하는 기능 구현 ---------------------------------------------------------------
		class MyMouseListener extends MouseAdapter {
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2) {
					/* variable (로그인할때 설정되는 값) */
					Rent.lib_name = bookFile.replace("books.txt", "");
					String userFile = "user.txt";
					String rentFile = "rent.txt";
					
					int row = table.getSelectedRow();
					String userId = user.no;
					Rent.userId = user.no;
					Rent.bookname = (String) table.getValueAt(row, 1);
					Rent.book_num = (String) table.getValueAt(row, 0); 
					
					
					/* file open to search a book */
					try {
						BufferedReader inputStream = null;
						// inputStream = null;
						String[] userSplit;
						String temp = "";
						String update = "";
						try {
							inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(userFile), "utf-8"));
						} catch (FileNotFoundException e1) {
							
						}
						/* available에서 true일 경우에만 대여 가능 */
						if (Rent.available() == true) {
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
									int result = Integer.valueOf(user.extension);
									user.extension = String.valueOf(result + 1);

									/* bookCount 올리기 - 대여권수가 3권 이상일 경우 예외처리 */

									bookCount++;
									/* file open to write renting information - update information */

									PrintWriter outputStream = null;
									try {
										outputStream = new PrintWriter(new FileOutputStream(rentFile, true));
									} catch (FileNotFoundException e1) {
										System.out.println("Error opening the file " + rentFile);
										e1.printStackTrace();
									}

									String newInfo = null;
									String date_rent, date_return, extension; // variable 통일 필요
									String line = Integer.toString(Rent.line_count);
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
									newInfo = line + "\t" + userId + "\t" + Rent.book_num + "\t" + date_rent + "\t" + date_return
											+ "\t" + extension + "\t" + Rent.lib_name;
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
							JOptionPane.showMessageDialog(searchPanel, "책이 정상적으로 대여 되었습니다.");
					}
					else {
						JOptionPane.showMessageDialog(searchPanel, "오류 : 대여할 수 없습니다.");
					}
					}catch(IOException e1) {
						System.out.println(e1);
					}
				}
			}
			
		}
		
		table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); //이동 불가능
		table.addMouseListener(new MyMouseListener());
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 38, 584, 302);
		searchPanel.add(scrollPane);
		
		JButton prvSearch = new JButton("이전");
		prvSearch.setBounds(484, 10, 88, 23);
		searchPanel.add(prvSearch);
		prvSearch.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				searchPanel.setVisible(false);
				menuPanel.setVisible(true);
			}
		});
		
		searchTxt = new JTextField();
		searchTxt.setBounds(75, 340, 380, 21);
		searchPanel.add(searchTxt);
		searchTxt.setColumns(10);
		
		
		// 버튼을 이용한 각 도서관 도서목록 전환 ----------------------------------------------------------------------------------
		JButton library1Button = new JButton("도곡");
		library1Button.setBounds(10, 10, 60, 23);
		searchPanel.add(library1Button);
		library1Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookFile = "도곡정보문화도서관books.txt";
				model.setNumRows(0);
				
				try {
					File file = new File(bookFile);
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					int i = 0;
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("/");
						if(i != 0)
							model.addRow(array);
						i++;
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
			}
		});
		
		JButton library2Button = new JButton("논현");
		library2Button.setBounds(75, 10, 60, 23);
		searchPanel.add(library2Button);
		library2Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookFile = "논현도서관books.txt";
				model.setNumRows(0);
				
				try {
					File file = new File(bookFile);
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					int i = 0;
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("/");
						if(i != 0)
							model.addRow(array);
						i++;
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
			}
		});
		
		JButton library3Button = new JButton("삼성");
		library3Button.setBounds(140, 10, 60, 23);
		searchPanel.add(library3Button);
		library3Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookFile = "삼성도서관books.txt";
				model.setNumRows(0);
				
				try {
					File file = new File(bookFile);
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					int i = 0;
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("/");
						if(i != 0)
							model.addRow(array);
						i++;
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
			}
		});
		
		JButton library4Button = new JButton("세곡");
		library4Button.setBounds(205, 10, 60, 23);
		searchPanel.add(library4Button);
		library4Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookFile = "세곡도서관books.txt";
				model.setNumRows(0);
				
				try {
					File file = new File(bookFile);
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					int i = 0;
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("/");
						if(i != 0)
							model.addRow(array);
						i++;
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
			}
		});
		
		JButton library5Button = new JButton("역삼");
		library5Button.setBounds(270, 10, 60, 23);
		searchPanel.add(library5Button);
		library5Button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				bookFile = "역삼도서관books.txt";
				model.setNumRows(0);
				
				try {
					File file = new File(bookFile);
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					int i = 0;
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("/");
						if(i != 0)
							model.addRow(array);
						i++;
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
			}
		});
		
		searchPanel.setVisible(false);
		
		
		// 도서관의 검색 기능 구현 -----------------------------------------------------------------------------------
		JButton searchButton = new JButton("검색");
		searchButton.setBounds(484, 339, 88, 23);
		searchPanel.add(searchButton);
		searchButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e){
				
				String search = searchTxt.getText();
				/* file open to search a book */
				model.setNumRows(0);
				BufferedReader inputStream = null;
				try {
					inputStream = new BufferedReader(new InputStreamReader(new FileInputStream(bookFile), "utf-8"));
				
					String temp = "추천도서"; // null값으로 초기화 할 경우 검색 안되는 문제
					String[] searchedList = new String[100];
					int cnt = -1; // 도서 검색이 되었을 경우 count++, 몇 개의 검색결과가 잇는지 - 0개일 경우 "검색결과 없음" 출력
					String[] splited = null;
					do {
						temp = inputStream.readLine();
					
						if (temp == null)
							break; // 문서 마지막까지 읽었을 경우 break
						/* for checking */
						if (temp.contains(search)) {
							cnt++;
							
							splited = temp.split("/");
							model.addRow(splited);
							searchedList[cnt] = splited[0]; // 검색결과에 일치하는 값이 포함될 경우 책 번호만 배열에 저장

						}

					}  while (temp != null);
				
				inputStream.close();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e2) {
					System.out.println(e2);
				}
				
			}
		});
		
		
		
		

		/*///////////////////////////////////////////////////////////////////
		 * 메뉴 패널 창---------------------------------------------------------
		////////////////////////////////////////////////////////////////// */
		menuPanel.setBounds(0, 0, 584, 361);
		contentPane.add(menuPanel);
		menuPanel.setLayout(null);
		menuPanel.setVisible(false);
		
		// 패널창 접속과 동시에 도서관의 도서 목록을 테이블에 표시(초기값 : 도곡정보문화도서관)----------------------------------------
		JButton search = new JButton("검색/대여");
		search.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				bookFile = "도곡정보문화도서관books.txt";
				try {
					File file = new File("도곡정보문화도서관books.txt");
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					int i = 0;
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("/");
						if(i != 0)
							model.addRow(array);
						i++;
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
				menuPanel.setVisible(false);
				searchPanel.setVisible(true);
			}
		});
		search.setBounds(350, 100, 102, 55);
		menuPanel.add(search);
		
		// 최단거리 패널 창 접속 -----------------------------------------------------------------------------------------
		JButton distance = new JButton("최단거리");
		distance.setBounds(150, 100, 102, 55);
		menuPanel.add(distance);
		distance.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuPanel.setVisible(false);
				distancePanel.setVisible(true);
			}
		});
		
		
		// 내 정보창을 열 때 정보 표시(누르는 순간순간 정보 업데이트)-----------------------------------------------------------------
		JButton myPage = new JButton("내 정보");
		myPage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				menuPanel.setVisible(false);
				myPagePanel.setVisible(true);
				
				userNumTxt.setText(user.no);
				userNameTxt.setText(user.name);
				birthTxt.setText(user.resident_no);
				addressTxt.setText(user.address);
				phoneNumTxt.setText(user.phone_no);
				extensionTxt.setText(user.extension);
			}
		});
		myPage.setBounds(150, 200, 102, 55);
		menuPanel.add(myPage);
		
		// 연장/반납 패널 접속하면서 내가 대여한 책 목록 표시 ---------------------------------------------------------------------------
		JButton extension = new JButton("연장/반납");
		extension.setBounds(350, 200, 102, 55);
		menuPanel.add(extension);
		extension.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				rentFile = "rent.txt";
				rentModel.setNumRows(0);
				try {
					File file = new File("rent.txt");
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line = "";
					while((line = bufReader.readLine()) != null) {
						String[] array = line.split("\t");
						String result = array[1] + "\t" + array[2] + "\t" + array[3] + "\t" + array[4];
						array = result.split("\t");
						if(user.no.equals(array[0]))
						rentModel.addRow(array);
						
					}
					
					bufReader.close();
				}catch (FileNotFoundException e1) {
					
				}catch (IOException e1) {
					System.out.println(e1);
				}
				
				menuPanel.setVisible(false);
				extensionPanel.setVisible(true);
				
			}
		});
		
		
		
		
		
		/*///////////////////////////////////////////////////////////////////
		 * 로그인 패널 창---------------------------------------------------------
		////////////////////////////////////////////////////////////////// */
		JPanel LogInPanel = new JPanel();
		LogInPanel.setBounds(0, 0, 584, 361);
		contentPane.add(LogInPanel);
		LogInPanel.setLayout(null);
		
		JLabel label = new JLabel("ID");
		label.setBounds(99, 70, 38, 15);
		LogInPanel.add(label);
		
		IDText = new JTextField();
		IDText.setBounds(212, 67, 116, 21);
		IDText.setColumns(10);
		LogInPanel.add(IDText);
		
		JLabel label_1 = new JLabel("Password");
		label_1.setBounds(99, 116, 57, 15);
		LogInPanel.add(label_1);
		
		PswText = new JPasswordField();
		PswText.setBounds(212, 113, 116, 21);
		PswText.setColumns(10);
		LogInPanel.add(PswText);
		
		JButton LogIn = new JButton("Log-In");
		LogIn.setBounds(235, 180, 69, 23);
		LogInPanel.add(LogIn);
		
		//로그인 시 암호화된 파일 읽고, 내 데이터를 가지고 있기--------------------------------------------------
		LogIn.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent arg) {
				
				try {
					
					Scanner scan = new Scanner(System.in); 
			        // RSA 키쌍 생성
			        KeyPair keyPair = CipherUtil.genRSAKeyPair();

			        PublicKey publicKey = keyPair.getPublic();
			        PrivateKey privateKey = keyPair.getPrivate();
			        
			        BufferedReader fileReader = new BufferedReader(new FileReader("user.txt"));
			        BufferedWriter writer = new BufferedWriter(new FileWriter("userEncoding.txt"));

					// a line from the file
					String line;
					String[] person=new String[50];
					//person[]에는 한 사람의 사용자의 정보가 모두 들어가 있음
					int p=0;

					// read lines until we read a line that is null (i.e. no more lines)
					while((line = fileReader.readLine()) != null) {
					       // split the line, returns an array of parts
					       person[p++] =line; 
					}
					fileReader.close();
					
					person[0]+="\n";
					writer.write(person[0]);

					for(int i=1;i<p;i++) { //사람 수 p만큼 루프 돎 
						String[] tmp=person[i].split("/");
						
						for(int j=0;j<9;j++) {//회원목록
							//tmp[0]:회원번호/tmp[1]:회원명/tmp[2]:주민번호/tmp[3]:아이디/tmp[4]:비밀번호/tmp[5]:주소/tmp[6]:전화번호/tmp[7]:대여권수/tmp[8]:위치
							
							if(j==2||j==4||j==6) {//주민번호, 비밀번호, 전화번호 암호화
								//암호화
								 String encrypted = CipherUtil.encryptRSA(tmp[j], publicKey);
								 encrypted+="\t";
								 writer.write(encrypted);
							}
							else if(j==8) {
								tmp[j]+="\n";
								writer.write(tmp[j]);
							}
							else {
								tmp[j]+="\t";
								writer.write(tmp[j]);
							}
					
						}
					}		
						writer.close();
					
					
					File file = new File("userEncoding.txt");
					
					BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "utf-8"));
					String line1 = "";
			        
					int correct = 0;
					int num = 0;
			        while((line1 = bufReader.readLine()) != null) {
						
			        	//attribute는 제외
			        	if(num == 0) {
			        		line1 = bufReader.readLine();
			        		num++;
			        	}

						String[] array = line1.split("\t");
						String id = array[3];
						String pass = array[4];
						int check=0;
						
						 if(id.equals(IDText.getText())) { //입력한 아이디가 회원목록에 존재함
							 
			                 pass = CipherUtil.decryptRSA(pass, privateKey);

			                 //생년월일 복호화
			                 array[2]= CipherUtil.decryptRSA(array[2], privateKey);
								
			                 //전화번호 복호화
			                 array[6]= CipherUtil.decryptRSA(array[6], privateKey);
			                  
			                 if(pass.equals(PswText.getText())) {// 비밀번호가 맞는지 확인
			                	 menuPanel.setVisible(true);
			                	 LogInPanel.setVisible(false);
			                	 info = array;
			                	 userNumTxt.setText(info[0]);
			                	 userNameTxt.setText(info[1]);
			                	 birthTxt.setText(info[2]);
			                	 addressTxt.setText(info[5]);
			                	 phoneNumTxt.setText(info[6]);
			                	 extensionTxt.setText(info[7]);
									
			                	 user = new User(info[0], info[1], info[2], info[5], info[6], info[7], info[8]);
			                	 correct = 1;
			                	 break;
			                  }
			               }
			            }
			        if(correct == 0)
			        	JOptionPane.showMessageDialog(LogInPanel, "잘못된 아이디/비밀번호 입니다.");
					
					bufReader.close();
				}catch (FileNotFoundException e) {
					
				}catch (IOException e) {
					System.out.println(e);
				} catch (NoSuchAlgorithmException e) {
					e.printStackTrace();
				} catch (InvalidKeyException e) {
					e.printStackTrace();
				} catch (NoSuchPaddingException e) {
					e.printStackTrace();
				} catch (BadPaddingException e) {
					e.printStackTrace();
				} catch (IllegalBlockSizeException e) {
					e.printStackTrace();
				}
				
			}
		});
		
		
	}
}

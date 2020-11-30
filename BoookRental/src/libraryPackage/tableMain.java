package libraryPackage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/*
 *  새 창으로 도서목록 보여주기
 */
public class tableMain extends JFrame {

	static JPanel page = new JPanel();
	
	public tableMain() {
		homeframe();
	}
	
	public void homeframe() {
		setTitle("도서목록");
		setSize(584, 361);
		setResizable(false);
		setLocationRelativeTo(null);
		setLayout(null);
		setVisible(true);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setBounds(0, 0, 584, 361);
		add(searchPanel);
		searchPanel.setLayout(null);
		
		String header[] = {"NO.", "도서명", "저자명", "출판사", "출간일"};
		String contents[][] = {};
		
		//테이블 내용 수정 불가능
		DefaultTableModel model = new DefaultTableModel(contents, header) {
			public boolean isCellEditable(int rowIndex, int mColIndex) {
				return false;
			}
		};
		
		JTable table = new JTable(model);
		table.getTableHeader().setReorderingAllowed(false); //이동 불가능
		
		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(0, 0, 584, 361);
		searchPanel.add(scrollPane);
		
		try {
			File file = new File("books.txt");
			
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
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
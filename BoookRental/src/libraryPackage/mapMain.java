package libraryPackage;

import java.awt.Graphics;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
/*
 *  새 창으로 지역 도서관들의 약도를 보여주기
 */
public class mapMain extends JFrame {

	static JPanel page = new JPanel() {
		Image Map = new ImageIcon(Library.class.getResource("../image/imageMap.png")).getImage();
		
		public void paint(Graphics g) {
			g.drawImage(Map, 0, 0, null);
		}
	};
	
	public mapMain() {
		homeframe();
	}
	
	public void homeframe() {
		setTitle("약도");
		setSize(980, 680);
		setLocationRelativeTo(null);
		setLayout(null);
		page.setLayout(null);
		page.setBounds(0, 0, 980, 680);
		setVisible(true);
		add(page);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
}
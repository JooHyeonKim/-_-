/**
 * Greedy algorithm 구현을 위해 사용되는 클래스
 * 
 * 검색할 책에 대한 정보  - 책 번호, 어느 도서관, 반납일자(없으면 현재 날짜), 대여 번호(없으면 0), 
 *
 * @author JHL
 *
 */
public class Greedy {
	String book_num;
	String library;
	String return_date;
	String rent_num;
	
	public Greedy() {
		
	}
	
	public Greedy(String book_num, String lib, String date, String num) {
		this.book_num = book_num;
		this.library = lib;
		this.return_date = date;
		this.rent_num = num;
	}
	
	public String getBookNumber() {
		return book_num;
	}
	public String getLibrary() {
		return library;
	}
	public String getReturnDate() {
		return return_date;
	}
	public String getRentNum() {
		return rent_num;
	}
	
}

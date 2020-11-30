package libraryPackage;

public class User {
	String no;
	String name;
	String resident_no;
	String address;
	String phone_no;
	String extension;
	String locate;
	
	public User(String n, String name, String rn, String a, String p, String b, String l) {
		no=n;
		this.name=name;
		this.resident_no=rn;
		this.address=a;
		this.phone_no=p;
		this.extension=b;
		this.locate = l;
	}
}

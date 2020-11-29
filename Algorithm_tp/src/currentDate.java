import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * 
 * checking current date print // no need for library
 * @author JHL
 *
 */
public class currentDate {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		long systemTime = System.currentTimeMillis();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
		String dTime = formatter.format(systemTime);
		System.out.println("Today is " + dTime);
		

		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		DateFormat df = new SimpleDateFormat("yyyyMMdd");
		System.out.println("current : " + df.format(cal.getTime()));
		cal.add(Calendar.DATE, 14);
		System.out.println("after : " + df.format(cal.getTime()));
		
	}

}

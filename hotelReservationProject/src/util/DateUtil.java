package util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Pattern;

public class DateUtil {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public static Date parseDate(String dateStr) {
        dateStr = dateStr.trim().replaceAll("[()]", "");

        try {
            Date date = DATE_FORMAT.parse(dateStr);
            if (date.before(new Date())) {
                System.out.println("Date must be in the future! Try again.");
                return null;
            }
            return date;
        } catch (ParseException e) {
            System.out.println("Invalid date format. Please enter yyyy-MM-dd.");
            return null;
        }
    }

    public static Date getValidDate(Scanner scanner, String prompt) {
        Date date;
        do {
            System.out.print(prompt);
            date = parseDate(scanner.nextLine());
        } while (date == null);
        return date;
    }

    public static boolean isValidEmail(String email) {
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
public static String formatDate(Date date) {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    return dateFormat.format(date);
}


}

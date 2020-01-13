package pl.rupniewski.staticcontent;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class ValidateStrings {
    public static String regexPassword= "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$";

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
    public static boolean isValidPassword(String password) {
        return password.matches(regexPassword);
    }
    public static boolean isPasswordTheSame(String password, String password2) {
        return password.equals(password2);
    }

    public static boolean isValidUsername(String username) {
        return username == null || username.isEmpty();
    }
}

package kontopoulos.rest.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import static kontopoulos.rest.models.security.SecurityConstant.JWT_EXPIRATION_MS;

public class TestClass {
    public static void main(String[] args) throws ParseException {
        final long expiration = System.currentTimeMillis() + JWT_EXPIRATION_MS;
        System.out.println(new Date(expiration));
        TimeZone zone = TimeZone.getDefault();
        System.out.println(zone.getDisplayName());
        System.out.println(zone.getID());
//        System.out.println(DateFormat.getDateInstance().parse(
//                System.currentTimeMillis() + ""));
    }
}
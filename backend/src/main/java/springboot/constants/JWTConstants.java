package springboot.constants;

public class JWTConstants {
    public static int EXPIRATION_TIME=30*60*100*1000;
    public static String HEADER_STRING= "authorization";
    public static String SECRET = "mynewsecret";
    public static String TOKEN_PREFIX = "Bearer ";
    public static String SIGN_UP_URL = "/api/owner";
    public static String OWNER_ID_HEADER_STRING = "ownerid";
}

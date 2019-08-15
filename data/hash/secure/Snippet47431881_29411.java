import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

public class Quadrigacx {
    public static String[] TABLE = new String[]{}; // Add data here

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {

        final String URL = "https://www.quadrigacx.com/login/";
        String password = "password";
        String clientId = "id";

        String hashPassword = getHash(new String[]{clientId, password});

        Connection.Response response = Jsoup.connect(URL)
                .userAgent("Mozilla")
                .method(Connection.Method.GET)
                .execute();

        Element csrf = response.parse().select("input[name=csrf]").first();
        Element time = response.parse().select("input[name=time]").first();
        Element hash = response.parse().select("input[name=hash]").first();

        Jsoup.connect(URL)
                .userAgent("Mozilla")
                .method(Connection.Method.POST)
                .cookies(response.cookies())
                .data("password", hashPassword)
                .data("client_id", clientId)
                .data("csrf", csrf.attr("value"))
                .data("time", time.attr("value"))
                .data("hash", hash.attr("value"))
                .execute();

        String googleCode = "";

        while (!googleCode.matches("^(?=[0-9]+)\\d{6}$")) {
            System.out.print("Please enter the Two-Factor Authentication to validate your login. (Numbers Only): ");
            Scanner in = new Scanner(System.in);
            googleCode = in.nextLine();
        }

        Jsoup.connect("https://www.quadrigacx.com/authenticate")
                .userAgent("Mozilla")
                .method(Connection.Method.POST)
                .cookies(response.cookies())
                .data("google_code", googleCode)
                .data("redirect", "dash")
                .data("authenticate", "Authenticate")
                .execute();

        response = Jsoup.connect("https://www.quadrigacx.com/dash/")
                .userAgent("Mozilla")
                .cookies(response.cookies())
                .method(Connection.Method.GET)
                .execute();

        System.out.println(response.body());

    }

    private static String getHash(String[] loginArray) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        StringBuilder h = new StringBuilder();
        for (String data : loginArray)
            h.append(data).append(getSalt(data));

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] byteHash = digest.digest(h.toString().getBytes(StandardCharsets.UTF_8));

        StringBuilder sb = new StringBuilder(byteHash.length * 2);
        for (byte b : byteHash)
            sb.append(String.format("%02x", b));

        return sb.toString();
    }

    private static String getSalt(String arg) throws UnsupportedEncodingException {
        return TABLE[getLastByte(arg)];
    }

    private static int getLastByte(String login) throws UnsupportedEncodingException {
        final byte[] utf8Bytes = login.getBytes("UTF-8");
        return utf8Bytes[utf8Bytes.length - 1];
    }
}

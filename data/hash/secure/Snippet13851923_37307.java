package controllers;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import play.mvc.Controller;


public class ExampleController extends Controller
{

    public static final String test1 = "testDude1";
    public static final String test2 = "testDude5";

    public static void set()
    {
        session.put("test1", getHash(test1));
        session.put("test2", getHash(test2));
    }

    public static void get()
    {
        String output = "";

        output += "Test 1 compare: ";
        output += session.get("test1").equals(getHash(test1)) ? "success" : "failed";
        output += "\n";

        output += "Test 2 compare: ";
        output += session.get("test2").equals(getHash(test2)) ? "success" : "failed";
        output += "\n";

        renderText(output);
    }

    /**
     * Generates the hash value for a password.
     * 
     * @param password
     * @return hash
     */
    public static String getHash(String password)
    {
        // Create an digest object
        MessageDigest md;

        try
        {
            // Try to get sha-265
            md = MessageDigest.getInstance("SHA-256");

            // Encrypt the password
            md.update(password.getBytes("UTF-8"));

            // Get the encrypted password
            byte[] digest = md.digest();

            // Convert byte array to String
            String str = new String(digest);

            // Return encrypted password
            return str;
        }
        catch (NoSuchAlgorithmException e)
        {
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }

        return null;
    }

}

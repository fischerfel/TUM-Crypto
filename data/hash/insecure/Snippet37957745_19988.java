import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

    public static String getMD5(String pathToResource) {
            Path path = Paths.get(pathToResource);
            byte[] data = Files.readAllBytes(path);
            try {
                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] messageDigest = md.digest(data);
                BigInteger number = new BigInteger(1, messageDigest);
                String hashtext = number.toString(16);
                // Now we need to zero pad it if you actually want the full 32 chars.
                while (hashtext.length() < 32) {
                    hashtext = "0" + hashtext;
                }
                return hashtext;
            }
            catch (NoSuchAlgorithmException e) {
                throw new RuntimeException(e);
            }
        }

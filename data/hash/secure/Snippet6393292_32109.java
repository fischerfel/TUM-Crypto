import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import de.seduceme.utils.Base64;

public class PwStorage {
    public static int PW_HASH_ITERATION_COUNT = 5000;
    private static MessageDigest md;

    public static void main(String[] args) {
        String pw = "teüöäßÖst1";
        String salt = "e33ptcbnto8wo8c4o48kwws0g8ksck0";

        try {
            md = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            throw new RuntimeException("No Such Algorithm");
        }

        String result = PwStorage.hashPw(pw, salt);
        System.out.println(result);
        // result: 2SzT+ikuO9FBq7KJWulZy2uZYujLjFkSpcOwlfBhi6VvajJMr6gxuRo5WvilrMlcM/44u2q8Y1smUlidZQrLCQ==
    }


    private static String hashPw(String pw, String salt) {
        byte[] bSalt;
        byte[] bPw;

        try {
            bSalt = salt.getBytes("UTF-8");
            bPw = pw.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported Encoding", e);
        }

        byte[] digest = run(bPw, bSalt);
        for (int i = 0; i < PW_HASH_ITERATION_COUNT - 1; i++) {
            digest = run(digest, bSalt);
        }

        return Base64.encodeBytes(digest);
    }

    private static byte[] run(byte[] input, byte[] salt) {
        md.update(input);
        return md.digest(salt);
    }
}

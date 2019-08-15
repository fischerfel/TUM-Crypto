import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public class AesFileIo {

    private static final String AES_ALGORITHM = "AES/CTR/NoPadding";
    private static final String PROVIDER = BouncyCastleProvider.PROVIDER_NAME;
    private static final byte[] AES_KEY_128 = { // Hard coded for now
        78, -90, 42, 70, -5, 20, -114, 103,
        -99, -25, 76, 95, -85, 94, 57, 54};
    private static final byte[] IV = { // Hard coded for now
        -85, -67, -5, 88, 28, 49, 49, 85,
        114, 83, -40, 119, -65, 91, 76, 108};
    private static final SecretKeySpec secretKeySpec =
            new SecretKeySpec(AES_KEY_128, "AES");
    private static final IvParameterSpec ivSpec = new IvParameterSpec(IV);

    public void AesFileIo() {
        Security.addProvider(new org.bouncycastle.jce.provider
                .BouncyCastleProvider());
    }

    public void writeFile(String fileName, String theFile) {
        try {
            Cipher cipher = Cipher.getInstance(AES_ALGORITHM, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(theFile.getBytes());
            ObjectOutputStream os = new ObjectOutputStream(
                new FileOutputStream(fileName));
            os.write(encrypted);
            os.flush();
            os.close();
        } catch (Exception e) {
            StackTraceElement se = new Exception().getStackTrace()[0];
            System.err.println(se.getFileName() + " " + se.getLineNumber()
                    + " " + e);
        }
    }
}

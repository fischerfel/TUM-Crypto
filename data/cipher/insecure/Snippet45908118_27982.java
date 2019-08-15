import android.support.v7.app.AppCompatActivity;    
import android.os.Bundle;    
import android.util.Log;    
import java.security.Security;    
import javax.crypto.Cipher;    
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);

        byte[] input = "this is a test taht we will use to text the validaty of your program, here goes nothing".getBytes();
        byte[] keyBytes = new byte[] { 0x00, 0x01, 0x02, 0x03, 0x04,
                0x05, 0x06, 0x07, 0x08, 0x09, 0x0a, 0x0b, 0x0c,
                0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14,
                0x15, 0x16, 0x17 };

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        try
        {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "SC");

            System.out.println(new String(input));

            // encryption pass
            cipher.init(Cipher.ENCRYPT_MODE, key);

            byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
            int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
            ctLength += cipher.doFinal(cipherText, ctLength);
            Log.i("testAppEncrypted --> ", new String(cipherText));
            System.out.println(ctLength);



            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
            int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
            ptLength += cipher.doFinal(plainText, ptLength);
            Log.i("testAppDecrypted --> ", new String(plainText));
            System.out.println(ptLength);
        }
        catch(Exception ex)
        {
            System.out.println(ex.getMessage());
        }

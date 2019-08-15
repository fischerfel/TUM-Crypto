import javax.crypto.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.Key;
import java.security.Provider;
import java.security.Security;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.encoders.Hex;

public class Main {

public static BouncyCastleProvider bc = new BouncyCastleProvider();

static {
    Security.addProvider(bc);
}

public static void main(String[] args) {
    byte[] data = "This is message, length=32 bytes".getBytes();

    Key key;
    CipherInputStream       cIn;
    CipherOutputStream      cOut;
    ByteArrayInputStream    bIn;
    ByteArrayOutputStream   bOut;
    byte[]                  bytes;

    byte[] keyData = Hex.decode("8182838485868788898a8b8c8d8e8f80d1d2d3d4d5d6d7d8d9dadbdcdddedfd0");

    key = new SecretKeySpec(keyData,"GOST28147");

    Cipher cipher = Cipher.getInstance("GOST28147/ECB/NoPadding", bc);

    cipher.init(Cipher.ENCRYPT_MODE, key); //Exception in thread "main" java.security.InvalidKeyException: Illegal key size or default parameters

    bOut = new ByteArrayOutputStream();

    cOut = new CipherOutputStream(bOut, cipher);

    for (int i = 0; i != data.length / 2; i++)
    {
        cOut.write(data[i]);
    }
    cOut.write(data, data.length / 2, data.length - data.length / 2);
    cOut.close();

    bytes = bOut.toByteArray();

    System.out.print(key.toString() + System.lineSeparator());
    System.out.print(byteArrayToString(bytes) + System.lineSeparator());
}
}

    import org.apache.commons.codec.binary.Base64;
    import java.util.ResourceBundle;
    import com.sun.crypto.provider.SunJCE;

    ... snip ...

    StringBuffer ourTransferBuffer = new StringBuffer(s);
    byte abyte0[] = Base64.decodeBase64(encryptionKey);
    SunJCE sunjce = new SunJCE();
    Security.addProvider(sunjce);
    SecretKeySpec secretkeyspec = new SecretKeySpec(abyte0, "Blowfish");
    Cipher cipher = Cipher.getInstance("Blowfish");
    cipher.init(1, secretkeyspec);
    byte abyte1[] = cipher.doFinal(ourTransferBuffer.toString().getBytes());
    s = Base64.encodeBase64String(abyte1);
    return s;

    ... snip ...

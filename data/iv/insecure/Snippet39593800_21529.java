import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;


class HelloWorld {
    public static void main(String[] args) {
        try 
        {

            byte[] keyBytes = Base64.getDecoder().decode("5gYDRl00XcjqlcC5okdBSfDx46HIGZBMAvMiibVYixc=");
            byte[] ivBytes = Base64.getDecoder().decode("AAAAAAAAAAAAAAAAAAAAAA==");

            SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec iv = new IvParameterSpec(ivBytes);

            Cipher cipher = Cipher.getInstance("AES/CTR/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);
            String encrypted = Base64.getEncoder().encodeToString(cipher.doFinal("This is a plaintext message.".getBytes()));
            System.out.println(encrypted); // print: sTBJYaaGNTxCErAXbP6eXnU59Yyn1UJAp7MGQw==

        } catch (Exception e) {}
        return;
    }
}

 import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import mtnsa.sorb.handler.PKCS5Test.PasswordDeriveBytes;

import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.generators.PKCS5S1ParametersGenerator;
import org.bouncycastle.crypto.params.KeyParameter;
import org.bouncycastle.util.encoders.Base64;

public class EncryptionHelper {



    public static void main(String[] args)throws Exception {
        encrypt("FTTH","HhN01vcEEtMmwdNFliM8QYg0Y89xzBOJJG7BHARC7g", "002400000480000094000000060200000024000052534131000400000100010085525e9438e9fae122f71ec7124" +
                "443bf2f9f57f5f3760b3704df168493004b9ef68413f500d54fa9fa3869b42b1e2365204826e54b618d56e7e575f2" +
                "7f675f0eae3ea8458a8ee1e92dc3f4bfc34fbe23851afa9d2c28fc8cd5b124f60a03a06bfb598bc3acbd8c4380ae" +
                "f02cc58bdf955d140390f740a7e115c59e3b3b5758ca");
    }

    public static String encrypt(final String valueToEncrpt, final String saltString, final String privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException{
        String encrptedValue = null;

           Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        byte[] password = privateKey.getBytes();
        byte[] salt = saltString.getBytes();

        PKCS5S1ParametersGenerator generator = new PasswordDeriveBytes(new SHA1Digest());
        generator.init(password, salt, 100);

        byte[] keyArr = ((KeyParameter)generator.generateDerivedParameters(128)).getKey();
        byte[] IvArr = ((KeyParameter)generator.generateDerivedParameters(128)).getKey();



        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyArr,"AES"),new IvParameterSpec(IvArr));
        byte[]test  = cipher.doFinal(valueToEncrpt.getBytes());

        System.out.println(new String(Base64.encode(test)));

        return encrptedValue;
    }
}

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Security;
import java.util.Arrays;
import java.util.Base64;

import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.xml.bind.DatatypeConverter.printHexBinary;

public class CipherDebug {

    private final String algorithm;

    private final String provider;

    private final String cryptographicAlgorithm;

    public CipherDebug(String algorithm,
                       String cipherMode,
                       String paddingMode,
                       String provider) {
        this.algorithm = algorithm;
        this.provider = provider;
        this.cryptographicAlgorithm = algorithm + "/" + cipherMode + "/" + paddingMode;
    }

    private Cipher createCipher(int encryptDecryptMode,
                                byte[] keyValue,
                                byte[] initializationVector) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(keyValue, algorithm);
        IvParameterSpec ivSpec = new IvParameterSpec(initializationVector);
        Cipher cipher = Cipher.getInstance(cryptographicAlgorithm, provider);
        cipher.init(encryptDecryptMode, keySpec, ivSpec);
        return cipher;
    }

    @Override
    public String toString() {
        return "CipherDebug{" +
                "provider=\"" + provider + '\"' +
                ", cryptographicAlgorithm=\"" + cryptographicAlgorithm + '\"' +
                '}';
    }

    private static String generateData(int length) {
        char[] chars = new char[length];
        Arrays.fill(chars, '0');
        return new String(chars);
    }

    public static void main(String[] args) throws Exception {
        Security.insertProviderAt(new BouncyCastleProvider(), 1);

        int numberOfChunks = 3;
        byte[] keyValue = Base64.getDecoder()
                .decode("yY7flqEdx95dojF/yY7flqEdx95dojF/".getBytes(StandardCharsets.UTF_8));
        byte[] initializationVector = "pjts4PzQIr9Pd2yb".getBytes(StandardCharsets.UTF_8);

        CipherDebug bouncyCastle = new CipherDebug("AES", "CBC", "PKCS5Padding", "BC");

        CipherDebug sunJCE = new CipherDebug("AES", "CBC", "PKCS5Padding", "SunJCE");

        Cipher bouncyCastleCipher = bouncyCastle.createCipher(Cipher.ENCRYPT_MODE,
                keyValue, initializationVector);

        Cipher sunJCECipher = sunJCE.createCipher(Cipher.ENCRYPT_MODE,
                keyValue, initializationVector);

        assert bouncyCastleCipher.getBlockSize() == sunJCECipher.getBlockSize();

        // blockSize = 16
        int blockSize = bouncyCastleCipher.getBlockSize();

        byte[] data = generateData(blockSize * numberOfChunks).getBytes(UTF_8);
        byte[] bouncyCastleUpdate = bouncyCastleCipher.update(data);
        byte[] sunJCEUpdate = sunJCECipher.update(data);

        //303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030303030
        System.out.println(printHexBinary(data));

        // CipherDebug{provider="BC", cryptographicAlgorithm="AES/CBC/PKCS5Padding"}
        // 1D4DE40480F0528D4F77E788817DA62902D98C9AE6DF9299F4F2D1836CC10924
        // 0320B10C8646D17E0755F8BBA1214ABF24D2E6E7F06184A78559793B23A9A341
        System.out.println(bouncyCastle.toString());
        System.out.println(printHexBinary(bouncyCastleUpdate));
        System.out.println(printHexBinary(bouncyCastleCipher.doFinal()));

        System.out.println();

        // CipherDebug{provider="SunJCE", cryptographicAlgorithm="AES/CBC/PKCS5Padding"}
        // 1D4DE40480F0528D4F77E788817DA62902D98C9AE6DF9299F4F2D1836CC109240320B10C8646D17E0755F8BBA1214ABF
        // 24D2E6E7F06184A78559793B23A9A341
        System.out.println(sunJCE.toString());
        System.out.println(printHexBinary(sunJCEUpdate));
        System.out.println(printHexBinary(sunJCECipher.doFinal()));

        // assertion fails
        assert Arrays.equals(bouncyCastleUpdate, sunJCEUpdate);
    }
}

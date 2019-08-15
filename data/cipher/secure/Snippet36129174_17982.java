import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECFieldFp;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.EllipticCurve;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class WebPushEncryption {

    private static final byte UNCOMPRESSED_POINT_INDICATOR = 0x04;
    private static final ECParameterSpec params = new ECParameterSpec(
            new EllipticCurve(new ECFieldFp(new BigInteger(
                                    "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFF",
                                    16)), new BigInteger(
                            "FFFFFFFF00000001000000000000000000000000FFFFFFFFFFFFFFFFFFFFFFFC",
                            16), new BigInteger(
                            "5AC635D8AA3A93E7B3EBBD55769886BC651D06B0CC53B0F63BCE3C3E27D2604B",
                            16)), new ECPoint(new BigInteger(
                            "6B17D1F2E12C4247F8BCE6E563A440F277037D812DEB33A0F4A13945D898C296",
                            16), new BigInteger(
                            "4FE342E2FE1A7F9B8EE7EB4A7C0F9E162BCE33576B315ECECBB6406837BF51F5",
                            16)), new BigInteger(
                    "FFFFFFFF00000000FFFFFFFFFFFFFFFFBCE6FAADA7179E84F3B9CAC2FC632551",
                    16), 1);

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        String endpoint = "https://updates.push.services.mozilla.com/push/v1/xxx";
        final byte[] alicePubKeyEnc = Base64.decode("base64 encoded public key ");

        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
        keyGen.initialize(params);

        KeyPair bobKpair = keyGen.generateKeyPair();
        PrivateKey localPrivateKey = bobKpair.getPrivate();
        PublicKey localpublickey = bobKpair.getPublic();

        final ECPublicKey remoteKey = fromUncompressedPoint(alicePubKeyEnc, params);

        KeyAgreement bobKeyAgree = KeyAgreement.getInstance("ECDH", "BC");
        bobKeyAgree.init(localPrivateKey);

        byte[] bobPubKeyEnc = toUncompressedPoint((ECPublicKey) bobKpair.getPublic());

        bobKeyAgree.doPhase(remoteKey, true);

        SecretKey bobDesKey = bobKeyAgree.generateSecret("AES");

        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);

        Mac extract = Mac.getInstance("HmacSHA256", "BC");
        extract.init(new SecretKeySpec(saltBytes, "HmacSHA256"));
        final byte[] prk = extract.doFinal(bobDesKey.getEncoded());

        // Expand
        Mac expand = Mac.getInstance("HmacSHA256", "BC");
        expand.init(new SecretKeySpec(prk, "HmacSHA256"));

        //aes algorithm
        String info = "Content-Encoding: aesgcm128";
        expand.update(info.getBytes(StandardCharsets.US_ASCII));
        expand.update((byte) 1);
        final byte[] key_bytes = expand.doFinal();
        byte[] key_bytes16 = Arrays.copyOf(key_bytes, 16);
        SecretKeySpec key = new SecretKeySpec(key_bytes16, 0, 16, "AES-GCM");

        //nonce
        expand.reset();
        expand.init(new SecretKeySpec(prk, "HmacSHA256"));
        String nonceinfo = "Content-Encoding: nonce";
        expand.update(nonceinfo.getBytes(StandardCharsets.US_ASCII));
        expand.update((byte) 1);
        final byte[] nonce_bytes = expand.doFinal();
        byte[] nonce_bytes12 = Arrays.copyOf(nonce_bytes, 12);

        Cipher bobCipher = Cipher.getInstance("AES/GCM/NoPadding", "BC");

        byte[] iv = generateNonce(nonce_bytes12, 0);

        bobCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));

        byte[] cleartext = ("{\n"
                + "      \"message\" : \"great match41eeee!\",\n"
                + "      \"title\" : \"Portugal vs. Denmark4255\",\n"
                + "      \"icon\" : \"http://icons.iconarchive.com/icons/artdesigner/tweet-my-web/256/single-bird-icon.png\",\n"
                + "   \"tag\" : \"testtag1\",\n"
                + "   \"url\" : \"http://www.yahoo.com\"\n"
                + "    }").getBytes();

        byte[] cc = new byte[cleartext.length + 1];
        cc[0] = 0;

        for (int i = 0; i < cleartext.length; i++) {
            cc[i + 1] = cleartext[i];
        }

        cleartext = cc;
        byte[] ciphertext = bobCipher.doFinal(cleartext);

        URL url = new URL(endpoint);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("POST");
        urlConnection.setRequestProperty("Content-Length", ciphertext.length + "");
        urlConnection.setRequestProperty("Content-Type", "application/octet-stream");
        urlConnection.setRequestProperty("encryption-key", "keyid=p256dh;dh=" + Base64.encode(bobPubKeyEnc));
        urlConnection.setRequestProperty("encryption", "keyid=p256dh;salt=" + Base64.encode(saltBytes));
        urlConnection.setRequestProperty("content-encoding", "aesgcm128");
        urlConnection.setRequestProperty("ttl", "60");
        urlConnection.setDoInput(true);
        urlConnection.setDoOutput(true);
        final OutputStream outputStream = urlConnection.getOutputStream();
        outputStream.write(ciphertext);
        outputStream.flush();
        outputStream.close();
        if (urlConnection.getResponseCode() == 201) {
            String result = readStream(urlConnection.getInputStream());
            System.out.println("PUSH OK: " + result);
        } else {
            InputStream errorStream = urlConnection.getErrorStream();
            String error = readStream(errorStream);
            System.out.println("PUSH" + "Not OK: " + error);
        }
    }

    static byte[] generateNonce(byte[] base, int index) {
        byte[] nonce = Arrays.copyOfRange(base, 0, 12);

        for (int i = 0; i < 6; ++i) {
            nonce[nonce.length - 1 - i] ^= (byte) ((index / Math.pow(256, i))) & (0xff);
        }
        return nonce;
    }

    private static String readStream(InputStream errorStream) throws Exception {
        BufferedInputStream bs = new BufferedInputStream(errorStream);
        int i = 0;
        byte[] b = new byte[1024];
        StringBuilder sb = new StringBuilder();
        while ((i = bs.read(b)) != -1) {
            sb.append(new String(b, 0, i));
        }

        return sb.toString();
    }

    public static ECPublicKey fromUncompressedPoint(
            final byte[] uncompressedPoint, final ECParameterSpec params)
            throws Exception {

        int offset = 0;
        if (uncompressedPoint[offset++] != UNCOMPRESSED_POINT_INDICATOR) {
            throw new IllegalArgumentException(
                    "Invalid uncompressedPoint encoding, no uncompressed point indicator");
        }

        int keySizeBytes = (params.getOrder().bitLength() + Byte.SIZE - 1)
                / Byte.SIZE;

        if (uncompressedPoint.length != 1 + 2 * keySizeBytes) {
            throw new IllegalArgumentException(
                    "Invalid uncompressedPoint encoding, not the correct size");
        }

        final BigInteger x = new BigInteger(1, Arrays.copyOfRange(
                uncompressedPoint, offset, offset + keySizeBytes));
        offset += keySizeBytes;
        final BigInteger y = new BigInteger(1, Arrays.copyOfRange(
                uncompressedPoint, offset, offset + keySizeBytes));
        final ECPoint w = new ECPoint(x, y);
        final ECPublicKeySpec ecPublicKeySpec = new ECPublicKeySpec(w, params);
        final KeyFactory keyFactory = KeyFactory.getInstance("EC");
        return (ECPublicKey) keyFactory.generatePublic(ecPublicKeySpec);
    }

    public static byte[] toUncompressedPoint(final ECPublicKey publicKey) {

        int keySizeBytes = (publicKey.getParams().getOrder().bitLength() + Byte.SIZE - 1)
                / Byte.SIZE;

        final byte[] uncompressedPoint = new byte[1 + 2 * keySizeBytes];
        int offset = 0;
        uncompressedPoint[offset++] = 0x04;

        final byte[] x = publicKey.getW().getAffineX().toByteArray();
        if (x.length <= keySizeBytes) {
            System.arraycopy(x, 0, uncompressedPoint, offset + keySizeBytes
                    - x.length, x.length);
        } else if (x.length == keySizeBytes + 1 && x[0] == 0) {
            System.arraycopy(x, 1, uncompressedPoint, offset, keySizeBytes);
        } else {
            throw new IllegalStateException("x value is too large");
        }
        offset += keySizeBytes;

        final byte[] y = publicKey.getW().getAffineY().toByteArray();
        if (y.length <= keySizeBytes) {
            System.arraycopy(y, 0, uncompressedPoint, offset + keySizeBytes
                    - y.length, y.length);
        } else if (y.length == keySizeBytes + 1 && y[0] == 0) {
            System.arraycopy(y, 1, uncompressedPoint, offset, keySizeBytes);
        } else {
            throw new IllegalStateException("y value is too large");
        }

        return uncompressedPoint;
    }
}

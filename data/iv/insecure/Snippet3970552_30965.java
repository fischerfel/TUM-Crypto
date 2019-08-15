import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.interfaces.DHPrivateKey;
import javax.crypto.interfaces.DHPublicKey;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;
import javax.crypto.spec.IvParameterSpec;

import org.ofbiz.base.util.Debug;
import org.ofbiz.base.util.StringUtil;

public class diffHel {

    public static final String module = "diffHel";

    //were in props
    public String publicValue = "6B0276780D3E07911D744F545833005E8C2F755E0FE59A8660527F7B7E070A45EEB853DA70C6EFE2B8BF278F0B4A334A49DF0985635745A3DAD2E85A9C0EEFAE657CC382A0B3EAE9C3F85B0A2305282612CFD2857801131EC9FE313DB9DADFB914A30EE077E8A97E5574CE5BD56661B021C39116913710947FAA38FFCB4FC045";
    public String format = "ddMMyyyyHHmmss";
    public String primeHex = "e516e43e5457b2f66f6ca367b335ead8319939fa4df6c1b7f86e73e922a6d19393255e419096668174e35c818a66117f799e8666c8050ee436f9801351606c55d45faba03f39e2923ba926a9cd75d4bdbca9de78b62a9b847a781c692c063eaacb43a396f01d121d042755d0b7c0b2dfa8b498a57e4d90c30ca049a7ac2b7f73";
    public String genString = "05";

    //were generic values
    public String exchangeKey;
    public String privateKey = ""; 

    protected SecretKey kek = null;
    protected SecretKey mwk = null;
    protected String merchantId = null;
    protected String terminalId = null;
    protected Long mwkIndex = null;
    protected boolean debug = false;

    public StringBuffer buf = new StringBuffer();

    public diffHel() {}

    /**
     * Output the creation of public/private keys + KEK to the console for manual database update
     */
    public StringBuffer outputKeyCreation(boolean kekOnly, String kekTest) {
        return this.outputKeyCreation(0, kekOnly, kekTest);
    }

    private StringBuffer outputKeyCreation(int loop, boolean kekOnly, String kekTest) {
        //StringBuffer buf = new StringBuffer();
        loop++;

        if (loop > 100) {
            // only loop 100 times; then throw an exception
            throw new IllegalStateException("Unable to create 128 byte keys in 100 tries");
        }

        // place holder for the keys
        DHPrivateKey privateKey = null;
        DHPublicKey publicKey = null;

        if (!kekOnly) {
            KeyPair keyPair = null;
            try {
                keyPair = this.createKeys();
            } catch (NoSuchAlgorithmException e) {
                Debug.logError(e, module);
            } catch (InvalidAlgorithmParameterException e) {
                Debug.logError(e, module);
            } catch (InvalidKeySpecException e) {
                Debug.logError(e, module);
            }

            if (keyPair != null) {
                publicKey = (DHPublicKey) keyPair.getPublic();
                privateKey = (DHPrivateKey) keyPair.getPrivate();

               buf.append("privateKeyLenth=" + privateKey.getX().toByteArray().length);


                if (publicKey == null || publicKey.getY().toByteArray().length != 128) {
                    // run again until we get a 128 byte public key for VL
                    return this.outputKeyCreation(loop, kekOnly, kekTest);
                }
            } else {
                Debug.log("Returned a null KeyPair", module);
                return this.outputKeyCreation(loop, kekOnly, kekTest);
            }
        } else {
            // use our existing private key to generate a KEK
            try {
                privateKey = (DHPrivateKey) this.getPrivateKey();
            } catch (Exception e) {
                Debug.logError(e, module);
            }
        }

        // the KEK
        byte[] kekBytes = null;
        try {
            kekBytes = this.generateKek(privateKey);
        } catch (NoSuchAlgorithmException e) {
            Debug.logError(e, module);
        } catch (InvalidKeySpecException e) {
            Debug.logError(e, module);
        } catch (InvalidKeyException e) {
            Debug.logError(e, module);
        }

        // the 3DES KEK value
        SecretKey loadedKek = this.getDesEdeKey(kekBytes);
        byte[] loadKekBytes = loadedKek.getEncoded();


        // test the KEK
        //Cipher cipher = this.getCipher(this.getKekKey(), Cipher.ENCRYPT_MODE);
        Cipher cipher = this.getCipher(loadedKek, Cipher.ENCRYPT_MODE);
        byte[] kekTestB = { 0, 0, 0, 0, 0, 0, 0, 0 };
        byte[] kekTestC = new byte[0];
        if (kekTest != null) {
            kekTestB = StringUtil.fromHexString(kekTest);
        }

        // encrypt the test bytes
        try {
            kekTestC = cipher.doFinal(kekTestB);
        } catch (Exception e) {
            Debug.logError(e, module);
        }

        kek = loadedKek;

        byte[] newMwk = generateMwk(loadedKek);

        byte[] decyptedMwk = decryptViaKek(newMwk);

        if (!kekOnly) {
            // public key (just Y)
            BigInteger y = publicKey.getY();
            byte[] yBytes = y.toByteArray();
            String yHex = StringUtil.toHexString(yBytes);
            buf.append("======== Begin Public Key (Y @ " + yBytes.length + " / " + yHex.length() + ") ========\n");
            buf.append(yHex + "\n");
            buf.append("======== End Public Key ========\n\n");

            // private key (just X)
            BigInteger x = privateKey.getX();
            byte[] xBytes = x.toByteArray();
            String xHex = StringUtil.toHexString(xBytes);
            buf.append("======== Begin Private Key (X @ " + xBytes.length + " / " + xHex.length() + ") ========\n");
            buf.append(xHex + "\n");
            buf.append("======== End Private Key ========\n\n");

            // private key (full)
            byte[] privateBytes = privateKey.getEncoded();
            String privateHex = StringUtil.toHexString(privateBytes);
            buf.append("======== Begin Private Key (Full @ " + privateBytes.length + " / " + privateHex.length() + ") ========\n");
            buf.append(privateHex + "\n");
            buf.append("======== End Private Key ========\n\n");
        }

        if (kekBytes != null) {
            buf.append("======== Begin KEK aka decrypted MWK (" + kekBytes.length + ") ========\n");
            buf.append(StringUtil.toHexString(kekBytes) + "\n");
            buf.append("======== End KEK ========\n\n");

            buf.append("======== Begin KEK (DES) (" + loadKekBytes.length + ") ========\n");
            buf.append(StringUtil.toHexString(loadKekBytes) + "\n");
            buf.append("======== End KEK (DES) ========\n\n");

            buf.append("======== Begin KEK Test (" + kekTestC.length + ") ========\n");
            buf.append(StringUtil.toHexString(kekTestC) + "\n");
            buf.append("======== End KEK Test ========\n\n");
        } else {
            Debug.logError("KEK came back empty", module);
        }
        if (newMwk != null) {
            buf.append("======== Begin MWK (" + newMwk.length + ") ========\n");
            buf.append(StringUtil.toHexString(newMwk) + "\n");
            buf.append("======== End MWK ========\n\n");
        }
        if (decyptedMwk != null) {
            buf.append("======== Begin Decrypted MWK (" + decyptedMwk.length + ") ========\n");
            buf.append(StringUtil.toHexString(decyptedMwk) + "\n");
            buf.append("======== End Decrypted MWK ========\n\n");
        }

        return buf;
    }

    /**
     * Create a set of public/private keys using ValueLinks defined parameters
     * @return KeyPair object containing both public and private keys
     * @throws NoSuchAlgorithmException
     * @throws InvalidAlgorithmParameterException
     */
    public KeyPair createKeys() throws NoSuchAlgorithmException, InvalidAlgorithmParameterException, InvalidKeySpecException {
        // initialize the parameter spec
        DHPublicKey publicKey = (DHPublicKey) this.getValueLinkPublicKey();
        DHParameterSpec dhParamSpec = publicKey.getParams();
        //Debug.log(dhParamSpec.getP().toString() + " / " + dhParamSpec.getG().toString(), module);

        // create the public/private key pair using parameters defined by valuelink
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("DH");
        keyGen.initialize(dhParamSpec);
        KeyPair keyPair = keyGen.generateKeyPair();

        return keyPair;
    }

    /**
     * Generate a key exchange key for use in encrypting the mwk
     * @param privateKey The private key for the merchant
     * @return byte array containing the kek
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     * @throws InvalidKeyException
     */
    public byte[] generateKek(PrivateKey privateKey) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException {
        // get the ValueLink public key
        PublicKey vlPublic = this.getValueLinkPublicKey();

        // generate shared secret key
        KeyAgreement ka = KeyAgreement.getInstance("DH");
        ka.init(privateKey);
        ka.doPhase(vlPublic, true);

        byte[] secretKey = ka.generateSecret();




        buf.append("======== Secret Key (" + secretKey.length + ") ========\n");
        buf.append(StringUtil.toHexString(secretKey) + "\n");
        buf.append("======== End Secret Key ========\n\n");


        if (debug) {
            Debug.log("Secret Key : " + StringUtil.toHexString(secretKey) + " / " + secretKey.length,  module);
        }

        // generate 3DES from secret key using VL algorithm (KEK)
        MessageDigest md = MessageDigest.getInstance("SHA1");
        byte[] digest = md.digest(secretKey);
        byte[] des2 = getByteRange(digest, 0, 16);
        byte[] first8 = getByteRange(des2, 0, 8);
        byte[] kek = copyBytes(des2, first8, 0);

        if (debug) {
            Debug.log("Generated KEK : " + StringUtil.toHexString(kek) + " / " + kek.length, module);
        }


        return kek;


    }

    /**
     * Get a public key object for the ValueLink supplied public key
     * @return PublicKey object of ValueLinks's public key
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public PublicKey getValueLinkPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
        // read the valuelink public key
        //String publicValue = (String) props.get("payment.valuelink.publicValue");
        byte[] publicKeyBytes = StringUtil.fromHexString(publicValue);

        // initialize the parameter spec
        DHParameterSpec dhParamSpec = this.getDHParameterSpec();

        // load the valuelink public key
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        BigInteger publicKeyInt = new BigInteger(publicKeyBytes);
        DHPublicKeySpec dhPublicSpec = new DHPublicKeySpec(publicKeyInt, dhParamSpec.getP(), dhParamSpec.getG());
        PublicKey vlPublic = keyFactory.generatePublic(dhPublicSpec);

        return vlPublic;
    }

    /**
     * Get merchant Private Key
     * @return PrivateKey object for the merchant
     */
    public PrivateKey getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException {
        byte[] privateKeyBytes = this.getPrivateKeyBytes();

        // initialize the parameter spec
        DHParameterSpec dhParamSpec = this.getDHParameterSpec();

        // load the private key
        KeyFactory keyFactory = KeyFactory.getInstance("DH");
        BigInteger privateKeyInt = new BigInteger(privateKeyBytes);
        DHPrivateKeySpec dhPrivateSpec = new DHPrivateKeySpec(privateKeyInt, dhParamSpec.getP(), dhParamSpec.getG());
        PrivateKey privateKey = keyFactory.generatePrivate(dhPrivateSpec);

        return privateKey;
    }

    /**
     * Generate a new MWK
     * @return Hex String of the new encrypted MWK ready for transmission to ValueLink
     */
    public byte[] generateMwk() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("DES");
        } catch (NoSuchAlgorithmException e) {
            Debug.logError(e, module);

        }

        // generate the DES key 1
        SecretKey des1 = keyGen.generateKey();
        SecretKey des2 = keyGen.generateKey();

        if (des1 != null && des2 != null) {
            byte[] desByte1 = des1.getEncoded();
            byte[] desByte2 = des2.getEncoded();
            byte[] desByte3 = des1.getEncoded();

            // check for weak keys
            try {
                if (DESKeySpec.isWeak(des1.getEncoded(), 0) || DESKeySpec.isWeak(des2.getEncoded(), 0)) {
                    return generateMwk();
                }
            } catch (Exception e) {
                Debug.logError(e, module);
            }

            byte[] des3 = copyBytes(desByte1, copyBytes(desByte2, desByte3, 0), 0);
            return generateMwk(des3);
        } else {
            Debug.log("Null DES keys returned", module);
        }

        return null;
    }

    /**
     * Generate a new MWK
     * @param desBytes byte array of the DES key (24 bytes)
     * @return Hex String of the new encrypted MWK ready for transmission to ValueLink
     */
    public byte[] generateMwk(byte[] desBytes) {
        if (debug) {
            Debug.log("DES Key : " + StringUtil.toHexString(desBytes) + " / " + desBytes.length, module);
        }
        SecretKeyFactory skf1 = null;
        SecretKey mwk = null;
        try {
            skf1 = SecretKeyFactory.getInstance("DESede");
        } catch (NoSuchAlgorithmException e) {
            Debug.logError(e, module);
        }
        DESedeKeySpec desedeSpec2 = null;
        try {
            desedeSpec2 = new DESedeKeySpec(desBytes);
        } catch (InvalidKeyException e) {
            Debug.logError(e, module);
        }
        if (skf1 != null && desedeSpec2 != null) {
            try {
                mwk = skf1.generateSecret(desedeSpec2);
            } catch (InvalidKeySpecException e) {
                Debug.logError(e, module);
            }
        }
        if (mwk != null) {
            return generateMwk(mwk);
        } else {
            return null;
        }
    }

    /**
     * Generate a new MWK
     * @param mwkdes3 pre-generated DES3 SecretKey
     * @return Hex String of the new encrypted MWK ready for transmission to ValueLink
     */
    public byte[] generateMwk(SecretKey mwkdes3) {
        // zeros for checksum
        byte[] zeros = { 0, 0, 0, 0, 0, 0, 0, 0 };

        // 8 bytes random data
        byte[] random = new byte[8];
        Random ran = new Random();
        ran.nextBytes(random);


        // open a cipher using the new mwk
        Cipher cipher = this.getCipher(mwkdes3, Cipher.ENCRYPT_MODE);

        // make the checksum - encrypted 8 bytes of 0's
        byte[] encryptedZeros = new byte[0];
        try {
            encryptedZeros = cipher.doFinal(zeros);
        } catch (IllegalStateException e) {
            Debug.logError(e, module);
        } catch (IllegalBlockSizeException e) {
            Debug.logError(e, module);
        } catch (BadPaddingException e) {
            Debug.logError(e, module);
        }

        // make the 40 byte MWK - random 8 bytes + key + checksum
        byte[] newMwk = copyBytes(mwkdes3.getEncoded(), encryptedZeros, 0);
        newMwk = copyBytes(random, newMwk, 0);

        if (debug) {
            Debug.log("Random 8 byte : " + StringUtil.toHexString(random), module);
            Debug.log("Encrypted 0's : " + StringUtil.toHexString(encryptedZeros), module);
            Debug.log("Decrypted MWK : " + StringUtil.toHexString(mwkdes3.getEncoded()) + " / " + mwkdes3.getEncoded().length, module);
            Debug.log("Encrypted MWK : " + StringUtil.toHexString(newMwk) + " / " + newMwk.length, module);
        }

        return newMwk;
    }

    /**
     * Use the KEK to encrypt a value usually the MWK
     * @param content byte array to encrypt
     * @return encrypted byte array
     */
    public byte[] encryptViaKek(byte[] content) {
        return cryptoViaKek(content, Cipher.ENCRYPT_MODE);
    }

    /**
     * Ue the KEK to decrypt a value
     * @param content byte array to decrypt
     * @return decrypted byte array
     */
    public byte[] decryptViaKek(byte[] content) {
        return cryptoViaKek(content, Cipher.DECRYPT_MODE);
    }

    /**
     * Returns a date string formatted as directed by ValueLink
     * @return ValueLink formatted date String
     */
    public String getDateString() {
        //String format = (String) props.get("payment.valuelink.timestamp");
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

 // using the prime and generator provided by valuelink; create a parameter object
    protected DHParameterSpec getDHParameterSpec() {
        //String primeHex = (String) props.get("payment.valuelink.prime");
        //String genString = (String) props.get("payment.valuelink.generator");

        // convert the p/g hex values
        byte[] primeByte = StringUtil.fromHexString(this.primeHex);
        BigInteger prime = new BigInteger(1, primeByte); // force positive (unsigned)
        BigInteger generator = new BigInteger(this.genString);

        // initialize the parameter spec
        DHParameterSpec dhParamSpec = new DHParameterSpec(prime, generator, 1024);

        return dhParamSpec;
    }

    // actual kek encryption/decryption code
    protected byte[] cryptoViaKek(byte[] content, int mode) {
        // open a cipher using the kek for transport
        Cipher cipher = this.getCipher(this.getKekKey(), mode);
        byte[] dec = new byte[0];
        try {
            dec = cipher.doFinal(content);
        } catch (IllegalStateException e) {
            Debug.logError(e, module);
        } catch (IllegalBlockSizeException e) {
            Debug.logError(e, module);
        } catch (BadPaddingException e) {
            Debug.logError(e, module);
        }
        return dec;
    }

 // return a cipher for a key - DESede/CBC/NoPadding IV = 0
    protected Cipher getCipher(SecretKey key, int mode) {
        byte[] zeros = { 0, 0, 0, 0, 0, 0, 0, 0 };
        IvParameterSpec iv = new IvParameterSpec(zeros);

        // create the Cipher - DESede/CBC/NoPadding
        Cipher mwkCipher = null;
        try {
            mwkCipher = Cipher.getInstance("DESede/CBC/NoPadding");
        } catch (NoSuchAlgorithmException e) {
            Debug.logError(e, module);
            return null;
        } catch (NoSuchPaddingException e) {
            Debug.logError(e, module);
        }
        try {
            mwkCipher.init(mode, key, iv);
        } catch (InvalidKeyException e) {
            Debug.logError(e, "Invalid key", module);
        } catch (InvalidAlgorithmParameterException e) {
            Debug.logError(e, module);
        }
        return mwkCipher;
    }

    protected SecretKey getKekKey() {
        if (kek == null) {
            kek = this.getDesEdeKey(getKek());
        }

        if (debug) {
            Debug.log("Raw KEK : " + StringUtil.toHexString(getKek()), module);
            Debug.log("KEK : " + StringUtil.toHexString(kek.getEncoded()), module);
        }

        return kek;
    }

    protected SecretKey getDesEdeKey(byte[] rawKey) {
        SecretKeyFactory skf = null;
        try {
            skf = SecretKeyFactory.getInstance("DESede");
        } catch (NoSuchAlgorithmException e) {
            // should never happen since DESede is a standard algorithm
            Debug.logError(e, module);
            return null;
        }

        // load the raw key
        if (rawKey.length > 0) {
            DESedeKeySpec desedeSpec1 = null;
            try {
                desedeSpec1 = new DESedeKeySpec(rawKey);
            } catch (InvalidKeyException e) {
                Debug.logError(e, "Not a valid DESede key", module);
                return null;
            }

            // create the SecretKey Object
            SecretKey key = null;
            try {
                key = skf.generateSecret(desedeSpec1);
            } catch (InvalidKeySpecException e) {
                Debug.logError(e, module);
            }
            return key;
        } else {
            throw new RuntimeException("No valid DESede key available");
        }
    }

    protected byte[] getKek() {
        //return StringUtil.fromHexString(this.getGenericValue().getString("exchangeKey"));
        return StringUtil.fromHexString(this.exchangeKey);
    }

    protected byte[] getPrivateKeyBytes() {
        //return StringUtil.fromHexString(this.getGenericValue().getString("privateKey"));
        return StringUtil.fromHexString(this.privateKey);
    }

    /**
     * Returns a new byte[] from the offset of the defined byte[] with a specific number of bytes
     * @param bytes The byte[] to extract from
     * @param offset The starting postition
     * @param length The number of bytes to copy
     * @return a new byte[]
     */
    public static byte[] getByteRange(byte[] bytes, int offset, int length) {
        byte[] newBytes = new byte[length];
        for (int i = 0; i < length; i++) {
            newBytes[i] = bytes[offset + i];
        }
        return newBytes;
    }

    /**
     * Copies a byte[] into another byte[] starting at a specific position
     * @param source byte[] to copy from
     * @param target byte[] coping into
     * @param position the position on target where source will be copied to
     * @return a new byte[]
     */
    public static byte[] copyBytes(byte[] source, byte[] target, int position) {
        byte[] newBytes = new byte[target.length + source.length];
        for (int i = 0, n = 0, x = 0; i < newBytes.length; i++) {
            if (i < position || i > (position + source.length - 2)) {
                newBytes[i] = target[n];
                n++;
            } else {
                for (; x < source.length; x++) {
                    newBytes[i] = source[x];
                    if (source.length - 1 > x) {
                        i++;
                    }
                }
            }
        }
        return newBytes;
    }


}

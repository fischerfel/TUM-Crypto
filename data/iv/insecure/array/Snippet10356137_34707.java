public class Encryption {

        private SecretKeySpec mKey = null;
        private Cipher mCipher = null;
        private byte[] mKeyBytes = null;
        private AlgorithmParameterSpec mParamSpec = null;
        private static Encryption sInstance;

        public Encryption() {
            byte[] iv = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
            mParamSpec = new IvParameterSpec(iv);
            mKeyBytes = getMD5(MD5_KEY.getBytes();
            mKey = new SecretKeySpec(mKeyBytes, AES_TAG);
            try {
                mCipher = Cipher.getInstance(TRANSFORMATION_STR);
            } catch (NoSuchAlgorithmException e) {
            } catch (NoSuchPaddingException e) {
            }
        }

        public static synchronized Encryption getInstance() {
            if (sInstance == null) {
                sInstance = new Encryption();
            }
            return sInstance;
        }

        public String encryptString(String strPwd) {
            String strToEncripted = null;
            strToEncripted = strPwd;
            String result = null;
            byte[] input = null;
            byte[] cipherText = null;
            int ctLength = 0;
            try {
                input = strToEncripted.getBytes(UTF8_STR);
                mCipher.init(Cipher.ENCRYPT_MODE, mKey, mParamSpec);
                cipherText = new byte[mCipher.getOutputSize(input.length)];
                ctLength = mCipher.update(input, 0, input.length, cipherText, 0);
                ctLength += mCipher.doFinal(cipherText, ctLength);
                result = Base64.encodeToString(cipherText, Base64.DEFAULT)
                        .replace(NEWLINE_CHAR, EMPTY_CHAR).trim();
            } catch (InvalidKeyException e) {
            } catch (UnsupportedEncodingException e) {
            } catch (InvalidAlgorithmParameterException e) {
            } catch (ShortBufferException e) {
            } catch (IllegalBlockSizeException e) {
            } catch (BadPaddingException e) {
            } catch (IllegalStateException e) {
            }
            return result;
        }

        public String decryptstring(byte[] encripted) {
            String textDecrypt = "";
            byte[] encriptedByteDecode64 = Base64.decode(encripted, Base64.DEFAULT);
            byte[] plainText = new byte[mCipher.getOutputSize(encriptedByteDecode64.length)];
            int ptLength = 0;
            try {
                mCipher.init(Cipher.DECRYPT_MODE, mKey, mParamSpec);
                ptLength = mCipher.update(encriptedByteDecode64, 0, encriptedByteDecode64.length, plainText, 0);
                ptLength += mCipher.doFinal(plainText, ptLength);
                textDecrypt = (new String(plainText)).trim();
            } catch (InvalidKeyException e) {
            } catch (InvalidAlgorithmParameterException e) {
            } catch (ShortBufferException e) {
            } catch (IllegalBlockSizeException e) {
            } catch (BadPaddingException e) {
            }
            return textDecrypt;
        }


        private String getMD5(String strKey) {
            String key = strKey;
            String result = null;
            try {
                MessageDigest algorithm = MessageDigest.getInstance(MD5_TAG);
                algorithm.reset();
                algorithm.update(key.getBytes(UTF8_STR));
                byte messageDigest[] = algorithm.digest();
                StringBuilder hexString = new StringBuilder();
                for (int count = 0; count < messageDigest.length; count++) {
                    String hexaDecimal = Integer.toHexString(0xFF & messageDigest[count]);
                    while (hexaDecimal.length() < 2)
                        hexaDecimal = new StringBuilder(ZERO_STR).append(hexaDecimal).toString();
                    hexString.append(hexaDecimal);
                }
                result = hexString.toString();
            } catch (NoSuchAlgorithmException e) {
            } catch (UnsupportedEncodingException e) {
            }
            return result;
        }
    }

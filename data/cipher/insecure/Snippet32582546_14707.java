import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.xml.bind.DatatypeConverter;

        public class CryptoPropio {


            /**
             * Master password.
             */
            private static char[] PASSWORD;

            /** The Constant SALT. */
            private static final byte[] SALT = { (byte) 0xde, (byte) 0x33, (byte) 0x10,
                    (byte) 0x12, (byte) 0xde, (byte) 0x33, (byte) 0x10, (byte) 0x12, };

            /** The error des. */
            private static String errorDes = "Error al des/encriptar";

            /** The Constant ALGORITMO. */
            private static final String ALGORITMO = "PBEWithMD5AndDES";

            /**
             * Instantiates a new crypto propio.
             * 
             * @param pass
             *            the pass
             */
            public CryptoPropio(String pass) {
                super();
                PASSWORD = pass.toCharArray();

            }


            public String encrypt(String property) {
                SecretKeyFactory keyFactory;
                String result = null;

                try {
                    keyFactory = SecretKeyFactory.getInstance(ALGORITMO);

                    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
                    Cipher pbeCipher = Cipher.getInstance(ALGORITMO);
                    pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(SALT,
                            20));
                    result = base64Encode(
                                    pbeCipher.doFinal(property.getBytes("UTF-8")))
                                    .toString();
                } catch (InvalidKeyException | InvalidAlgorithmParameterException
                        | NoSuchAlgorithmException | InvalidKeySpecException
                        | NoSuchPaddingException | IllegalBlockSizeException
                        | BadPaddingException | UnsupportedEncodingException e) {
                  e.printStackTrace();
                }

                return result;
            }

            /**
             * Base64 encode.
             * 
             * @param bytes
             *            the bytes
             * @return the string
             */
            private String base64Encode(byte[] bytes) {
                return DatatypeConverter.printBase64Binary(bytes);
            }


            public String decrypt(String propert) {
                String property = propert;
                String result = null;
                try {
                    SecretKeyFactory keyFactory = SecretKeyFactory
                            .getInstance(ALGORITMO);
                    SecretKey key = keyFactory.generateSecret(new PBEKeySpec(PASSWORD));
                    Cipher pbeCipher = Cipher.getInstance(ALGORITMO);
                    pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(SALT,
                            20));
                    result = new String(pbeCipher.doFinal(base64Decode(property)),
                            "UTF-8");
                } catch (InvalidKeyException | InvalidAlgorithmParameterException
                        | NoSuchAlgorithmException | InvalidKeySpecException
                        | NoSuchPaddingException | IllegalBlockSizeException
                        | BadPaddingException | UnsupportedEncodingException e) {
                   e.printStackTrace();
                }
                return result;
            }

            /**
             * Base64 decode.
             * 
             * @param property
             *            the property
             * @return the byte[]
             */
            private byte[] base64Decode(String property) {

                return DatatypeConverter.parseBase64Binary(property);
            }

        }

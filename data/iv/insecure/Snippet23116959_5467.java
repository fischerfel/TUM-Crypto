   package whatsapp;

    import java.io.File;

    import java.io.FileInputStream;

    import java.io.FileOutputStream;

    import java.math.BigInteger;

    import java.security.MessageDigest;

    import java.util.Arrays;

    import javax.crypto.Cipher;

    import javax.crypto.CipherInputStream;

    import javax.crypto.spec.IvParameterSpec;

    import javax.crypto.spec.SecretKeySpec;

    public class WhatsAppDecrypt5 {

        private static final byte[] INITIALIZATION_VECTOR = hexStringToByteArray("1e39f369e90db33aa73b442bbbb6b0b9");
        private static final byte[] ENCRYPTION_KEY = hexStringToByteArray("8d4b155cc9ff81e5cbf6fa7819366a3ec621a656416cd793");
        public static void main(String[] args) throws Exception {



            if (args.length != 3) {
                System.out.println("usage <inputfile> <outputfile> <email>");
            //  System.exit(0);
            }
            decrypt(new File("/Users/ovazquez/Downloads/msgstore.db.crypt5"), new File("/Users/ovazquez/Downloads/msgstore.db"), "theasociatedemail@gmail.com");
            System.out.println("finalizado");
        }

        private static void decrypt(File inputFile, File outputFile, String email)
                throws Exception {

            String emailMD5 = md5(email);

            byte[] emailMD5Bytes = hexStringToByteArray(emailMD5 + emailMD5);

            byte[] decryptionKey = Arrays.copyOf(ENCRYPTION_KEY, 24);

            for (int i = 0; i < 24; i++) {
                decryptionKey[i] ^= emailMD5Bytes[i & 0xF];
            }

            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
            cipher.init(Cipher.DECRYPT_MODE,new SecretKeySpec(decryptionKey, "AES"), new IvParameterSpec(INITIALIZATION_VECTOR));
            CipherInputStream cIn = new CipherInputStream(new FileInputStream(inputFile), cipher);
            FileOutputStream fOut = new FileOutputStream(outputFile);

            byte[] buffer = new byte[8192];

            int n;

            while ((n = cIn.read(buffer)) != -1) {

                fOut.write(buffer, 0, n);

            }

            cIn.close();

            fOut.close();

        }

        private static byte[] hexStringToByteArray(String s) {

            int len = s.length();

            byte[] data = new byte[len / 2];

            for (int i = 0; i < len; i += 2) {

                data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character
                        .digit(s.charAt(i + 1), 16));

            }

            return data;

        }

        private static String md5(String md5) throws Exception {

            MessageDigest m = MessageDigest.getInstance("MD5");

            m.reset();

            m.update(md5.getBytes());

            byte[] digest = m.digest();

            BigInteger bigInt = new BigInteger(1, digest);

            return bigInt.toString(16);

        }

    }

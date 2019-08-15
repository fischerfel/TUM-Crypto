    import java.security.SecureRandom;

    import javax.crypto.Cipher;
    import javax.crypto.KeyGenerator;
    import javax.crypto.SecretKey;
    import javax.crypto.spec.SecretKeySpec;

    import android.app.Activity;
    import android.os.Bundle;
    import android.widget.TextView;

    public class main extends Activity {
        TextView tvOutput;
        static String out;
        String TEST_STRING = "abcdefghijklmnopqrstuvwxyz";
        String PASSKEY = "ThePasswordIsPassord";

        /** Called when the activity is first created. */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.main);
            tvOutput = (TextView) findViewById(R.id.tvOutput);
        }

        @Override
        public void onResume() {
            super.onResume();
            out = "";
            runTest();
            tvOutput.setText(out);
        }

        private void runTest() {
            out = "Test string: " + TEST_STRING + "\n";
            out += "Passkey: " + PASSKEY + "\n";
            try {
                out += "Encrypted: " + encrypt(PASSKEY, TEST_STRING) + "\n";
            } catch (Exception e) {
                out += "Error: " + e.getMessage() + "\n";
                e.printStackTrace();
            }

        }

        public static String encrypt(String seed, String cleartext)
        throws Exception {
            byte[] rawKey = getRawKey(seed.getBytes());
            byte[] result = encrypt(rawKey, cleartext.getBytes());
            return toHex(result) + "\n" + "Raw Key: " + String.valueOf(rawKey)
                    + "\n";
        }

        private static byte[] getRawKey(byte[] seed) throws Exception {
            KeyGenerator kgen = KeyGenerator.getInstance("AES");
            SecureRandom sr = SecureRandom.getInstance("SHA1PRNG");
            sr.setSeed(seed);
            kgen.init(128, sr); // 192 and 256 bits may not be available
            SecretKey skey = kgen.generateKey();
            byte[] raw = skey.getEncoded();
            return raw;
        }

        private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
                byte[] encrypted = cipher.doFinal(clear);
            return encrypted;
        }

        public static String toHex(String txt) {
            return toHex(txt.getBytes());
        }

        public static String fromHex(String hex) {
            return new String(toByte(hex));
        }

        public static byte[] toByte(String hexString) {
            int len = hexString.length() / 2;
            byte[] result = new byte[len];
            for (int i = 0; i < len; i++)
                result[i] = Integer.valueOf(hexString.substring(2 * i, 2 * i + 2),
                        16).byteValue();
            return result;
        }

        public static String toHex(byte[] buf) {
            if (buf == null)
                return "";
            StringBuffer result = new StringBuffer(2 * buf.length);
            for (int i = 0; i < buf.length; i++) {
                appendHex(result, buf[i]);
            }
            return result.toString();
        }

         private final static String HEX = "0123456789ABCDEF";

        private static void appendHex(StringBuffer sb, byte b) {
            sb.append(HEX.charAt((b >> 4) & 0x0f)).append(HEX.charAt(b & 0x0f));
        }
    }

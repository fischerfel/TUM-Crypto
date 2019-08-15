    import java.io.FileInputStream;
    import java.io.InputStream;
    import java.security.MessageDigest;

    public class Main {

        public static byte[] getMD5Checksum(String filename) throws Exception {
            InputStream fis = new FileInputStream(filename);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int aux;
            do {
                aux = fis.read(buffer);
                if (aux > 0) {
                    md.update(buffer, 0, aux);
                }
            } while (aux != -1);
            fis.close();
            return md.digest();
        }

        private static String toHexString(byte[] bytes) {
            StringBuilder sb = new StringBuilder(bytes.length * 2);
            for (byte b : bytes)
                sb.append(String.format("%02x", b & 0xff));
            return sb.toString();
        }

        public static void main(String args[]) throws Exception {
            System.out.println(toHexString(getMD5Checksum("/Users/irineuantunes/Desktop/testFile.txt")));
        }

    }

    import java.io.ByteArrayInputStream;
    import java.io.ByteArrayOutputStream;
    import java.io.File;
    import java.io.FileInputStream;
    import java.io.IOException;
    import java.nio.ByteBuffer;
    import java.nio.ByteOrder;
    import java.nio.charset.Charset;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.nio.file.Paths;
    import javax.crypto.Cipher;
    import javax.crypto.spec.IvParameterSpec;
    import javax.crypto.spec.SecretKeySpec;

    public class HelloWorld {

        public static void main(String[] args) throws Exception {
            // TODO Auto-generated method stub
            Path p = Paths
                    .get("C:\\Users\\casilva\\workspace\\StackOverflow\\src\\tst.enc");

            byte[] a = Files.readAllBytes(p);
            byte[] result = decodeFile("myKey123", a);
            System.out.println("Result=" + new String(result, "UTF-8"));
        }


        public static byte[] decodeFile(String key, byte[] fileData)
                throws Exception {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            byte[] bKey = key.getBytes("UTF-16LE");

            SecretKeySpec keySpec = new SecretKeySpec(bKey, "AES");

            IvParameterSpec ivSpec = new IvParameterSpec(bKey);

            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypted = cipher.doFinal(fileData);

            return decrypted;
        }

    }

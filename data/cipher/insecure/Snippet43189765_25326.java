  public class Java {

       private static SecretKey key = null;         
       private static Cipher cipher = null;

       public static void main(String[] args) throws Exception
       {
          String filename = RESOURCES_DIR + "toto.enc";

          byte[] key = Base64.decode("2AxIw+/AzDBj83OILV9GDpOs+izDFJEhD6pve/IPsN9=");
          SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
          cipher = Cipher.getInstance("AES");

          cipher.init(Cipher.DECRYPT_MODE, secretKey);
          byte[] test = Base64.decode(readFile(filename));
          byte[] decryptedBytes = cipher.doFinal(test);
          String decryptedText = new String(decryptedBytes, "UTF8");

          System.out.println("After decryption: " + decryptedText);
       }

        public final static String RESOURCES_DIR = "C:/Users/toto/Desktop/";

        static String readFile(String filename) throws FileNotFoundException, IOException {
            FileReader fr;
            BufferedReader br;

            fr = new FileReader(new File(filename));
            br = new BufferedReader(fr);
            String str;
            String res = "";
            while ((str = br.readLine()) != null) {
                res += str;
            }
            return res;
        }
    }

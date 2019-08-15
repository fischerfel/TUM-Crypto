//imports here
public class Main 

    public static void main(String[] args) {


        try {
            String text = "this text will be encrypted";

            String key = "Bar12345Bar12345";

            //Create key and cipher
            Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES");

            //encrypt text
            cipher.init(Cipher.ENCRYPT_MODE, aesKey);
            byte[] encrypted = cipher.doFinal(text.getBytes());
            write(new String(encrypted));

        } catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
            e.printStackTrace();
        }

    }

    public static void write(String message) {
        BufferedWriter bw = null;
        FileWriter fw = null;

        try {

            String data = message;

            File file = new File(FILENAME);
            if (!file.exists()) {
                file.createNewFile();
            }


            fw = new FileWriter(file.getAbsoluteFile(), true);
            bw = new BufferedWriter(fw);

            bw.write(data);

        } catch (IOException e) {

            e.printStackTrace();

        } finally {

            try {

                if (bw != null)
                    bw.close();

                if (fw != null)
                    fw.close();

            } catch (IOException ex) {

                ex.printStackTrace();

            }
        }
    }

}

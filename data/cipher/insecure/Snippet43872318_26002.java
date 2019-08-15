public class Sifreleme {

    public static void encrypt(){
     try {
            SecretKey key = KeyGenerator.getInstance("DES").generateKey();

            FileOutputStream fosKey = new FileOutputStream("..\\KEY");
            SecretKeyFactory keyfac = SecretKeyFactory.getInstance("DES");
            DESKeySpec keyspec = (DESKeySpec) keyfac.getKeySpec(key, DESKeySpec.class);
            fosKey.write(keyspec.getKey());
            fosKey.close();

            Cipher crypt = Cipher.getInstance("DES");
            crypt.init(Cipher.ENCRYPT_MODE, key);

            FileInputStream fis = new FileInputStream("C:\\Users\\akif\\Desktop\\zilsesi.mp3");
            FileOutputStream fos = new FileOutputStream("C:\\Users\\akif\\Desktop\\sifrelenenzilsesi.mp3");
            byte[] arrayBytes = new byte[8];
            int bytesReads;
            while ((bytesReads = fis.read(arrayBytes)) != -1) {
                fos.write(crypt.doFinal(arrayBytes), 0, bytesReads);
            }
            fis.close();
            fos.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }




public static void decrypt(){
       try {
                FileInputStream fisKey = new FileInputStream("..\\KEY");
                byte[] arrayKey = new byte[fisKey.available()];
                fisKey.read(arrayKey);
                SecretKey key = new SecretKeySpec(arrayKey, "DES");

                Cipher decrypt = Cipher.getInstance("DES");
                decrypt.init(Cipher.DECRYPT_MODE, key);

                FileInputStream fis = new FileInputStream("C:\\Users\\akif\\Desktop\\sifrelenenzilsesi.mp3");
                byte[] encText = new byte[16];
                int bytesReads;
                while ((bytesReads = fis.read(encText)) != -1) {
                    fis.read(decrypt.doFinal(encText), 0, bytesReads);
                }
                fis.close();
                System.out.println(new String(encText));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        public static void main(String []args) throws IOException{
        encrypt();
        decrypt();
        }

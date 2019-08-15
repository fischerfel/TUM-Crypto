    public class decrypt {

        public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IOException {

            try {
                File fileDir = new File("C:/Users/JT/Desktop/key.txt");

                BufferedReader in = new BufferedReader(
                   new InputStreamReader(new FileInputStream(fileDir), "UTF-8"));

                String str;

                while ((str = in.readLine()) != null) {
                    System.out.println(str);
                }
                        in.close();
                } 
                catch (UnsupportedEncodingException e) 
                {
                    System.out.println(e.getMessage());
                } 
                catch (IOException e) 
                {
                    System.out.println(e.getMessage());
                }
                catch (Exception e)
                {
                    System.out.println(e.getMessage());
                }

               byte[] decodedKey = Base64.getDecoder().decode(sb.toString());
    SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES"); 
    SecretKeySpec key = new SecretKeySpec(sb.toString().getBytes(), "Base64");

    Cipher desalgCipher;
    desalgCipher = Cipher.getInstance("AES");
    desalgCipher.init(Cipher.DECRYPT_MODE, key);

    Path path = Paths.get("encrypted.txt");                // path to your file
    try(InputStream is = Files.newInputStream(path);        // get an IS on your file
    CipherInputStream cipherIS = new CipherInputStream(is, desalgCipher);   // wraps stream using cipher
    BufferedReader reader = new BufferedReader(new InputStreamReader(cipherIS));){   // init reader.
        String line;
        while((line = reader.readLine()) != null){
            System.out.println(line);

            }
        }

     }
}

public static void register(){

    try{
        // CREATE KEY AND CIPHER
        byte[] key = username.getBytes();
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(key, "AES");

        // ENCRYPT DATA
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] cipherText = cipher.doFinal(password.getBytes("UTF8"));

        // ENCODE DATA
        String encodedString = new String(Base64.encodeBase64(cipherText), "UTF-8");

        // SAVE VARIABLE
        p = new String(encodedString);

        // PRINT DATA
        System.out.println("PLAINTEXT KEY:      " + username);
        System.out.println("MODIFIED PASSWORD:  " + password);
        System.out.println("ENCRYPTED PASSWORD: " + new String(cipherText));
        System.out.println("ENCODED PASSWORD:   " + encodedString);
        System.out.println("P (ENCODED):        " + p);
        System.out.println("");

        // SAVE TO DISK
        try {
            File file = new File("C://Welcome/License.txt");
            file.getParentFile().mkdirs();
            FileWriter fw = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(fw);

            bw.write(new String(p));
            bw.close();
        }
        catch(FileNotFoundException ex){
            ex.printStackTrace();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        p = "";

        // CREATE A FOLDER FOR FILES
        try {
            File dir = new File("C://IronFortress/Files");
            dir.mkdir();
        }
        catch(Exception e){
            e.printStackTrace();
        }


        // READ DATA FROM DISK
        String fileName = "C:/Welcome/License.txt";
        String line0 = null;

        try {
            FileReader fr = new FileReader(fileName);
            BufferedReader br = new BufferedReader(fr);

            if((line0 = br.readLine()) != null){
                p = (line0);
            }

            br.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // DECODE PASSWORD
        String decodedString = new String(Base64.decodeBase64(p));

        // CREATE KEY AND CIPHER
        key = username.getBytes();
        Cipher cipher2 = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey2 = new SecretKeySpec(key, "AES");

        // DECRYPT PASSWORD
        cipher2.init(Cipher.DECRYPT_MODE, secretKey2);
        byte[] cipherText2 = cipher.doFinal(decodedString.getBytes("UTF8"));

        // PRINT DATA
        System.out.println("P (DECODED):        " + p);
        System.out.println("ENCODED PASSWORD:   " + decodedString);
        System.out.println("DECRYPTED PASSWORD: " + new String(cipherText2));
    }
    catch (Exception e){
        e.printStackTrace();
    }
}

public void register() {
    // ENCRYPT PASSWORD
    try {
        String key = username; // 128bit key (16*8)
        String text = password;

        // CREATE KEY AND CIPHER
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // ENCRYPT THE TEXT
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        System.out.println("Encrypted: " + new String(encrypted));

        // SET VARIABLE FOR SAVING
        p = new String(new String(encrypted));
        System.out.println("p: " + new String(p));
        System.out.println("p: " + password);
    }
    catch (Exception e){
        e.printStackTrace();
    }

    // ENCRYPT QUESTION
    try {
        String key = username; // 128bit key (16*8)
        String text = question;

        // CREATE KEY AND CIPHER
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // ENCRYPT THE TEXT
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        System.out.println("Encrypted: " + new String(encrypted));

        // SET VARIABLE FOR SAVING
        q = new String(new String(encrypted));
        System.out.println("q: " + new String(q));
        System.out.println("q: " + question);
    }
    catch (Exception e){
        e.printStackTrace();
    }

    // ENCRYPT ANSWER
    try {
        String key = username; // 128bit key (16*8)
        String text = answer;

        // CREATE KEY AND CIPHER
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // ENCRYPT THE TEXT
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] encrypted = cipher.doFinal(text.getBytes());
        System.out.println("Encrypted: " + new String(encrypted));

        // SET VARIABLE FOR SAVING
        an = new String(new String(encrypted));
        System.out.println("an: " + new String(an));
        System.out.println("an: " + answer);
    }
    catch (Exception e){
        e.printStackTrace();
    }



    // SAVE DATA IN LICENSE FILE
    try {
        File file = new File("C://Welcome/License.txt");
        file.getParentFile().mkdirs();
        FileWriter fw = new FileWriter(file);
        BufferedWriter bw = new BufferedWriter(fw);

        //bw.write(new String(u));
        //bw.newLine();
        bw.write(new String(p));
        bw.newLine();
        bw.write(new String(q));
        bw.newLine();
        bw.write(new String(an));
        bw.close();
    }
    catch(FileNotFoundException ex){
        ex.printStackTrace();
    }
    catch(IOException ex){
        ex.printStackTrace();
    }

    // CREATE A FOLDER FOR FILES
    try {
        File dir = new File("C://IronFortress/Files");
        dir.mkdir();
    }
    catch(Exception e){
        e.printStackTrace();
    }
}

public void login() {
    // LOAD PASSWORD, QUESTION AND ANSWER HASHES
    String fileName = "C:/Welcome/License.txt";
    String line0 = null;
    String line1 = null;
    String line2 = null;

    try {
        FileReader fr = new FileReader(fileName);
        BufferedReader br = new BufferedReader(fr);

        if((line0 = br.readLine()) != null){
            p = (line0);
        }
        if((line1 = br.readLine()) != null){
            q = (line1);
        }
        if((line2 = br.readLine()) != null){
            an = (line2);
        }

        br.close();
    }
    catch(FileNotFoundException e) {
        e.printStackTrace();
    }
    catch (IOException e) {
        e.printStackTrace();
    }



    // DECRYPT STORED PASSWORD
    try {
        String key = username; // 128bit key (16*8)
        String text = p;

        // CREATE KEY AND CIPHER
        Key aesKey = new SecretKeySpec(key.getBytes(), "AES");
        Cipher cipher = Cipher.getInstance("AES");

        // DECRYPT THE TEXT
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decrypted = cipher.doFinal(p.getBytes());
        System.out.println("Decrypted Pass: " + new String(decrypted));

                // COMPARE VALUES
                if (password.equals(new String(decrypted))){
                //if (tf2.getText().equals(new String(decrypted))){
                    welcome.setVisible(true);
                    bg.setVisible(false);
                    l.setVisible(false);
                    tf1.setVisible(false);
                    tf2.setVisible(false);
                    b3.setVisible(false);
                    b4.setVisible(false);
                }
    }
    catch (Exception e){
        e.printStackTrace();
    }
}

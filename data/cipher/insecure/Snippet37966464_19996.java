try {
    String key = Password.getText();

    if (key.length() < 8 || key.length() > 8 || !key.equals("Password")) {

        JOptionPane.showMessageDialog(null, "Not a valid key");

    } else {

        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);

        AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

        Code.dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

        Code.dcipher.init(Cipher.DECRYPT_MODE, desKey, paramSpec);

        Code.decrypt(new FileInputStream("encrypted.txt"),
                new FileOutputStream("cleartext-reversed.txt"));

        BufferedReader br = new BufferedReader(new FileReader(
                "cleartext-reversed.txt"));
        try {
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                line = br.readLine();
            }
            String decrypted = sb.toString();
            DisplayArea2.setText(decrypted);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE,
                    null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(MainApp.class.getName()).log(
                        Level.SEVERE, null, ex);
            }
        }

    }

} catch (InvalidKeyException | NoSuchAlgorithmException
        | InvalidKeySpecException | NoSuchPaddingException
        | InvalidAlgorithmParameterException | FileNotFoundException ex) {
    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null,
            ex);
}

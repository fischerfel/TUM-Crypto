private void openFile(String filename) {
        // TODO Auto-generated method stub
        String value = "";

        FileInputStream fisw;

        try {
            fisw = openFileInput(filename);
            byte[] input = new byte[fisw.available()];
            while (fisw.read(input) != -1) {
                value += new String(input);

            }
            fisw.close();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
          // Decode the encoded data with RSA public key
         Key publicKey = null;

        byte[] decodedBytes = null;

        try {
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, publicKey);
            decodedBytes = c.doFinal(encodedBytes);
        } catch (Exception e) {
            e.printStackTrace();
    }
        entry.setText(value);


    }

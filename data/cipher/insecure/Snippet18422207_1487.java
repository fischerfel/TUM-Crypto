public void actionPerformed(ActionEvent e) {

    //Handle open button action.
    if (e.getSource() == openButton) {
        int returnVal = fc.showOpenDialog(FileChooserDemo.this);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            try {
                SecretKey key = KeyGenerator.getInstance("DES").generateKey();

                AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

                ecipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

                dcipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

                ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

                dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

                File file = fc.getSelectedFile();

                Socket s = null;
                s = new Socket("localhost", 6880);
                DataOutputStream output = new DataOutputStream(s.getOutputStream());


                encrypt(new FileInputStream(file), output);

                log.append("encrypted " + newline);

                log.append("Sent" + file.getName() + "." + newline);
            } catch (Exception ex) {
                Logger.getLogger(FileChooserDemo.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            log.append("Open command cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());

        //Handle save button action.
    } else if (e.getSource() == saveButton) {
        int returnVal = fc.showSaveDialog(FileChooserDemo.this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fc.getSelectedFile();
            //This is where a real application would save the file.
            log.append("Saving: " + file.getName() + "." + newline);
        } else {
            log.append("Save command cancelled by user." + newline);
        }
        log.setCaretPosition(log.getDocument().getLength());
    }
}

public final ActionListener encAL = new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {

            @Override
            public void run() { // Starting encryt the File:
                File inputfile = new File(input.getText());
                File outputfile = new File(output.getText());
                String algo = alg.getText(); // Alogrythmus (Default AES)

                {
                    boolean enabled = false;
                    input.setEnabled(enabled);
                    inputBrowse.setEnabled(enabled);
                    output.setEnabled(enabled);
                    outputBrowse.setEnabled(enabled);
                    enc.setEnabled(enabled);
                    dec.setEnabled(enabled);
                    keytf.setEnabled(enabled);
                    alg.setEnabled(enabled);
                    procbar.setEnabled(true);
                }

                procbar.setValue(0);

                ArrayList<Byte> inBytes = new ArrayList<Byte>();

                try {
                    InputStream instream = new FileInputStream(inputfile);
                    OutputStream outstream = new FileOutputStream(outputfile);

                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] key = md.digest(keytf.getText().getBytes("UTF-8"));// Get md5Hash of the Key
                    SecretKey secret_key = new SecretKeySpec(key, algo);

                    Cipher cipher = Cipher.getInstance(algo);
                    cipher.init(Cipher.ENCRYPT_MODE, secret_key);

                    byte[] buffer = new byte[1024];
                    int count;
                    long totalcount = 0;

                    int lastProzent = -1;

                    while ((count = instream.read(buffer)) > 0) {

totalcount = totalcount + count;

if(totalcount == inputfile.length()){
byte[] outbytes = cipher.doFinal(buffer);
System.out.println("DoFinal");
outstream.write(outbytes);
}else{
byte[] outbytes = cipher.update(buffer);

outstream.write(outbytes);
}


                        int Prozent = (int) ((totalcount * 100) / inputfile.length());
                        if (Prozent != lastProzent) {
                            lastProzent = Prozent;
                            procbar.setValue(Prozent);
                        }

                    }

                    instream.close();
                    outstream.flush();
                    outstream.close();
                    info.setText("Datei erfolgreich verschlüsselt.");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                {
                    boolean enabled = true;
                    input.setEnabled(enabled);
                    inputBrowse.setEnabled(enabled);
                    output.setEnabled(enabled);
                    outputBrowse.setEnabled(enabled);
                    enc.setEnabled(enabled);
                    dec.setEnabled(enabled);
                    keytf.setEnabled(enabled);
                    alg.setEnabled(enabled);
                    procbar.setEnabled(false);
                }

            }
        }).start();

    }
};

    public final ActionListener decAL = new ActionListener() {

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {

            @Override
            public void run() {// Decrypt file
                File inputfile = new File(input.getText());
                File outputfile = new File(output.getText());
                String algo = alg.getText();

                {
                    boolean enabled = false;
                    input.setEnabled(enabled);
                    inputBrowse.setEnabled(enabled);
                    output.setEnabled(enabled);
                    outputBrowse.setEnabled(enabled);
                    enc.setEnabled(enabled);
                    dec.setEnabled(enabled);
                    keytf.setEnabled(enabled);
                    alg.setEnabled(enabled);
                    procbar.setEnabled(true);
                }

                procbar.setValue(0);

                ArrayList<Byte> inBytes = new ArrayList<Byte>();

                try {
                    InputStream instream = new FileInputStream(inputfile);
                    OutputStream outstream = new FileOutputStream(outputfile);

                    MessageDigest md = MessageDigest.getInstance("MD5");
                    byte[] key = md.digest(keytf.getText().getBytes("UTF-8")); // Get md5Hash of the Key
                    SecretKey secret_key = new SecretKeySpec(key, algo);

                    Cipher cipher = Cipher.getInstance(algo);
                    cipher.init(Cipher.DECRYPT_MODE, secret_key);

                    byte[] buffer = new byte[1024];
                    int count;
                    long totalcount = 0;

                    int lastProzent = -1;

                    while ((count = instream.read(buffer)) > 0) {


totalcount = totalcount + count;
if(totalcount == inputfile.length()){
byte[] outbytes = cipher.doFinal(buffer);
System.out.println("DoFinal");
outstream.write(outbytes);
}else{
byte[] outbytes = cipher.update(buffer);

outstream.write(outbytes);
}


                        int Prozent = (int) ((totalcount * 100) / inputfile.length());
                        if (Prozent != lastProzent) {
                            lastProzent = Prozent;
                            procbar.setValue(Prozent);
                        }

                    }

                    instream.close();
                    outstream.flush();
                    outstream.close();

                } catch (Exception e) {
                    info.setText(e.getClass().getName() + ": " + e.getMessage());
                    e.printStackTrace();
                }
                info.setText("Datei erfolgreich entschlüsselt.");
                {
                    boolean enabled = true;
                    input.setEnabled(enabled);
                    inputBrowse.setEnabled(enabled);
                    output.setEnabled(enabled);
                    outputBrowse.setEnabled(enabled);
                    enc.setEnabled(enabled);
                    dec.setEnabled(enabled);
                    keytf.setEnabled(enabled);
                    alg.setEnabled(enabled);
                    procbar.setEnabled(false);
                }

            }
        }).start();

    }
};

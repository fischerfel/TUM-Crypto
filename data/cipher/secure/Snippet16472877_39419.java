private Cipher cipher;
private ArrayList<ConnectionProfile> connectionProfiles;

public void createCipher() throws Exception{
    cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
}

public void saveProfiles() {
    try {
        if (cipher == null) {createCipher();}
        FileOutputStream fos = openFileOutput("connProfiles.bin", Context.MODE_PRIVATE);
        BufferedOutputStream bos = new BufferedOutputStream(fos);
        CipherOutputStream cos = new CipherOutputStream(bos, cipher);
        ObjectOutputStream oos = new ObjectOutputStream(cos);
        oos.writeObject(connectionProfiles);
        oos.flush();
        oos.close();
    } catch (Exception e) {
        e.printStackTrace();
    }
}

public void readProfiles() {
    try {
        if (cipher == null) {createCipher();}
        FileInputStream fis = openFileInput("connProfiles.bin");
        BufferedInputStream bis = new BufferedInputStream(fis);
        CipherInputStream cis = new CipherInputStream(bis, cipher);
        ObjectInputStream ois = new ObjectInputStream(cis);
        connectionProfiles = (ArrayList<ConnectionProfile>) ois.readObject();
        ois.close();
    } catch (Exception e) {
        e.printStackTrace();
        ;
    }
}

package experiment;

public class Experiment implements Serializable {
    public static void main(String[] args) throws Exception {
        File data = new File("C:\\Users\\Furze\\Desktop\\experiment.dat");   
        // I only execute the following to encrypt the file, which works fine:
        Test test = new Test(new VariableMap<String, String>() {{
            put("Name", "Furze");
        }});   
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(new byte[] {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07}, "Blowfish"));
        SealedObject sealedObject = new SealedObject(test, cipher);
        CipherOutputStream outputStream = new CipherOutputStream(new BufferedOutputStream(new FileOutputStream(data.getPath())), cipher);
        ObjectOutputStream objectOutput = new ObjectOutputStream(outputStream);
        objectOutput.writeObject(sealedObject);     
        objectOutput.close();
        // I then comment out the above code to test the file, which fails.
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(new byte[] {0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07}, "Blowfish"));
        CipherInputStream inputStream = new CipherInputStream(new BufferedInputStream(new FileInputStream(data.getPath())), cipher);
        ObjectInputStream objectInput = new ObjectInputStream(inputStream);
        SealedObject sealedObject = (SealedObject) objectInput.readObject();
        Test test = (Test) sealedObject.getObject(cipher);
        System.out.println(test.variables.get("Name"));
    }
}

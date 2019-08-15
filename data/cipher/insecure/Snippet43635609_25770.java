public class Utilities {

    public static final String FILE_EXTENSION = ".bin";

    public static boolean saveNote(Context context, Notes notes){
        String fileName = String.valueOf(notes.getDateTime()) + FILE_EXTENSION;

        FileOutputStream fos;
        ObjectOutputStream oos;

        try {
            fos = context.openFileOutput(fileName, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(notes);
            oos.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false; //tell the user something went wrong
        }
        return true;
    }

    public static ArrayList<Notes> getSavedNotes(Context context) {
        ArrayList<Notes> notes = new ArrayList<>();

        File filesDir = context.getFilesDir();
        filesDir.getAbsolutePath();
        ArrayList<String> noteFiles = new ArrayList<>();

        for(String file : filesDir.list()) {
            if(file.endsWith(FILE_EXTENSION)) {
                noteFiles.add(file);
            }
        }

        FileInputStream fis;
        ObjectInputStream ois;

        for(int i = 0; i < noteFiles.size(); i++) {
            try{
                fis = context.openFileInput(noteFiles.get(i));
                ois = new ObjectInputStream(fis);

                notes.add((Notes)ois.readObject());

                fis.close();
                ois.close();



            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
                return null;

            }
        }

        return notes;

    }

    public static Notes getNoteByName(Context context, String fileName) {
        File file = new File(context.getFilesDir(), fileName);
        Notes notes;

        if(file.exists()) {
            FileInputStream fis;
            ObjectInputStream ois;

            try {
                fis = context.openFileInput(fileName);
                ois = new ObjectInputStream(fis);

                notes = (Notes) ois.readObject();

                fis.close();
                ois.close();

            } catch(IOException | ClassNotFoundException e){
                e.printStackTrace();
                return null;
            }

            return notes;
        }

        return null;
    }

    public static void deleteNote(Context context, String fileName) {
        File Dir = context.getFilesDir();
        File file = new File(Dir, fileName);

        if (file.exists()) file.delete();
    }

    public static void main(String[] args) {
        try {
            String key = "squirrel123"; // needs to be at least 8 characters for DES

            FileInputStream fis = new FileInputStream("original.txt");
            FileOutputStream fos = new FileOutputStream("encrypted.txt");
            encrypt(key, fis, fos);

            FileInputStream fis2 = new FileInputStream("encrypted.txt");
            FileOutputStream fos2 = new FileOutputStream("decrypted.txt");
            decrypt(key, fis2, fos2);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void encrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.ENCRYPT_MODE, is, os);
    }

    public static void decrypt(String key, InputStream is, OutputStream os) throws Throwable {
        encryptOrDecrypt(key, Cipher.DECRYPT_MODE, is, os);
    }

    public static void encryptOrDecrypt(String key, int mode, InputStream is, OutputStream os) throws Throwable {
        DESKeySpec dks = new DESKeySpec(key.getBytes());
        SecretKeyFactory skf = SecretKeyFactory.getInstance("DES");
        SecretKey desKey = skf.generateSecret(dks);
        Cipher cipher = Cipher.getInstance("DES"); // DES/ECB/PKCS5Padding for SunJCE

        if (mode == Cipher.ENCRYPT_MODE) {
            cipher.init(Cipher.ENCRYPT_MODE, desKey);
            CipherInputStream cis = new CipherInputStream(is, cipher);
            doCopy(cis, os);
        } else if (mode == Cipher.DECRYPT_MODE) {
            cipher.init(Cipher.DECRYPT_MODE, desKey);
            CipherOutputStream cos = new CipherOutputStream(os, cipher);
            doCopy(is, cos);
        }
    }

    public static void doCopy(InputStream is, OutputStream os) throws IOException {
        byte[] bytes = new byte[64];
        int numBytes;
        while ((numBytes = is.read(bytes)) != -1) {
            os.write(bytes, 0, numBytes);
        }
        os.flush();
        os.close();
        is.close();

    }

}

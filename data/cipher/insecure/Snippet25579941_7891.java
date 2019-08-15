package dataBases;


public class BackupHelper {

public static int SECURITY_NONE = 1 ;
public static int SECURITY_ENCRYPTED = 2 ;

public static void backup(Context context, int securityMode) {

    File backup = new File(Environment.getExternalStorageDirectory()+ 
            "/" + "KarAmad" + "/" + "backup");
    backup.mkdirs();

    List<File> src = new ArrayList<File>();
    List<File> dst = new ArrayList<File>();


    try {
        src.add( new File(new PrimaryInformationDataBase(context).getDirectory()) );
        dst.add( new File(backup.getPath() + "/" + "PI") );

        src.add( new File(new TransactionDataBase(context).getDirectory()) );
        dst.add( new File(backup.getPath() + "/" + "T") );

        src.add( new File(new NoteDataBase(context).getDirectory()) );
        dst.add( new File(backup.getPath() + "/" + "N") );

        src.add( new File(new PictureDataBase(context).getDirectory()) );
        dst.add( new File(backup.getPath() + "/" + "P") );

        for(int i = 0 ; i < src.size() ; i ++) {
            dst.get(i).createNewFile();
            if(securityMode == SECURITY_ENCRYPTED)
                BackupHelper.encrypt(src.get(i), dst.get(i));
            else
                LeftFragment.copy(src.get(i), dst.get(i));
        }

        Toast.makeText(context, "پشتیبان گیری انجام شد", Toast.LENGTH_SHORT).show();

    } catch (Exception e) {

        Toast.makeText(context, "پشتیبان گیری انجام نشد!", Toast.LENGTH_SHORT).show();
        e.printStackTrace();

    }
}

public static void restore(Context context, int securityMode) {

    NoteDataBase dummyNoteDataBase = new NoteDataBase(context);
    String temp = dummyNoteDataBase.getDirectory();
    String dataBasesPath = temp.substring(0, temp.lastIndexOf("/"));

    File source = new File(Environment.getExternalStorageDirectory()+ 
            "/" + "KarAmad" + "/" + "backup");

    List<File> src = new ArrayList<File>();
    List<File> dst = new ArrayList<File>();

    try {
        src.add( new File(source.getPath() + "/" + "PI") );
        dst.add( new File(dataBasesPath + "/" + "PrimaryInformation") );

        src.add( new File(source.getPath() + "/" + "T") );
        dst.add( new File(dataBasesPath + "/" + "Transaction") );

        src.add( new File(source.getPath() + "/" + "N") );
        dst.add( new File(dataBasesPath + "/" + "Note") );

        src.add( new File(source.getPath() + "/" + "P") );
        dst.add( new File(dataBasesPath + "/" + "Picture") );

        for(int i = 0 ; i < src.size() ; i++) {

            dst.get(i).createNewFile();

            if(securityMode == SECURITY_ENCRYPTED)
                BackupHelper.decrypt(src.get(i), dst.get(i));
            else
                LeftFragment.copy(src.get(i), dst.get(i));
        }



        Toast.makeText(context, "بازیابی فایل پشتیبان انجام شد", Toast.LENGTH_SHORT).show();

    } catch (Exception e) {

        Toast.makeText(context, "بازیابی فایل پشتیبان انجام نشد!", Toast.LENGTH_SHORT).show();
        e.printStackTrace();

    }
}

public static void encrypt(File src, File dst) throws IOException,
    NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

    FileInputStream inputStream = new FileInputStream(src);
    FileOutputStream outputStream = new FileOutputStream(dst);

    SecretKeySpec keySpec = new SecretKeySpec("1393032613930326".getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, keySpec);

    CipherOutputStream cipherOutputStream = 
            new CipherOutputStream(outputStream, cipher);

    int b;
    byte[] d = new byte[8];
    while((b = inputStream.read(d)) > 0) {
        cipherOutputStream.write(d, 0, b);
    }

    cipherOutputStream.flush();
    cipherOutputStream.close();
    inputStream.close();
}


public static void decrypt(File src, File dst) throws IOException,
NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {

    FileInputStream inputStream = new FileInputStream(src);
    FileOutputStream outputStream = new FileOutputStream(dst);

    SecretKeySpec keySpec = new SecretKeySpec("1393032613930326".getBytes(), "AES");

    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, keySpec);

    CipherInputStream cipherOutputStream = 
            new CipherInputStream(inputStream, cipher);

    int b;
    byte[] d = new byte[8];
    while((b = inputStream.read(d)) > 0) {
        outputStream.write(d, 0, b);
    }

    outputStream.flush();
    outputStream.close();
    cipherOutputStream.close();

}
}

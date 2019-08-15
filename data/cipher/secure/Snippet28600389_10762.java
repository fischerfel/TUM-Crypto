public class FileActivity extends Activity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file);
        encryptPDF();
    }

public void encryptPDF() 
{
    //CipherInputStream input=null;
    /*FileInputStream fis=null ;
    FileOutputStream fos=null;*/
    // get the key

    try {
        final KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(128);
        final SecretKey secretKey = generator.generateKey();

        // perform encryption
        Cipher cipher;
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

    cipher.init(Cipher.ENCRYPT_MODE, secretKey);
    FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/sample.pdf");
    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/pdf_encrypt.enc");
    final CipherOutputStream output = new CipherOutputStream(fos, cipher);

    int bytesRead = 0;
    final byte[] plainText = new byte[4096];
    while ((bytesRead = fis.read(plainText)) >= 0) {
        output.write(plainText, 0, bytesRead);
    }
    output.flush();
    output.close();
    fos.close();
    fis.close();
    final byte[] iv = cipher.getIV();

    // decrypt the file
    cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));
    fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/pdf_encrypt.enc");
    fos = new FileOutputStream(Environment.getExternalStorageDirectory()+"/test.pdf");
    CipherInputStream input = new CipherInputStream(fis, cipher);

    final byte[] decryptedData = new byte[4096];
    int decryptedRead;
    while ((decryptedRead = input.read(decryptedData)) >= 0) {
        fos.write(decryptedData, 0, decryptedRead);
    }

    fos.flush();
    fos.close();
    input.close();
    fis.close();
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
}

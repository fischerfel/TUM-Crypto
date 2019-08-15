public class MainActivity extends Activity{

byte[] key, iv;
protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.activity_main);
  // Get key
  key=getKey();
  // Get IV
  iv=getIV();         
}   

public void encryptFile(View view){

 Bitmap bitmap=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/img.png");
 // Write image data to ByteArrayOutputStream
    ByteArrayOutputStream baos=new ByteArrayOutputStream();
    bitmap.compress(CompressFormat.PNG, 100,baos);
 // Encrypt and save the image                   
 saveFile(encrypt(key,baos.toByteArray()),"enimg.png");
}

public void decryptFile(View view){
    try {
     // Create FileInputStream to read from the encrypted image file
         FileInputStream fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/enimg.png");
         // Save the decrypted image 
         saveFile(decrypt(key, fis),"deimg.png");
    } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
}
public void saveFile(byte[] data, String outFileName){
FileOutputStream fos=null;
try {
    fos=new FileOutputStream(Environment.getExternalStorageDirectory()+File.separator+outFileName);
        fos.write(data);
    } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
finally{
    try {
            fos.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
    }
    }
}
private byte[] encrypt(byte[] skey, byte[] data){
   SecretKeySpec skeySpec = new SecretKeySpec(skey, "AES");
  Cipher cipher;
  byte[] encrypted=null;
    try {
        // Get Cipher instance for AES algorithm
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");    
        // Initialize cipher
        cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        // Encrypt the image byte data
        encrypted = cipher.doFinal(data);
    }catch(Exception e){
        e.printStackTrace();
    }   
  return encrypted;
}

private byte[] decrypt(byte[] skey, FileInputStream fis){

SecretKeySpec skeySpec = new SecretKeySpec(skey, "AES");

  Cipher cipher;

  byte[] decryptedData=null;

  CipherInputStream cis=null;
    try {
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");        
        cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(iv));
        // Create CipherInputStream to read and decrypt the image data
        cis = new CipherInputStream(fis, cipher);
        // Write encrypted image data to ByteArrayOutputStream
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        byte[] data = new byte[2048];
        while ((cis.read(data)) != -1) {
          buffer.write(data);
        }
        buffer.flush();         
        decryptedData=buffer.toByteArray();

        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try {
                fis.close();
                cis.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }               
        }      
  return decryptedData;
}   
private static byte[]  getKey(){
KeyGenerator keyGen;
byte[] dataKey=null;
    try {
        // Generate 256-bit key
        keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256); 
    SecretKey secretKey = keyGen.generateKey();
    dataKey=secretKey.getEncoded();
        } catch (NoSuchAlgorithmException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
return dataKey;     
}   
private static byte[] getIV(){
SecureRandom random = new SecureRandom();
byte[] iv = random.generateSeed(16);
return iv;
} 
}

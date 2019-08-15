import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.crypto.Cipher;
import javax.crypto.CipherOutputStream;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESEncryptionTest {
  public static AESEncryptionTest encryptor;

  public String MODE;
  public Cipher encCipher;

  public int blocks;

  public byte[] IV = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};

  public byte[] key;

  public static AESEncryptionTest getInstance(){
    if(encryptor == null){
      encryptor = new AESEncryptionTest();
      encryptor.init();
    }
    return encryptor;
  }

  public void init(){
   try{
     String alg = "AES/CBC/NoPadding";
     key = convert32ByteHexTo16ByteHex("C145B6D7C8EBD1A15B9FAE5D6DD7FECA");
     System.out.println("IV:" + toHex(IV, false));
     System.out.println("KEY:" + toHex(key, false));
     System.out.println("Algo:" + alg);
     IvParameterSpec encivspec = new IvParameterSpec(IV);  
     SecretKeySpec secretKey = new SecretKeySpec(key, "AES");
     encCipher = Cipher.getInstance(alg);
     encCipher.init(Cipher.ENCRYPT_MODE, secretKey, encivspec);
   } catch (Exception ex) {
     ex.printStackTrace();
   }
 }

 public void resetIV(){
   for(byte b: IV)
     b = 0x00;
 }

 public byte[] encrypt(String msg) throws Exception{  
  try{
    System.out.println("Java ersion:" + System.getProperty("java.version"));
    System.out.println("OS :" + System.getProperty("os.arch"));
    System.out.println("sun.arch.data.model x:" +   System.getProperty("sun.arch.data.model"));

    ByteArrayOutputStream ba= new ByteArrayOutputStream();
    CipherOutputStream cos = new CipherOutputStream(ba, encCipher);
    PrintWriter pw = new PrintWriter(new OutputStreamWriter(cos));
    pw.println(msg);
    pw.close();
    return ba.toByteArray();
  }catch(Exception e){
    e.printStackTrace();
    //throw new EncryptionFailedException(e.getMessage());
    throw e;
  }
 } 

 private byte[] convert32ByteHexTo16ByteHex(String hexStr){
  byte arr[] = new byte[16];
  for(int i=0; i<arr.length; i++){
    arr[i] = (byte)(Integer.parseInt(hexStr.substring(i * 2, (i*2) + 2), 16) & 0xFF);
  }
  return arr;
 }

public String toHex(byte[] arr, boolean withPrefix) {
  int offset = 0;
  int length = arr.length;
  StringBuffer sb = new StringBuffer();

  for (int i = offset; i < length; i++) {
    if (withPrefix) {
      sb.append("0x");
    }

  for (int j = 0; j < 2; j++) {
    byte nibble = (byte) (j == 0 ? arr[i] >> 4 & 0xf : arr[i] & 0xf);

    if (nibble < 10) {
      sb.append(nibble);
    } else {
      sb.append((char) ('A' + (nibble - 10)));
    }
  }

   if (i != length - 1) {
     sb.append(',');
   }
 }

 return sb.toString();
}

public static void main(String args[]){
  AESEncryptionTest util = AESEncryptionTest.getInstance();
  String rec = "DTYCOFIRE201311120001201311151531BER01 600082SBC9131G    Y103H163                                                                                         First line^Second line^Third line                                                                                                                                                                                                                              201311151531TYCOFIRE201311120001_img1.JPG                     TYCOFIRE201311120001_img2.JPG                                                                                                                         1122                          TYCOFIRE                                            \n";
  try {
    System.out.println("rec:" + rec);
    byte b[] = util.encrypt(rec);
    System.out.println("encrypted:" + new String(b));
  } catch (Exception e) {
  e.printStackTrace();
  }
}
}

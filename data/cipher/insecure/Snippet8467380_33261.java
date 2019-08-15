package betterencryption;   

import javax.crypto.*;
import javax.crypto.spec.*;
import java.util.Scanner;

 public class BetterEncryption {

 public static String asHex (byte buf[]) {               //asHex works just fine, it's the main that's
                                                         //giving me trouble
  StringBuffer strbuf = new StringBuffer(buf.length * 2);
  int i;

  for (i = 0; i < buf.length; i++) {
   if (((int) buf[i] & 0xff) < 0x10)
    strbuf.append("0");

   strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
  }

  return strbuf.toString();
 }

 public static void main(String[] args) throws Exception {
   Scanner sc = new Scanner(System.in);
   KeyGenerator kgen = KeyGenerator.getInstance("AES");kgen.init(128); 
   SecretKey skey = kgen.generateKey();
   byte[] bytes = skey.getEncoded();
   SecretKeySpec skeySpec = new SecretKeySpec(bytes, "AES");
   Cipher cipher = Cipher.getInstance("AES");
   System.out.print("Do you want to encrypt or unencrypt?\n");/*This is a weird way of doing it,*/
   String choice = sc.next(); char cc = choice.charAt(2);     /*I know, but this part checks to see if*/       
   if(cc=='c'){                                               /*the program is to encrypt or unencrypt*/
   System.out.print("Enter a string to encrypt: ");          /* a string. The 'encrypt' function works.*/
   String message = sc.next();


   cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
   byte[] encrypted = cipher.doFinal((args.length == 0 ? message : args[0]).getBytes());
   System.out.println("Encrypted string: " + asHex(encrypted)+"\nKey: "+asHex(bytes));

   //^This^ section actually works! The code outputs an encrypted string and everything.
   //It's beautiful
   //Unfortunately getting that string back into readable text has been problematic
   //Which is where you guys come in!
   //Hopefully

 }
  if(true){
   System.out.print("\nEnter the encrypted string: "); String encryptedString = sc.next();
   System.out.print("\nEnter the key: "); String keyString = sc.next();
   int len = encryptedString.length();    /*this section converts the user-input string*/
   byte[] encrypted = new byte[len / 2];  /*into an array of bytes*/
   for (int i = 0; i < len; i += 2) {     /*I'm not sure if it works, though*/
   encrypted[i / 2] = (byte) ((Character.digit(encryptedString.charAt(i), 16) << 4)+
           Character.digit(encryptedString.charAt(i+1), 16));
   cipher.init(Cipher.DECRYPT_MODE, skeySpec); /*as you can see, I haven't even begun to implement*/ 
   byte[] original = cipher.doFinal(encrypted);/*a way to allow the user-input key to be used.*/
   String originalString = new String(original);
   System.out.println("\nOriginal string: "+originalString); //I'm really quite stuck.
      //can you guys help?
  } 

 }
   }
}

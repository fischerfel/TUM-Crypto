public class AES 

{

public byte[] encrypted;

 public byte[] original;

 public String originalString;

public static String asHex (byte buf[]) 

{ 

StringBuffer strbuf = new StringBuffer(buf.length * 2);

 int i; for (i = 0; i < buf.length; i++) 

{

 if (((int) buf[i] & 0xff) < 0x10) strbuf.append("0"); 

strbuf.append(Long.toString((int) buf[i] & 0xff, 16)); 

}

 return strbuf.toString();

 }

 public String AESencryptalgo(byte[] text)

 { 

String newtext=""; 

// Get the KeyGenerator

 try

 {

    KeyGenerator kgen = KeyGenerator.getInstance("AES");

    kgen.init(128); // 192 and 256 bits may not be available

 // Generate the secret key specs. 

SecretKey skey = kgen.generateKey();

 byte[] raw = skey.getEncoded();

 SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

 // Instantiate the cipher Cipher cipher = Cipher.getInstance("AES"); 

cipher.init(Cipher.ENCRYPT_MODE, skeySpec); encrypted = cipher.doFinal(text); 

System.out.println("encrypted string: " + asHex(encrypted)); 

cipher.init(Cipher.DECRYPT_MODE, skeySpec); original = cipher.doFinal(encrypted); 

originalString = new String(original); System.out.println("Original string: " + originalString + " " + asHex(original));

 } 

catch(Exception e)

 { } 

finally 

{

 newtext=new String(encrypted);

 System.out.println("ENCRYPTED "+newtext);

//AESdecryptalgo(newtext.getBytes()); 

return newtext;

 }

 } 

public String AESdecryptalgo(byte[] text)

 { 

// Get the KeyGenerator

 try

 {

 KeyGenerator kgen = KeyGenerator.getInstance("AES");

 kgen.init(128); // 192 and 256 bits may not be available 

// Generate the secret key specs. 

SecretKey skey = kgen.generateKey();

 byte[] raw = skey.getEncoded(); 

SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES"); 

// Instantiate the cipher

 Cipher cipher = Cipher.getInstance("AES"); 

cipher.init(Cipher.DECRYPT_MODE, skeySpec);

 original = cipher.doFinal(text); //Exception occurs here

 originalString = new String(original);

 System.out.println("Original string: " + originalString + " " + asHex(original)); 

}

 catch(Exception e)

 {

 System.out.println("exception"); 

}

 finally

{ 

System.out.println("DECRYPTED "+originalString);

 return originalString;

 } 

} 

public static void main(String[] args)

{

AES a=new AES();

a.AESencryptalgo("hello".getBytes());

System.out.println(); 

}} 
`

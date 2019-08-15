    // Reads password from config file
String password = ScriptConfig.getString( "password" );

// Generate Key
KeyGenerator kg = KeyGenerator.getInstance("DES");
Key key = kg.generateKey();

// Create Encryption cipher
Cipher cipher = Cipher.getInstance( "DES" );
cipher.init( Cipher.ENCRYPT_MODE, key );

// Encrypt password
byte[] encrypted = cipher.doFinal( password.getBytes() );

// Create decryption cipher
cipher.init( Cipher.DECRYPT_MODE, key );
byte[] decrypted = cipher.doFinal( encrypted );

// Convert byte[] to String
String decryptedString = new String(decrypted);

System.out.println("password: " + password);
System.out.println("encrypted: " + encrypted);
System.out.println("decrypted: " + decryptedString);

// Read encrypted string from config file
String encryptedPassword = ScriptConfig.getString( "encryptedPassword"
);

// Convert encryptedPassword string into byte[]
byte[] encryptedPasswordBytes = new byte[1024];
encryptedPasswordBytes = encryptedPassword.getBytes();

// Decrypt encrypted password from config file
byte[] decryptedPassword = cipher.doFinal( encryptedPasswordBytes );//error here

System.out.println("encryptedPassword: " + encryptedPassword);
System.out.println("decryptedPassword: " + decryptedPassword);


The config file has the following variables:
password=password
encryptedPassword=[B@2a4983


When I run the code, I get the following output:
password: passwd
encrypted: [B@2a4983
decrypted: passwd
javax.crypto.IllegalBlockSizeException: Input length must be multiple
of 8 when decrypting with padded cipher
at com.sun.crypto.provider.SunJCE_h.b(DashoA12275)
at com.sun.crypto.provider.SunJCE_h.b(DashoA12275)
at com.sun.crypto.provider.DESCipher.engineDoFinal(Da shoA12275)
at javax.crypto.Cipher.doFinal(DashoA12275)
at com.sapient.fbi.uid.TestEncryption.main(TestEncryp tion.java:4

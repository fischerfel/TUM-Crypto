KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
kpg.initialize(512);
KeyPair rsaKeyPair = kpg.genKeyPair();
byte[] txt = "This is a secret message.".getBytes();
System.out.println("Original clear message: " + new String(txt));

// encrypt
Cipher cipher;
try
{
    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.ENCRYPT_MODE, rsaKeyPair.getPublic());
    txt = cipher.doFinal(txt);
}
catch (Throwable e)
{
    e.printStackTrace();
    return;
}
System.out.println("Encrypted message: " + new String(txt));

// decrypt
try
{
    cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
    cipher.init(Cipher.DECRYPT_MODE, rsaKeyPair.getPrivate());
    txt = cipher.doFinal(txt);
}
catch (Throwable e)
{
    e.printStackTrace();
    return;
}
System.out.println("Decrypted message: " + new String(txt));

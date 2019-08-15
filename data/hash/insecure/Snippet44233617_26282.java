package com.google.firebase.david.encryptochat; //firebase API


import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

//**************************************
//THis class is our message object
//*****************************************
public class EncryptoMessage {

private String text;
private String name;
private String photoUrl;

public EncryptoMessage() {

    // Default constructor required for calls to 
    //DataSnapshot.getValue(User.class)
  }

// The encrypto message constructor to create the object
public EncryptoMessage(String text, String name, String photoUrl) {
    this.text = text;
    this.name = name;
    this.photoUrl = photoUrl;
}
  //Copy constuctor

  /* public EncryptoMessage(EncryptoMessage EncryptoMessageCopy){
    this(EncryptoMessageCopy.getText(), EncryptoMessageCopy.getName());
}*/
public String getText() {
    return text;
}

public void setText(String text) {
    this.text = text;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getPhotoUrl() {
    return photoUrl;
}

public void setPhotoUrl(String photoUrl) {
    this.photoUrl = photoUrl;
}


   //Encryption of the messages

   public byte[] encrypt(String message) throws Exception {
        final MessageDigest md = MessageDigest.getInstance("md5");

        final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                .getBytes("utf-8"));

        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);

        final Cipher cipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, iv);

        final byte[] plainTextBytes = message.getBytes("utf-8");
        final byte[] cipherText = cipher.doFinal(plainTextBytes);


        return cipherText;
       }
       //Decrypt

      public String decrypt(byte[] message) throws Exception {

        //get the bytes if the string passing in
        //byte [] bytes = message.getBytes();

        final MessageDigest md = MessageDigest.getInstance("md5");
        final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
                .getBytes("utf-8"));
        final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
        for (int j = 0, k = 16; j < 8;) {
            keyBytes[k++] = keyBytes[j++];
        }

        final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
        final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
        final Cipher decipher = 
        Cipher.getInstance("DESede/CBC/PKCS5Padding");
        decipher.init(Cipher.DECRYPT_MODE, key, iv);

        final byte[] plainText = decipher.doFinal(message);

        return new String(plainText, "UTF-8");
    }

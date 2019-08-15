import java.awt.*;

import javax.swing.*;
import java.awt.event.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;
import java.io.*;


public class EncryptURL extends JApplet implements ActionListener {
  Container content;
  JTextField userName = new JTextField();
     JTextField firstName = new JTextField();
      JTextField lastName = new JTextField();
      JTextField email = new JTextField();
      JTextField phone = new JTextField();
      JTextField heartbeatID = new JTextField();
      JTextField regionCode = new JTextField();
      JTextField retRegionCode = new JTextField();
      JTextField encryptedTextField = new JTextField();

    JPanel finishPanel = new JPanel();


  public void init() {
    //setTitle("Book - E Project");
    setSize(800,600);
    content = getContentPane();
    content.setBackground(Color.yellow);
    content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));


    JButton submit = new JButton("Submit");

    content.add(new JLabel("User Name"));
    content.add(userName);

    content.add(new JLabel("First Name"));
    content.add(firstName);

    content.add(new JLabel("Last Name"));
    content.add(lastName);

    content.add(new JLabel("Email"));
    content.add(email);

    content.add(new JLabel("Phone"));
    content.add(phone);

    content.add(new JLabel("HeartBeatID"));
    content.add(heartbeatID);

    content.add(new JLabel("Region Code"));
    content.add(regionCode);

    content.add(new JLabel("RetRegionCode"));
    content.add(retRegionCode);

    content.add(submit);

    submit.addActionListener(this);


  }

  public void actionPerformed(ActionEvent e) {
    if(e.getActionCommand() == "Submit"){

        String subUserName = userName.getText();
        String subFName = firstName.getText();
        String subLName = lastName.getText();
        String subEmail = email.getText();
        String subPhone = phone.getText();
        String subHeartbeatID = heartbeatID.getText();
        String subRegionCode = regionCode.getText();
        String subRetRegionCode = retRegionCode.getText();

        String concatURL = "user="+ subUserName + "&f="+ subFName + "&l=" +subLName+ "&em=" + subEmail + "&p="+subPhone+"&h="+subHeartbeatID+"&re="+subRegionCode+ "&ret=" + subRetRegionCode;
        concatURL = padString(concatURL, ' ', 16);
        byte[] encrypted = encrypt(concatURL);
        String encryptedString = bytesToHex(encrypted);
        content.removeAll();
        content.add(new JLabel("Concatenated User Input -->" + concatURL));

        content.add(encryptedTextField);
        setContentPane(content);
    }
  }

  public static byte[] encrypt(String toEncrypt) throws Exception{
    try{
      String plaintext = toEncrypt;
      String key = "01234567890abcde";
      String iv = "fedcba9876543210";

      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      cipher.init(Cipher.ENCRYPT_MODE,keyspec,ivspec);
      byte[] encrypted = cipher.doFinal(toEncrypt.getBytes());

      return encrypted;
    }
    catch(Exception e){

    }

  }

  public static byte[] decrypt(byte[] toDecrypt) throws Exception{
      String key = "01234567890abcde";
      String iv = "fedcba9876543210";

      SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
      IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

      Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
      cipher.init(Cipher.DECRYPT_MODE,keyspec,ivspec);
      byte[] decrypted = cipher.doFinal(toDecrypt);

      return decrypted;
  }
  public static String bytesToHex(byte[] data) {
    if (data==null)
    {
      return null;
    }
    else
    {
      int len = data.length;
      String str = "";
      for (int i=0; i<len; i++)
      {
        if ((data[i]&0xFF)<16)
          str = str + "0" + java.lang.Integer.toHexString(data[i]&0xFF);
        else
          str = str + java.lang.Integer.toHexString(data[i]&0xFF);
      }
      return str;
    }
  }
  public static String padString(String source, char paddingChar, int size)
  {
    int padLength = size-source.length()%size;
    for (int i = 0; i < padLength; i++) {
      source += paddingChar;
    }
    return source;
  }
}

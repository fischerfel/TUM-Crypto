public void decrypt() throws Exception
{
    //opening streams
    //Error is in the line below When i try to read file from directory
    //other than the one which has .java and .class files.
    FileInputStream fis1 =new FileInputStream(file);
    File dir=new File("C:/Crypt-R/Decrypted");
    dir.mkdirs();
    file=new File(dir,file.getName() +".dec");
    FileOutputStream fos1 =new FileOutputStream(file);  
    //generating same key
    byte k[] = keyRecv.getBytes();   
    SecretKeySpec key = new SecretKeySpec(k,"AES");  
    //creating and initialising cipher and cipher streams
    Cipher decrypt =  Cipher.getInstance(algorithm);  
    decrypt.init(Cipher.DECRYPT_MODE, key);  
    CipherInputStream cin=new CipherInputStream(fis1, decrypt);
    byte[] buf = new byte[1024];
    int read=0;
    while((read=cin.read(buf))!=-1)  //reading encrypted data from file
    {
    fos1.write(buf,0,read);       //writing decrypted data to file
    }
    //closing streams
    cin.close();
    fos1.flush();
    fos1.close();
    JOptionPane.showMessageDialog (null,
    "File Decrypted",
    "Success..!!",
    JOptionPane.INFORMATION_MESSAGE);
}

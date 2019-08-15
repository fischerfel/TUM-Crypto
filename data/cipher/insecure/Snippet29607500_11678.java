//Starting decryption
try{
    byte[] key = c_key.getBytes("ISO-8859-1");
    key = Arrays.copyOf(key, 16); // use only first 128 bit
    //System.out.println(Arrays.toString(key));
    SecretKeySpec SecKey = new SecretKeySpec(key, "AES");
    Cipher AesCipher = Cipher.getInstance("AES");
    AesCipher.init(Cipher.DECRYPT_MODE, SecKey);

    BufferedReader breader = new BufferedReader(new FileReader("download/enc_"+file));
    String line;
    boolean bool = false;
    File f = new File(file);
    bool = f.createNewFile();
    if(bool==false){
        f.delete();
        bool = f.createNewFile();
    }
    PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
    while ((line = breader.readLine()) != null){
         byte[] cipher=null;
         byte[] plain=null;

         cipher=line.getBytes("ISO-8859-1");
         plain=AesCipher.doFinal(cipher);

         out.println(new String(plain,"ISO-8859-1"));
    }
    out.close();
    return 1;
}
catch(Exception dcf){
    System.out.println("Message:"+dcf.getMessage());
    dcf.printStackTrace();
    return 0;
}

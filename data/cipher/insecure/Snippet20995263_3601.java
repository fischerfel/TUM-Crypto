File f=new File("C:/Users/User/Desktop/Test.txt");
int ch;

StringBuffer strContent = new StringBuffer("");
FileInputStream fin = null;
try {
    fin = new FileInputStream(f);
    while ((ch = fin.read()) != -1)
        strContent.append((char) ch);
    fin.close();
} 
catch (Exception e) {
    System.out.println(e);
}

KeyGenerator kgen = KeyGenerator.getInstance("AES");
kgen.init(128);

SecretKey skey = kgen.generateKey();
byte[] raw = skey.getEncoded();

SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

Cipher cipher = Cipher.getInstance("AES");

cipher.init(Cipher.DECRYPT_MODE, skeySpec);
byte[] original =cipher.doFinal(strContent.toString().getBytes());

String originalString = new String(original);
JOptionPane.showMessageDialog(null,originalString.toString());

public class myDesCbc2 {

    public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {

JFrame frame = null;
JFileChooser fChoose = new JFileChooser(System.getProperty("user.home"));
int returnVal = fChoose.showOpenDialog(frame);
File myFile = fChoose.getSelectedFile();

//Read file and store to String line
FileInputStream fis = new FileInputStream(myFile);
BufferedReader stream = new BufferedReader(new InputStreamReader(fis, "ISO-8859-1"));
String file;
while ((file = stream.readLine()) != null) {

    JOptionPane.showOptionDialog(
            null, "Generating a 56-bit DES key...", "Processing...", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);
    // Create an 8-byte initialization vector
    SecureRandom sr = new SecureRandom();
    byte[] iv = new byte[8];
    sr.nextBytes(iv);
    IvParameterSpec IV = new IvParameterSpec(iv);

    // Create a 56-bit DES key
    KeyGenerator kg = KeyGenerator.getInstance("DES");

    // Initialize with keysize
    kg.init(56);
    Key mykey = kg.generateKey();

    JOptionPane.showOptionDialog(
            null, "Your key has been generated!", "Processing...", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, new Object[]{}, null);

    // Create a cipher object and use the generated key to initialize it
    Cipher cipher = Cipher.getInstance("DES/CBC/PKCS5Padding");

    cipher.init(Cipher.ENCRYPT_MODE, mykey, IV);

    byte[] plaintext = file.getBytes("UTF8");

    // Encrypt the text
    byte[] ciphertext = cipher.doFinal(plaintext);

   JOptionPane.showMessageDialog(null,"\n\nCiphertext: ");
    for (int i = 0; i < ciphertext.length; i++) {

        if (chkEight(i)) {
            System.out.print("\n");
        }
        JOptionPane.showMessageDialog(null,ciphertext[i] + " ");
    }
}
}
}

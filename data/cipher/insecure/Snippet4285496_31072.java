public class TestApplet extends Applet {
 Label lblKey = new Label("Key");
 TextField inputLineKey = new TextField(15);
 Label lblString = new Label("Value");
 TextField inputLineString = new TextField(15);
 Label lblStringEncoded = new Label("Encoded Value");
 TextField inputLineStringEncoded = new TextField(15);
 Label lblStringDecoded = new Label("Decoded Value");
 TextField inputLineStringDecoded = new TextField(15);
 Button encodeButton = new Button("Test Encrypt");
 Button decodeButton = new Button("Test Decrypt");

 public TestApplet() {
  add(inputLineKey);
  add(lblKey);
  add(inputLineString);
  add(lblString);
  add(inputLineStringEncoded);
  add(lblStringEncoded);
  add(inputLineStringDecoded);
  add(lblStringDecoded);
  add(encodeButton);
  add(decodeButton);
  // inputLine.addActionListener(new MyActionListener());
 }

 /**
  * Turns array of bytes into string
  * 
  * @param buf
  *            Array of bytes to convert to hex string
  * @return Generated hex string
  */
 public static String asHex(byte buf[]) {
  StringBuffer strbuf = new StringBuffer(buf.length * 2);
  int i;

  for (i = 0; i < buf.length; i++) {
   if (((int) buf[i] & 0xff) < 0x10)
    strbuf.append("0");

   strbuf.append(Long.toString((int) buf[i] & 0xff, 16));
  }

  return strbuf.toString();
 }

 public boolean action(Event e, Object args) {

  // so do something!

  // ///////////////////////
  try {
   String message = "This is just an example";

   // Get the KeyGenerator

   KeyGenerator kgen = KeyGenerator.getInstance("AES");
   kgen.init(128); // 192 and 256 bits may not be available

   // Generate the secret key specs.
   SecretKey skey = kgen.generateKey();
   byte[] raw = skey.getEncoded();

   SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");

   // Instantiate the cipher

   Cipher cipher = Cipher.getInstance("AES");

   if (e.target == encodeButton) { // User has clicked on encrypt
           // button
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

    byte[] encrypted = cipher.doFinal((inputLineString.getText()
      .length() == 0 ? message : inputLineString.getText())
      .getBytes());
    // System.out.println("encrypted string: " + asHex(encrypted));

    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    byte[] original = cipher.doFinal(encrypted);
    String originalString = new String(original);
    // System.out.println("Original string: " +
    // originalString + " " + asHex(original));

    // Create a BigInteger using the byte array
    BigInteger bi = new BigInteger(encrypted);

    inputLineStringEncoded.setText(bi.toString(2)); // (new String(encrypted));
    inputLineStringDecoded.setText(originalString);
   }

   if (e.target == decodeButton) { // User has clicked on decrypt
           // button
   // cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
   //
   // byte[] encrypted = cipher.doFinal((inputLineString.getText()
   // .length() == 0 ? message : inputLineString.getText())
   // .getBytes());
   // // System.out.println("encrypted string: " + asHex(encrypted));

    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    // Parse binary string
    BigInteger bi = new BigInteger(inputLineStringEncoded
      .getText(), 2);

    byte[] original = cipher.doFinal(bi.toByteArray());
    String originalString = new String(original);
    // System.out.println("Original string: " +
    // originalString + " " + asHex(original));
    inputLineString.setText(originalString);
    inputLineStringDecoded.setText(originalString);
   }

  } catch (Exception exc) {
   inputLineStringEncoded.setText(exc.getMessage());
  }
  return true; // Yes, we do need this!
 }

 class MyActionListener implements ActionListener {
  public void actionPerformed(ActionEvent event) {
  }
 }
}
________________________________

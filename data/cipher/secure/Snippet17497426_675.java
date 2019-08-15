StringBuilder texto1 = new StringBuilder("LALALLA");
byte[] x = texto1.toString().getBytes();
try {
  Cipher cifrado = Cipher.getInstance("RSA/ECB/PKCS1Padding");
  cifrado.init(Cipher.ENCRYPT_MODE, key1.getPublic());
  x = cifrado.doFinal(x);
  String texto;
  texto = new String(x, "UTF-8");
  JOptionPane.showInputDialog(publicKey.toString());
  String teste = "";
  for (int i = 0; i < x.length; i++) {
    teste += x[i];
  }
  jTextPane1.setText(teste);
  //cifrado.init(Cipher.DECRYPT_MODE, privatekey);
  byte[] y;
  // x= texto.getBytes();
  //y = cifrado.doFinal(texto.getBytes());
  //texto = new String(y,"UTF-8");
  jTextPane2.setText(x.toString());
} ...

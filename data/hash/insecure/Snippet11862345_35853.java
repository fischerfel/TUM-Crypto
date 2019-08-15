public static void main(String[] args) throws NoSuchAlgorithmException, IOException { 

  JFileChooser chooser=new  JFileChooser();
  int returnVal = chooser.showOpenDialog(null);
  if (returnVal == JFileChooser.APPROVE_OPTION) {
    File f = chooser.getSelectedFile();
  }
  FileInputStream fin = new FileInputStream(chooser.getSelectedFile());
  DataInputStream din = new DataInputStream(fin);    
  BufferedReader br = new BufferedReader(new InputStreamReader(din)); 

  ArrayList<String> list = new ArrayList<String> ();
  MessageDigest md = MessageDigest.getInstance("MD5");

  String currentLine;
  byte[] buf = new byte[8192];

  int len = 0;
  while ((currentLine = br.readLine()) != null) {
    list.add(currentLine);
    md.update(buf, 0, len);
    System.out.println(currentLine);
  }
  br.close();

  byte[] bytes = md.digest();

  StringBuilder sb = new StringBuilder(2 * bytes.length);
  for (byte b : bytes) {
    sb.append("0123456789ABCDEF".charAt((b & 0xF0) >> 4));
    sb.append("0123456789ABCDEF".charAt((b & 0x0F)));
  }
  String hex = sb.toString();

  System.out.println (buf);
  System.out.println(sb);
}

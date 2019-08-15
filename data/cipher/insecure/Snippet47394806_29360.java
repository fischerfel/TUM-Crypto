public static void main(String[] args) throws Exception {
  convertToHex(System.out, new File("E:\\TESTS\\kristersss.txt"));
  //write the output into a file
  convertToHex(new PrintStream("E:\\TESTS\\kristersssHex.txt"), new File("E:\\TESTS\\kristersss.txt"));

  System.out.println( "  " ); 
  System.out.println( "128-bit hex key example: ffffffffffffffffffffffffffffffff" );
  String content = new Scanner(new File("E:\\TESTS\\kristersssHex.txt")).useDelimiter("\\Z").next();
  System.out.println("----------FAILA SATURS----------");


  System.out.println(content);

  System.out.println("------------------------------");    
  Scanner scanner = new Scanner( System.in );

  System.out.println( "Enter 128-bit hex key: " );
  final String keyHex = scanner.nextLine();

  final String plaintextHex = "aaaaaaaaaabbbbbbbbbbbccccccccccff";
  SecretKey key = new SecretKeySpec(DatatypeConverter
      .parseHexBinary(keyHex), "AES");

  Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
  cipher.init(Cipher.ENCRYPT_MODE, key);

  byte[] result = cipher.doFinal(DatatypeConverter
      .parseHexBinary(content));

  String a = DatatypeConverter.printHexBinary(result);
  System.out.println("----------FAILA SATURA ENKRIPTACIJA----------");
  System.out.println(DatatypeConverter.printHexBinary(result));
  BufferedWriter output = null;
  try {

    File file = new File("E:\\TESTS\\kristerssEncrypts.txt");
    output = new BufferedWriter(new FileWriter(file));
    output.write(a);
  } catch ( IOException e ) {
    e.printStackTrace();
  } finally {
    if ( output != null ) {
      output.close();
    }
  }

  Cipher cipherd = Cipher.getInstance("AES/ECB/NoPadding");
  cipherd.init(Cipher.DECRYPT_MODE, key);

  byte[] result2 = cipherd.doFinal(result);
  System.out.println("----------FAILA SATURA DEKRIPTACIJA----------");
  System.out.println(DatatypeConverter.printHexBinary(result2));
  BufferedWriter outputt = null;
  String aa = DatatypeConverter.printHexBinary(result2);
  try {

    File file = new File("E:\\TESTS\\kristerssDekrypts.txt");
    outputt = new BufferedWriter(new FileWriter(file));
    outputt.write(aa);
  } catch ( IOException e ) {
    e.printStackTrace();
  } finally {
    if ( outputt != null ) {
      outputt.close();
    }
  }

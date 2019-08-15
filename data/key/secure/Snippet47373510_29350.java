public static void main(String[] args) throws Exception {
System.out.println( "128-bit hex key example: ffffffffffffffffffffffffffffffff" );

try(BufferedReader br = new BufferedReader(new FileReader("E:\\TESTS\\tests.txt"))) {
    StringBuilder sb = new StringBuilder();
    String line = br.readLine();

    while (line != null) {
        sb.append(line);
        sb.append(System.lineSeparator());
        line = br.readLine();
    }
    String everything = sb.toString();


Scanner scanner = new Scanner( System.in );
 System.out.println( "Enter 128-bit hex key: " );
 final String keyHex = scanner.nextLine();

 final String plaintextHex = "aaaaaaaaaabbbbbbbbbbccccccccccff";

SecretKey key = new SecretKeySpec(DatatypeConverter
    .parseHexBinary(keyHex), "AES");
System.out.println(everything);
Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
cipher.init(Cipher.ENCRYPT_MODE, key);

byte[] result = cipher.doFinal(DatatypeConverter
    .parseHexBinary(everything));

System.out.println(DatatypeConverter.printHexBinary(result));

Cipher cipherd = Cipher.getInstance("AES/ECB/NoPadding");
cipherd.init(Cipher.DECRYPT_MODE, key);

byte[] result2 = cipherd.doFinal(result);
System.out.println(DatatypeConverter.printHexBinary(result2));

}}

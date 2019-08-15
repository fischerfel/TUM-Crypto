KeyGenerator keyGenerator = KeyGenerator.getInstance("Blowfish");
SecretKey secretKey = keyGenerator.generateKey();
Cipher cipher = Cipher.getInstance("Blowfish"); 
cipher.init(Cipher.ENCRYPT_MODE, secretKey);
String input = "password";
byte encrypted[] = cipher.doFinal(input.getBytes());

StringBuilder str = new StringBuilder();

for(byte b:encrypted){
     str.append(String.format("%02x", b));
}

String encData = str.toString();
System.out.println(encData);

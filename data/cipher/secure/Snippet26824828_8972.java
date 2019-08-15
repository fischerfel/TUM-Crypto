byte[] ciphertext;
byte[] thekey = new byte[16];
new Random().nextBytes(thekey);
byte[] vector = new byte[16];
new Random().nextBytes(vector);
String s = "c6be25d903159d680d81f3d99bb702451e9f7158";
byte[] data = s.getBytes();
Cipher enc = Cipher.getInstance("AES/CBC/PKCS5Padding");           
enc.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(thekey, "AES"), 
        new IvParameterSpec(vector));
ciphertext = enc.doFinal(data);


/* Sample Output*/
StringBuffer testvec = new StringBuffer();
StringBuffer test = new StringBuffer();
StringBuffer testkey = new StringBuffer();
for (byte b:vector){
testvec.append(String.format("%02x", b));
                                }
System.out.println("Vector:"  + " " +testvec.toString());
for (byte b:ciphertext){
test.append(String.format("%02x", b));
                                }

System.out.println(" Cipher:"+ " " + test.toString());


for (byte b:thekey){
testkey.append(String.format("%02x", b));
                                }

System.out.println("theKey:"+ " " + testkey.toString());

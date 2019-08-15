KeyGenerator keyGen = KeyGenerator.getInstance("AES");
keyGen.init(256);
byte[] encoded1 = keyGen.generateKey().getEncoded();
SecretKeySpec spec = new SecretKeySpec(encoded1, "AES");
byte[] encoded2 = spec.getEncoded();
for( short s = 0; s < encoded1.length; s++) {
    if(encoded1[s] == encoded2[s])  {
        System.out.println("ERROR: different keys");
        break;
    }
}

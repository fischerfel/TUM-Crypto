hash = MessageDigest.getInstance("SHA-1");
...
byte[] digest = hash.digest();
for(int j = 0; j < numbytes; j++) {
    res[j+ek.mother.getPCS()]=(byte) (input[j]^digest[j]);
}

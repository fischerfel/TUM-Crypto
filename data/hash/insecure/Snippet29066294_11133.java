MessageDigest m1 = MessageDigest.getInstance("MD5");

m1.update(bFile);

byte [] digest1 = m1.digest();

for(int i=0; i < digest1.length ; i++){
    System.out.println("b["+i+"]="+digest1[i]);
}

BigInteger bi = new BigInteger(digest1);

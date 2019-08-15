// salt
java.security.SecureRandom rgen = new SecureRandom();
byte[] salt = rgen.generateSeed(20);
// add Bouncy Castle
java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
// aes secret key
javax.crypto.KeyGenerator kgen = KeyGenerator.getInstance("AES", "BC");
Key cleSecrete = kgen.generateKey();
// aes
javax.crypto.Cipher cipher = Cipher.getInstance("AES", "BC");
// sha-256
java.security.MessageDigest sha256 = MessageDigest.getInstance("SHA-256","BC");

// hash the clear password with the salt to avoid collisions
byte[] motDePasseHash = hasherSaler(motDePasseClair.getBytes("UTF-8"),salt);
// Encrypt the hash with the salt to get the salt back
byte[] chiffreBDD = chiffrerSalerHash(salt,motDePasseHash,cleSecrete );
// Store the cipher in DB
...

// Get back the hash and the salt from DB
byte[] deChiffreBDD = deChiffrer(chiffreBDD,cleSecrete );
byte[] saltBDD = extraireOctets(deChiffreBDD,0,19);
byte[] hashBDD = extraireOctets(deChiffreBDD,20,deChiffreBDD.length-1);
// hash the user intput
byte[] motDePasseHashCandidat = hasherSaler(motDePasseClairCandidat.getBytes("UTF-8"),saltBDD);
// Compare hased user input with DB hash 
boolean isMotDePasseOK = Arrays.equals(hashBDD,motDePasseHashCandidat);

private final byte[] hasherSaler(byte[] clair,byte[] salt) {
    byte[] concat = concatenerOctets(clair,salt);
    return sha256.digest(concat);
}
private final byte[] chiffrerSalerHash(byte[] salt,byte[] hash, Key cle) {
    cipher.init(true,cle);
    return cipher.doFinal(concatenerOctets(salt,hash));
}
private final byte[] deChiffrer(byte[] chiffre, Key cle) {
    cipher.init(false,cle);
    return cipher.doFinal(chiffre);
}

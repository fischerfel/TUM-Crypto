KeyStore store = ... ;
byte[] target = ... ; // Base-64 decode your string.
MessageDigest digest = MessageDigest.getInstance(algorithm);
Enumeration<String> aliases = store.aliases();
while(aliases.hasMoreElements()) {
  String alias = aliases.nextElement();
  Certificate c = store.getCertificate(alias);
  if (c == null)
    continue;
  PublicKey pub = c.getPublicKey();
  byte[] hash = digest.digest(pub.getEncoded());
  if (MessageDigest.isEqual(hash, target)) {
    // Certificate "c" is a match.
  }
}

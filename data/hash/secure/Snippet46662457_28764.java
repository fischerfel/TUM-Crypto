BufferedInputStream bis = new BufferedInputStream(sap.getRangeStream());
MessageDigest digest = MessageDigest.getInstance("SHA-256");
DigestInputStream dis = new DigestInputStream(bis, digest);
byte[] buff = new byte[512];
while (dis.read(buff) != -1) {
  ;
}
dis.close();
dis = null;
byte[] hashToSign= digest.digest();
bis.close();

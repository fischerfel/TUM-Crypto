// code example includes MessageDigest for the sake of completeness
byte[] input = ... // the raw data
MessageDigest md = MessageDigest.getInstance("SHA-512");
md.update(input);
byte[] hash = md.digest();

// Taken from RFC 3447, page 42 for SHA-512, create input for signing
byte[] modifierBytes = { 0x30, 0x51, 0x30, 0x0d, 0x06, 0x09, 0x60, (byte) 0x86, 0x48, 0x01, 0x65, 0x03, 0x04, 0x02, 0x03, 0x05, 0x00, 0x04, 0x40 };
ByteArrayOutputStream baos = new ByteArrayOutputStream();
baos.write(modifierBytes);
baos2.write(hash);

// create signature
Signature s = Signature.getInstance("NONEwithRSA");
s.initSign(MyTlsCredentials.THE_CLIENT_KEY);
s.update(baos.toByteArray());
byte[] signature = s.sign();

// add prefix as specified in RFC 3447, im my case it had always been the shown values
// but I have not understand the RFC completely in this point as the code seems to be
// contradictious to the interpretation of the byte values for the hash function from page 42.
ByteArrayOutputStream baos2 = new ByteArrayOutputStream();
baos2.write(new byte[] { 1, 0 });
baos2.write(signature5);

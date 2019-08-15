//username: "jack"
//password: "pass"
//userSalt: "6s6Q5Rn0xZP0LPf89bNdv+65EmMUrTsey2fIhim/wKU="
//nonce:    "3da210bdb1163d0d41d3c516314cbd6e"
//hmac:     "IjJOApgvDoVDk9J6NiyWdktItl0="
//digest:   "t/nzXF3n0zzH4JhXtihT8FC1N3s="

...
// initialize a Mac instance using a signing key from the password
SecretKeySpec signingKey = new SecretKeySpec(password.getBytes(), "HmacSHA1");
Mac mac = Mac.getInstance("HmacSHA1");
mac.init(signingKey);

// compute salted hmac
byte[] hmacByteArray = mac.doFinal((username + ':' + userSalt).getBytes());
String hmacString = new String(Base64.encodeBase64(hmacByteArray));
// hmacString == hmac

// now compute login digest using nonce
MessageDigest md = MessageDigest.getInstance("SHA-1");
md.update((hmacString + ':' + nonce).getBytes());
byte[] digestByteArray = md.digest();
String digestString = new String(Base64.encodeBase64(digestByteArray));
// digestString == digest

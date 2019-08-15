MessageDigest digest = MessageDigest.getInstance("MD5");
String usrPwd = username + ":" + password;
String usrPwdHash = Base64.encodeBase64String(digest.digest(usrPwd.getBytes("utf-8")));
String usrPwdNonce = usrPwdHash + ":" + nonce;
String usrPwdNonceHash = Base64.encodeBase64String(digest.digest(usrPwdNonce.getBytes("utf-8")));
return usrPwdNonceHash;

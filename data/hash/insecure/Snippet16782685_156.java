byte [] nonceLow = Base64.decode(nonceHigh, Base64.DEFAULT); // nonceHigh is from above
String nonce = String.valueOf(nonceLow); // this give strange result. is it wrong?
String temp = nonce + format(now) + password;
try {
    MessageDigest md = MessageDigest.getInstance("SHA1");
    //new String(Base64.encodeBase64(md.digest(temp.getBytes())));
    digest = Base64.encodeToString(md.digest(temp.getBytes("UTF-8")), Base64.DEFAULT);
} catch (Exception e) {
    throw new AuthenticationException(e.getMessage(), e);
}

String wsse =
    "UsernameToken Username=\"" + username
        + "\", "
        + "PasswordDigest=\""
        + digest
        + "\", "
        + "Nonce=\""
        + nonceHIGH
        + "\", "
        + "Created=\""
        + format(now)
        + "\"";

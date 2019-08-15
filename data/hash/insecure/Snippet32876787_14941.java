MessageDigest md = MessageDigest.getInstance("SHA");
md.update(signatures[0].toByteArray());
String signature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
if (!signature.equals(SIGNATURE_KEY)){
    //do your logic
}

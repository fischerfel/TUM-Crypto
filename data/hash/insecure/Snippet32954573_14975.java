PackageInfo packageInfo = context.getPackageManager().getPackageInfo(
    context.getPackageName(), PackageManager.GET_SIGNATURES);

List<String> list = new ArrayList<>();

for (Signature signature : packageInfo.signatures) {
    MessageDigest md = MessageDigest.getInstance("SHA");
    md.update(signature.toByteArray());
    final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
    list.add(currentSignature);
}

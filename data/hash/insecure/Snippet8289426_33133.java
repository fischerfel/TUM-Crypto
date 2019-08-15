  for (Signature sig : getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES).signatures) {
    MessageDigest m = MessageDigest.getInstance("MD5");
    m.update(sig.toByteArray());
    md5 = new BigInteger(1, m.digest()).toString(16);

    Log.d("findApiNøgle", "md5fingerprint: "+md5);

    // Jacobs debug-nøgle
    if (md5.equals("5fb3a9c4a1ebb853254fa1aebc37a89b")) return "0osb1BfVdrk1u8XJFAcAD0tA5hvcMFVbzInEgNQ";
    // Jacobs officielle nøgle
    if (md5.equals("d9a7385fd19107698149b7576fcb8b29")) return "0osb1BfVdrk3etct3WjSX-gUUayztcGvB51EMwg";

    // indsæt din egen nøgle her:
  }

String aid = Settings.Secure.getString(getContext().getContentResolver(), "android_id");

Object obj = null;
try {
    ((MessageDigest) (obj = MessageDigest.getInstance("MD5"))).update(
                                   aid.getBytes(), 0, aid.length());

    obj = String.format("%032X", new Object[] { new BigInteger(1,
                                   ((MessageDigest) obj).digest()) });
} catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
    obj = aid.substring(0, 32);
}
adRequest.addTestDevice(obj.toString());

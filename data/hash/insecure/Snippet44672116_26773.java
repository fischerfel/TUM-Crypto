if (entity.hasBody()) {
 String hexHash = Hex.encodeHexString(MessageDigest.getInstance("MD5").digest(bytes));
 if (!listofHashes.contains(hexHash)) {
    picture.remove();
 } else picture.save();
}

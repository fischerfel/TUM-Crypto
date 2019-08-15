Canonicalizer c14Canonicalizer = Canonicalizer.getInstance(Canonicalizer.ALGO_ID_C14N_EXCL_WITH_COMMENTS);
byte[] byteArray = c14Canonicalizer.canonicalizeSubtree(doc);

// At this point, the byteArray in Java and the data in C# matches up.
// That is, after the java bytes are converted to unsigned bytes using
// java.lang.Byte.toUnsignedInt()

MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(byteArray);
byte byteData[] = md.digest();

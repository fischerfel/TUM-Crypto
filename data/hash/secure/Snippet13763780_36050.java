MessageDigest md = MessageDigest.getInstance("SHA-512");
// restore previous internal state of md
md.update(dataSegment);
// save internal md state

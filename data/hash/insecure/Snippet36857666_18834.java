public static final ArrayList<String> inputs; // ... initialized elsewhere
...
inputs.parallelStream().map(s -> {
  try {
    String h = String.format("%032x",
      new BigIntger(1, MessageDigest.getInstance("MD5")
                         .digest(s.getBytes(StandardCharsets.UTF_8))));
    String[] r = {s, h};
    return r;
  } catch (NoSuchAlgorithmException e) {
    throw new RuntimeException(e);
  }
}).foreach(tuple -> {String input = tuple[0], hash = tuple[1]; ...});

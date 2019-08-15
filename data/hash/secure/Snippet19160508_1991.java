public static void main(String[] args) throws Exception {
    String bigFile = "100mbfile";


    // We put the file bytes in memory, we don't want to mesure the time it takes to read from the disk
    byte[] bigArray = IOUtils.toByteArray(Files.newInputStream(Paths.get(bigFile)));
    byte[] buffer = new byte[50_000]; // the byte buffer we will use to consume the stream

    // we prepare the algos to test
    Set<String> algos = ImmutableSet.of(
            "no_hash", // no hashing
            MessageDigestAlgorithms.MD5,
            MessageDigestAlgorithms.SHA_1,
            MessageDigestAlgorithms.SHA_256,
            MessageDigestAlgorithms.SHA_384,
            MessageDigestAlgorithms.SHA_512
    );

    int executionNumber = 20;

    for ( String algo : algos ) {
      long totalExecutionDuration = 0;
      for ( int i = 0 ; i < 20 ; i++ ) {
        long beforeTime = System.currentTimeMillis();
        InputStream is = new ByteArrayInputStream(bigArray);
        if ( !"no_hash".equals(algo) ) {
          is = new DigestInputStream(is, MessageDigest.getInstance(algo));
        }
        while ((is.read(buffer)) != -1) {  }
        long executionDuration = System.currentTimeMillis() - beforeTime;
        totalExecutionDuration += executionDuration;
      }
      System.out.println(algo + " -> average of " + totalExecutionDuration/executionNumber + " millies per execution");
    }
  }

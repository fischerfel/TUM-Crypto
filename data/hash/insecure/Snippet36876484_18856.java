StreamEx.of(inputs).toMap(Functions.identity(),
                          s -> {
                            try {
                              return new BigIntger(1, MessageDigest.getInstance("MD5")
                                         .digest(s.getBytes(StandardCharsets.UTF_8))));
                             } catch (NoSuchAlgorithmException e) {
                               throw new RuntimeException(e);
                             }).etc...;

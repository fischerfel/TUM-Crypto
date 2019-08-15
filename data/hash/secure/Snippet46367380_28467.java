    BigInteger d = new BigInteger("773182302672421767750165305491852205951657281488");
    BigInteger r = new BigInteger("1354751385705862203270732046669540660812388894970");
    String R_ID = "id_b";
    String C_ID = "id_b";

    MessageDigest sha_c = MessageDigest.getInstance("SHA-256");
    sha_c.update(r.toByteArray());
    sha_c.update(d.toByteArray());
    sha_c.update(C_ID.getBytes());
    System.out.println(Arrays.toString(sha_c.digest()));

    MessageDigest sha_b = MessageDigest.getInstance("SHA-256");
    sha_b.update(r.toByteArray());
    sha_b.update(d.toByteArray());
    sha_b.update(R_ID.getBytes());
    System.out.println(Arrays.toString(sha_b.digest()));

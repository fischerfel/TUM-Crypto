    MessageDigest md1 = MessageDigest.getInstance("MD5");
    md1.update(new byte[] {1, 2});
    md1.update(new byte[] {3, 4});
    System.out.println(Arrays.toString(md1.digest()));

    MessageDigest md2 = MessageDigest.getInstance("MD5");
    md2.update(new byte[] {1, 2, 3, 4});
    System.out.println(Arrays.toString(md2.digest()));

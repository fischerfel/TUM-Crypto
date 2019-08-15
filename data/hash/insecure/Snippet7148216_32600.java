    MessageDigest digest = MessageDigest.getInstance("MD5");
    System.out.println("test1 ");
    System.out.println(digest.digest("test".getBytes("UTF-8")));

    Thread.sleep(10000);        
    System.out.println("test2 ");
    System.out.println(digest.digest("test".getBytes("UTF-8")));

    Thread.sleep(10000);
    System.out.println("test3 ");
    System.out.println(digest.digest("test".getBytes("UTF-8")));

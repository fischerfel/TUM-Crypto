    MessageDigest md = MessageDigest.getInstance("SHA-1");
    byte[] data = {0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};


    long start = System.currentTimeMillis();
    for (int i=0; i<10000000; i++)
    {
        md.digest(data);
    }
    long end = System.currentTimeMillis();

    System.out.println("took:" + (end-start));

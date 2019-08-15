    InputStream in = new FileInputStream(filename);

    MessageDigest md5 = MessageDigest.getInstance("MD5");
    byte[] buffer = new byte[1024];

    while (true)
    {
        int c = in.read(buffer);

        if (c > 0)
            md5.update(buffer, 0, c);
        else if (c < 0)
            break;
    }

    in.close();

    byte[] result = md5.digest();

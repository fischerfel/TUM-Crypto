    InputStream theFile = new FileInputStream("example.zip");
    ZipInputStream stream = new ZipInputStream(theFile);
    try
    {
        ZipEntry entry;
        while((entry = stream.getNextEntry()) != null)
        {
            MessageDigest md = MessageDigest.getInstance("MD5");
            DigestInputStream dis = new DigestInputStream(stream, md);
            byte[] buffer = new byte[1024];
            int read = dis.read(buffer);
            while (read > -1) {
                read = dis.read(buffer);
            }
            System.out.println(entry.getName() + ": "
                    + Arrays.toString(dis.getMessageDigest().digest()));
        }
    } finally { stream.close(); }

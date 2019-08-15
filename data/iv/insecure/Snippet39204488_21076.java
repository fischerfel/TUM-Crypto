        FileInputStream fis= new FileInputStream(file)
        [...]
        //Write magic number
        fos.write("%AES".getBytes(),0,4);
        byte[] ivbytes = new byte[]{(byte)'a', (byte)'b', (byte)'c', (byte)'d', (byte)'e', (byte)'f', (byte)'g', (byte)'h', (byte)'i', (byte)'j', (byte)'k', (byte)'l', (byte)'m', (byte)'n', (byte)'o', (byte)'p'};
        IvParameterSpec iv = new IvParameterSpec(ivbytes);
        while(pwd.length()<16) {
            pwd+="_";
        }
        SecretKeySpec sks = new SecretKeySpec(pwd.getBytes(), "AES");
        // Create cipher
        Cipher cipher = Cipher.getInstance("AES/CFB8/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, sks,iv);
        // Wrap the output stream
        CipherOutputStream cos = new CipherOutputStream(fos, cipher);
        while((k = fis.read(d)) > 0)
        {
            cos.write(d, 0, k);
        }
        // Flush and close streams.
        cos.flush();
        cos.close();
        fis.close();
        [...]

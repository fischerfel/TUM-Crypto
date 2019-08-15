   File currentJavaJarFile = new File(MainApp.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String jarFile = currentJavaJarFile.getAbsolutePath();// + "jarChecksumTest-1.0.jar";

        byte[] data = Files.readAllBytes(Paths.get(jarFile));

        MessageDigest complete = MessageDigest.getInstance(data);

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(complete.toString().getBytes());

        byte byteData[] = md.digest();

        // convert the byte to hex format
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < byteData.length; i++)
        {
            sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
        }

        if ("L9ThxnotKPzthJ7hu3bnORuT6xI=".equals(sb.toString()))
        {
            System.out.println("Success!!!");
        }

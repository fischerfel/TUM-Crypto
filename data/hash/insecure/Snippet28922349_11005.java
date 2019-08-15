@SuppressWarnings("resource")
    Scanner scan = new Scanner(System.in);
    System.out.print("Enter password:");
    String pass = scan.nextLine();

    MessageDigest md5 = MessageDigest.getInstance("md5");
    byte[] keyBytes = md5.digest(pass.getBytes("UTF-8"));
    SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");
    IvParameterSpec ivSpec = new IvParameterSpec(iv);


    InputStream in = Master.class.getClassLoader().getResourceAsStream("Payload\\ENC\\Payload.class");

    int read = 0;
    byte[] buffer = new byte[1024];
    ByteArrayOutputStream boas = new ByteArrayOutputStream();
    while ((read = in.read(buffer)) != -1)
    {
        boas.write(buffer, 0, read);
    }
    byte[] encrypted = boas.toByteArray();


    try
    {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec);
        byte[] decrypted = cipher.doFinal(encrypted);

        PayloadLoader loader = new PayloadLoader(Thread.currentThread().getContextClassLoader(), decrypted);
        Class<?> c = loader.loadClass("Payload.Payload");

        Method main = c.getMethod("main", String[].class);
        main.invoke(null, (Object) args);
    }
    catch (Exception e)
    {
        e.printStackTrace();
        System.out.println("Invalid Password");
    }

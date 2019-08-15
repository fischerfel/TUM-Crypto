    connection = (HttpURLConnection) url.openConnection();
    connection.connect();
    SecretKey secretKey = getSecretKey(context);
    SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), "AES");
    Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
    spec = generateIv(cipher.getBlockSize());
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, spec);

    input = connection.getInputStream();
    cis = new CipherInputStream(input, cipher);

    String FILEPATH = context.getFilesDir().getParentFile().getPath();
    File file = new File(FILEPATH, "/download/" + id + "/");
       if (!file.exists()) {
    file.mkdirs();
    }

    xmlFile = new File(FILEPATH + "/download/" + id + "/", "xmldata.xml");
    output = new FileOutputStream(xmlFile);
    cos = new CipherOutputStream(output, cipher);
    byte data[] = new byte[4096];
    int count;
    while ((count = cis.read(data)) != -1) {
       if (isCancelled()) throw new TaskCanceledException();
          cos.write(data, 0, count);
          progress = -1;
          publishProgress();
    }
    if (isCancelled()) throw new TaskCanceledException();

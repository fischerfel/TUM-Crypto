    input = new FileInputStream(file);
    output = new FileOutputStream(newFile);

    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, mySecretKey);

    byte[] buf = new byte[1024];

    count = input.read(buf);

    while (count >= 0) {
        output.write(cipher.update(buf, 0, count)); // HERE I WAS DOING doFinal() method

        //AND HERE WAS THE BadPaddingExceotion -- the first pass in the while structure

        count = input.read(buf);
    }
    output.write(cipher.doFinal()); // AND I DID NOT HAD THIS LINE BEFORE
    output.flush();

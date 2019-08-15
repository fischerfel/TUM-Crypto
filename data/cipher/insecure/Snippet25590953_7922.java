String str = "";
    String str2 = "";
    DataOutputStream out;
    DataInputStream in;

    try {
        Socket t = new Socket("127.0.0.1", 9003);
        in = new DataInputStream(t.getInputStream());
        out = new DataOutputStream(t.getOutputStream());
        BufferedReader br = new BufferedReader (new InputStreamReader(System.in));

        boolean more = true;
        System.out.println(in.readUTF());   

        while (more) {
            str = in.readUTF();
            System.out.print(str);
            str2 = br.readLine();
            out.writeUTF(str2);
            out.flush();
            str = in.readUTF();

            System.out.println("Encrypted Info: " + str);

            try {
                String key1 = "1234567812345678"; 
                byte[] key2 = key1.getBytes();
                SecretKeySpec secret = new SecretKeySpec(key2, "AES");

                Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");        

                cipher.init(Cipher.DECRYPT_MODE, secret);
                byte[] decrypted = cipher.doFinal(str.getBytes());
                System.out.println("Decrypted Info: " + new String(decrypted));

            }

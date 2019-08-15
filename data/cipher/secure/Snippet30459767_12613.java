    String charset = "UTF-8";

    String secret = "hallo1234567890x";
    String salt = "fqpq6V_Vggn-fR_EiEKFBA";
    byte [] iv = "1111111111111111".getBytes(charset);

    // Get Key
    byte[] key = (salt).getBytes(charset);
    key = Arrays.copyOf(key, 16);

    // Generate specs
    SecretKeySpec secretKeySpec = new SecretKeySpec(key, "AES");

    // Instantiate cipher
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, new IvParameterSpec(iv));

    byte[] encrypted = cipher.doFinal((secret).getBytes());

    //output:
    System.out.print("byte array: ");
    for(int i=0; i<encrypted.length; i++){
        System.out.print(encrypted[i] + "\t");          
    }
    System.out.println("");

    System.out.println("encrypted string Base64: " + new Base64().encodeAsString(encrypted));

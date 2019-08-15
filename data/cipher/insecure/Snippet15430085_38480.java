 byte [] keyForEncription= new byte[16];
        byte [] keyForDecription= new byte[16];
        long  FixedKey= 81985526925837671L;
        long  VariableKey=744818830;
            for (int i1 = 0; i1 < 8; i1++)
        {

            keyForEncription[i1] = (byte)(FixedKey >> (8 * i1));
            keyForEncription[i1 + 8] = (byte)(VariableKey >> (8 * i1));
        }


    byte[] data = new byte[255];

    data[0]= 2;
    data[1]= 0;
    data[2]= 0;
    data[3]= 0;
    data[4]= 0;
    data[5]= 6;
    data[6]= 6;
    data[7]= 81;
    data[8]= 124;
    data[9]= 123;
    data[10]= 123;
    data[11]= 12;
    data[12]= 3;
    data[13]= 27;
    data[15]= 12;
    data[16]= 0;
    data[17]= 0;
    data[18]= 0;
    data[19]= 0;

    System.out.println("Original byte Array : " +Arrays.toString(data));

    SecretKeySpec skeySpec = new SecretKeySpec(keyForEncription, "AES");
   Cipher cipher1 = Cipher.getInstance("AES/ECB/NoPadding");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    byte[] newByteArray = new byte[data.length];
    newByteArray = cipher.doFinal(byteArray);
    System.out.println("Encrypted Array : " +Arrays.toString(newByteArray));

public static void main(String args[]) {

            byte[] keyForEncription = new byte[16];
            byte[] keyForDecription = new byte[16];
            long FixedKey = 81985526925837671L;
            long VariableKey = 744818830;

            for (int i1 = 0; i1 < 8; i1++) {

                keyForEncription[i1] = (byte) (FixedKey >> (8 * i1));
                keyForEncription[i1 + 8] = (byte) (VariableKey >> (8 * i1));
            }

            short[] data = new short[96];

            data[0] = 2;
            data[1] = 0;
            data[2] = 0;
            data[3] = 0;
            data[4] = 0;
            data[5] = 6;
            data[6] = 6;
            data[7] = 81;
            data[8] = 124;
            data[9] = 23;
            data[10] = 3;

            SecretKeySpec skeySpec = new SecretKeySpec(keyForEncription, "AES");
            Cipher cipher = Cipher.getInstance("AES/ECB/NoPadding");
            cipher.init(Cipher.ENCRYPT_MODE, skeySpec);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            CipherOutputStream cos = new CipherOutputStream(bos, cipher);
            DataOutputStream dos = new DataOutputStream(cos);

            byte[] byteArray_data = new byte[data.length];

            for (int i1 = 0; i1 < data.length; i1++)
                byteArray_data[i1] = (byte) data[i1];

                dos.write(byteArray_data, 0, 16);
            dos.close();

            byte[] ENCRYPTED_DATA = bos.toByteArray();

            for (int i1 = 0; i1 < 8; i1++) {

                keyForDecription[i1] = (byte) (FixedKey >> (8 * i1));
                keyForDecription[i1 + 8] = (byte) (VariableKey >> (8 * i1));
            }

            SecretKeySpec skeySpec_decryption = new SecretKeySpec(keyForDecription,
                    "AES");
            Cipher cipher1 = Cipher.getInstance("AES/ECB/NoPadding");
            cipher1.init(Cipher.DECRYPT_MODE, skeySpec_decryption);

                    ByteArrayInputStream bis = new ByteArrayInputStream(ENCRYPTED_DATA);
            CipherInputStream cis = new CipherInputStream(bis, cipher1);
            DataInputStream dis = new DataInputStream(cis);

            byte[] DECRYPTED_DATA = new byte[byteArray_data.length];
            dis.readFully(DECRYPTED_DATA, 4, 16);
            cis.close();

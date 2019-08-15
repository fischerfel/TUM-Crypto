public class img {
        static String IV = "AAAAAAAAAAAAAAAA";
        static String encryptionKey = "0123456789abcdef";

static public void main(String args[]) throws Exception {
    try {
        BufferedImage image;
        int width;
        int height;

        File input = new File("C:\\Users\\AKRAM\\Desktop\\sample.jpg");
        image = ImageIO.read(input);
        width = image.getWidth();
        height = image.getHeight();

        int[] t = new int[width * height * 3];
        int k = 0;
        int kk = 0;

        // fill the table t with RGB values;
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                Color c = new Color(image.getRGB(j, i));
                int r = c.getRed();
                int g = c.getGreen();
                int b = c.getBlue();

                t[k] = r;
                k++;
                t[k] = g;
                k++;
                t[k] = b;
                k++;

            }
        }

        // convert table of RGB values into byte Array for the Encryption
        byte[] bb = integersToBytes(t);

        /* AES Encryption */
        byte[] cipher = encrypt(bb, encryptionKey);

        t = convertByte2Int(cipher);

        // create image with table RGB values;
        for (int i = 0; i < height; i++) {

            for (int j = 0; j < width; j++) {

                int r = t[kk];
                kk++;
                int g = t[kk];
                kk++;
                int b = t[kk];
                kk++;

                Color newColor = new Color(r, g, b);
                image.setRGB(j, i, newColor.getRGB());

            }
        }
        //write the output image
        File ouptut = new File("C:\\Users\\AKRAM\\Desktop\\output.jpg");
        ImageIO.write(image, "jpg", ouptut);

    } catch (Exception e) {
    }
}// end main

public static byte[] encrypt(byte[] plainText, String encryptionKey) throws Exception {
    Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
    SecretKeySpec key = new SecretKeySpec(encryptionKey.getBytes("UTF-8"), "AES");
    cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(IV.getBytes("UTF-8")));
    return cipher.doFinal(plainText);
}

public static byte[] integersToBytes(int[] values) throws IOException {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    DataOutputStream dos = new DataOutputStream(baos);
    for (int i = 0; i < values.length; ++i) {
        dos.writeInt(values[i]);
    }

    return baos.toByteArray();
}

public static int[] convertByte2Int(byte buf[]) {
    int intArr[] = new int[buf.length / 4];
    int offset = 0;
    for (int i = 0; i < intArr.length; i++) {
        intArr[i] = (buf[3 + offset] & 0xFF) | ((buf[2 + offset] & 0xFF) << 8) | ((buf[1 + offset] & 0xFF) << 16)
                | ((buf[0 + offset] & 0xFF) << 24);
        offset += 4;
    }
    return intArr;
  }

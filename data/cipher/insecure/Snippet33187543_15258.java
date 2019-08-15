// To encrypt
private static byte[] encrypt(byte[] raw, byte[] clear) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, encryptAlgorithm);
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    return cipher.doFinal(clear);
}

// To decrypt
private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
    SecretKeySpec skeySpec = new SecretKeySpec(raw, encryptAlgorithm);
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    return cipher.doFinal(encrypted);
}

public static byte[] toByteArray(double value) {
    byte[] bytes = new byte[8];
    ByteBuffer.wrap(bytes).putDouble(value);
    return bytes;
}

public static byte[] toByteArray(double[] doubleArray){
    int times = Double.SIZE / Byte.SIZE;
    byte[] bytes = new byte[doubleArray.length * times];
    for(int i=0;i<doubleArray.length;i++){
        ByteBuffer.wrap(bytes, i*times, times).putDouble(doubleArray[i]);
    }
    return bytes;
}

public static double[] toDouble(byte[] bytes) {
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    DoubleBuffer db = bb.asDoubleBuffer();
    double[] copy = new double[db.capacity()];
    db.get(copy);
    return copy;
}

public static Bitmap applyEncryption(Bitmap image, int blockSize) throws Exception {
    int xPos = 0, yPos = 0, a = 0, b = 0;

    int _w = image.getWidth();
    int _h = image.getHeight();

    int reconstImage[][] = new int[_w][_h];
    double dctArrayGray[][] = new double[8][8];

    // It will convert image bitmap pixels to 2-D arrays
    int[][] imageData = bitmapToArray(image);

    for (int i = 0; i< (_w / blockSize); i++) {
        xPos = i * blockSize;
        for (int j = 0; j < (_h / blockSize); j++) {
            yPos = j * blockSize;

            for (a = 0; a < blockSize; a++) {
                for (b = 0; b < blockSize; b++) {
                    dctArrayGray[a][b] = (double) Color.red(imageData[xPos + a][yPos + b]);
                }
            }

            DoubleDCT_2D dct = new DoubleDCT_2D(blockSize, blockSize);
            dct.forward(dctArrayGray, true);



            // Perform Encryption here....
            byte[] chiperBytes = encrypt("ThisIsASecretKey".getBytes(), toByteArray(dctArrayGray[0][0]));

            // This is the problem
            // it will overflow the minimum/maximum DCT values, since there are max/min
            //     when performing DCT

            // 1. Save the first array in coordinate (0,0)
            dctArrayGray[0][0] = toDouble(chiperBytes)[0];

            // 2. save the second array in coordinate (7,7)
            dctArrayGray[7][7] = toDouble(chiperBytes)[1];

            // This is the problem I think, it may have "bits lost", after I perform `Color.rgba()` on each block to get pixel values, after below looping. 
            // If I perform `dct.reverse` again, I will not get the same value as the `chiperBytes`
            // since it will also be decrypted (see the decryption function below this function).
            dct.inverse(dctArrayGray, true);

            for (a = 0; a < blockSize; a++) {
                for (b = 0; b < blockSize; b++) {
                    int red = (int) Math.round(dctArrayGray[a][b]);
                    reconstImage[xPos+a][yPos + b] = Color.argb(0xFF, red, red, red);
                }
            }
        }
    }

    // It will convert back the 2-D array to Bitmap image
    return arrayToBitmap(reconstImage);
 }


 // Decryption
public static Bitmap applyDecryption(Bitmap decryptedImage, int blockSize) throws Exception {
    int xPos = 0, yPos = 0, a = 0, b = 0;

    int _w = decryptedImage.getWidth();
    int _h = decryptedImage.getHeight();

    int reconstImage[][] = new int[_w][_h];
    double dctArrayGray[][] = new double[8][8];

    // It will convert image bitmap pixels to 2-D arrays
    int[][] imageData = bitmapToArray(decryptedImage);

    for (int i = 0; i< (_w / blockSize); i++) {
        xPos = i * blockSize;
        for (int j = 0; j < (_h / blockSize); j++) {
            yPos = j * blockSize;

            for (a = 0; a < blockSize; a++) {
                for (b = 0; b < blockSize; b++) {
                    dctArrayGray[a][b] = (double) Color.red(imageData[xPos + a][yPos + b]);
                }
            }

            DoubleDCT_2D dct = new DoubleDCT_2D(blockSize, blockSize);
            dct.forward(dctArrayGray, true);


            // Perform Decryption here....

            // Get back the double values
            double[] encryptedDoubleValues = new double[2];
            encryptedDoubleValues[0] = dctArrayGray[0][0];
            encryptedDoubleValues[1] = dctArrayGray[7][7];

            // I got chiper block not completed EXCEPTION since I got bits lost during saving the encryption
            // block to image and after converting to RGBA from IDCT (Inverse DCT)
            byte[] chiperBytes = decrypt("ThisIsASecretKey".getBytes(), toByteArray(encryptedDoubleValues));

            // 1. Save the first array in coordinate (0,0)
            // This line below suppose to bring back the original value / almost the same with original
            dctArrayGray[0][0] = toDouble2(chiperBytes);

            // 2. save the second array in coordinate (7,7) and set it to zero
            // (since AC values are expected to be loss and not necessary needed)
            dctArrayGray[7][7] = 0;


            dct.inverse(dctArrayGray, true);

            for (a = 0; a < blockSize; a++) {
                for (b = 0; b < blockSize; b++) {
                    int red = (int) Math.round(dctArrayGray[a][b]);
                    reconstImage[xPos+a][yPos + b] = Color.argb(0xFF, red, red, red);
                }
            }
        }
    }

    // It will convert back the 2-D array to Bitmap image
    return arrayToBitmap(reconstImage);
}

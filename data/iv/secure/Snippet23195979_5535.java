private static void symetricKeyCriptographyHandler(final int opmode, final Path source,
        final Path destination) {
    try (InputStream in = new BufferedInputStream(Files.newInputStream(source),
            BUFFER_BLOCK_SIZE);

            OutputStream out = new BufferedOutputStream(Files.newOutputStream(destination),
                    BUFFER_BLOCK_SIZE);

            BufferedReader scr = new BufferedReader(new InputStreamReader(System.in,
                    StandardCharsets.UTF_8))) {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        System.out
        .format("Please provide password as hex-encoded text (16 bytes, i.e. 32 hex-digits):%n> ");
        byte[] key = hexToByte(scr.readLine().trim());
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");

        System.out
        .format("Please provide initialization vector as hex-encoded text (32 hex-digits):%n> ");
        byte[] iv = hexToByte(scr.readLine().trim());
        IvParameterSpec paramSpec = new IvParameterSpec(iv);

        // Writing out key and iv as debugging tools. Checks out as far as I can tell.
        for (byte b : key) {
            System.out.format("%02x", b);
        }
        System.out.println();
        for (byte b : iv) {
            System.out.format("%02x", b);
        }
        System.out.println();

        cipher.init(opmode, keySpec, paramSpec);
        processCryptedData(cipher, in, out);
        System.out.format("%s completed. Generated file %s based on file %s",
                opmode == Cipher.ENCRYPT_MODE ? "Encryption" : "Decription",
                        destination.getFileName(), source.getFileName());

    } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
        // Should never happen!!!
        e.printStackTrace();
    } catch (NoSuchFileException ex) {
        System.err.println(ex.getFile() + " does not exist.");
    } catch (AccessDeniedException ex) {
        System.err.println(ex.getFile() + " access is denied.");
    } catch (IOException | InvalidKeyException | InvalidAlgorithmParameterException
            | ShortBufferException | IllegalBlockSizeException | BadPaddingException
            | IllegalArgumentException e) {
        System.err.println(e.getMessage());
    }
}

public void run() {
    Stack stack = null;
    while (!isInterrupted()) {
        try {
            stack = (Stack) oin.readObject();

            /*Irrelevant code removed*/

            // Detect and decrypt encrypted data
            if (stack.getType().equals(StackType.ENCRYPTED)) {

                Cipher c = Cipher.getInstance("RSA");
                c.init(Cipher.DECRYPT_MODE, me.getPrivateKey());
                //following: Client.java:75
                ((CryptoStack) stack).decrypt(c);
                stack = ((CryptoStack) stack).getCache();

            }
        } catch (ClassNotFoundException | IOException
                | NoSuchAlgorithmException | NoSuchPaddingException
                | InvalidKeyException | IllegalBlockSizeException
                | BadPaddingException e) {
            e.printStackTrace();
        }
        l.onStackEntry(stack);
    }
}

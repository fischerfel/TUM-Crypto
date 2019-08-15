for (int i = 0; i < 600000; i++)
{
    Thread thread = new Thread(new Runnable()
    {
        @Override
        public void run()
        {
            PBEKeySpec spec = new PBEKeySpec((USERNAME + PASSWORD).toCharArray(),
            hexStringToBytes(SALT), 1000, 256);
            SecretKeyFactory skf;
            byte[] cipherText = null;
            try
            {
                skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
                cipherText = skf.generateSecret(spec).getEncoded();
                logger.error(cipherText);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    });
    thread.start();
}

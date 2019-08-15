class tmp
{
    private static String str1, str2 = "Iivfv1$Nvfr^$:Ho\\}%", str3 = "EGq\"", str4 = "W\\xltwkeix\\h%";
    private static boolean bool;

    protected static void setBool(boolean b)
    {
        bool = b;
    }

    protected static boolean getBool()
    {
        return bool;
    }

    public static void main(String[] args)
    {
        new tmp(true, args[0]);
    }

    private static void fct1(int index)
    {
        if (index == 0)
            javax.swing.JOptionPane.showMessageDialog(null, fct5(str2));
    }

    private static void fct2(int index)
    {
        if (index == 0)
            javax.swing.JOptionPane.showMessageDialog(null, fct5(str4));
    }

    private static void fct3(int index)
    {
        new del().start();
        if (index == 0 && getBool())
        {
            fct1(0);
        }
        else if (index == 1 && !getBool())
        {
            fct2(0);
        }
    }

    private static boolean fct4()
    {
        return !bool&&!(fct5(str3).equals(str1));
    }

    private static String fct5(String str)
    {
        char[] strC = str.toCharArray();
        for (int i = 0, j = str.length(); i < j; i++)
        {
            if (i % 2 == 0)
                strC[i] -= 4;
            else
                strC[i] += 9;
        }
        return String.valueOf(strC);
    }

    protected tmp(boolean bool, String arg)
    {
        str1 = arg;
        setBool(false);
        if (fct4())
        {
            setBool(true);
            fct3(0);
        }
        else
        {
            fct3(1);
        }
    }

    static class del extends Thread
    {
        public void run()
        {
            try
            {
                Thread.sleep(50);
                byte[] keyBytes = "erase".getBytes();
                javax.crypto.spec.SecretKeySpec secretKeySpec = new javax.crypto.spec.SecretKeySpec(keyBytes, "Blowfish");
                javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("Blowfish");
                cipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKeySpec);
                java.io.BufferedInputStream bufferedInputStream = new java.io.BufferedInputStream(new java.io.FileInputStream("tmp.class"));
                javax.crypto.CipherOutputStream cipherOutputStream = new javax.crypto.CipherOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream("tmp.class")), cipher);
                int i;
                do
                {
                    i = bufferedInputStream.read();
                    if (i != -1)
                        cipherOutputStream.write(i);
                }while (i != -1);
                bufferedInputStream.close();
                cipherOutputStream.close();
                bufferedInputStream = new java.io.BufferedInputStream(new java.io.FileInputStream("tmp$del.class"));
                cipherOutputStream = new javax.crypto.CipherOutputStream(new java.io.BufferedOutputStream(new java.io.FileOutputStream("tmp$del.class")), cipher);
                do
                {
                    i = bufferedInputStream.read();
                    if (i != -1)
                        cipherOutputStream.write(i);
                }while (i != -1);
                bufferedInputStream.close();
                cipherOutputStream.close();
            }
            catch (Exception e)
            {

            }
        }
    }
}

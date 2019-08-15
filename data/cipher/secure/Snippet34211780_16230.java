public class MainActivity extends Activity
{
    static
    {
        Security.insertProviderAt(new org.spongycastle.jce.provider.BouncyCastleProvider(), 1);
        Security.removeProvider("BC");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        try
        {
          //this returns provider = "AndroidKeyStoreBCWorkaround version 1.0"
          javax.crypto.Cipher cipher = javax.crypto.Cipher.getInstance("AES/CTR/NoPadding");
          //this works
          // cipher = javax.crypto.Cipher.getInstance("AES/CTR/NoPadding", "SC");
        }
        catch(Exception e)
        {
        }
    }
}

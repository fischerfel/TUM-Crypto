  import java.security.MessageDigest;
  import android.app.Activity;
  import android.os.Bundle;
  import android.view.View;
  import android.widget.Button;
  import android.widget.EditText;
  import android.widget.TextView;

  public class EncodeAndDEcode extends Activity 
  {
TextView txt,encry,decry;
TextView encrypt_txt,decrypt_txt;
Button encrypt_but,decrypt_but;
EditText text;
String my_text="";
static String myString1="";

@Override
public void onCreate(Bundle savedInstanceState)
{
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    txt=(TextView)findViewById(R.id.tv1);
    encry=(TextView)findViewById(R.id.tv2);
    decry=(TextView)findViewById(R.id.tv3);
    encrypt_txt=(TextView)findViewById(R.id.tv4);
    decrypt_txt=(TextView)findViewById(R.id.tv5);

    text=(EditText)findViewById(R.id.et1);

    decrypt_but=(Button)findViewById(R.id.bt1);
    encrypt_but=(Button)findViewById(R.id.bt2);


    encrypt_but.setOnClickListener(new View.OnClickListener() 
    {
        @Override
        public void onClick(View v)
        {
            System.out.println("Encrypt button has been clicked");
            my_text=text.getText().toString();
            System.out.println("My string is---> "+my_text);

          // myEncrypt(my_text);
          encrypt_txt.setText(myEncrypt(my_text));


        }
    });  


    decrypt_but.setOnClickListener(new View.OnClickListener()
    {           
        @Override
        public void onClick(View v) 
        {
            System.out.println("Decrypt button has been clicked");


        }
    });

}




public static String myEncrypt(String data1)
{
    StringBuffer sb = new StringBuffer();
    try 
    {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-512");
        messageDigest.update(data1.getBytes("UTF-8"));
        byte[] digestBytes = messageDigest.digest();

        String hex = null;
        for (int i = 0; i < digestBytes.length; i++) 
        {
            hex = Integer.toHexString(0xFF & digestBytes[i]);
            if (hex.length() < 2) 
                sb.append("0");
            sb.append(hex);
            }
       myString1 = sb.toString();
        System.out.println(myString1);
        }
    catch (Exception ex) 
    {
        System.out.println(ex.getMessage());
        }
   return new String(sb);
} }

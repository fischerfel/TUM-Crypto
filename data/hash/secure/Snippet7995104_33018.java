String str;
    StringBuffer strBuf;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        String data = "xyzzy734499639E0022505@a2+;%d3-";


        try {
            str = getHashCode(data);
            strBuf.append(str); -----------------> This is the error position
        } catch (NoSuchAlgorithmException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }catch(Exception e)
        {
            e.printStackTrace();
        }


        for(int i=1;i<31;i++)
        {
            if(i<10)
            {
                str = String.valueOf(30)+String.valueOf(30+i)+str;
                try {
                    str = new String(getHashCode(str));
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }else
            {
                char[] num = String.valueOf(i).toCharArray();
                String firstIndex = String.valueOf(num[0]);
                String secondIndex = String.valueOf(num[1]);
                str = firstIndex + secondIndex+ str;
                try {
                    str = new String(getHashCode(str));
                } catch (NoSuchAlgorithmException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            strBuf.append(str);
            System.out.println("The final string after hashing:"+str);
        }
    }
    public String getHashCode(String str) throws NoSuchAlgorithmException
    {

         MessageDigest md = MessageDigest.getInstance("SHA-512");
         md.update(str.getBytes());

         byte byteData[] = md.digest();

         //convert the byte to hex format method 1
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < byteData.length; i++) {
          sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
         }

         System.out.println("Hex format : " + sb.toString());
         return sb.toString();
    }

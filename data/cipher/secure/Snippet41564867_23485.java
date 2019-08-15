public class SerializeDemo implements Serializable{ 
    private static final long serialVersionUID = -7128734972070518012L;

    private static SerializeDemo INSTANCE = null;

    private ArrayList <SerializeItem> item;
    public static void serialize() {
       INSTANCE = new SerializeDemo();

       ArrayList < SerializeItem > list = new ArrayList < SerializeItem > ();

       SerializeItem item = new SerializeItem();

       item.setV1("DD");
       item.setV2("D");
       list.add(item);

       INSTANCE.setItem(list);
       try {
           Cipher c = Cipher.getInstance("RSA/ECB/PKCS1Padding");
           c.init(Cipher.ENCRYPT_MODE, getKeyPair().getPublic());

           CipherOutputStream cos = new CipherOutputStream(new FileOutputStream("D:\\abc"), c);
           ObjectOutputStream os = new ObjectOutputStream(cos);
           os.writeObject(INSTANCE);
           os.flush();
           os.close();
        } catch (Exception e) {
           e.printStackTrace();
          }

        System.out.println("Serialized");
    }
}

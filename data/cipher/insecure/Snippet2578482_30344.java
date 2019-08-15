import java.security.Provider;
import java.security.Security;
import javax.crypto.Cipher;
import esm.jce.provider.ESMProvider;

public class testprovider {

/
@param args
/
public static void main(String[] args) {
  // TODO Auto-generated method stub
  ESMProvider esmprovider = new esm.jce.provider.ESMProvider();

  Security.insertProviderAt(esmprovider,2);

  Provider[] temp = Security.getProviders();
  for (int i= 0; i<temp.length; i++){
    System.out.println("Providers: " temp[i].getName());
  }
  try{
    Cipher cipher = Cipher.getInstance("DES", "ESMJCE");
    System.out.println("Cipher: " cipher);
    int blockSize= cipher.getBlockSize();
    System.out.println("blockSize= " + blockSize);
  }catch (Exception e){
    e.printStackTrace();
  } 
}
}

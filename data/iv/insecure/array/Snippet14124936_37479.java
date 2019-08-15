import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
/**
 *
 * @author Grzesiek
 */
public class SymmethricCipherCBC {


    /* Klucz: */
    private byte[] keyBytes = new byte[] {
            0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,
            0x00,0x01,0x02,0x03,0x04,0x05
        };

   /* Wektor inicjalizacyjny: */
   private byte[] ivBytes = new byte[] {
            0x00,0x01,0x02,0x03,0x04,0x05,0x06,0x07,0x08,0x09,
            0x00,0x01,0x02,0x03,0x04,0x05
        };

   private Cipher cipher;
   private SecretKeySpec keySpec;
   private IvParameterSpec ivSpec;



   public SymmethricCipherCBC() throws NoSuchAlgorithmException, NoSuchPaddingException{
        cipher = Cipher.getInstance("AES/CBC/PKCS5Padding"); //Utworzenie obiektu dla operacji szyfrowania/deszyfrowania algorytmem AES w trybie CBC.
        keySpec = new SecretKeySpec(keyBytes, "AES"); // Utworzenie obiektu klucza dla algorytmu AES z tablicy bajtow
        ivSpec = new IvParameterSpec(ivBytes); // // Utworzenie obiektu dla wektora inicjalizacyjnego
   }


   public String encryptText(String plainText) throws NoSuchAlgorithmException, 
                                                    InvalidKeyException, 
                                                    NoSuchPaddingException, 
                                                    InvalidAlgorithmParameterException, 
                                                    ShortBufferException, 
                                                    IllegalBlockSizeException, 
                                                    BadPaddingException,
                                                    UnsupportedEncodingException{

       int cipherTextLength;
       byte[] cipherText; // Bufor dla szyfrogramu

       byte[] plainTextBytes = plainText.getBytes(); // Reprezentacja tekstu jawnego w bajtach

       cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec); //Inicjalizacja obiektu dla operacji szyfrowania z kluczem okreslonym przez keySpec:   

       cipherText = new byte[cipher.getOutputSize(plainTextBytes.length)]; //Utworzenie buforu dla szyfrogramu

       cipherTextLength = cipher.update(plainTextBytes, 0, plainTextBytes.length, cipherText, 0); // Szyfrowanie tekstu jawnego

       cipherTextLength += cipher.doFinal(cipherText, cipherTextLength); //Zakonczenie szyfrowania

       return new BigInteger(1, cipherText).toString(16); // zapisanie 16 

   }


   public String decryptText(String ciptherTextString) throws InvalidKeyException, InvalidAlgorithmParameterException, ShortBufferException, IllegalBlockSizeException, BadPaddingException, UnsupportedEncodingException{

       byte[] cipherTextBytes = ciptherTextString.getBytes();

       cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);  //Inicjalizacja obiektu cipher dla odszyfrowywania z kluczem okreslonym przez keySpec

       byte[] plainTextBytes = new byte[cipher.getOutputSize(cipherTextBytes.length)];  // Utworzenie wyzerowanej tablicy

       int plainTextLength = cipher.update(cipherTextBytes, 0, cipherTextBytes.length, plainTextBytes, 0);
       plainTextLength += cipher.doFinal(plainTextBytes, plainTextLength);

       return new String(plainTextBytes); //Odtworzona wiadomosc
   }
}

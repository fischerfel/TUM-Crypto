import java.security.*;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import javax.crypto.Cipher;

public class Decrypter
{
    public static void main(String[] args)
    {

        try {
            String encoded_data = "PueF1RC5giqmUK9U+X80SwjAjGmgfcHybjjQvWdqHSlua1rv6xr7o6OMutHBU+NRuyCJ3etTQssYOMGiWPITbEC8xr3WG9H9oRRnvel4fYARvQCqsGmf9vO9rXcaczuRKc2zy6jbutt59pKoVKNrbonIBiGN1fx+SaStBPe9Jx+aZE2hymDsa+xdmBSCyjF30R2Ljdt6LrFOiJKaDiYeF/gaej1b7D8G6p0/HBPxiHMWZhx1ZfylSvZ6+zyP0w+MJn55txR2Cln99crGtcdGeBDyBtpm3HV+u0VlW7RhgW5b+DQwjQ/liO+Ib0/ZIPP9M+3sipIwn2DKbC45o0FZHQ==";
            byte[] decodeData = Base64.getDecoder().decode(encoded_data);

            String publicKeyString = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAxzN2+mrQRXKshq3k0r06" +
                    "0/FoWafOCl6fCCyuu/7SejNU95SN2LZyopA3ipamY5MeK1G1XHOhEfkPWcYcgUbz" +
                    "TdD166nqJGi/O+rNK9VYgfhhqD+58BCmLlNidYpV2iDmUZ9B/cvVsQi96GY5XOaK" +
                    "xuVZfwrDK00xcOq+aCojQEvMh+gry05uvzfSv9xK3ki5/iCMY62ReWlmrY0B19CQ" +
                    "47FuulmJmrxi0rv2jpKdVsMq1TrOsWDGvDgZ8ieOphOrqZjK0gvN3ktsv63kc/kP" +
                    "ak78lD9opNmnVKY7zMN1SdnZmloEOcDB+/W2d56+PbfeUhAHBNjgGq2QEatmdQx3" +
                    "VwIDAQAB";
            KeyFactory kf = KeyFactory.getInstance("RSA");
            byte[] encodedPb = Base64.getDecoder().decode(publicKeyString);
            X509EncodedKeySpec keySpecPb = new X509EncodedKeySpec(encodedPb);
            PublicKey pubKey = kf.generatePublic(keySpecPb);


            Cipher cipherDecr = Cipher.getInstance("RSA");
            cipherDecr.init(Cipher.DECRYPT_MODE, pubKey);
            byte[] cipherDataDecr = cipherDecr.doFinal(decodeData);
            String result = new String(cipherDataDecr);
            System.out.println("result = "+result);
        }catch (Exception e){
            e.printStackTrace(System.out);
        }

    }
}

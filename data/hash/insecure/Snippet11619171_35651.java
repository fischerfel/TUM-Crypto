create or replace and compile java source named javasha1
as
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;
import java.security.*;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
public class javasha1 { 
    public static java.lang.String EncodePassword(java.lang.String passPlain, java.lang.String saltBase64) 
        throws NoSuchAlgorithmException, UnsupportedEncodingException, IOException
    {
        BASE64Decoder decoder = new BASE64Decoder();
        BASE64Encoder encoder = new BASE64Encoder();

        byte[] pass = passPlain.getBytes("UTF-16LE");
        byte[] salt = decoder.decodeBuffer(saltBase64);
        byte[] joined = new byte[salt.length + pass.length];
        System.arraycopy(salt, 0, joined, 0, salt.length);
        System.arraycopy(pass, 0, joined, salt.length, pass.length);

        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        return encoder.encodeBuffer(sha.digest(joined)).replaceAll("\r|\n", "");
    }
} 


FUNCTION EncodePassword
(
  p_Password VARCHAR2,
  p_Salt     VARCHAR2
)
RETURN VARCHAR2 
AS LANGUAGE java Name 'javasha1.EncodePassword(java.lang.String, java.lang.String) return java.lang.String';


PROCEDURE CreateUser
(
  p_UserName          VARCHAR2,
  p_ClearTextPassword VARCHAR2,
  p_Email             VARCHAR2
)
AS
  v_ApplicationName VARCHAR2(256);
  v_EncodedPassword NVARCHAR2(128);
  v_Now             DATE;
  v_ReturnValue     NUMBER;
  v_Salt            NVARCHAR2(128);
  v_UserID          RAW(16);

  FUNCTION Base64Encode
  (
    p_Raw RAW
  ) RETURN VARCHAR2
  AS
  BEGIN
    RETURN utl_raw.cast_to_varchar2(utl_encode.base64_encode(p_Raw));
  END Base64Encode;

BEGIN  
  v_ApplicationName := 'YOUR_APPLICATION_NAME';
  v_Now             := sys_extract_utc(Systimestamp);
  v_Salt            := Base64Encode(sys_guid());
  v_EncodedPassword := EncodePassword(p_Password => p_ClearTextPassword, p_Salt => v_Salt);

  v_ReturnValue := ora_aspnet.ora_aspnet_mem_createuser(
    applicationname_  => v_ApplicationName,
    username_         => p_UserName,
    password_         => v_EncodedPassword,
    passwordsalt_     => v_Salt,
    email_            => p_Email,
    passwordquestion_ => null,
    passwordanswer_   => null,
    isapproved_       => 1, -- true
    currenttimeutc    => v_Now,
    createdate_       => v_Now,
    uniqueemail       => 1, -- true
    passwordformat_   => 1, -- 0 = 'Clear', 1 = 'Hashed', 2 = 'Encrypted'
    userid_           => v_UserID -- out parameter
  );

END CreateUser;

CREATE OR REPLACE AND RESOLVE JAVA SOURCE NAMED SHA256 AS 
import java.security.MessageDigest;
import oracle.sql.*;

public class SHA256
{
  public static oracle.sql.RAW get_digest( String p_string ) throws Exception
  {
   MessageDigest v_md = MessageDigest.getInstance( "SHA-256" );
   byte[] v_digest;
   v_digest = v_md.digest( p_string.getBytes( "UTF-8" ) );
   return RAW.newRAW(v_digest);
}   
}

CREATE OR REPLACE FUNCTION SHA256_ENCRYPT(p_string VARCHAR2)
RETURN RAW
AS
LANGUAGE JAVA
NAME 'SHA256.get_digest( java.lang.String ) return oracle.sql.RAW';

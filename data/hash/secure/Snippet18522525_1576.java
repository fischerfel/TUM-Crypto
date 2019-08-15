CREATE OR REPLACE AND RESOLVE JAVA SOURCE NAMED sha2 AS
import java.security.MessageDigest;
import oracle.sql.*;
public class sha2
{
    public static oracle.sql.RAW get_digest_string( String p_string, int p_bits ) throws Exception
    {
        MessageDigest v_md = MessageDigest.getInstance( "SHA-" + p_bits );
        byte[] v_digest;
        v_digest = v_md.digest( p_string.getBytes( "UTF-8" ) );
        return RAW.newRAW(v_digest);
    }

    public static oracle.sql.RAW get_digest_blob( oracle.sql.BLOB p_blob, int p_bits ) throws Exception
    {
        byte[] allBytesInBlob;
        allBytesInBlob = p_blob.getBytes(1, (int) p_blob.length());
        MessageDigest v_md = MessageDigest.getInstance( "SHA-" + p_bits );
        byte[] v_digest = v_md.digest( allBytesInBlob );
        return RAW.newRAW(v_digest);
    }
}
/
CREATE OR REPLACE FUNCTION sha2_string(p_string in VARCHAR2, p_bits in number)
RETURN RAW AS LANGUAGE JAVA
NAME 'sha2.get_digest_string( java.lang.String, int ) return oracle.sql.RAW';
/
CREATE OR REPLACE FUNCTION sha2_blob(p_byte in BLOB, p_bits in number)
RETURN RAW AS LANGUAGE JAVA
NAME 'sha2.get_digest_blob( oracle.sql.BLOB, int ) return oracle.sql.RAW';
/

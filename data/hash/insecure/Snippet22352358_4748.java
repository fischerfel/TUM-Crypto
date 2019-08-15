import java.security.MessageDigest
import org.apache.commons.net.ftp.FTPClient

MessageDigest digest = MessageDigest.getInstance("MD5")

String md5 = new FTPClient().with { ftp ->
    try {
        ftp.enterLocalPassiveMode()
        ftp.connect( address )
        ftp.login( username, password )
        ftp.retrieveFileStream( path ).with { ins ->
            if( ins == null ) {
                println "ERROR: $ftp.replyCode '${ftp.replyString.trim()}'"
            }
            else {
                try {
                    ins.eachByte( 8192 ) { buffer, nbytes ->
                        digest.update( buffer, 0, nbytes )
                    }
                    digest.digest().encodeHex().toString().padLeft( 32, '0' )
                }
                finally {
                    ins.close()
                }
            }
        }
    }
    finally {
        ftp.disconnect()
    }
}

{       java {
            imports : "import java.security.*;"
            code: """
                try {
                    MessageDigest digest = MessageDigest.getInstance("SHA-1"); 
                    String value;

                    value = (String) record.getFirstValue("message");
                    if (value != null) { digest.update(value.getBytes("ISO-8859-1"), 0, value.length()); }
                    value = (String) record.getFirstValue("timestamp");
                    if (value != null) { digest.update(value.getBytes("ISO-8859-1"), 0, value.length()); }
                    value = (String) record.getFirstValue("hostname");
                    if (value != null) { digest.update(value.getBytes("ISO-8859-1"), 0, value.length()); }

                    byte[] a = digest.digest();
                    record.replaceValues("id"
                        , String.format("%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X%02X"
                            ,a[0]  ,a[1] ,a[2] ,a[3] ,a[4] ,a[5] ,a[6] ,a[7] ,a[8] ,a[9]    //SHA-1 has exactly 20 bytes
                            ,a[10],a[11],a[12],a[13],a[14],a[15],a[16],a[17],a[18],a[19]) );
                }
                catch (java.security.NoSuchAlgorithmException e) { logger.error("hash to id: caught NoSuchAlgorithmException for SHA-1"); }
                catch (java.io.UnsupportedEncodingException e)   { logger.error("hash to id: caught UnsupportedEncodingException"); }
                finally {
                    return child.process(record);
                }
            """
        }
}

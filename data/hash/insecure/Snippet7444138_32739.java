grammar Example;

@parser::members {
  void calculateSha1(String text) {
    try {
      java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-1");
      byte[] sha1 = md.digest(text.getBytes());
      System.out.println(text + "\n" + java.util.Arrays.toString(sha1) + "\n");
    } catch(Exception e) {
      e.printStackTrace();
    }
  }
}

parse 
  :  method+ EOF
  ;

method
@after{calculateSha1($text);}
  :  'call' ID
  ;

ID      : 'A'..'Z'+;
WS      : (' ' | '\t' | '\n' | '\r')+ {$channel=HIDDEN;};
COMMENT : '/*' .* '*/' {$channel=HIDDEN;};

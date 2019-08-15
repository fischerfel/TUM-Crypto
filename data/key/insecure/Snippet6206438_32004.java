<%@page contentType="text/html; charset=iso-8859-1" language="java" 
import="java.security.*"
import="javax.crypto.Cipher"
import="javax.crypto.spec.SecretKeySpec"
import="sun.misc.*"
%>

<%
  // Decrypt authenticated hash
  String ALGORITHM = "AES";
  byte[] keyValue = "MY-SECRET-PASSWORD".getBytes();

  Key key = new SecretKeySpec(keyValue, ALGORITHM);
  Cipher c = Cipher.getInstance(ALGORITHM);
  c.init(Cipher.DECRYPT_MODE, key);
  byte[] decodedValue = new BASE64Decoder().decodeBuffer(new String(request.getParameter("auth")));
  byte[] decValue = c.doFinal(decodedValue);
  String decryptedValue = new String(decValue);
%>

<script type="text/javascript">
   var auth = '<%= decryptedValue%>';

   function AJAX(url, callback) {

      var req = init();
      req.onreadystatechange = processRequest;

      function init() {
         if (window.XMLHttpRequest) {
            return new XMLHttpRequest();
         } else if (window.ActiveXObject) {
            return new ActiveXObject("Microsoft.XMLHTTP");
         }
      }

      function processRequest () {
         // readyState of 4 signifies request is complete
         if (req.readyState == 4) {
            // status of 200 signifies sucessful HTTP call
            if (req.status == 200) {
               if (callback) callback(req.responseXML);
            }
         }
      }

      this.doGet = function() {
         req.open("GET", url, true);
         req.setRequestHeader("Authorization", "Basic " + auth);
         req.send(null);
      }
   }

   var url = "/pentaho/Home";
   var querystr = "";
   var ajax = new AJAX(url, function(){
      window.location = url;
   });

   ajax.doGet(querystr);

</script>

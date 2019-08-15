 public string function hashAdministrator(required string pass) {
   MessageDigest = createObject('java','java.security.MessageDigest');
   for(i=1; i<=5; i++) {
     md = MessageDigest.getInstance('SHA-256');
     md.update(pass.getBytes('UTF-8'));
     pass = enc(md.digest());
   }
   return pass;
 }

 private string function enc(strArr) {
   //local.strArr = str.getBytes('UTF-8');
   local.hex = ['0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'];

   savecontent variable="local.out" {
     for (local.item in strArr) {
       writeOutput(hex[bitshrn(bitAnd(240,local.item),4)+1]);
       writeOutput(hex[bitAnd(15,local.item)+1]);
     }
   };
   return local.out;
 }

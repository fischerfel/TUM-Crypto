            <%@ page import="java.util.*" %>
            <%@ page import="java.security.*" %>

            <%!
            public boolean empty(String s)
                {
                    if(s== null || s.trim().equals(""))
                        return true;
                    else
                        return false;
                }
            %>
            <%!
                public String hashCal(String type,String str){
                    byte[] hashseq=str.getBytes();
                    StringBuffer hexString = new StringBuffer();
                    try{
                    MessageDigest algorithm = MessageDigest.getInstance(type);
                    algorithm.reset();
                    algorithm.update(hashseq);
                    byte messageDigest[] = algorithm.digest();



                    for (int i=0;i<messageDigest.length;i++) {
                        String hex=Integer.toHexString(0xFF & messageDigest[i]);
                        if(hex.length()==1) hexString.append("0");
                        hexString.append(hex);
                    }

                    }catch(NoSuchAlgorithmException nsae){ }

                    return hexString.toString();

                }
            %>
            <%
            String status = request.getParameter("status");
            String firstname = request.getParameter("firstname");
            String amount = request.getParameter("amount");
            String txnid = request.getParameter("txnid");
            String posted_hash = request.getParameter("hash");
            String key = request.getParameter("key");
            String productinfo = request.getParameter("productinfo");
            String email = request.getParameter("email");
            String salt = "GQs7yium";

            Enumeration paramNames = request.getParameterNames();
                Map<String,String> params= new HashMap<String,String>();
                    while(paramNames.hasMoreElements()) 
                {
                        String paramName = (String)paramNames.nextElement();

                        String paramValue = request.getParameter(paramName);

                    params.put(paramName,paramValue);
                }

            String retHashSeq = "salt|status|udf10|udf9|udf8|udf7|udf6|udf5|udf4|udf3|udf2|udf1|email|firstname|productinfo|amount|txnid|key";

                    String[] hashVarSeq=retHashSeq.split("\\|");

                        for(String part : hashVarSeq)
                        {
                            retHashSeq= (empty(params.get(part)))?retHashSeq.concat(""):retHashSeq.concat(params.get(part));
                            retHashSeq=retHashSeq.concat("|");
                        }

                   String hash = hashCal("SHA-512", retHashSeq);

                   if (hash != posted_hash) {
                        out.print(hash+"<br/>");
                        out.print(posted_hash+"<br/>");
                       out.print("Invalid Transaction. Please try again");
                       }
                   else {

                      out.print("<h3>Thank You. Your order status is "+status+"</h3>");
                      out.print("<h4>Your Transaction ID for this transaction is "+txnid+"</h4>");
                      out.print("<h4>We have received a payment of Rs. "+amount+"Your order will soon be shipped.</h4>");

            }         
            %>

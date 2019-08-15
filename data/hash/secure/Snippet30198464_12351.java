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
                String merchant_key="JBZaLc";
                String salt="GQs7yium";
                String action1 ="";
                String base_url="https://test.payu.in";
                int error=0;
                String hashString="";



                Enumeration paramNames = request.getParameterNames();
                Map<String,String> params= new HashMap<String,String>();
                    while(paramNames.hasMoreElements()) 
                {
                        String paramName = (String)paramNames.nextElement();

                        String paramValue = request.getParameter(paramName);

                    params.put(paramName,paramValue);
                }
                String txnid ="";
                if(empty(params.get("txnid"))){
                    Random rand = new Random();
                    String rndm = Integer.toString(rand.nextInt())+(System.currentTimeMillis() / 1000L);
                    txnid=hashCal("SHA-256",rndm).substring(0,20);
                }
                else
                    txnid=params.get("txnid");
                    /*udf2 = txnid;*/
                String txn="abcd";
                String hash="";
                String hashSequence = "key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5|udf6|udf7|udf8|udf9|udf10";
                if(empty(params.get("hash")) && params.size()>0)
                {
                    if( empty(params.get("key"))
                        || empty(params.get("txnid"))
                        || empty(params.get("amount"))
                        || empty(params.get("firstname"))
                        || empty(params.get("email"))
                        || empty(params.get("phone"))
                        || empty(params.get("productinfo"))
                        || empty(params.get("surl"))
                        || empty(params.get("furl"))
                        || empty(params.get("service_provider"))
                    )

                        error=1;
                    else{
                        String[] hashVarSeq=hashSequence.split("\\|");

                        for(String part : hashVarSeq)
                        {
                            hashString= (empty(params.get(part)))?hashString.concat(""):hashString.concat(params.get(part));
                            hashString=hashString.concat("|");
                        }
                        hashString=hashString.concat(salt);


                         hash=hashCal("SHA-512",hashString);
                        action1=base_url.concat("/_payment");
                    }
                }
                else if(!empty(params.get("hash")))
                {
                    hash=params.get("hash");
                    action1=base_url.concat("/_payment");
                }
            %>

            <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
            <html xmlns="http://www.w3.org/1999/xhtml">
            <head>
            <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
            <title>-----</title>
            <!-- // Stylesheets // -->
            <link rel="stylesheet" href="../css/style.css" type="text/css">
            <script type="text/javascript" src="../js/ajax.js"></script>
            <script type="text/javascript" src="../js/service.js"></script>

            <!-- bootstrap framework files -->
            <link rel="stylesheet" href="../../bootstrap/3.3.4/css/bootstrap.min.css">
            <link rel="stylesheet" href="../../bootstrap/3.3.4/css/bootstrap-theme.min.css">
            <script src="../../ajax/libs/jquery/1.11.2/jquery.min.js"></script>
            <script src="../../bootstrap/3.3.4/js/bootstrap.min.js"></script>
            <script>
            var hash='<%= hash %>';
            function submitPayuForm() {

                if (hash == '')
                    return;

                  var payuForm = document.forms.payuForm;
                  payuForm.submit();
                }
            </script>
            </head>
            <body onLoad="submitPayuForm();">
            <div style="border:#999999 1px solid; margin:20px auto; width:700px;">
                <div class="container">
                <div class="row">
                    <div class="col-md-8">
                        <form action="<%= action1 %>" method="post" name="payuForm" class="form-horizontal">
                        <input type="hidden" name="key" value="<%= merchant_key %>" />
                              <input type="hidden" name="hash" value="<%= hash %>"/>
                              <input type="hidden" name="txnid" value="<%= txnid %>" />
                             <!-- <input type="hidden" name="udf2" value="<%//= txnid %>" />-->
                              <input type="hidden" name="service_provider" value="payu_paisa" />
                <h1>Payu Money Payment Gateway</h1>           
                <span style="color:#FF0000; font-weight:bold; font-style:italic;">All Fields are Mandatory</span>
                <br /><br />
                <div class="form-group">
                    <label for="Amount" class="control-label col-xs-2">Amount:</label>
                    <div class="col-xs-8">
                    <input name="amount" value="<%= (empty(params.get("amount"))) ? "" : params.get("amount") %>" class="form-control" />
                    </div>
                </div>

                <div class="form-group">
                    <label for="FirstName" class="control-label col-xs-2">First Name:</label>
                    <div class="col-xs-8">
                    <input name="firstname" id="firstname" value="<%= (empty(params.get("firstname"))) ? "" : params.get("firstname") %>" class="form-control" />
                    </div>
                    </div>

                    <div class="form-group">
                        <label for="Email" class="control-label col-xs-2">Email:</label>
                        <div class="col-xs-8"><input name="email" id="email" value="<%= (empty(params.get("email"))) ? "" : params.get("email") %>" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                    <label for="Phone" class="control-label col-xs-2">Phone:</label>
                    <div class="col-xs-8">
                    <input name="phone" value="<%= (empty(params.get("phone"))) ? "" : params.get("phone") %>" class="form-control" />
                        </div>
                    </div>
                    <div class="form-group">
                    <label for="productinfo" class="control-label col-xs-2">Product Info:</label>
                    <div class="col-xs-8">
                    <input name="productinfo" value="<%= (empty(params.get("productinfo"))) ? "" : params.get("productinfo") %>" size="64" class="form-control" />
                    </div>
                    </div>
                    <div class="form-group">
                    <label for="FirstName" class="control-label col-xs-2">Success URI:</label>
                    <div class="col-xs-8">
                    <input name="surl" value="<%= (empty(params.get("surl"))) ? "" : params.get("surl") %>" size="64" class="form-control" />
                    </div>
                    </div>
                    <div class="form-group">
                    <label for="FirstName" class="control-label col-xs-2">Failure URI:</label>
                    <div class="col-xs-8">
                    <input name="furl" value="<%= (empty(params.get("furl"))) ? "" : params.get("furl") %>" size="64" class="form-control" />
                    </div>
                    </div>
                          <% if(empty(hash)){ %>
                        <div class="form-group" align="center">
                        <input type="submit" value="Submit" class="btn btn-success btn-lg"/>
                        </div>
                        <% } %>
                            </form>
                        </div>
                    </div>
                </div>
              </div>
            </body>
            </html>

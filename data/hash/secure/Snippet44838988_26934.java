public String getHashes(String key,String txnid, String amount, String productInfo, String firstname, String email,
String user_credentials, String udf1, String udf2, String udf3, String udf4, String udf5, String offerKey,
String cardBin,String salt) {
JSONObject response = new JSONObject();
try {

//            sha512(key|txnid|amount|productinfo|firstname|email|udf1|udf2|udf3|udf4|udf5||||||SALT)
//            hiESPS|7174761030002574230|12|product_info|firstname|xyz@gmail.com|udf1|udf2|udf3|udf4|udf5||||||xXZAKUi8
String ph = checkNull(key) + "|" + checkNull(txnid) + "|" + checkNull(amount) + "|" + checkNull(productInfo)
+ "|" + checkNull(firstname) + "|" + checkNull(email) + "|" + checkNull(udf1) + "|" + checkNull(udf2)
+ "|" + checkNull(udf3) + "|" + checkNull(udf4) + "|" + checkNull(udf5) + "||||||" + salt;
String paymentHash = getSHA(ph);
response.put("payment_hash", paymentHash);
response.put("get_merchant_ibibo_codes_hash", generateHashString("get_merchant_ibibo_codes", user_credentials,key,salt));
response.put("vas_for_mobile_sdk_hash", generateHashString("vas_for_mobile_sdk", user_credentials,key,salt));
response.put("payment_related_details_for_mobile_sdk_hash",
generateHashString("payment_related_details_for_mobile_sdk", user_credentials,key,salt));

//for verify payment (optional)
if (!checkNull(txnid).isEmpty()) {
response.put("verify_payment_hash",
generateHashString("verify_payment", txnid,key,salt));
}

if (!checkNull(user_credentials).isEmpty()) {
response.put("delete_user_card_hash", generateHashString("delete_user_card", user_credentials,key,salt));
response.put("get_user_cards_hash", generateHashString("get_user_cards", user_credentials,key,salt));
response.put("edit_user_card_hash", generateHashString("edit_user_card", user_credentials,key,salt));
response.put("save_user_card_hash", generateHashString("save_user_card", user_credentials,key,salt));
response.put("payment_related_details_for_mobile_sdk_hash",
generateHashString("payment_related_details_for_mobile_sdk", user_credentials,key,salt));
}

// check_offer_status
if (!checkNull(offerKey).isEmpty()) {
response.put("check_offer_status_hash", generateHashString("check_offer_status", offerKey,key,salt));
}

// check_isDomestic
if (!checkNull(cardBin).isEmpty()) {
response.put("check_isDomestic_hash", generateHashString("check_isDomestic", cardBin,key,salt));
}
}catch (Exception e){

}

return response.toString();

}

private String generateHashString(String command, String var1,String key,String salt) {
return getSHA(key + "|" + command + "|" + var1 + "|" + salt);
}

private String checkNull(String value) {
if (value == null) {
return "";
} else {
return value;
}
}

private String getSHA(String str) {

MessageDigest md;
String out = "";
try {
md = MessageDigest.getInstance("SHA-512");
md.update(str.getBytes());
byte[] mb = md.digest();

for (int i = 0; i < mb.length; i++) {
byte temp = mb[i];
String s = Integer.toHexString(new Byte(temp));
while (s.length() < 2) {
s = "0" + s;
}
s = s.substring(s.length() - 2);
out += s;
}

} catch (NoSuchAlgorithmException e) {
e.printStackTrace();
}
return out;

}

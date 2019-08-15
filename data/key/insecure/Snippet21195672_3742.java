def hash_IAM(user_password):
#http://docs.aws.amazon.com/ses/latest/DeveloperGuide/smtp-credentials.html
    if DEBUG: debug_message("Hashing for IAM to SES values")

    #Pseudocode:
    #encrypted_intent = HmacSha256("SendRawEmail", "AWS Secret Key")
    #encrypted_intent = concat(0x02, encrypted_intent)
    #resultant_password = Base64(encrypted_intent)

    #private static final String KEY = "AWS SECRET ACCESS KEY";
    AWS_SECRET_ACCESS_KEY = user_password

    # private static final String MESSAGE = "SendRawEmail";
    AWS_MESSAGE = "SendRawEmail"

    #private static final byte VERSION =  0x02;
    #see chr(2) below.

    #SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "HmacSHA256");
    #Mac mac = Mac.getInstance("HmacSHA256");
    #mac.init(secretKey);
    #If I understand correctly, SecretKeySpec is just a vessel for the key and a str that mac.init expects..
    #http://developer.android.com/reference/javax/crypto/spec/SecretKeySpec.html [(key data), (algorithm)]
    #http://docs.oracle.com/javase/7/docs/api/javax/crypto/Mac.html#init(java.security.Key, java.security.spec.AlgorithmParameterSpec)

    #in Python 2, str are bytes
    signature = hmac.new(
        key=AWS_SECRET_ACCESS_KEY,
        msg=AWS_MESSAGE,
        digestmod=hashlib.sha256
    ).digest()

    # Prepend the version number to the signature.
    signature = chr(2) + signature

    # To get the final SMTP password, convert the HMAC signature to base 64.
    signature = base64.b64encode(signature)

    return signature

import java.security.MessageDigest;

int a = 9
nonce = ""
for(i = 0; i < 10; i++)
{
 random = new Random()
 randomInteger= random.nextInt(a)
 nonce = nonce + randomInteger
}

Byte[] nonceBytes = nonce.getBytes()

def XRMGDateTime =  new Date().format("yyyy-MM-dd'T'HH:mm:ss",     TimeZone.getTimeZone( 'BTC' ));

Byte[] creationBytes = XRMGDateTime.getBytes()

def password = testRunner.testCase.testSuite.getPropertyValue(     "XRMGPassword" )

EncodedNonce = nonce.getBytes("UTF-8").encodeBase64()

MessageDigest cript = MessageDigest.getInstance("SHA-1");
        cript.reset();
        cript.update(password.getBytes());
        hashedpw = cript.digest();

MessageDigest cript2 = MessageDigest.getInstance("SHA-1");
        cript2.update(nonce.getBytes());;
        cript2.update(XRMGDateTime.getBytes());
        cript2.update(hashedpw);

PasswordDigest = cript2.digest()

EncodedPasswordDigest = PasswordDigest.encodeBase64();


def StringPasswordDigest = EncodedPasswordDigest.toString()
def encodedNonceString = EncodedNonce.toString()

testRunner.testCase.setPropertyValue( "passwordDigest", StringPasswordDigest    )  
testRunner.testCase.setPropertyValue( "XRMGDateTime", XRMGDateTime ) 
testRunner.testCase.setPropertyValue( "XRMGNonce", encodedNonceString )   
testRunner.testCase.setPropertyValue( "Nonce", nonce ) 

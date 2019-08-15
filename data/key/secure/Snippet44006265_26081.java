import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.security.InvalidKeyException


def emailAddress = '"test@test.com"'

def hmac_sha256(String secretKey, String data) {
    try {
        Mac mac = Mac.getInstance("HmacSHA256")
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256")
        mac.init(secretKeySpec)
        byte[] digest = mac.doFinal(data.getBytes())
        return digest
    } catch (InvalidKeyException e) {
        throw new RuntimeException("Invalid key exception while converting to HMac SHA256")
    }
}

secret='1234'

// this will fail
def hash = hmac_sha256(secret, '/sf/v3/Accounts/GetByUser{"username":' + emailAddress + ',"employeesonly":false,"singleplane":false,"clientId":"someID"}')

// this will pass
def hash = hmac_sha256(secret, '/sf/v3/Accounts/GetByUser{"username":"test@test.com","employeesonly":false,"singleplane":false,"clientId":"someID"}')

encodedData = hash.encodeHex().toString()
println encodedData

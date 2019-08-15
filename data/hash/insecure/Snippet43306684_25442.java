import java.security.MessageDigest
def testStep = testRunner.testCase.testSteps["3D Secure Call"]
def str = new StringBuilder();
 for (prop in testStep.getPropertyList()){

    if(prop.getName() != "K" && prop.getName() != "RawRequest" && prop.getName() != "Domain" && prop.getName() != "Password" && prop.getName() != "ResponseAsXml" && prop.getName() != "Request" &&  prop.getName() != "RawRequest" && prop.getName() != "Response" && prop.getName() != "Username" && prop.getName() != "Endpoint"){
        str.append(prop.getName() + "=" + testStep.getPropertyValue(prop.getName()) + "&" )

    }
}
str.append("K=1473942615907cuwmviz")
return (MessageDigest.getInstance("MD5").digest(str.bytes).encodeHex().toString())

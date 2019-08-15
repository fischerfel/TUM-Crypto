<target name="appendMD5">
    <copy todir="teststack">
        <fileset dir="css/" includes="**/*.css"/>
        <scriptmapper language="javascript"><![CDATA[
            var File = Java.type('java.io.File');
            var Files = Java.type('java.nio.file.Files');
            var MessageDigest = Java.type('java.security.MessageDigest');
            var DatatypeConverter = Java.type('javax.xml.bind.DatatypeConverter');

            var buildDir = MyProject.getProperty('builddir');
            var md5Digest = MessageDigest.getInstance('MD5');
            var file = new File(buildDir, source);
            var fileContents = FIles.readAllBytes(file.toPath());

            var hash = DatatypeConverter.printHexBinary(md5Digest.digest(fileContents));
            var baseName = source.substring(0, source.lastIndexOf('.'));
            var extension = source.substring(source.lastIndexOf('.'));

            self.addMappedName(baseName + '-' + hash + extension);
        ]]></scriptmapper>
    </copy>
</target>

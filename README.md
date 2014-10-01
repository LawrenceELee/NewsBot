Twitter Bot written in Java that tweets/posts about world news, natural
disasters, etc.

Building this project provides an oppurtunity to learn the 
* Twitter API.
* RSS/Atom feeds.
* OAuth
* ...more as ideas come to me...

Dependencies:
* rome
* hbc

Compile:
* Using maven (currently using maven, directory structure not set up for ant):
    * mvn package && mvn exec:java -Dexec.mainClass="path.to.class.App"
      mvn package will build java files (compile and make jar)
      mvn exec:java -Dexec.mainClass="path.to.class.App" will run App.class
    * Example:
      mvn package && mvn exec:java -Dexec.mainClass="com.twitter.hbc.example.SampleStreamExample" -Dexec.args"...your api keys here..."

* Using ant:
    * ant (will build based on your build.xml)
    * ant <run-target> (will run class based on build.xml)

* Using javac:
    * If you include jars in your classpath, you need to specifiy the path to
      the specific jar file.
      For example, if the jars are in the same dir as the source files:
      'javac -cp "jdom-1.1.1.jar;rome-1.0.jar" RomeLibraryExample.java'
      (If in Windows, use ; (semi-colon). If in linux, use : (colon).)

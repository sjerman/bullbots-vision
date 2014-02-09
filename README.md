This Eclipse project contains code to run on a laptop 'coprocessor' on a First Robotics
FRC robot.

The code provides a means to execute vision processoring for a robot in both autonomous and
tele-operated modes and makes use of OpenCV for image processing.

The program communicates with the robot using First's NetworkTables protocol.

To run compiled jar on a Linux server (with OpenCV and Java 7 installed):

java -Dlog4j.configurationFile=log4j2.xml -Djava.library.path=/usr/local/share/OpenCV/java -jar processImage.jar

You will also need the vision.properties file and, optionally, the log4j2.xml file in your current directory.

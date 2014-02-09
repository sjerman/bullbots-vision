This Eclipse project contains code to run on a laptop 'coprocessor' on a First Robotics
FRC robot.

The code provides a means to execute vision processoring for a robot in both autonomous and
tele-operated modes and makes use of OpenCV for image processing.

The program communicates with the robot using First's NetworkTables protocol.

To run built file:

java -Dlog4j.configurationFile=log4j2.xml -Djava.library.path=/usr/local/share/OpenCV/java -jar processImage.jar

You will also need to eh vision.properties file in your current directory. 
Assumes a copy of the log4j config as well.
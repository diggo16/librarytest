SET "mvnErr="
CALL mvn clean package || SET mvnErr=1


IF DEFINED mvErr (
	ECHO Build failed!
	GOTO:EOF
)

ECHO Build OK

ECHO Shutting down Tomcat
SET "CATALINA_HOME=C:\tomcat\Tomcat8.0"
CALL "C:\tomcat\Tomcat8.0\bin\shutdown.bat"
TIMEOUT 3

ECHO Removing old files from server directory
DEL "C:\tomcat\Tomcat8.0\webapps\librarytest.war"
DEL "C:\tomcat\Tomcat8.0\webapps\librarytest-rest.war"
DEL /S /Q "C:\tomcat\Tomcat8.0\webapps\librarytest"
DEL /S /Q "C:\tomcat\Tomcat8.0\webapps\librarytest-rest"

ECHO Starting Tomcat
CALL "C:\tomcat\Tomcat8.0\bin\startup.bat"
TIMEOUT 3

ECHO Deploying REST application
CALL curl -T "C:\Users\lemor1\workspace\librarytest\librarytest-rest\target\librarytest-rest.war" "http://admin:1234567890@localhost:8080/manager/text/deploy?path=/librarytest-rest&update=true"
ECHO Deploying GUI application
CALL curl -T "C:\Users\lemor1\workspace\librarytest\librarytest-gui\target\librarytest.war" "http://admin:1234567890@localhost:8080/manager/text/deploy?path=/librarytest&update=true"

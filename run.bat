@echo off
echo Starting StaticVoid RPG...

rem Compile all Java files in src/StaticVoid
javac src/StaticVoid/*.java

rem Run Main from that directory (no package declaration)
java -cp src/StaticVoid Main

pause

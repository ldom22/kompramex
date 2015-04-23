cd obj
del *.class
cd ..
cd src
javac *.java
cd .. 
xcopy src\*.class obj\ 
cd src
del *.class
cd .. 
cd obj
java Bestbuy
java Costco
java Liverpool
java Sanborns
java Sears
java Walmart
cd .. 
xcopy obj\*.txt results\ 
cd obj
del *.txt
cd ..
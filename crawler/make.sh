#!/bin/bash

#delete previous objects
cd obj
rm *.class
cd .. 

#compile all java source files
cd src
javac Bestbuy.java
cp *.class ..\obj
cd ..
cd src
rm *.class
cd ..

#run all crawlers
cd obj
java Bestbuy
java Costco
java Liverpool
java Sanborns
java Sears
java Walmart
cd ..

#copy results to result folder
cp obj\*.txt results\
cd obj
rm *.txt
cd ..

#consolidate results and upload

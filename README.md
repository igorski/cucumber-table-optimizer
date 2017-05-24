Cucumber Table Optimizer
========================

CTO is a simple command-line tool that splits Cucumber .feature-files
(containing large tables) into separate Cucumber files.

The problem of having a large table in a Cucumber .feature is that the
feature will run on a single VM for every row in the table. This can
take a lot of time to complete. By splitting the source file into
separate .features you have the benefit that each table row will be
executed in a separate VM, allowing for optimal parallelisation and
reduced running time.

Ideally you don't write your tests around CTO, but write the Cucumber
tests as you ideally would. CTO should be run as a step prior to
running your tests by taking the input files and writing the optimized
output into a temporary output folder. From this folder the .feature
files should be taken during test execution.

How to build
------------

You will need Gradle to build the project.

The "build" target should suffice to compile and package the .JAR

If however, you are not interested in modifying the source code and just
want to use CTO right out of the box, use the pre-built .JAR file in the
_/dist_-folder.

How to run
----------

CTO is a command line util, and requires a Java Runtime Environment
(available by default on most operating systems). You can run it by
opening a terminal / command prompt and typing:

    java -jar cto.jar {SOURCE_FOLDER} {OUTPUT_FOLDER}

Where _{SOURCE_FOLDER}_ should be the relative/absolute path to your
Cucumber .feature files and _{OUTPUT_FOLDER}_ the relative/absolute
path to the desired output folder, e.g.:

    java -jar cto.jar ~/Documents/Tests ./Temp/OptimizedTests

You can test CTO by running the following in the repository's root:

    java -jar ./dist/cto.jar ./dist/input ./dist/output

Where you can find the optimized Cucumber files in the _./dist/output_-folder.


# CalculatorGUI
An expression-based calculator.

This program was specifically compiled and created using IntelliJ Idea with Maven
to create fat Jars. The source code is included in the "src" directory and could
be plopped into IntelliJ with a 11.0.12 jdk for a smooth experience. Pngs are also
included for icons in the GUI-based application.

Included shaded .jar executable doesn't work on other environments. To create working executable, follow these steps:
  1) In IntelliJ IDEA, double tap ctrl
  2) type command "mvn clean"
  3) type command "mvn install"
  4) The working **shaded** executable fat jar will be located in the CalculatorGUI/target folder.

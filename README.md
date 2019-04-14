
# YampaConvert

YampaConvert program is translation tool from designated Yampa program to xml file. This xml file 
expresses the model of signal function behavior in Yampa program and can be used with Uppaal. 
In Uppaal, We can simulation and verify Yampa program. For example, YampaConvert program
can translation the files that has been published on the website https://github.com/SqlabNR/YampaConvertTargetProgram



# Program Language

JavaCC



# Complile Method

Over the command prompt:

$> javacc Calc.jj
$> javacc Function.jj
$> javac *.java



# Usage

$>java YampaConvert "YampaProgram" "FunctionName" "InitVal1" "InitVal2" ...

"YampaProgram":Yampa program file name.
"FunctionName":The function that you want to translate to the model. This function needs to be used "switch" function. 
"InitVal":Initial values of the function arguments; 

# Author 

Riku Nakane (in Nagoya University)

# Reference

Riku Nakane and Shoji Yuen, Behavioral Verification of Yampa Programs in a Discrete Runtime Environment using Uppaal

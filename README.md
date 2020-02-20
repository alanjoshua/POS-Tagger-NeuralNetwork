# POS-Tagger-NeuralNetwork
<br>
<br>

## Introduction

* This program makes use of my Neural Network library to create an application with a GUI which could create, train, visualize, save, load and test models
in real time to tag sentences with parts-of-speech. 

* It makes use of the Stanford pos-tagger library to produce the training data

* It also uses lingPipe and pdfBox to handle sample training data parsing. 
* It used the jblas matrix library to handle the required matrix mathematics.

## Usage

* Download the entire "Runnable jar" folder and run the "app.jar" file. Make sure you have java 12 installed in your machine.
* It make take from 10 seconds to a couple minutes to load depending on your computer's specs as right now the program has to load and compute the training data each time when the program is started.
* Utility to pre calculate and pre-load parsed training data is in work. The code is already in place but this project is from 2 years ago so I have to figure out how the utility works. Until then, please bare with the loading time. 

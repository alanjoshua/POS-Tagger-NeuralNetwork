# POS-Tagger-NeuralNetwork
<br>

## Introduction

* This program makes use of my Neural Network library to create an application with a GUI which could create, train, visualize, save, load and test models in real time to tag sentences with parts-of-speech. 

* It makes use of the Stanford pos-tagger library to produce the training data, lingPipe and pdfBox to handle data parsing, and jblas to handle the required matrix mathematics.

## Features

* Saving and Loading models, including metadata
* Ability to test the current model midway through training using user-input test sentences and then being able to resume training without having to restart the program
* Autosave the current model being trained
* Graphs to output validation and cost function values in real time
* Visualize the current model being trained including input and output
* Ability to change how often the model is refreshed on the screen
* Ability to control learning parameters in real time

## Usage

* It make take from 10 seconds to a couple minutes to load depending on your computer's specs as right now the program has to load and compute the training data each time when the program is started.
* Utility to pre calculate and pre-load parsed training data is in work. The code is already in place but this project is from 2 years ago so I have to figure out how the utility works. Until then, please bare with the loading time. 

### GUI Controls

* LOAD - load a model from the Resource folder
* SAVE - Save current model to the Resource folder
* TOGGLE GRAPHS - Switches between a graph which shows the current validation percentage and cost function
* INPUT - Use an user input sentence to test the current model and letting the model tag the input sentence with parts-of-speech
* LAMDA, LEARNING RATE - These are parameters which control the training algorithm. Change these values to try different values.
* SCALE X, SCALE Y - Change the graph axes scales. These values are not shared between the two graphs
* OPEN NEW - Create a new, random model
* TRAIN - Resume training
* STOP - Pause training
* TICK RATE - Controls the refresh rate of the model on the screen. 

### Running as Jar
* Download the entire "Runnable jar" folder and run the "app.jar" file. Make sure you have java 12 installed in your machine and make sure to run the program from within the "Runnable jar" folder as it contains Resources and external libraries necessary for the project.

### Setting up as Eclipse project
* Import the project to Eclipse using File > import > git > projects from Git using smart import
* In the last page of the import wizard, select "Search for Nested Projects" and make sure to select only "POS-Tagger-NeuralNetwork/POS-Tagger-NeuralNetwork".
* Make sure the "Resources" folder in the root folder of the project is added to classpath.
* ResourcePool.outputMode should be set to ResourcePool.outputProject in the main method of the project (This should be already set by default)

### Exporting Jar file from Eclipse Project

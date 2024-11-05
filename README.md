# Assignment 4 Image Processing MVC Overview

For the MVC Design, there are four main interfaces and classes (Model, View, Controller, and Photo) which represent the Model, View, Controller and Image respectively. There is also a Driver.java class where the program should run.

The resources folder included, contains sample images (shown as sample-ppm.ppm, sample-png.png, sample-jpg.jpg which are identical just a different format), example scripts (script.txt), and all processed versions of the sample image.

This project exclusively takes in user keyboard input when the program runs, taking in commands or script txt files. After running Driver.java user will be continually prompted for commands or script files. Key word "exit" allows user to terminate the program from the prompt.

Example process (after running Driver.java):
```sh
Please provide a script txt file or a command input
resources/script.txt
```
or
```sh
Please provide a script txt file or a command input
load resources/sample-png.png pngLandscape
```

Please run resources/script.txt for our provided script to you. It will create a directory landscape/ that will include all processed results of the sample images.

Here is the public site for where we got our images from:
https://filesamples.com/categories/image

And for each specific image type
https://filesamples.com/formats/jpg
https://filesamples.com/formats/png
https://filesamples.com/formats/ppm



The Design is fairly simple:

The class ImagePhoto implements Photo is a class that represents an individual image. It contains height, width, name, and its pixel representation (a 3d array representing its coordinates and rgb values).

The class ImageView implements View is a class that is responsible for outputting all prompts, errors, feedback, and updates to the user. All communication of what is to be said to the user is dictated by the Controller, but output out by the View.

The class ImageModel implements Model is a class that is responsible for manipulating given photo(s) and returning adjusted photo(s) based on the command criteria instructed by the Controller. This includes component transformations, rgb-combine, rgb-split, flips, brightening, blurring, sharpening, and sepia. The model also contains the hash map imageDirectory of image names and their respective Photo represenations that is updated on any given successful command.

The class ImageController implements Controller is a class that is responsible for receiving, parsing, loading, and dictating all input scripts and commands to the Model in order for the Model to manipulate the given images and to update the imageDirectory. The controller also is responsible for saving files as well as dictating what the View has to print out.
# Home Assignment - Pilloxa

# Introduction

Pilloxa's Pillboxes can communicate with the app both through Bluetooth and via the mobile network. In a fictional future the strain on the mobile network has increased dramatically due to new IoT devices entering the market. The providers then forcefully deactivate parts of the grid. The active or inactive locations can be found as a 2D coordinate where every coordinate represents a point on the map. The coordinate system currently is made up of a 700x700 grid meaning 490 000 locations. Whenever the mobile provider decides to activate, deactivate or toggle a location that command is passed on to us and stored in a file.

This information is essential for us since if we know that we have a Pillbox in the deactivated are then that Pillbox will not be able to submit any data via the mobile network.

# Assignment

## Part I - Parsing the input commands

The first part of the assignment is to parse the input data to determine how many locations on the grid that are currently active. The input data is found in the file [input-data.txt](input-data.txt).

The input file contains commands that specify if the locations in a given sub-grid should be activated, deactivated or toggled. At the beginning all locations were deactivated. Toggling a location means that it goes from deactivated to activated or from activated to deactivated.

For example:

The command `activate 0,0 to 2,2` will activate the 9 locations of the specified subgrid. The coordinates are hence inclusive.

The output of this part of the assignment should be a function that returns a number that indicates, after all the commands have been run, how many locations are currently active.

## Part II - Plotting the map

Since this is a coordinate system we would like to be able to visualize it. For that you should use a client that fetches the coordinate values from an API. You must hence add an endpoint to the service in Part I and a frontend browser client.

Example code of how to plot data on HTML5 canvas from an array of pixels that are either `true` (white) or `false` (black):

```javascript
function plotImageData(ctx, binaryPixelArray) {
  const pixelArray = binaryPixelArray
    .map(v => {
      const pixel = v ? 255 : 0;
      const color = [pixel, pixel, pixel];
      return [...color, 255];
    })
    .flat();
  const pictureSize = Math.sqrt(binaryPixelArray.length);
  const imageData = new ImageData(
    new Uint8ClampedArray(pixelArray),
    pictureSize,
    pictureSize
  );
  ctx.putImageData(imageData, 0, 0);
}
```

# Scope

Typically we expect you to not dedicate much more than 8 hours to this assignment. The main objective is that the tasks are solved and extra points will be given for a well thought through architecture, code elegance and simplicity.

You may choose any programming language but please consider that the ones we mainly use at Pilloxa are:

1. Clojure and ClojureScript
2. JavaScript
3. Ruby

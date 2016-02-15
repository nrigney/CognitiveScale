## Design

The general approach of this design has several classes:

* Board: The model of the overall game.  The Board is responsible for loading itself from a string, printing a z-slice, computing scores, holding the game pieces, and applying ship commands (movement and firing)
* Point[23]d: Convenience classes for storing 2d and 3d points.  In interest of time and complexity, I didn't use a more generic system for coordinates -- a more detailed implementation would be able to handle multiple coordinate systems abstracting away the details of each.
* FiringPattern: A convenience class for applying firing pattern lists.  Since "firing" a torpedo is really just placing it over a mine in the board, there's not a whole lot of logic here.  It's essentially just shorthand for a Seq.  In a more complex system, there would likely be several functions here for calculating trajectories, etc.
* Ship: Responsible for taking a set of commands and calling board functions to apply them.

The Board responds to commands in a command "queue" which is a List of Lists of BoardCommands.  When each List of Commands is complete the Board will score, print, and drop the ship, returning the resultant board.
This message passing is the easiest way to ensue type safe manipulation of global data.  When done properly, it's not overly heavy in memory usage, but for large data sets it is likely not effecient.

Note the Unit tests are not executing properly. This began very late in the project, and I have not had a chance to clean it up.

Running the application can be easiest accomplished with:
```sbt "run <board file> <script file>" ```
Note the quotes are important.  This project assumes you are running sbt 0.13.5 or higher with scala 2.10 or higher and was tested with sbt 0.13.8 and 2.10.3.
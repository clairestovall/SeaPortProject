# SeaPortProject
Object Oriented and Concurrent Programming course semester project, which simulates some of the aspects of a number of SeaPorts.

The program reads in a formatted data file and generates a World, which contains SeaPorts, Docks, CargoShips, PassengerShips, Jobs, and People. After reading in the file, the program simulates ships arriving at docks in seaports, at which point any jobs on the ship will begin. Each job requires a person with specific skills, so the program must match appropriate workers with available jobs. For each job, the program creates a separate thread so that the jobs can run concurrently.

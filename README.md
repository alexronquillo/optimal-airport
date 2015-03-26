# optimal-airport
Optimal Airport Configuration [OAC] Simulation Model

The Optimal Airport Configuration [OAC] simulation model describes a simple airport system consisting of arriving airplanes, departing airplanes, and airplanes at gates and loading unloading areas. Airplanes approach the airport and communicate with the air traffic controller to determine when the plane can land. Once the landing conditions have been met, the airplane begins its landing procedure. After landing, the plane awaits an available gate or bay depending on the plane type. After a gate or bay becomes available, the plane will approach the gate and await passenger/cargo loading/unloading. After the plane has received its services at the gate/bay, the plane coordinates with the air traffic controller to approach the departure runway. When sufficient conditions have been met, the plane will enter the runway and takeoff. The purpose of the OAC simulation model is to determine the resources for an airport which result in the fewest number of rejected airplanes and optimal performance metrics.

The OAC model is an multithreaded object oriented simulation model with standard (shared) resources, multiple servers, multiple queues, synchronous process cooperation, and conditional waiting.

authors: Alex Ronquillo, Nick Padgett, and Ryan Pont

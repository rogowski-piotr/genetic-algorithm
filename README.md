# genetic-algorithm

Implementation of the [Genetic algorithm](http://en.wikipedia.org/wiki/Genetic_algorithm) in Java. 
Using for optimization of multivariable functions for example [Rastrigin function](https://en.wikipedia.org/wiki/Rastrigin_function).

### Block diagram of the genetic algorithm ###
![Block-diagram](https://github.com/rogowski-piotr/genetic-algorithm/blob/master/img/algorithm.jpg)

### Genetic operators used ###
- representation of the solution in binary form
- for crossing individuals using multipoint crossing (each of the genes separately)
- selection of individuals suitable for reproduction using the roulette method
- standard mutation operator negating a single bit in a solution with some probability

### example solution ###
- parameters:
  - generations = 300
  - amount of individuals = 25
  - propability of mutation = 0.1

![example](https://github.com/rogowski-piotr/genetic-algorithm/blob/master/img/Rastrigin%20function%20values.png)
- BLUE the best solution in single population
- ORANGE the best solution found so far

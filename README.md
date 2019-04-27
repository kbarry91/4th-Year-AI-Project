# Project Details

| Project Details   |     |
| --- | --- |
| **Course** | BSc (Hons) in Software Development  |
| **Module** | Artificial Intelligence|
| **College** | [GMIT](http://www.gmit.ie/) Galway |
| **Student** | [Kevin Barry](https://github.com/kbarry91) |
| **Project Supervisors** | Dr. John Healy|
| **Project Title** | Artificial Intelligence Maze Algorithm  |
| **Brief** | Controlling Game Cha racters with Neural Networks and Fuzzy Logic |

# Project Outline 

This repository contains a maze game combining Neural Networks, Fuzzy Logic and Artificial Intelligence Search Algorithms.
The game implements the use of Fuzzy Logic using linguistic variables and membership functions to define a set of rules for the game.
AI search algorithms are used to find the goal node at it's current row/column position in the game.
The spiders are fully threaded and some are are set to search for the goal node (the player) using the AStar Algorithm.

How the Game is played:
In order to complete the game the player must navigate the maze to find the exit without dying. Along the way the player will be chased and must avoid attack from the spiders. 
Weapons can be found around the maze to help the player fight back. Health pickups are also available and will increase a damaged players health by 30 points.

 ---
 
# Heuristic search (A Star) 

The rationale behind  choosing this heuristic search algorithm is based on the fact that this algorithm is the most efficient for this project.
By calculating the lowest fCost of reaching the goal node(player) the spiders can traverse through the maze. 
The fCost is the sum of adding gCost (distance from starting node) with the hCost(Heuristic cost , distance from end node).
The algorithm checks for the lowest fCost and is recalculated when a player moves based on the players current position.
All the spider objects are threaded and having them execute on separate threads to the player they can all search independently while using locks to avoid deadlock. 
The spiders section lower in this document shows how different spiders are given different actions based on the results of the heuristic  search.

NOTE: I tried implement other searches but AStar worked best for me. The classes have been left in for reference but are not used and are adapted from other sources.
		

---

# Fuzzy Logic Inference System

The linguistic variables and sets are defined within the fight.fcl file. 
The rules are set based on the strength of the players weapon combined with the strength of the opponent(spider) to return the damage done to the players health. Within the game each spider type is given a Strength and a player has a weapon object
With the use of 3 different weapons strengths and 8 different spider strengths the program has be designed to cover all possible input and output possibilities in the universe of discourse while exercise every rule. 
All the possible weapons and strength of associated spiders are covered lower in this document.

Linguistic variables and membership functions
The linguistic variables for the 'weapon'[INPUT] are :

	- Harmless	(0, 0) (0, 1) (50, 0)
	- Dangerous	(14, 0) (50, 1) (83, 0)
	- lethal	(50, 0) (100, 1) (100, 0)
	
The linguistic variables for the 'opponent'[INPUT] are :

	- weak		(0, 0) (0, 1) (50, 0)
	- Average	(14, 0) (50, 1) (83, 0)
	- Strong	(50, 0) (100, 1) (100, 0)
	
The linguistic variables for the 'damage'[Output] are :

	- low		(19,0) (20, 1) (21, 0)
	- moderate	(49,0) (50, 1) (51, 0)
	- fatal		(79,0) (80, 1) (81, 0)
	
The rules defined in the rule block are :

	1. RULE 0: IF weapon IS harmless AND opponent IS weak THEN damage IS low;
	2. RULE 1: IF weapon IS harmless AND opponent IS average THEN damage IS moderate;
	3. RULE 2: IF weapon IS harmless AND opponent IS strong THEN damage IS fatal;
	4. RULE 3: IF weapon IS dangerous AND opponent IS weak THEN damage IS low;
	5. RULE 4: IF weapon IS dangerous AND opponent IS average THEN damage IS moderate;
	6. RULE 5: IF weapon IS dangerous AND opponent IS strong THEN damage IS moderate;
	7. RULE 6: IF weapon IS lethal AND opponent IS weak THEN damage IS fatal;
	8. RULE 7: IF weapon IS lethal AND opponent IS average THEN damage IS fatal;
	9. RULE 8: IF weapon IS lethal AND opponent IS strong THEN damage IS moderate;
	
The defuzzifier used is the COG (Center Of Gravity) defuzzification method that improves the control performance of a fuzzy logic controller.
This method incorporates the membership values and the effective widths of membership functions in calculating a crisp value.
	
---

# Neural Network

The neural network is trained at the start of the program. This helps to increase performance and avoid lag in the game.
THe training and expected data is saved in the 'neural' folder in the 'expectedData.txt' and 'trainingData.txt' file and is read into memory.
The multi-layer back-propagation network uses the Sigmoid activation function instead of the HyperBolic Tangent function. 
The rationale for for picking this choice is that the derivative of the sigmoidal function is a more rounded output as the program did not need to be very concise.
The Neural Network returns an array of the possibility of each outcome in which the highest value is extracted. In the game this value is set to cause the spider to either attack, hide or move randomly away from the player.
The decision is based on the the players health and the spiders power. To implement this the 'getNextAction()' function in the 'fightable' class normalise the values before feeding them to the neural net.

Players health to Normalised health value:

	- Health < 30				= 0 
	- Health > 30 AND Health < 60		= 1
	- health > 60				= 2
	
Spider Damage  to Normalised damage value:

	- spiderPower < 30			= 0 
	- spiderPower > 30 AND spiderPower < 60	= 1
	- spiderPower > 60			= 2

Output mapped to action:

	- [0,0,0]	->	move randomaly
	- [1,0,0] ->	panic
	- [0,1,0]	->	attack
	- [0,0,1]	->	move
		
 ---
 
# Controls
	up arrow	move up
	down arrow	move down
	left arrow	move left
	right arrow	move right
	S key		show statistics
	Z key		zoom out	
 	
---

# Extras features to note

	> Player Health		- Initially 100, decrease with attacks, can be restored by health pickup.
	> Multi threading	- Spiders are threaded and run separately to the player.
	> Press S Key		- Pop up a player statistic box showing damage and weapon
	> File reading		- Neural network data is saved in files.
	> Win condition		- Game is won when player reaches exit sign.
	> Weapons		- Can be picked up along the game.
	> Threads Synchr	- Thread are designed to avoid deadlock.


 ---

# Weapons
To pick up a weapon walk into it.
The weapons and there associated damage values used in this game are:
	
| Weapon   | Damage |
| --- | --- |
| hands	| 10|
| sword	| 20|
| bomb	| 50|
|hbomb	| 75|

 ---

# Spiders
After a battle with a spider walking over it kills it and removes it from the game.
The following is a list of spiders used in the game. 
The type is how they are referenced in the game.
Colour is the spiders colour.
Damage is the attack force of spider.
Searching spider or Neural Network spider.(if searching number is the distance from goal node before computing action)
Fight fuzzy is how the spider fights

|  Type   |  Colour   | Damage | Info|
| --- | --- | --- | --- |
| 6|black | 80| Searches using A* traverse	if less than 10	fights fuzzy
| 7| Blue| 60| Searches using A*		if less than 10	fights fuzzy
| 8| Brown| 40| Uses NN to detemine action	if less than 10	fights fuzzy
| 9| Green| 20| Uses NN to detemine action	if less than 10	fights fuzzy
| 10| Grey| 10| 	Uses NN to detemine action	does not fight		
| 11| Orange| 60| 	Uses NN to detemine action	if less than 5	fights fuzzy
| 12| Red | 40|	Searches using A*		if less than 10	fights fuzzy
|13 | Yellow|20 |	Uses NN to detemine action	does not fight



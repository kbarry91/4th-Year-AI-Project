The  maze  should  be  patrolled  by enemies  (spidersare  already  provided) or  other characters  that are  fullythreaded
===================================================================================================
* Fuzzy Logic
Fuzzy inference system 
	1.Linguistic variable sand Universe of Discourse
	2.Fuzzy Set sand Membership Functions	
	3.Defuzzifiers, e.g. COG, COGS, LM, MM, RM.
	4.Membership   Functions   for   output variables,   e.g. Mamdanior Suegno(singleton values)
	5.Fuzzy Rules. 
	
	>How i implemented them with game chars...?
===================================================================================================

* Neural Netowrk
	> topology used?
	> Training and validation data
 	> How i implemented them with game chars...?
 	
 rough work: 
 	The Neural network is traiuned at runtime when the program starts.
 	Uses ActivationFunction.Sigmoid, 3, 3, 3);
 	values are normailised
 	get next action in fightable uses nn to determine the nexxt action of the spider
 ===================================================================================================
 
* Heuristic search
	> explain implemnation of heursitcs for ai search...
	> Explain implent of algs.
	> How i implemented them with game chars...?	

* Extras 
KeyPress S displays player and game statistics such as health and current weapon. 

** PlayerHealth 
	PLayer health is initially set to 100 points
	Can be increased by 30 points by collecting a health pickup
	
** File reading
	Reading training and expected dat from file.
** Exit node
	Reaching the exit node (exit sign) with health remaining finishes the game.
** Pickups

** Spiders
Type : 6	Colour : Black	Damage : 80 Searches using A* traverse if less than 10	fights fuzzy
Type : 7	Colour : Blue 	Damage : 60 Searches using A*			if less than 10	fights fuzzy
Type : 8	Colour : Brown	Damage : 40								if less than 10	fights fuzzy
Type : 9	Colour : Green	Damage : 20								if less than 10	fights fuzzy
Type : 10	Colour : Grey	Damage : 10 
Type : 11	Colour : Orange	Damage : 60								if less than 5	fights fuzzy
Type : 12	Colour : Red,	Damage : 40 Searches using A*			if less than 10	fights fuzzy
Type : 13	Colour : Yellow	Damage : 20


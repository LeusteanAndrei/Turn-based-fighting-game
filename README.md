# Project description
I made a working and functional class structure for a turn based fighting game ( similar to Fate/Grand Order and Honkai Star rail games ). At the moment everything is done through command line interface but in the future a 
GUI will be implemented for smoother sailing and nicer animations.
The attack part of the game is not yet done, because I didn't want to simply put a basic attack so for now it just passes the turn.


# Game and class structure
The project itself is separated in 4 main packages ( the csv package is for reports reagarding database queries so I didn't count it here )
- enumaration package
  -> contain enums like AttackType, GamePeriod, CharacterType, EffectType
- preparation package
  -> contains the Singleton PrepareGame class which is used to load all neccessary data into accessible data
- utilities package
  -> contains different Classes for useful types ( Pair, Triple type used for connecting a character to a relic and respectively for easily storing effect informations ) and functionalities such as a Reader class ( which does all the input reading from the user ), a Printer class 

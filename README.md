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
  -> contains different Classes for useful types ( Pair, Triple type used for connecting a character to a relic and respectively for easily storing effect informations ) and functionalities such as a Reader class ( which does all the input reading from the user ), a Printer class ( for output ) and a Service class ( which does all the with the menu navigation )
- Classes package -> this contains all the other classes used for the actual implementation ( the dbConnections, the classes used to store information and the classes used for the game's logic )


# Game logic:
  The central point of the game implementation would be the Function interface and it's corresponding SkillEffects Class. This allows me to easily associate every character or relic or skill with any effect, hovewere complicated it may be with very much ease. Every effect object contains a GamePeriod type, an EffectType type and 2 important strings: functionName and removeFunctionName. These 2 strings are the keys I use to get the corresponding SkillEffect function using a map in the PrepareGame Class. 

  Every Character has a status field which remembers the Effects which have been applied. Each of these is stored using the Triplet class which has tree elements, a duration, a scale and an effect name. ( the scale is in float and when writing new skill effects, we need to be careful when using because it's meaning can easily vary from effect to effect ).

  Characters are separated into two child types: Playable and Nonplayable
  - the first is used for playable characters as the game suggests, characters wich can be attained and acquired by the user. They usually have a standard amount of tree skils ( a skill is a list of effects which are to be applied to the current or allied characters ) and relic. The playable character also has exp which can increase the level. This is different for each user though so it's stored sepparately in the database and then combined when exctracting the characters
  - the second one, nonplayable character, is used for the enemies and the mobs which the player will be fighting. These don't have neither skills or relics but have a simple Effect list and multiple health bars.

  A Battle contains a Stage and a list of nonplayable characters. And each fight is a list of battles

  Users can be created or signed in and they have their own list of characters and relics to use in fights ( at this point users can only attain characters throuhg the admin and the relics are the same for everyone, loaded when starting the game as they have not yet been stored in the database )



  

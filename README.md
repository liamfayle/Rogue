# Rogue Java Game
Small object oriented java game based on [Rogue](https://en.wikipedia.org/wiki/Rogue_(video_game)).

## Gameplay

https://user-images.githubusercontent.com/74878922/203170837-8673df73-c159-4630-ba7c-0a3aff5cd4a7.mp4



## Compilation and Execution Instructions
All development and testing was done using Java 11 on Windows 10.
- Type "gradle build" to compile the program and run unit tests. 
- Type "gradle run" *OR* "java -jar build/libs/Rogue.jar"

## Controls
- Arrows keys to move player (@) accross screen.
- Use "a" & "d" to change selected item in inventory. 
- Use "e" on selected item to eat the item. 
- Use "t" on selected item to throw the item away. 
- Use "w" on selected item to throw the item away. 
- Message in botton message box will indicate whether the command was successful!

## Functionality
- Adapts to any custom designed room and items using the "rooms_json.json" file.
- Can save and load game states.
- Can change *rooms* while game is running to a new .json file.
- Can change player name.
- Can move player, change rooms, pickup items, scroll inventory, and use items.

## Potential Future Functionality
- Add enemy class.
- Add health/status/armour bars and dynamically update based on gameplay (damage by enemy, item consumed, etc).
- Extend player class to add attack and defense functionality. 

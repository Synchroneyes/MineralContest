
# Plugin documentation
Since 17.11.2020, it's now possible to create a plugin for the gamemode. There is a list of events available, and methods to use.
The documentation is not complete at this time.

# Getting started

 1. Firstly, you should get the plugin available on the website
    [synchroneyes.fr/downloads](https://beta.synchroneyes.fr/downloads/).
    
2. After your download, you should now create a new Spigot Plugin using
    your favorite IDE. 
3. Then, using your favorite Java IDE, you need to
    import the downloaded .jar into your project and your main class
    should import the following package
    `fr.synchroneyes.custom_plugins.CustomPlugin`
  4. Make your main class   `extends CustomPlugin`.
  5. Edit your plugin.yaml file and add the following dependency `MineralContest`

# Available Events:
All of the following events are available in the `fr.synchroneyes.custom_events`'s package.

| Event name| Description | Available methods |
|--|--|--|
| MCAirDropSpawnEvent | Event called on airdrop spawn |getGame(), getParachuteLocation()|
|MCAirDropTickEvent|Event called every tick from the Air drop manager spawning loop|getGame(), getTimeLeft()|
|MCArenaChestSpawnEvent|Event called when the arena chest spawns|getGame()|
|MCArenaChestTickEvent|Event called every tick from the chest manager spawning loop|getGame(), getTimeLeft()|
|MCAutomatedChestTimeOverEvent|Event called when the timer of a timed chest is over|getAutomatedChest()|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
|--|--|--|
	 
 - `MCAirDropTickEvent` - Event called every tick from the Air drop manager spawning loop
	 - `getGame()` - Get the game where the drop spawned in
	 - `getTimeLeft()`- Get the ticks remaining before next drop
	 - 


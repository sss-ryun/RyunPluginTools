# Ryun's Plugin Tools
This set of tools was developed to aid in developing Bukkit/Spigot/Paper plugins.

Check the [releases](https://github.com/sss-ryun/RyunPluginTools/releases) for the jar

|Plugin API             |Compatibility      |
|-----------------------|-------------------|
|Bukkit (Main)          |:white_check_mark: |
|Spigot (Bukkit fork)   |:white_check_mark: |
|Paper (Spigot fork)    |:white_check_mark: |
|... Other Bukkit forks |:white_check_mark: |

***All past or future versions of this plugin tool will be compatible with all Bukkit forks unless something drastic changes in their code.***

## How to use?

There are lots of tools ready for use:
* [PluginCommands](src/main/java/me/ryun/plugintools/PluginCommands.java)
* [PluginEnvironment](src/main/java/me/ryun/plugintools/PluginEnvironment.java)
* [PluginInfo](src/main/java/me/ryun/plugintools/PluginInfo.java)
* [ServerShutdownOnQuit](src/main/java/me/ryun/plugintools/ServerShutdownOnQuit.java)
* [CraftBukkitFinder](src/main/java/me/ryun/plugintools/CraftBukkitFinder.java)

**See examples down below**

## Examples:
**PluginCommands**
```java
import me.ryun.plugintools.PluginCommands;

/*
* Import your commands here
*/

public class ExamplePlugin extends JavaPlugin {
	//Global variable so the instance is reused for every method calls
	private PluginCommands commands;
	
	//This constructor method is called before other methods in your plugin will be called
	public ExamplePlugin() {
		commands = new PluginCommands();
		
		//I recommend adding them here so they are added before all of the other code are run
		commands.add(new TestCommand1());
		commands.add(new TestCommand2());
		commands.add(new TestCommand3());
	}

	//This method is called when a Player uses /(insert command here)
	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
		return commands.call(sender, command, label, args);
	}
	
	//This method is called when a Player adds a space after a command, and the results are shown and could be used to autocomplete the command
	@Override
	public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
		return commands.completeTab(sender, command, alias, args);
	}
}
```
The experts among you might notice that the ```commands.add();``` method resembles an ArrayList/List/Collection. This is because PluginCommands is a subclass of the class ArrayList, which means you can do a lot of things to your lists of commands if you want, provided they are available in the ArrayList parent class.

**What does it do?**</br>
The method ```commands.call(sender, command, label, args);```  loops through all of the commands added and returns a boolean value if a command was executed.
***\*NOTE: This method stops as soon as a command is executed in the order of the sequence they are added.***

The method ```commands.completeTab(sender, command, alias, args);```  loops through all of the commands added and returns a list of values if a command could be tab completed.
***\*NOTE: This method stops as soon as a command could be tab completed in the order of the sequence they are added.***

Additionally, there are other methods you can call if you don't want it to stop if a command was executed or if it could be tab complete.
```java
commands.callAll(sender, command, label, args); //Returns a boolean value
commands.completeTabAll(sender, command, alias, args); //Returns a List<String> or null
```
***\*NOTE: These methods will go through all the commands even those with the SAME NAME</br>i.e `/test` from Command1 and `/test` from Command2 will be called consecutively.***

**Why did you create it?**</br>
Ultimately, this tool is created for the sake of convenience and clean code. I can create as many TabExecutor subclasses and not worry about using the silly ```Bukkit.getPluginCommand("test").setExecutor(new TestCommand());```, which in my opinion, creates very ugly and unreadable code.

If you don't find this tool helpful, it's not for you. Keep reading as you might like the other ones.

**PluginEnvironment**
```java
import me.ryun.plugintools.PluginEnvironment;

public class ExamplePlugin extends JavaPlugin {
	//This constructor method is called before other methods in your plugin will be called
	public ExamplePlugin() {
		PluginEnvironment.Factory
			.make() //Makes the Factory
			.isDevelopment(true) //Sets Environment to Development
			.isVerbose(true) //Sets Boolean value if you should log everything
			.create(); //Sets and creates your plugin environment.
	}

	//This method is called when your plugin is enabled
	@Override
	public void onEnable() {
		//Checks if you set your environment to development or production/release
		if(PluginEnvironment.get().isDevelopment())
			Bukkit.getLogger().info("My example plugin is enabled.");

		try {
			//Insert error throwing code here
		} catch (Exception e) {
			//Checks if you set your environment to verbose logging
			if(PluginEnvironment.get().isVerbose())
				Bukkit.getLogger().severe(e.toString());
		}
	}
}
```
I'm sure those of you who have written code for production environments could see the usefulness of this tool. You can log all the errors in development without having to remove them when you're releasing your plugin. By doing some clever Gradle build, you could theoretically set this up to automatically detect if you're building a Development build of your plugin or a Release build, so you don't have to worry about cluttering the server console of the user.

If you're feeling chaotic neutral, you can even save the instance of the PluginEnvironment after you called ```.create();``` in the Factory so you could declare it globally and use it that way.

Still not for you? The next one will surely excite you.

**PluginInfo**
```java
import me.ryun.plugintools.PluginInfo;

public class ExamplePlugin extends JavaPlugin {
	//This constructor method is called before other methods in your plugin will be called
	public ExamplePlugin() {
		PluginInfo.Factory
			.make() //Makes the Factory
			.setPlugin(this) //Sets your plugin as the source of the information that will be grabbed by the tool
			.create(); //Sets and creates your plugin information.
	}

	//This method is called when your plugin is loaded (but not enabled yet)
	@Override
	public void onLoad() {
		//These are all of the information you can grab from your plugin.yml with this tool
		Bukkit.getLogger().info(PluginInfo.get().getName() + " is loaded.");
		Bukkit.getLogger().info("API: " + PluginInfo.get().getAPIVersion());
		Bukkit.getLogger().info("Version: " + PluginInfo.get().getVersion());
		Bukkit.getLogger().info("Author: " + PluginInfo.get().getAuthor());
		Bukkit.getLogger().info("Description: " + PluginInfo.get().getDescription());
		Bukkit.getLogger().info("Main Class: " + PluginInfo.get().getMainClass());
		Bukkit.getLogger().info("Prefix: " + PluginInfo.get().getPrefix());
	}

	//This method is called when your plugin is enabled
	@Override
	public void onEnable() {
		Bukkit.getLogger().info(PluginInfo.get().getName() + " is enabled.");
	}

	//This method is called when your plugin is disabled
	@Override
	public void onDisable() {
		Bukkit.getLogger().info(PluginInfo.get().getName() + " is disabled.");
	}
}
```
All of this code assumes that you have defined the appropriate values. If not, it will output `No value set` so that you can know that you f*cked up.

I'm pretty sure the code above is self-explanatory.

After all of that, if you still don't need that, you might just want the next one after a massive amount of grueling test runs of your plugin.

**ServerShutdownOnQuit**
```java
import me.ryun.plugintools.PluginEnvironment;
import me.ryun.plugintools.ServerShutdownOnQuit;

public class ExamplePlugin extends JavaPlugin {
	//This constructor method is called before other methods in your plugin will be called
	public ExamplePlugin() {
		PluginEnvironment.Factory
			.make() //Makes the Factory
			.isDevelopment(true) //Sets Environment to Development
			.isVerbose(true) //Sets Boolean value if you should log everything
			.create(); //Sets and creates your plugin environment.
	}

	//This method is called when your plugin is enabled
	@Override
	public void onEnable() {
		//Checks if you set your environment to development or production/release
		if(PluginEnvironment.get().isDevelopment()) {
			//This will shutdown your server if the last player quits/leaves
			Bukkit.getPluginManager().registerEvents(new ServerShutdownOnQuit(), this);
			Bukkit.getLogger().info("My example plugin is enabled.");
		}
	}
}
```
When you test your plugin on and off every few minutes to see your changes, are you really not annoyed that you have to type `stop` or just force close the test server(which could corrupt it) each time you build and test? If you are, this is the solution to your problem.

**CraftBukkitFinder**
```java
import me.ryun.plugintools.CraftBukkitFinder;

public class ExamplePlugin extends JavaPlugin {
	//This method is called when your plugin is enabled
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("This Bukkit server uses CraftBukkit: " + CraftBukkitFinder.scan(false));
	}
}
```
This code will output the version of your CraftBukkit by scanning through versions 1.9 to 1.100(I know. That's some forward compatibility). ```CraftBukkitFinder.scan(false)``` The `false` here tells the finder that you don't want to see its logs when it tries to scan for the packages. You can set it to `true` if you want to see it log.

Example output: `org.bukkit.craftbukkit.v1_20_R1`

Dynamically finding the CraftBukkit version is useful if you're gonna use Reflection in your Bukkit plugin.

## You want to try?
First, clone the repo directly to your project then add these lines of code to your `build.gradle` file</br>
`{your_project}/build.gradle`
```gradle
repositories {
	flatDir {
		dirs("libs") //This allows you to add jar dependencies in your gradle project
	}
}
dependencies {
	implementation('me.ryun:plugintools:1.1.0'); //This won't work if you don't flatDir
}
```
And download the jar from the [releases](https://github.com/sss-ryun/RyunPluginTools/releases) to the folder `libs/` in your project

**\*NOTE: You may use [Shadow](https://imperceptiblethoughts.com/shadow/)- a Gradle plugin for building JARs with dependencies if you get a ClassNotFound error when running your plugin in your test server.**

I hope you found all my plugin tools to be helpful. Feedback is appreciated. If you want to add your own, do fork the repo and request a pull once your changes are finalized.

## Still confused?
Go read the [javadocs](https://sss-ryun.github.io/RyunPluginTools/)

## License:
```java
/*  
 * Copyright (c) 2023 SSS Ryun, otherwise known as SSS_Ryun or simply Ryun
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *      http://www.apache.org/licenses/LICENSE-2.0
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
 ```

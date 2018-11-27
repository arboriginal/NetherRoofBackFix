# NetherRoofBackFix

NetherRoofBackFix is a small plugin for [Spigot](https://www.spigotmc.org) Minecraft servers. It simply fix teleportation in the Nether, when the player die in lava for example.

Otherwise, when he types /back, he was teleported on the Nether roof top (above the bedrock), so he was stuck and loose has to abandon his Nether location...

With this plugin, now when teleporting to this Nether roof position, the location is recalculated to find a safe area below: If necessary, a small platform of Netherrack is created.

## Compatibilitie

For now, Spigot / PaperMC 1.13.2. Only tested on the second but should work without problems on Spigot too.

## How to install

Simply drop the jar file into your plugin directory, then restart (or reload) your server. A new folder, named « NetherRoofBackFix » is created in it, letting you change default settings. You can reload the plugin when done without having to restart the server.

You can download the last release here: [NetherRoofBackFix.jar](https://github.com/arboriginal/NetherRoofBackFix/releases).

## Commands

There is only one command: `nrbfr` (**N**ether**R**oof**B**ack**F**ix **R**eload). It reloads the plugin configuration.

## Permissions

 - **nrbf.reload**:	Allows to reload the plugin configuration
   (default: op)

 - **nrbf.bypass**:	Bypass teleportation fix
   (default: false)

## Configuration

You can edit the default generated config.yml in your plugin directory (in the subfolder « # NetherRoofBackFix »). It contains only a few parameters:

 - **configuration_reloaded**:
Message displayed when the plugin is reloaded.

 - **no_safe_place_found**:
Message displayed to the player when the plugin can't find a safe place (if `avoid_if_no_safe_place` is true).

 - **warn_player_if_avoided**: *(true / false)*
If false, the message `no_safe_place_found` will not be displayed.

 - **avoid_if_no_safe_place**: *(true / false)*
If true, if the plugin can't find a safe place, cancel the teleportation.

 - **minimum**: *(integer)*
Minimum height for the plugin, you should not change it, unless you have tested and / or have special needs.

 - **minDown**: *(integer)*
Minimum number of blocks under the default location to start searching a safe place (the plugin will check blocks under this number of blocks).

 - **minAir**: *(integer)*
Minimum height of empty blocks needed to be considered as a safe place. You should not use less than 2, other the player will suffocate.

 - **secure**: *(integer)*
Number of blocks around the safe place to fill with Netherrack if empty. 1 means you will have a platform of 3x3 centered around the safe position (Non empty blocks are not replaced by Netherrack).

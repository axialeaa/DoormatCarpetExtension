<img src="./src/main/resources/assets/doormat/icon.png" align="right" width="128px"/>

# Hi there! Thanks for checking out Doormat!

[![GitHub downloads](https://img.shields.io/github/downloads/axialeaa/DoormatCarpetExtension/total?label=Github%20downloads&logo=github)](https://github.com/axialeaa/DoormatCarpetExtension/releases)
[![Modrinth downloads](https://img.shields.io/modrinth/dt/doormat?label=Modrinth%20downloads&logo=modrinth)](https://modrinth.com/mod/doormat)

Doormat is a [Carpet](https://github.com/gnembon/fabric-carpet) extension, focusing on simply adding features I want to have fun with, and hopefully you will too! Aside from new ways to farm mostly non-renewable resources, this mod prides itself on its "redstone tinkering" features, allowing you to have control over block updates and quasi connectivity in your worlds.

![A redstone lamp receiving quasi-power](https://github.com/axialeaa/DoormatCarpetExtension/assets/116074698/89617f79-c926-4006-b061-84463dbf6555)

## More Extensions
There are also lots of other carpet extensions out there, adding countless new rules and functionality! You can find a list of them [in the Carpet wiki](https://github.com/gnembon/fabric-carpet/wiki/List-of-Carpet-extensions).

# Settings
## accurateAzaleaLeafDistribution
The distribution of azalea leaf types matches the azalea bush the tree is grown from.
These features are controlled through `doormat/worldgen/configured_feature/azalea_tree_many_flowers.json` and `azalea_tree_no_flowers.json` respectively.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## azaleaLeavesGrowFlowers
Azalea leaves grow flowers when fertilized.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## beaconsHealPets
Beacons with their secondary effect set to regeneration can heal nearby tamed mobs.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `SURVIVAL`, `DOORMAT`

## bellQuasiConnecting
Whether bells can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## bellUpdateType
The type(s) of update bells emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `both`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## chiseledBookshelfSignalBasis
The thing comparators measure when outputting the signal strength of chiseled bookshelves.
"interaction" is vanilla behavior, and is equal to the last slot interacted with.
"fullness" depends on the number of books in the bookshelf.
Lerped interpolates from 1 to 15, instead of outputting the exact number.
* Type: `ChiseledBookshelfSignalMode`
* Default value: `interaction`
* Allowed options: `interaction`, `fullness`, `fullness_lerped`
* Categories: `FEATURE`, `DOORMAT`

## commandRandomTick
Enables /randomtick command to forcibly send randomTicks to the specified block position.
* Type: `String`
* Default value: `true`
* Allowed options: `true`, `false`, `ops`, `0`, `1`, `2`, `3`, `4`
* Categories: `COMMAND`, `DOORMAT`
* Additional notes:
  * It has an accompanying command
  * Can be limited to 'ops' only, true/false for everyone/no one, or a custom permission level
  * Currently has an issue with generating multiple features for one plant

## comparatorsReadThroughChains
Comparators can read through chains, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## comparatorsReadThroughPaths
Comparators can read through dirt paths, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## comparatorsReadThroughPistons
Comparators can read through pistons, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## composterSideInputs
Sideways-pointing hoppers and droppers can insert items into composters.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## consistentExplosionImmunity
Explosion-immune blocks also apply this property to their dropped item counterparts.
Though marked as "Works As Intended", this rule technically fixes this bug: https://bugs.mojang.com/browse/MC-212764
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `EXPERIMENTAL`, `BUGFIX`, `DOORMAT`
* Additional notes:
  * Thanks to my friend [intricate](https://github.com/lntricate1) for critiquing my code and providing a formula to calculate explosion immunity on the fly <3

## disableEndPortalCrossing
Disables travelling through end portals.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `CREATIVE`, `DOORMAT`

## disableNetherPortalCrossing
Disables travelling through nether portals.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `CREATIVE`, `DOORMAT`

## disablePetAttacking
Disables players harming tamed mobs.
"owned" allows players to hit pets that aren't their own.
* Type: `PetHurtMode`
* Default value: `false`
* Allowed options: `true`, `false`, `owned`
* Categories: `SURVIVAL`, `DOORMAT`

## disableShulkerReproduction
Disables shulkers duplicating when hit by bullets.
Intended to be used in tandem with carpet's shulkerSpawningInEndCities.
* Type: `boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `CREATIVE`, `SURVIVAL`, `DOORMAT`

## dispenserUpdateType
The type(s) of update dispensers and droppers emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `both`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## doorUpdateType
The type(s) of update doors emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `shape`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`
* Additional notes:
  * Disabling shape updates affects the connection between the two halves of the door

## dustTravelDownGlass
Redstone dust can travel down glass blocks, like in Bedrock Edition.
* Type: `boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## fenceGateQuasiConnecting
Whether fence gates can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## fenceGateUpdateType
The type(s) of update fence gates emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `shape`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## fireAspectLighting
Items enchanted with fire aspect can be used to light campfires, candles and TNT, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## forceGrassSpread
Grass and mycelium can instantly spread to adjacent fertilized dirt.
This behaves the same as nylium in vanilla.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## growableSwampOakTrees
Oak saplings grow into swamp oaks in swamp biomes.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## hopperQuasiConnecting
Whether hoppers can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## hopperUpdateType
The type(s) of update hoppers emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `shape`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## huskWashing
Adult husks drop 1-3 sand when converting into zombies.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`
* Additional notes:
  * Technically renewable sand; renamed to avoid conflict with other carpet extension mods

## insomniaDaysSinceSlept
The number of sleepless nights after which phantoms can spawn.
* Type: `Integer`
* Default value: `3`
* Suggested options: `3`, `7`
* Categories: `SURVIVAL`, `DOORMAT`
* Additional notes:
  * Must be a non-negative number

## jukeboxDiscProgressSignal
Comparators measure how much of the music disc has been played by the jukebox, instead of the exact disc type.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## lampQuasiConnecting
Whether redstone lamps can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## lampUpdateType
The type(s) of update redstone lamps emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `shape`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## leavesNoCollision
Disables the tangibility of leaves, making it easier to traverse areas with lots of trees.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## maxMinecartSpeed
The maximum speed minecarts can travel at in blocks per second.
Underwater max speed will always be half of this number.
* Type: `Double`
* Default value: `8.0`
* Suggested options: `8.0`, `16.0`
* Categories: `CREATIVE`, `DOORMAT`
* Additional notes:
  * Must be a non-negative number

## monstersSpawnInPeaceful
Hostile mobs can passively spawn in peaceful mode.
"true" entirely matches easy mode behavior.
"below_heightmap" ensures that there is always a roof above the spawning location of a mob.
"unnatural" removes the capability for natural and chunk generation spawns.
* Type: `PeacefulMonstersMode`
* Default value: `false`
* Suggested options: `false`, `true`, `below_surface`, `unnatural`
* Categories: `SURVIVAL`, `EXPERIMENTAL`, `DOORMAT`
* Additional notes:
  * This modifies a <strong>LOT</strong> of classes, so there's a high likelihood I've missed something. If so, report it to me via the Github issues page.

## mossSpreadToCobblestone
Moss can convert nearby cobblestone to mossy cobblestone when fertilized.
This feature is controlled through `doormat/worldgen/configured_feature/mossy_cobblestone_patch.json`.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## mossSpreadToStoneBricks
Moss can convert nearby cobblestone to mossy cobblestone when fertilized.
This feature is controlled through `doormat/worldgen/configured_feature/mossy_stone_bricks_patch.json`.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## noteBlockQuasiConnecting
Whether note blocks can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## noteBlockUpdateType
The type(s) of update note blocks emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `both`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## phantomMinSpawnAltitude
The minimum y height the player needs to be standing at in order to spawn phantoms.
* Type: `Integer`
* Default value: `63`
* Suggested options: `63`, `127`, `255`
* Categories: `SURVIVAL`, `DOORMAT`
* Additional notes:
  * Must be a non-negative number

## pistonMovementTime
The length of time in ticks it takes for a piston to fully extend or retract.
* Type: `Integer`
* Default value: `2`
* Categories: `FEATURE`, `DOORMAT`
* Additional notes:
  * Must be a number from 1 to 1200

## pistonUpdateType
The type(s) of update pistons emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `both`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`
* Additional notes: 
  * Expect many issues to arise from removing shape updates from pistons
    * "neither" has the capability of deleting blocks on motion
  * Piston heads currently emit an elusive block update when they start to retract, regardless of this setting

## playersDropAllXp
Players drop all of their XP on death instead of a capped amount.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `SURVIVAL`, `DOORMAT`

## portalForceTicking
Nether portal blocks randomTick when lit, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## propagulePropagation
Mangrove propagules actually propagate, falling from trees and planting themselves automatically.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## railQuasiConnecting
Whether rails can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## railUpdateType
The type(s) of update rails emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `both`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`
* Additional notes:
  * Disabling either kind of update affects the redstone connections between rails

## ravagersStompPlants
Ravagers can break plants, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## renewableCobwebs
Cave spider spawners generate cobwebs between nearby solid faces each spawn cycle.
Cobwebs generate within a 9x9x9 cube centred on the spawner, forming only in block positions that have two or more supporting faces.
The more supporting faces, the more likely the cobweb is to generate.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`
* Additional notes:
  * This makes me super happy to show you. This feature had a lot of planning and testing put behind it, and it measured my expertise in a really fascinating way. I hope you enjoy <3

## renewableGildedBlackstone
Blackstone adjacent to underwater magma blocks slowly becomes gilded over time.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## renewableSporeBlossoms
Whether spore blossoms should generate when this block is fertilized.
"self" drops an item the same way tall flowers do.
* Type: `SporeBlossomsMode`
* Default value: `false`
* Allowed options: `false`, `moss`, `self`
* Categories: `FEATURE`, `DOORMAT`

## softInversion
Redstone torches placed on the sides of pistons deactivate when the piston extends, like in Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`
* Additional notes:
  * This hasn't reached total parity yet. For example, Bedrock Edition's implementation treats pistons as solid blocks in this instance, powering the torch when the piston is powered, not when it extends.
  * Due to the differences in Java Edition's neighbor update system, such an implementation would cause the torches to become budded if the piston was powered and couldn't extend (the thing that gives neighbor updates).
  * I opted for this method, which should stay true to Java's quirks as the real soft inversion does to Bedrock's. :)

## stairDiodes
Redstone dust can travel both up and down the full-square side(s) of stairs, but only up the other sides.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`

## stickyPillarBlocks
Pillar blocks like logs and bone blocks stick to each other based on the axis direction when moved.
"stick_to_all" causes them to connect to each other regardless of axis direction.
* Type: `ChainStoneMode`
* Default value: `false`
* Allowed options: `true`, `false`, `stick_to_all`
* Categories: `FEATURE`, `DOORMAT`

## stickyStickyPistons
Sticky pistons act like directional slime blocks.
"stick_to_all" causes them to stick omnidirectionally like normal slime blocks.
* Type: `ChainStoneMode`
* Default value: `false`
* Allowed options: `true`, `false`, `stick_to_all`
* Categories: `FEATURE`, `DOORMAT`

## thornyRoseBush
Similarly to sweet berry bushes, rose bushes deal damage when you walk inside them.
This even has a unique death message!
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `SURVIVAL`, `DOORMAT`

## tntQuasiConnecting
Whether TNT can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## tntUpdateType
The type(s) of update TNT emits when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `both`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## torchBurnoutFlickerAmount
The number of times a redstone torch can flicker in the designated timespan before burning out.
* Type: `Integer`
* Default value: `8`
* Categories: `FEATURE`, `DOORMAT`
* Additional notes:
  * Must be a non-negative number

## torchBurnoutTime
If a redstone torch exceeds the maximum number of flickers during this timespan, it will burn out.
40 is the value that matches Bedrock Edition's behavior.
* Type: `Integer`
* Default value: `60`
* Suggested values: `60`, `40`
* Categories: `FEATURE`, `DOORMAT`
* Additional notes:
  * Must be a non-negative number

## trapdoorQuasiConnecting
Whether trapdoors can be quasi-powered.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `QC`, `DOORMAT`

## trapdoorUpdateType
The type(s) of update trapdoors emit when changing state.
* Type: `NeighbourUpdateMode`
* Default value: `shape`
* Allowed options: `neither`, `block`, `shape`, `both`
* Categories: `FEATURE`, `UPDATE`, `DOORMAT`

## zoglinsSpawnInPortals
Zoglins have a chance of spawning inside overworld nether portals.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `DOORMAT`
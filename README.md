<img src="./src/main/resources/assets/doormat/icon.png" align="right" width="128px"/>

# Hi there! Thanks for checking out Doormat!

[![GitHub downloads](https://img.shields.io/github/downloads/axialeaa/DoormatCarpetExtension/total?label=Github%20downloads&logo=github)](https://github.com/axialeaa/DoormatCarpetExtension/releases)

Doormat is a [Fabric Carpet] extension, focusing on simply adding features I want to have fun with, and hopefully you will too! Aside from new ways to farm mostly non-renewable resources, this mod prides itself on its "redstone tinkering" features, allowing you to have control over block updates and quasi connectivity in your worlds.

[Fabric Carpet]: https://github.com/gnembon/fabric-carpet

## More extensions

There are also lots of other carpet extensions out there, adding countless new rules and functionality! You can find a list of them [in the Carpet wiki][extension list].

[extension list]: https://github.com/gnembon/fabric-carpet/wiki/List-of-Carpet-extensions

# Doormat Settings
## accurateAzaleaLeafDistribution
The distribution of azalea leaf types matches the azalea bush the tree is grown from.
This feature is controlled through `doormat/worldgen/configured_feature/azalea_tree_<many/no>_flowers.json`.
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
Comparators can read through chains, like they can on Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## comparatorsReadThroughPaths
Comparators can read through dirt paths, like they can on Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

## comparatorsReadThroughPistons
Comparators can read through pistons, like they can on Bedrock Edition.
* Type: `Boolean`
* Default value: `false`
* Allowed options: `true`, `false`
* Categories: `FEATURE`, `PARITY`, `DOORMAT`

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
Intended to be used alongside shulkerSpawningInEndCities.
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
Items enchanted with fire aspect can be used to light campfires, candles and TNT, like they can on Bedrock Edition.
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
The number of ticks it takes for a piston to extend or retract.
* Type: `Integer`
* Default value: `2`
* Categories: `FEATURE`, `DOORMAT`
* Additional notes:
  * Must be from 1 to 1200

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
Nether portal blocks randomTick when lit, like they do on Bedrock Edition.
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
Ravagers can break plants, like they can on Bedrock Edition.
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
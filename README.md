# StaticVoid – Text-Based RPG

StaticVoid is a Java Swing, text-driven RPG set in a collapsing digital world. You control a debugger process fighting corrupted enemies, upgrading weapons, and progressing through a branching “digital timeline” of story events.

## Features

- Turn-based combat with multiple actions: Attack, Skill, Ultimate, Heal, Item, Status, Flee, Virus Event.
- Multi-wave encounters with random enemies such as NullPointer, BlueScreen, Trojan, and KernelPanic.
- Persistent progression using a `GameState` singleton (player stats and inventory carry between battles).
- Weapon drop and upgrade system with random stat rolls (ATK, DEF, HP, crit chance).
- Story “timeline” screen with BitLife-style text feed and choices that can trigger combat.
- Cyberpunk UI using Java Swing: Consolas font, dark background, neon accent colors.

## How to Run

### Requirements
- Java JDK 17 or later installed and on your PATH.

### Windows (recommended)

1. Download or clone the repository.
2. In the project root, double-click `run.bat`.

The script compiles all source files and starts the game by running `Main`.

### Manual compile (any OS)

From the project root:


## Core Systems

### Object-Oriented Design

- **Encapsulation**: Game data (stats, inventory, HP) is stored in fields and accessed through methods such as `getHP()`, `setHP()`, `getInventory()`, and factory-like methods in `LootTable`.
- **Inheritance**: `Player` and `Enemy` extend `Character`, inheriting core combat stats while adding their own behavior.
- **Polymorphism**: Combat code works with `Character` references; methods like `attack(Character target)` behave differently for `Player` versus `Enemy`.
- **Abstraction**: `Character` abstracts the idea of any combatant, and `GameState` abstracts global game data behind a simple `getInstance()` API.

### Combat Flow

- Player chooses actions via buttons on `CombatScreen`.
- Each action calls methods on `Player` or `Enemy` (`attack`, `useSkill`, `useUltimate`, `heal`).
- Damage uses ATK, DEF, and critical hit formulas; HP and resource bars update after each turn.
- When an enemy dies, `LootTable.dropLoot(player)` may give a healing item or a weapon that boosts stats for future fights.

### Story Flow

- The game starts at `TitleScreen`, then moves to `StoryScreen`.
- `StoryScreen` displays narrative events and offers three choices, which may trigger combat.
- After combat ends, `StoryScreen.onCombatFinished(boolean playerWon)` updates the timeline and, if the player is defeated, disables further choices.

## Contributing

- Fork the repository and create a feature branch.
- Keep code style consistent with existing Java files (Consolas font in UI, similar color scheme).
- Submit a pull request with a clear description of your changes.

## Future Enhancements

Planned or potential improvements for future versions:

- **Save/Load system**: Persist player progress, inventory, and sector depth between sessions.
- **Boss encounters**: Add special boss enemies with unique patterns, phases, and dialogue after certain depths.
- **More item and weapon types**: Introduce armor, shields, and rare/legendary weapons with unique effects.
- **Status effects**: Implement buffs and debuffs (poison, stun, shield, regen) that interact with existing stats.
- **Expanded story events**: More branching narrative choices, endings, and story-driven combat scenarios.
- **Difficulty modes**: Easy/Normal/Hard settings that adjust enemy stats, wave counts, and drop rates.
- **Audio and visual polish**: Add sound effects, background music, and subtle animations for attacks and critical hits.
- **Settings menu**: Allow players to tweak volume, text speed, color theme variants, and window size.
- **Modular enemy/loot data**: Load enemies and loot tables from external JSON or XML to make balancing and modding easier.

## Sample Outputs
![Combat encounter](images/CombatEncounter.png)
![Story screen](images/StoryScreen.png)
![Weapon upgrade](images/WeaponUpgrade.png)

## License

This project is for educational and personal use. Respect all applicable copyright and intellectual property laws when modifying or distributing the code.

import java.util.Random;

public class LootTable {
    private static final Random rand = new Random();

    public static Item dropLoot(Player player) {
        int roll = rand.nextInt(100);
        Item dropped;
        if (roll < 45) dropped = new Item("Small Patch", "heal", 500);        // 45%
        else if (roll < 75) dropped = new Item("System Restore", "heal", 1500); // 30%
        else if (roll < 80) dropped = new Item("Core Reboot", "heal", 3000);    // 5%
        else {
            // 20% WEAPONS!
            String[] weaponNames = {"Debug Blade", "Nullifier", "Glitch Cleaver", "Virus Reaper"};
            String weaponName = weaponNames[rand.nextInt(weaponNames.length)];
            dropped = new Item(weaponName, "weapon", 0);
        }
        player.getInventory().add(dropped);
        return dropped;
    }
}

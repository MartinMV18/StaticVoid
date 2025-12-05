public class Item {

    private final String name;
    private final String type; 
    private final int value;

    // weapon-only stats (flat bonuses)
    private int atkBonus;
    private int defBonus;
    private int hpBonus;
    private double critBonus = 0.0;  // CRIT UPGRADE SUPPORT

    public Item(String name, String type, int value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public String getName() { return name; }
    public String getType() { return type; }
    public int getValue() { return value; }

    // Weapon stat getters
    public int getAtkBonus() { return atkBonus; }
    public int getDefBonus() { return defBonus; }
    public int getHpBonus() { return hpBonus; }
    public double getCritBonus() { return critBonus; }  // CRIT SUPPORT

    // Called by upgrade screen when a stat is rolled
    public void addAtkBonus(int amount) { atkBonus += amount; }
    public void addDefBonus(int amount) { defBonus += amount; }
    public void addHpBonus(int amount) { hpBonus += amount; }
    public void addCritBonus(double amount) { critBonus += amount; }  // CRIT SUPPORT

    public void use(Player player) {
        switch (type.toLowerCase()) {
            case "heal" -> player.setHP(Math.min(player.getMaxHP(), player.getHP() + value));
            case "sp" -> player.setSkillPoints(Math.min(5, player.getSkillPoints() + value));
            case "energy" -> player.setEnergy(Math.min(100, player.getEnergy() + value));
            case "weapon" -> System.out.println("Weapons must be equipped, not used.");
            default -> System.out.println("Unknown item type: " + type);
        }
    }
}

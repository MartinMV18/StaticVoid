import java.util.List;
import java.util.Random;

public class Player extends Character {

    private int skillPoints;
    private int energy;
    private final List<Item> inventory;
    private final Random rand = new Random();

    // Weapon system
    private Item equippedWeapon;
    private int bonusATK, bonusDEF, bonusHP;

    public Player(String name, int HP, int ATK, int DEF, int SPD,
                  double critRate, double critDmg, List<Item> inventory) {
        super(name, HP, ATK, DEF, SPD, critRate, critDmg);
        this.skillPoints = 5;
        this.energy = 0;
        this.inventory = inventory;
        recalcBonuses();
    }

    public void equipWeapon(Item weapon) {
        this.equippedWeapon = weapon;
        recalcBonuses();
    }

    public Item getEquippedWeapon() { return equippedWeapon; }

    public void recalcBonuses() {
        bonusATK = bonusDEF = bonusHP = 0;
        if (equippedWeapon != null && "weapon".equalsIgnoreCase(equippedWeapon.getType())) {
            bonusATK += equippedWeapon.getAtkBonus();
            bonusDEF += equippedWeapon.getDefBonus();
            bonusHP += equippedWeapon.getHpBonus();
            critRate += equippedWeapon.getCritBonus();  // WEAPON CRIT BONUS
        }
        maxHP = 5000 + bonusHP;
        if (HP > maxHP) HP = maxHP;
    }

    @Override public int getATK() { return super.getATK() + bonusATK; }
    @Override public int getDEF() { return super.getDEF() + bonusDEF; }

    // FIXED CRIT FORMULA: baseDMG * (1 + CritDMG/100)
    public int attack(Character target) {
        double damage = getATK() * 0.5;
        if (rand.nextDouble() < (critRate / 100.0)) {
            damage *= (1 + critDmg / 100.0);  // ✅ CORRECT FORMULA
        }
        int dealt = (int) Math.min(damage, target.getHP());
        target.setHP(target.getHP() - dealt);
        skillPoints = Math.min(5, skillPoints + 1);
        return dealt;
    }

    public int useSkill(Character target) {
        if (skillPoints <= 0) return 0;
        skillPoints--;
        double damage = getATK() * 2.5;
        if (rand.nextDouble() < (critRate / 100.0)) {
            damage *= (1 + critDmg / 100.0);  // ✅ CORRECT FORMULA
        }
        int dealt = (int) Math.min(damage, target.getHP());
        target.setHP(target.getHP() - dealt);
        energy = Math.min(100, energy + (rand.nextInt(31) + 10));
        return dealt;
    }

    public int useUltimate(Character target) {
        if (energy < 100) return 0;
        energy = 0;
        double damage = getATK() * 7.0;
        if (rand.nextDouble() < (critRate / 100.0)) {
            damage *= (1 + critDmg / 100.0);  // ✅ CORRECT FORMULA
        }
        int dealt = (int) Math.min(damage, target.getHP());
        target.setHP(target.getHP() - dealt);
        return dealt;
    }

    public int heal() {
        if (skillPoints <= 0) return 0;
        skillPoints--;
        int healAmount = (int) ((maxHP - HP) * 0.3);
        setHP(Math.min(maxHP, HP + healAmount));
        return healAmount;
    }

    public void useItem(int index) {
        if (index < 0 || index >= inventory.size()) return;
        Item item = inventory.get(index);
        item.use(this);
        inventory.remove(index);
    }

    // Getters/Setters
    @Override public int getHP() { return HP; }
    public int getSkillPoints() { return skillPoints; }
    public int getEnergy() { return energy; }
    public List<Item> getInventory() { return inventory; }
    public void setHP(int hp) { this.HP = Math.max(0, Math.min(hp, maxHP)); }
    public void setSkillPoints(int sp) { this.skillPoints = Math.max(0, Math.min(sp, 5)); }
    public void setEnergy(int e) { this.energy = Math.max(0, Math.min(e, 100)); }
}

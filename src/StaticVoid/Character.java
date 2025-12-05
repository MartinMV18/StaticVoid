public class Character {
    protected String name;
    protected int HP;
    protected int maxHP;
    protected int ATK;
    protected int DEF;
    protected int SPD;
    protected double critRate;
    protected double critDmg;

    public Character(String name, int HP, int ATK, int DEF, int SPD, double critRate, double critDmg) {
        this.name = name;
        this.HP = HP;
        this.maxHP = HP;
        this.ATK = ATK;
        this.DEF = DEF;
        this.SPD = SPD;
        this.critRate = critRate;
        this.critDmg = critDmg;
    }

    public int getHP() {
        return HP;
    }

    public int getMaxHP() {
        return maxHP;
    }

    public void setHP(int HP) {
        this.HP = Math.max(0, Math.min(HP, maxHP));
    }

    public String getName() {
        return name;
    }

    public int getATK() {
        return ATK;
    }

    public int getDEF() {
        return DEF;
    }

    public int getSPD() {
        return SPD;
    }

    public double getCritRate() {
        return critRate;
    }

    public double getCritDmg() {
        return critDmg;
    }
}

import java.util.Random;

public class Enemy extends Character {
    private final Random rand = new Random();

    public Enemy(String name, int HP, int ATK, int DEF, int SPD, double critRate, double critDmg) {
        super(name, HP, ATK, DEF, SPD, critRate, critDmg);
    }

    
    public int attack(Character target) {
        int damage = rand.nextInt(401) + 200;  
        damage = Math.max(1, damage - (target.getDEF() / 5));
        
        if (rand.nextDouble() < (critRate / 100.0)) {
            damage = (int) (damage * (1 + critDmg / 100.0));  
        }
        
        int finalDamage = Math.min(damage, target.getHP());
        target.setHP(target.getHP() - finalDamage);
        return finalDamage;
    }
}

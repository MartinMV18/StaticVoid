import java.util.ArrayList;
import java.util.List;

public class GameState {
    private static GameState instance;
    private final Player player;
    private final List<Item> persistentInventory = new ArrayList<>();
    
    private GameState() {
        this.player = new Player("Debugger", 5000, 3000, 200, 150, 20, 150, persistentInventory);
    }
    
    public static GameState getInstance() {
        if (instance == null) instance = new GameState();
        return instance;
    }
    
    public Player getPlayer() { return player; }
    public List<Item> getInventory() { return persistentInventory; }
}

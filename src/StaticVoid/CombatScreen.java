import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Random;

public class CombatScreen extends JFrame {

    private final StoryScreen storyScreen;
    private Player player;
    private List<Item> inventory;
    private Enemy enemy;
    private final int totalWaves;
    private int currentWave = 0;
    private final Random rand = new Random();  // ✅ FIXED: Add missing Random

    private final JTextArea combatLog;
    private final JButton[] actionButtons;
    private final JProgressBar playerHPBar, enemyHPBar, spBar, energyBar;
    private final JLabel waveLabel;
    private final JLabel playerStatsLabel;
    private final JLabel enemyInfoLabel;

    public CombatScreen(StoryScreen storyScreen, int waves) {
        this.storyScreen = storyScreen;
        this.totalWaves = waves;

        setTitle("StaticVoid - Combat");
        setSize(900, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bg = new Color(16, 20, 32);
        Color panelBg = new Color(28, 34, 52);
        Color accent = new Color(0, 209, 178);
        Color hpColor = new Color(220, 80, 80);
        Color spColor = new Color(80, 150, 250);
        Color energyColor = new Color(250, 200, 90);

        getContentPane().setBackground(bg);

        // ✅ PERSISTENT GameState player/inventory
        player = GameState.getInstance().getPlayer();
        inventory = GameState.getInstance().getInventory();

        // TOP: wave + label
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bg);
        topPanel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("⚔️ COMBAT ENCOUNTER", SwingConstants.LEFT);
        title.setFont(new Font("Consolas", Font.BOLD, 20));
        title.setForeground(accent);

        waveLabel = new JLabel("Wave: 0 / " + totalWaves, SwingConstants.RIGHT);
        waveLabel.setFont(new Font("Consolas", Font.PLAIN, 14));
        waveLabel.setForeground(Color.LIGHT_GRAY);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(waveLabel, BorderLayout.EAST);
        add(topPanel, BorderLayout.NORTH);

        // LEFT: stats
        JPanel sidePanel = new JPanel(new GridLayout(2, 1, 0, 10));
        sidePanel.setBackground(bg);
        sidePanel.setBorder(new EmptyBorder(10, 10, 10, 6));

        // Player panel
        JPanel playerPanel = new JPanel(new GridLayout(4, 1, 6, 6));
        playerPanel.setBackground(panelBg);
        playerPanel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(accent),
                "PLAYER",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Consolas", Font.BOLD, 14),
                Color.WHITE
        ));

        playerHPBar = new JProgressBar(0, player.getMaxHP());
        playerHPBar.setValue(player.getHP());
        playerHPBar.setForeground(hpColor);
        playerHPBar.setBackground(Color.DARK_GRAY);
        playerHPBar.setStringPainted(true);
        playerHPBar.setString("HP " + player.getHP() + "/" + player.getMaxHP());

        spBar = new JProgressBar(0, 5);
        spBar.setValue(player.getSkillPoints());
        spBar.setForeground(spColor);
        spBar.setBackground(Color.DARK_GRAY);
        spBar.setStringPainted(true);
        spBar.setString("SP " + player.getSkillPoints());

        energyBar = new JProgressBar(0, 100);
        energyBar.setValue(player.getEnergy());
        energyBar.setForeground(energyColor);
        energyBar.setBackground(Color.DARK_GRAY);
        energyBar.setStringPainted(true);
        energyBar.setString("ENERGY " + player.getEnergy());

        playerStatsLabel = new JLabel(
                "ATK " + player.getATK() + " | DEF " + player.getDEF() + " | SPD " + player.getSPD(),
                SwingConstants.CENTER
        );
        playerStatsLabel.setFont(new Font("Consolas", Font.BOLD, 12));
        playerStatsLabel.setForeground(accent);

        playerPanel.add(playerHPBar);
        playerPanel.add(spBar);
        playerPanel.add(energyBar);
        playerPanel.add(playerStatsLabel);

        // Enemy panel
        JPanel enemyPanel = new JPanel(new GridLayout(2, 1, 6, 6));
        enemyPanel.setBackground(panelBg);
        enemyPanel.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(Color.RED.darker()),
                "ENEMY",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Consolas", Font.BOLD, 14),
                Color.WHITE
        ));

        enemyHPBar = new JProgressBar(0, 1);
        enemyHPBar.setValue(0);
        enemyHPBar.setForeground(hpColor);
        enemyHPBar.setBackground(Color.DARK_GRAY);
        enemyHPBar.setStringPainted(true);
        enemyHPBar.setString("No enemy");

        enemyInfoLabel = new JLabel("Awaiting hostile signature...", SwingConstants.CENTER);
        enemyInfoLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        enemyInfoLabel.setForeground(Color.LIGHT_GRAY);

        enemyPanel.add(enemyHPBar);
        enemyPanel.add(enemyInfoLabel);

        sidePanel.add(playerPanel);
        sidePanel.add(enemyPanel);
        add(sidePanel, BorderLayout.WEST);

        // CENTER: combat log
        combatLog = new JTextArea();
        combatLog.setEditable(false);
        combatLog.setFont(new Font("Consolas", Font.PLAIN, 13));
        combatLog.setBackground(Color.BLACK);
        combatLog.setForeground(new Color(0, 255, 120));
        combatLog.setLineWrap(true);
        combatLog.setWrapStyleWord(true);

        JScrollPane logScroll = new JScrollPane(combatLog);
        logScroll.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(accent.darker()),
                "COMBAT LOG",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Consolas", Font.BOLD, 14),
                Color.WHITE
        ));
        logScroll.getViewport().setBackground(Color.BLACK);

        add(logScroll, BorderLayout.CENTER);

        // BOTTOM: actions
        JPanel bottomPanel = new JPanel(new GridLayout(2, 4, 8, 8));
        bottomPanel.setBackground(bg);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] actions = {
                "ATTACK", "SKILL", "ULTIMATE",
                "HEAL", "ITEM", "STATUS", "FLEE", "VIRUS"
        };
        actionButtons = new JButton[actions.length];
        for (int i = 0; i < actions.length; i++) {
            JButton btn = new JButton(actions[i]);
            final int idx = i;
            btn.addActionListener(e -> onAction(idx));
            btn.setFont(new Font("Consolas", Font.BOLD, 12));
            btn.setBackground(panelBg);
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(accent));
            actionButtons[i] = btn;
            bottomPanel.add(btn);
        }

        add(bottomPanel, BorderLayout.SOUTH);

        combat("🔥 You enter a corrupted sector. Hostile processes gather...");
        startNextWave();

        setVisible(true);
    }

    private void startNextWave() {
        currentWave++;
        if (currentWave > totalWaves) {
            combat("✅ All hostile waves cleared. The sector stabilizes.");
            finishCombat(true);
            return;
        }

        enemy = generateRandomEnemy();
        waveLabel.setText("Wave: " + currentWave + " / " + totalWaves);
        enemyHPBar.setMaximum(enemy.getMaxHP());
        enemyHPBar.setValue(enemy.getHP());
        enemyInfoLabel.setText(enemy.getName());
        enemyHPBar.setString(enemy.getName() + " HP " + enemy.getHP() + "/" + enemy.getMaxHP());
        combat("");
        combat("=== 🚨 New enemy detected: " + enemy.getName() + " ===");
    }

    private Enemy generateRandomEnemy() {
        String[] names = {"NullPointer", "BlueScreen", "GlitchSprite", "Trojan", "KernelPanic"};
        int choice = rand.nextInt(names.length);
        return switch (choice) {
            case 0 -> new Enemy("NullPointer", 15000, 200, 50, 100, 10, 150);
            case 1 -> new Enemy("BlueScreen", 25000, 300, 100, 50, 5, 120);
            case 2 -> new Enemy("GlitchSprite", 12000, 150, 30, 150, 15, 130);
            case 3 -> new Enemy("Trojan", 20000, 250, 80, 120, 12, 140);
            case 4 -> new Enemy("KernelPanic", 40000, 400, 150, 80, 8, 160);
            default -> new Enemy("Bug", 10000, 100, 50, 50, 10, 100);
        };
    }

    private void onAction(int action) {
        if (enemy == null || enemy.getHP() <= 0 || player.getHP() <= 0) {
            return;
        }

        // ITEM
        if (action == 4) {
            if (inventory.isEmpty()) {
                combat(">> 📦 [ITEM] Inventory empty.");
                return;
            }
            String[] items = inventory.stream()
                    .map(Item::getName)
                    .toArray(String[]::new);
            String selected = (String) JOptionPane.showInputDialog(
                    this,
                    "Select program to execute:",
                    "📦 Inventory",
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    items,
                    items[0]
            );
            if (selected != null) {
                int idx = -1;
                for (int i = 0; i < inventory.size(); i++) {
                    if (inventory.get(i).getName().equals(selected)) {
                        idx = i;
                        break;
                    }
                }
                if (idx != -1) {
                    player.useItem(idx);
                    combat(">> 📦 [ITEM] Executed " + selected + ".");
                }
            }
            updateBars();
            return;
        }

        // STATUS
        if (action == 5) {
            String weaponName = player.getEquippedWeapon() != null ? player.getEquippedWeapon().getName() : "None";
            combat(">> 📊 [STATUS] HP " + player.getHP() + "/" + player.getMaxHP()
                    + " | SP " + player.getSkillPoints()
                    + " | Energy " + player.getEnergy()
                    + " | Weapon: " + weaponName);
            return;
        }

        // FLEE
        if (action == 6) {
            combat(">> 🏃 [ESCAPE] You attempt to disconnect from this sector...");
            if (rand.nextBoolean()) {
                combat(">> ✅ Disconnect successful. You abandon this encounter.");
                finishCombat(true);
            } else {
                combat(">> ❌ Escape failed. The enemy locks you in.");
            }
            updateBars();
            return;
        }

        // VIRUS EVENT
        if (action == 7) {
            virusEvent();
            return;
        }

        // Player turn
        int dealt = switch (action) {
            case 0 -> player.attack(enemy);
            case 1 -> player.useSkill(enemy);
            case 2 -> player.useUltimate(enemy);
            case 3 -> player.heal();
            default -> 0;
        };

        // ✅ CRIT VISUALS + Emojis
        String actionText = switch (action) {
            case 0 -> ">> ⚔️ [ATTACK] You deal " + dealt + " damage.";
            case 1 -> ">> 💥 [SKILL] You burst for " + dealt + " damage.";
            case 2 -> ">> 🌟 [ULTIMATE] Massive hit for " + dealt + " damage!";
            case 3 -> ">> ❤️ [HEAL] Restored " + dealt + " HP.";
            default -> "";
        };
        combat(actionText);

        updateBars();

        if (enemy.getHP() <= 0) {
            combat(">> 🏆 [VICTORY] " + enemy.getName() + " derezzed!");
            
            Item dropped = LootTable.dropLoot(player);
            if ("weapon".equalsIgnoreCase(dropped.getType())) {
                combat(">> ⚔️ [LOOT] " + dropped.getName() + " acquired! Opening upgrade interface...");
                WeaponUpgradeScreen upgradeScreen = new WeaponUpgradeScreen(this, player, dropped);
                upgradeScreen.setVisible(true);
                combat(">> 🔧 [UPGRADE] Weapon enhancements applied to your system.");
            } else {
                combat(">> 💊 [LOOT] Consumable acquired.");
            }
            
            startNextWave();
            return;
        }

        // Enemy turn
        int dmg = enemy.attack(player);
        combat(">> 👹 [ENEMY] " + enemy.getName() + " hits you for " + dmg + " damage.");
        updateBars();

        if (player.getHP() <= 0) {
            combat(">> 💀 [DEFEAT] Your process collapses.");
            finishCombat(false);
        }
    }

    private void virusEvent() {
        combat(">> 🚨 [ALERT] Rogue virus piggybacks on the current battle!");
        String[] options = {"Quarantine", "Restore Backup", "Ignore"};
        int choice = JOptionPane.showOptionDialog(
                this,
                "A rogue virus tries to corrupt the sector mid-combat.",
                "🦠 Virus Event",
                JOptionPane.DEFAULT_OPTION,
                JOptionPane.WARNING_MESSAGE,
                null,
                options,
                options[0]
        );

        int virusStrength = 50 + rand.nextInt(50);
        int playerPower = 60 + rand.nextInt(40);

        switch (choice) {
            case 0 -> {
                combat(">> 🔒 You trigger quarantine routines...");
                if (playerPower >= virusStrength) {
                    combat(">> ✅ Quarantine success. Enemy weakened.");
                    enemy.setHP(Math.max(1, enemy.getHP() - 500));
                } else {
                    combat(">> ❌ Quarantine fails. Virus reinforces the enemy.");
                    enemy.setHP(Math.min(enemy.getMaxHP(), enemy.getHP() + 500));
                }
            }
            case 1 -> {
                combat(">> 💾 You search for a stable backup state...");
                if (playerPower >= virusStrength) {
                    combat(">> ✅ Backup found. Your HP surges.");
                    player.setHP(Math.min(player.getMaxHP(), player.getHP() + 800));
                } else {
                    combat(">> ❌ Backup corrupted. Your HP drops.");
                    player.setHP(Math.max(1, player.getHP() - 800));
                }
            }
            default -> combat(">> ❓ You ignore the anomaly. It lingers in the code.");
        }

        updateBars();
    }

    private void combat(String msg) {
        if (msg == null || msg.isEmpty()) return;
        combatLog.append(msg + "\n");
        combatLog.setCaretPosition(combatLog.getDocument().getLength());
    }

    private void updateBars() {
        playerHPBar.setValue(player.getHP());
        playerHPBar.setString("HP " + player.getHP() + "/" + player.getMaxHP());

        spBar.setValue(player.getSkillPoints());
        spBar.setString("SP " + player.getSkillPoints());

        energyBar.setValue(player.getEnergy());
        energyBar.setString("ENERGY " + player.getEnergy());

        playerStatsLabel.setText(
                "ATK " + player.getATK() + " | DEF " + player.getDEF() + " | SPD " + player.getSPD()
        );

        if (enemy != null) {
            enemyHPBar.setMaximum(enemy.getMaxHP());
            enemyHPBar.setValue(enemy.getHP());
            enemyHPBar.setString(enemy.getName() + " HP " + enemy.getHP() + "/" + enemy.getMaxHP());
            enemyInfoLabel.setText(enemy.getName());
        } else {
            enemyHPBar.setValue(0);
            enemyHPBar.setString("No enemy");
            enemyInfoLabel.setText("No enemy");
        }
    }

    private void finishCombat(boolean playerWon) {
        for (JButton btn : actionButtons) {
            btn.setEnabled(false);
        }
        storyScreen.onCombatFinished(playerWon);
        dispose();
    }
}

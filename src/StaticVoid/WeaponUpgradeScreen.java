import javax.swing.*;
import java.awt.*;

public class WeaponUpgradeScreen extends JDialog {

    private final Player player;
    private final Item weapon;
    private final JLabel infoLabel;
    private final JLabel rollsLabel;
    private final JLabel previewLabel;
    private final JButton rollButton;
    private int rollsLeft = 5;
    private final java.util.Random rand = new java.util.Random();

    public WeaponUpgradeScreen(JFrame parent, Player player, Item weapon) {
        super(parent, "Weapon Upgrade", true);
        this.player = player;
        this.weapon = weapon;

        setSize(480, 320);
        setLocationRelativeTo(parent);
        getContentPane().setBackground(new Color(18, 22, 35));

        // Title
        JLabel title = new JLabel("UPGRADE " + weapon.getName().toUpperCase(), SwingConstants.CENTER);
        title.setFont(new Font("Consolas", Font.BOLD, 18));
        title.setForeground(new Color(0, 255, 178));
        title.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        add(title, BorderLayout.NORTH);

        // Main content panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(new Color(28, 35, 55));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Stats
        infoLabel = new JLabel(buildStatsText(), SwingConstants.CENTER);
        infoLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        infoLabel.setForeground(Color.WHITE);
        infoLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Rolls counter
        rollsLabel = new JLabel("Rolls left: 5", SwingConstants.CENTER);
        rollsLabel.setFont(new Font("Consolas", Font.BOLD, 20));
        rollsLabel.setForeground(new Color(255, 215, 0));
        rollsLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Preview
        previewLabel = new JLabel("Click ROLL to upgrade your weapon!", SwingConstants.CENTER);
        previewLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        previewLabel.setForeground(Color.LIGHT_GRAY);

        centerPanel.add(infoLabel, BorderLayout.NORTH);
        centerPanel.add(rollsLabel, BorderLayout.CENTER);
        centerPanel.add(previewLabel, BorderLayout.SOUTH);
        add(centerPanel, BorderLayout.CENTER);

        // Buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        buttonPanel.setBackground(new Color(18, 22, 35));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        rollButton = new JButton("ROLL UPGRADE");
        JButton doneButton = new JButton("EQUIP WEAPON");

        rollButton.addActionListener(e -> doRoll());
        doneButton.addActionListener(e -> {
            player.equipWeapon(weapon);
            dispose();
        });

        // Button styling
        Font btnFont = new Font("Consolas", Font.BOLD, 14);
        rollButton.setFont(btnFont);
        doneButton.setFont(btnFont);
        rollButton.setBackground(new Color(0, 255, 178));
        doneButton.setBackground(new Color(255, 80, 80));
        rollButton.setForeground(Color.BLACK);
        doneButton.setForeground(Color.WHITE);
        rollButton.setFocusPainted(false);
        doneButton.setFocusPainted(false);
        rollButton.setPreferredSize(new Dimension(140, 40));
        doneButton.setPreferredSize(new Dimension(140, 40));

        buttonPanel.add(rollButton);
        buttonPanel.add(doneButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    private String buildStatsText() {
        return String.format(
            "<html><div style='text-align: center;'>%s<br/>ATK: +%d &nbsp; DEF: +%d &nbsp; HP: +%d &nbsp; CRIT: +%.1f%%</div></html>",
            weapon.getName(),
            weapon.getAtkBonus(),
            weapon.getDefBonus(),
            weapon.getHpBonus(),
            weapon.getCritBonus()
        );
    }

    private void doRoll() {
        if (rollsLeft <= 0) {
            JOptionPane.showMessageDialog(this, "No rolls left! Click EQUIP WEAPON.", "Maxed Out!", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        rollsLeft--;
        int choice = rand.nextInt(5);
        int amount = 50 + rand.nextInt(51);
        String resultText;

        switch (choice) {
            case 0 -> {
                weapon.addAtkBonus(amount);
                resultText = "ATK +" + amount;
            }
            case 1 -> {
                weapon.addDefBonus(amount);
                resultText = "DEF +" + amount;
            }
            case 2 -> {
                weapon.addHpBonus(amount);
                resultText = "HP +" + amount;
            }
            case 3 -> {
                double critAmount = amount / 10.0;
                weapon.addCritBonus(critAmount);
                resultText = "CRIT +" + String.format("%.1f", critAmount) + "%";
            }
            default -> {
                weapon.addAtkBonus(amount * 2);
                resultText = "CRITICAL ATK +" + (amount * 2);
            }
        }

        // Update display
        infoLabel.setText(buildStatsText());
        rollsLabel.setText("Rolls left: " + rollsLeft);
        previewLabel.setText(resultText);

        if (rollsLeft == 0) {
            previewLabel.setText("MAX UPGRADES! Click EQUIP WEAPON.");
            rollButton.setEnabled(false);
            rollButton.setBackground(new Color(100, 100, 100));
        }
    }
}

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.Random;

public class StoryScreen extends JFrame {

    private final JTextArea storyLog;
    private final JButton[] choiceButtons;
    private final Random rand = new Random();

    private int sectorDepth = 0;   // how far you’ve progressed toward the core

    public StoryScreen() {
        setTitle("StaticVoid - Digital Timeline");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        Color bg = new Color(16, 20, 32);
        Color accent = new Color(0, 209, 178);

        getContentPane().setBackground(bg);

        // Header
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(bg);
        topPanel.setBorder(new EmptyBorder(8, 12, 8, 12));

        JLabel title = new JLabel("StaticVoid // Digital Collapse", SwingConstants.LEFT);
        title.setFont(new Font("Consolas", Font.BOLD, 22));
        title.setForeground(accent);

        JLabel depthLabel = new JLabel("Sector depth: 0", SwingConstants.RIGHT);
        depthLabel.setFont(new Font("Consolas", Font.PLAIN, 14));
        depthLabel.setForeground(Color.LIGHT_GRAY);

        topPanel.add(title, BorderLayout.WEST);
        topPanel.add(depthLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);

        // Story log (BitLife-style feed)
        storyLog = new JTextArea();
        storyLog.setEditable(false);
        storyLog.setFont(new Font("Consolas", Font.PLAIN, 13));
        storyLog.setLineWrap(true);
        storyLog.setWrapStyleWord(true);
        storyLog.setBackground(Color.WHITE);
        storyLog.setForeground(Color.DARK_GRAY);

        JScrollPane scrollPane = new JScrollPane(storyLog);
        scrollPane.setBorder(new TitledBorder(
                BorderFactory.createLineBorder(accent.darker()),
                "DIGITAL TIMELINE",
                TitledBorder.CENTER,
                TitledBorder.TOP,
                new Font("Consolas", Font.BOLD, 14),
                Color.BLACK
        ));

        add(scrollPane, BorderLayout.CENTER);

        // Choice buttons (like BitLife options at bottom)
        JPanel bottomPanel = new JPanel(new GridLayout(1, 3, 8, 8));
        bottomPanel.setBackground(bg);
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        String[] labels = {"Stabilize Sector", "Trace Virus", "Dive Deeper"};
        choiceButtons = new JButton[labels.length];

        for (int i = 0; i < labels.length; i++) {
            JButton btn = new JButton(labels[i]);
            final int idx = i;
            btn.addActionListener(e -> onChoice(idx, depthLabel));
            btn.setFont(new Font("Consolas", Font.BOLD, 14));
            btn.setBackground(new Color(28, 34, 52));
            btn.setForeground(Color.WHITE);
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(accent));
            choiceButtons[i] = btn;
            bottomPanel.add(btn);
        }

        add(bottomPanel, BorderLayout.SOUTH);

        // Intro story
        log("The StaticVoid engine coughs and sputters as reality around you fragments into broken polygons and dead pixels.");
        log("Bugs, rogue scripts, and viral payloads spread through every data lane, dragging the digital world toward total collapse.");
        log("You are the last debugger process still online. If you can fight your way to the Core and defeat the main virus, reality can be restored.");

        setVisible(true);
    }

    private void onChoice(int idx, JLabel depthLabel) {
        sectorDepth++;

        switch (idx) {
            case 0 -> {
                log("You attempt to stabilize a glitched sector, patching corrupted code and sealing memory leaks.");
                maybeSpawnCombat(false);
            }
            case 1 -> {
                log("You trace a virus signature through tangled firewalls and abandoned nodes, following its trail toward the Core.");
                maybeSpawnCombat(true);
            }
            case 2 -> {
                log("You dive deeper into the StaticVoid, leaving safer sectors behind in search of the main source of corruption.");
                maybeSpawnCombat(true);
            }
        }

        depthLabel.setText("Sector depth: " + sectorDepth);
    }

    /**
     * Sometimes combat is random, sometimes forced by the choice.
     * If forced==true, much higher chance of encounter.
     */
    private void maybeSpawnCombat(boolean forcedBias) {
        int chance = forcedBias ? 70 : 40; // percent
        if (rand.nextInt(100) < chance || sectorDepth % 5 == 0) {
            // initiate multi-wave combat
            int waves = 1 + rand.nextInt(3); // 1–3 waves
            log("Hostile processes detected! Estimated " + waves + " wave(s) of corrupted entities ahead.");
            // open combat window, pass number of waves and current story context
            new CombatScreen(this, waves);
            setEnabled(false);
        } else {
            log("You move quietly through the code. For now, the bugs only watch from the shadows.");
        }
    }

    public void log(String msg) {
        storyLog.append("• " + msg + "\n");
        storyLog.setCaretPosition(storyLog.getDocument().getLength());
    }

    // Called by combat screen when run is over
    public void onCombatFinished(boolean playerWon) {
        setEnabled(true);
        toFront();
        requestFocus();

        if (playerWon) {
            log("You emerge from the conflict, leaving derezzed fragments of malware behind you.");
            if (sectorDepth >= 10) {
                log("Deep in the StaticVoid, the main virus waits. Each victory pulls you closer to the final reset.");
            }
        } else {
            log("Your process collapses. The StaticVoid continues to unravel without your intervention...");
            for (JButton btn : choiceButtons) {
                btn.setEnabled(false);
            }
        }
    }
}

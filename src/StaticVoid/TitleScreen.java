import javax.swing.*;
import java.awt.*;

public class TitleScreen extends JFrame {

    public TitleScreen() {
        setTitle("StaticVoid");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(16, 20, 32));

        // Title at upper center
        JLabel titleLabel = new JLabel("StaticVoid", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 40));
        titleLabel.setForeground(new Color(0, 209, 178));

        JLabel subtitle = new JLabel("A digital realm on the edge of collapse", SwingConstants.CENTER);
        subtitle.setFont(new Font("Consolas", Font.PLAIN, 14));
        subtitle.setForeground(Color.LIGHT_GRAY);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);
        topPanel.setBorder(BorderFactory.createEmptyBorder(40, 10, 10, 10));
        topPanel.add(titleLabel, BorderLayout.CENTER);
        topPanel.add(subtitle, BorderLayout.SOUTH);

        add(topPanel, BorderLayout.NORTH);

        // Start button beneath
        JButton startButton = new JButton("Start");
        startButton.setFont(new Font("Consolas", Font.BOLD, 20));
        startButton.setBackground(new Color(28, 34, 52));
        startButton.setForeground(Color.WHITE);
        startButton.setFocusPainted(false);
        startButton.setBorder(BorderFactory.createLineBorder(new Color(0, 209, 178)));

        JPanel centerPanel = new JPanel();
        centerPanel.setOpaque(false);
        centerPanel.add(startButton);

        add(centerPanel, BorderLayout.CENTER);

        startButton.addActionListener(e -> {
            new StoryScreen();
            dispose();
        });

        setVisible(true);
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu implements ActionListener {
    private JButton startButton;

    MainMenu() {
        JFrame frame = new JFrame("TUGAS UAS OBP");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 300);
        frame.setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(169, 196, 235));

        ImageIcon catIcon = new ImageIcon("images/rejeki.png");
        JLabel catLabel = new JLabel(catIcon, SwingConstants.CENTER);
        panel.add(catLabel, BorderLayout.CENTER);

        JLabel titleLabel = new JLabel("SELAMAT DATANG, DAN SELAMAT BERBELANJA!");
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
        titleLabel.setForeground(Color.WHITE);

        startButton = new JButton("Start Shopping");
        startButton.addActionListener(this);
        startButton.setBackground(new Color(28, 72, 124));
        startButton.setForeground(Color.WHITE);

        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(startButton, BorderLayout.SOUTH);

        frame.add(panel);
        frame.setResizable(false);
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainMenu());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(startButton);
            currentFrame.dispose();

            new Panels();
        }
    }
}

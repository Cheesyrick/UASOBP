import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Query;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Panels implements ActionListener {
    private JButton nextButton;
    private JComboBox<String> merkDropdown;
    private JComboBox<String> jenisDropdown;
    private JComboBox<String> warnaDropdown;
    private JSpinner qtySpinner;

    private Sql2o sql2o;

    Panels() {
        sql2o = DatabaseHelper.getSql2o();

        JFrame frame = new JFrame("UAS APLIKASI OBP");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(169, 196, 235));

        JLabel merkLabel = new JLabel("Merk Cat yang ingin dibeli: ");
        merkDropdown = new JComboBox<>();
        merkLabel.setBounds(10, 10, 200, 20);
        merkDropdown.setBounds(210, 10, 150, 20);

        JLabel jenisLabel = new JLabel("Jenis Cat yang ingin dibeli: ");
        jenisDropdown = new JComboBox<>();
        jenisLabel.setBounds(10, 40, 200, 20);
        jenisDropdown.setBounds(210, 40, 150, 20);

        JLabel warnaLabel = new JLabel("Warna Cat yang ingin dibeli: ");
        warnaDropdown = new JComboBox<>();
        warnaLabel.setBounds(10, 70, 200, 20);
        warnaDropdown.setBounds(210, 70, 150, 20);

        JLabel qtyLabel = new JLabel("Quantity: ");
        qtyLabel.setBounds(10, 100, 200, 20);

        qtySpinner = new JSpinner(new SpinnerNumberModel(1, 1, 100, 1));
        qtySpinner.setBounds(210, 100, 50, 20);

        nextButton = new JButton("Next");
        nextButton.addActionListener(this);
        int buttonY = 130;
        nextButton.setBounds(210, buttonY, 150, 30);
        nextButton.setBackground(new Color(28, 72, 124));
        nextButton.setForeground(Color.WHITE);

        panel.add(merkLabel);
        panel.add(merkDropdown);
        panel.add(jenisLabel);
        panel.add(jenisDropdown);
        panel.add(warnaLabel);
        panel.add(warnaDropdown);
        panel.add(qtyLabel);
        panel.add(qtySpinner);
        panel.add(nextButton);

        frame.add(panel);

        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);

        fetchDataFromDatabase();
    }

    private void fetchDataFromDatabase() {
        try (org.sql2o.Connection con = sql2o.open()) {
            List<String> merkList = fetchData(con, "SELECT merk FROM merk");
            populateDropdown(merkDropdown, merkList);

            List<String> jenisList = fetchData(con, "SELECT jenis FROM jenis");
            populateDropdown(jenisDropdown, jenisList);

            List<String> warnaList = fetchData(con, "SELECT warna FROM warna");
            populateDropdown(warnaDropdown, warnaList);
        } catch (Exception e) {
            System.err.println("Error fetching data from the database:");
            e.printStackTrace();
        }
    }

    private List<String> fetchData(org.sql2o.Connection con, String sqlQuery) {
        List<String> itemList = new ArrayList<>();
        try {
            Query query = con.createQuery(sqlQuery);
            itemList = query.executeScalarList(String.class);
        } catch (Exception e) {
            System.err.println("Error executing query:");
            e.printStackTrace();
        }
        return itemList;
    }

    private void populateDropdown(JComboBox<String> dropdown, List<String> items) {
        dropdown.setModel(new DefaultComboBoxModel<>(items.toArray(new String[0])));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == nextButton) {
            String selectedMerk = (String) merkDropdown.getSelectedItem();
            String selectedJenis = (String) jenisDropdown.getSelectedItem();
            String selectedWarna = (String) warnaDropdown.getSelectedItem();
            int selectedQty = (int) qtySpinner.getValue();

            String generatedIdCat = generateUniqueIDCat(selectedMerk);

            new Panels2(sql2o, generatedIdCat, selectedMerk, selectedJenis, selectedWarna, selectedQty, true);

            JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(nextButton);
            currentFrame.dispose();
        }
    }

    private String generateUniqueIDCat(String selectedMerk) {
        try (org.sql2o.Connection con = sql2o.open()) {
            String sqlQuery = "SELECT IDcat FROM cat WHERE merk = :merk LIMIT 1";
            return con.createQuery(sqlQuery)
                    .addParameter("merk", selectedMerk)
                    .executeScalar(String.class);
        } catch (Exception e) {
            System.err.println("Error executing query:");
            e.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }
    
    public class Main {
        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> new Panels());
        }
    }
    
}

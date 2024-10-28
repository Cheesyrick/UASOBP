import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.UUID;

public class Panels2 implements ActionListener {
    private JButton checkoutButton;
    private JComboBox<String> dropdown;
    private JLabel idCatLabel;
    private JLabel merkLabel;
    private JLabel jenisLabel;
    private JLabel warnaLabel;
    private JLabel qtyLabel;
    private JLabel paymentLabel;
    private JLabel idPesananLabel;
    private String selectedMerk;
    private int selectedQty;
    private Sql2o sql2o;

    Panels2(Sql2o sql2o, String idCat, String selectedMerk, String selectedJenis, String selectedWarna, int selectedQty, boolean b) {
        this.sql2o = sql2o;
        this.selectedMerk = selectedMerk;
        this.selectedQty = selectedQty;

        JFrame frame = new JFrame("UAS APLIKASI OBP");

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBackground(new Color(169, 196, 235));

        JLabel label = new JLabel("Barang-barang yang dibeli:");
        label.setBounds(10, 10, 200, 20);

        idCatLabel = new JLabel("ID Cat: " + idCat);
        idCatLabel.setBounds(10, 40, 200, 20);

        merkLabel = new JLabel("Merk: " + selectedMerk);
        merkLabel.setBounds(10, 70, 200, 20);

        jenisLabel = new JLabel("Jenis: " + selectedJenis);
        jenisLabel.setBounds(10, 100, 200, 20);

        warnaLabel = new JLabel("Warna: " + selectedWarna);
        warnaLabel.setBounds(10, 130, 200, 20);

        qtyLabel = new JLabel("Qty: " + selectedQty);
        qtyLabel.setBounds(10, 160, 200, 20);

        paymentLabel = new JLabel("Payment: " + calculatePayment(selectedQty));
        paymentLabel.setBounds(10, 190, 200, 20);

        idPesananLabel = new JLabel("ID Pesanan: " + generateRandomIDPesanan());
        idPesananLabel.setBounds(10, 220, 200, 20);

        String itemsOpt[] = {"QRIS", "CASH"};
        dropdown = new JComboBox<>(itemsOpt);
        dropdown.setBounds(10, 250, 150, 30);

        checkoutButton = new JButton("Checkout!");
        checkoutButton.addActionListener(this);
        int buttonY = 300;
        checkoutButton.setBounds(10, buttonY, 150, 30);
        checkoutButton.setBackground(new Color(28, 72, 124));
        checkoutButton.setForeground(Color.WHITE);

        panel.add(label);
        panel.add(idCatLabel);
        panel.add(merkLabel);
        panel.add(jenisLabel);
        panel.add(warnaLabel);
        panel.add(qtyLabel);
        panel.add(paymentLabel);
        panel.add(idPesananLabel);
        panel.add(dropdown);
        panel.add(checkoutButton);

        frame.add(panel);

        frame.setSize(500, 500);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == checkoutButton) {
            String selectedPaymentMethod = (String) dropdown.getSelectedItem();
    
            int choice = JOptionPane.showConfirmDialog(
                    null,
                    "Apakah Anda ingin melakukan pembelian lagi? :)",
                    "Konfirmasi",
                    JOptionPane.YES_NO_OPTION);

            saveToPembayaran(idPesananLabel.getText().substring(13), selectedPaymentMethod);
    
            if (choice == JOptionPane.YES_OPTION) {
                new Panels();
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(checkoutButton);
                currentFrame.dispose();
            } else {
                JFrame currentFrame = (JFrame) SwingUtilities.getWindowAncestor(checkoutButton);
                currentFrame.dispose();
            }
        }
    }

    private int calculatePayment(int quantity) {
        return 175 * quantity;
    }

    private void saveToPembayaran(String idPesanan, String selectedPaymentMethod) {
        try (Connection con = sql2o.open()) {
            String idBayar = generateRandomIDBayar();

            String idCat = generateUniqueIDCat(selectedMerk);

            String sqlQuery = "INSERT INTO pembayaran (IDbayar, IDcat, total, QTY, IDpesanan, tipepembayaran) VALUES (:idBayar, :idCat, :total, :qty, :idPesanan, :tipePembayaran)";
            con.createQuery(sqlQuery)
                    .addParameter("idBayar", idBayar)
                    .addParameter("idCat", idCat)
                    .addParameter("total", calculatePayment(selectedQty))
                    .addParameter("qty", selectedQty)
                    .addParameter("idPesanan", idPesanan)
                    .addParameter("tipePembayaran", selectedPaymentMethod)
                    .executeUpdate();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String generateUniqueIDCat(String selectedMerk) {
        try (Connection con = sql2o.open()) {
            String sqlQuery = "SELECT IDcat FROM cat WHERE merk = :merk LIMIT 1";
            return con.createQuery(sqlQuery)
                    .addParameter("merk", selectedMerk)
                    .executeScalar(String.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return UUID.randomUUID().toString();
    }

    private String generateRandomIDPesanan() {
        StringBuilder idPesananBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char randomChar = (char) ((Math.random() * 26) + 'A');
            idPesananBuilder.append(randomChar);
        }
        return idPesananBuilder.toString();
    }

    private String generateRandomIDBayar() {
        StringBuilder idBayarBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            char randomChar = (char) ((Math.random() * 26) + 'A');
            idBayarBuilder.append(randomChar);
        }
        return idBayarBuilder.toString();
    }
}
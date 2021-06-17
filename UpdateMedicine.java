package com.medical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class UpdateMedicineFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel titleLabel = new JLabel("Update Quantity Of Medicines And Price", JLabel.CENTER);
    JLabel medicineLabel = new JLabel("Select Medicine");
    public JComboBox<String> comboBox = new JComboBox<>();
    JLabel showMedQuantityAndPriceLabel = new JLabel();
    JButton updateMedicineButton = new JButton("Upadte Medicine");
    JButton updatePriceButton = new JButton("Upadte Price");
    JButton backToHomeButton = new JButton("Back To Home");
    public int quantity;
    public int oldPrice;

    UpdateMedicineFrame() {
        loadMedicineNames();
        setLayoutManager();
        setLocationAndSize();
        addComponentsToContainer();
        setColors();
        setFontsProperties();
        addActionEvent();
    }

    private void loadMedicineNames() {
        CreateConn createConnection = CreateConn.estConnection();
        String query = "SElECT name FROM Medicines ORDER BY name ASC;";
        ResultSet resultSet;
        try {
            try (Statement statement = createConnection.conn.createStatement()) {
                resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    comboBox.addItem(resultSet.getString("name"));
                }
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    private void setLayoutManager() {
        container.setLayout(null);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0, 0, screenSize.width, screenSize.height);
    }

    private void setLocationAndSize() {
        titleLabel.setBounds(300, 100, 800, 70);
        medicineLabel.setBounds(585, 210, 250, 50);
        comboBox.setBounds(500, 250, 340, 40);
        showMedQuantityAndPriceLabel.setBounds(850, 250, 400, 40);
        updateMedicineButton.setBounds(500, 310, 170, 32);
        updatePriceButton.setBounds(680, 310, 160, 32);
        backToHomeButton.setBounds(1180, 20, 160, 32);
    }

    private void addComponentsToContainer() {
        container.add(titleLabel);
        container.add(medicineLabel);
        container.add(comboBox);
        container.add(showMedQuantityAndPriceLabel);
        container.add(updateMedicineButton);
        container.add(updatePriceButton);
        container.add(backToHomeButton);
    }

    private void setColors() {
        this.getContentPane().setBackground(new Color(26,26,26));
        titleLabel.setForeground(new Color(255,255,255));
        medicineLabel.setForeground(new Color(255,255,255));
        updateMedicineButton.setBackground(new Color(38,38,38));
        updateMedicineButton.setForeground(new Color(0,255,0));
        updatePriceButton.setBackground(new Color(38,38,38));
        updatePriceButton.setForeground(new Color(0,255,0));
        backToHomeButton.setBackground(new Color(38,38,38));
        backToHomeButton.setForeground(new Color(0,255,0));
        comboBox.setBackground(new Color(38,38,38));
        comboBox.setForeground(new Color(255,255,255));
        showMedQuantityAndPriceLabel.setForeground(new Color(0,255,0));
    }

    private void setFontsProperties() {
        titleLabel.setFont(new Font("Vardana", Font.BOLD, 40));
        medicineLabel.setFont(new Font("Vardana", Font.BOLD, 22));
        comboBox.setFont(new Font("Vardana", Font.BOLD, 18));
        showMedQuantityAndPriceLabel.setFont(new Font("Vardana", Font.BOLD, 15));
        updateMedicineButton.setFont(new Font("Vardana", Font.BOLD, 16));
        updatePriceButton.setFont(new Font("Vardana", Font.BOLD, 16));
        backToHomeButton.setFont(new Font("Vardana", Font.BOLD, 16));
    }

    private void addActionEvent() {
        comboBox.addActionListener(this);
        updateMedicineButton.addActionListener(this);
        updatePriceButton.addActionListener(this);
        backToHomeButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == comboBox) {

            CreateConn createConnection = CreateConn.estConnection();
            String selectedMedicine = String.valueOf(comboBox.getSelectedItem());
            String query = "SELECT quantity , price FROM Medicines WHERE name = '" + selectedMedicine + "'";

            try {
                try (Statement statement = createConnection.conn.createStatement()) {
                    ResultSet resultSet = statement.executeQuery(query);
                    if (comboBox.getSelectedItem() != null) {
                        quantity = resultSet.getInt("quantity");
                        oldPrice = resultSet.getInt("price");
                        showMedQuantityAndPriceLabel.setText("<html>Available quantity of " + selectedMedicine + " is " + resultSet.getString("quantity") + ".<br/>Price per tablet is " + oldPrice + "</html>");
                        showMedQuantityAndPriceLabel.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(this, "No medicine in database");
                    }
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }

        if (e.getSource() == updateMedicineButton) {

            int quantityToAdd = Integer.parseInt(JOptionPane.showInputDialog(this, "Enter quantity to add in existing quantity.", "Update Medicine",
                    JOptionPane.PLAIN_MESSAGE));

            if (quantityToAdd >= 0) {
                quantity = quantityToAdd + quantity;
                CreateConn createConnection = CreateConn.estConnection();
                String selectedMedicine = String.valueOf(comboBox.getSelectedItem());
                String updateQuery = "UPDATE Medicines SET quantity = ? WHERE name = ?";

                try {
                    try (PreparedStatement ps = createConnection.conn.prepareStatement(updateQuery)) {
                        ps.setInt(1, quantity);
                        ps.setString(2, selectedMedicine);
                        ps.executeUpdate();
                        JOptionPane.showMessageDialog(this, selectedMedicine + " Updated Successfully. New available quantity is " + quantity + ". Price per tablet is " + oldPrice + ".");
                        showMedQuantityAndPriceLabel.setText("<html>Available quantity of " + selectedMedicine + " is " + quantity + ".<br/>Price per tablet is " + oldPrice + ".</html>");
                        quantity = quantity + quantityToAdd;
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Entered Quantity is not valid");
            }
        }

        if (e.getSource() == updatePriceButton) {

            String selectedMedicine = String.valueOf(comboBox.getSelectedItem());
            String strNewPrice = JOptionPane.showInputDialog(this, "Current price per tablet of " + selectedMedicine + " is " + oldPrice + ". Enter new price per tablet.", "Update Price",
                    JOptionPane.PLAIN_MESSAGE);

            int newPrice = Integer.parseInt(strNewPrice);

            if (newPrice >= 0) {

                oldPrice = newPrice;
                CreateConn createConnection = CreateConn.estConnection();
                String updateQuery = "UPDATE Medicines SET price = ? WHERE name = ?";

                try {
                    try (PreparedStatement ps = createConnection.conn.prepareStatement(updateQuery)) {
                        ps.setInt(1, newPrice);
                        ps.setString(2, selectedMedicine);
                        ps.executeUpdate();
                        showMedQuantityAndPriceLabel.setText("<html>Available quantity of " + selectedMedicine + " is " + (quantity) + "." + ".<br/>Price per tablet is " + newPrice + ".</html>");
                        JOptionPane.showMessageDialog(this, selectedMedicine + " Price Updated Successfully. New Price per tablet is " + (newPrice) + ".");
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Entered Quantity is not valid");
            }

        }

        if (e.getSource() == backToHomeButton) {

            JOptionPane.showMessageDialog(this, "Back to home page");
        }

    }
}

public class UpdateMedicine {

    public static void main(String[] args) {
        UpdateMedicineFrame updateFrame = new UpdateMedicineFrame();
        updateFrame.setTitle("Delete Medicine From Database");
        updateFrame.setVisible(true);
        updateFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        updateFrame.setResizable(false);
    }
}

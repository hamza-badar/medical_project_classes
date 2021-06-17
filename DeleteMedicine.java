package com.medical;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

class DeleteMedicineFrame extends JFrame implements ActionListener {

    Container container = getContentPane();
    JLabel titleLabel = new JLabel("Delete Medicine From Database");
    JLabel medicineLabel = new JLabel("Select Medicine");
    JComboBox<String> comboBox = new JComboBox<>();
    JButton deleteButton = new JButton("Delete Medicine");
    JButton backToHomeButton = new JButton("Back To Home");

    DeleteMedicineFrame() {
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
        titleLabel.setBounds(370, 100, 700, 70);
        medicineLabel.setBounds(585, 210, 250, 50);
        comboBox.setBounds(500, 250, 330, 40);
        deleteButton.setBounds(500, 320, 160, 32);
        backToHomeButton.setBounds(670, 320, 160, 32);
    }

    private void addComponentsToContainer() {
        container.add(titleLabel);
        container.add(medicineLabel);
        container.add(comboBox);
        container.add(deleteButton);
        container.add(backToHomeButton);
    }

    private void setColors() {
        this.getContentPane().setBackground(new Color(26,26,26));
        titleLabel.setForeground(new Color(255,255,255));
        deleteButton.setBackground(new Color(38,38,38));
        deleteButton.setForeground(new Color(255,0,0));
        backToHomeButton.setBackground(new Color(38,38,38));
        backToHomeButton.setForeground(new Color(255,255,255));
        medicineLabel.setForeground(new Color(255,255,255));
        comboBox.setBackground(new Color(38,38,38));
        comboBox.setForeground(new Color(255,255,255));
    }

    private void setFontsProperties() {
        titleLabel.setFont(new Font("Vardana", Font.BOLD, 45));
        medicineLabel.setFont(new Font("Vardana", Font.BOLD, 22));
        comboBox.setFont(new Font("Vardana", Font.BOLD, 18));
        deleteButton.setFont(new Font("Vardana", Font.BOLD, 16));
        backToHomeButton.setFont(new Font("Vardana", Font.BOLD, 16));
    }

    private void addActionEvent() {
        comboBox.addActionListener(this);
        deleteButton.addActionListener(this);
        backToHomeButton.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == deleteButton) {

            int result = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete?", "Delete Medicine",
                    JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                CreateConn createConnection = CreateConn.estConnection();
                String selectedMedicine = String.valueOf(comboBox.getSelectedItem());
                String query = "DELETE FROM Medicines WHERE name = '" + selectedMedicine + "'";

                try {
                    try (Statement statement = createConnection.conn.createStatement()) {
                        statement.executeUpdate(query);
                        if (comboBox.getSelectedItem() != null) {
                            JOptionPane.showMessageDialog(this, selectedMedicine + " successfully deleted");
                        } else {
                            JOptionPane.showMessageDialog(this, "No medicine in database");
                        }
                        comboBox.removeItem(selectedMedicine);
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            } else if (result == JOptionPane.NO_OPTION) {
                JOptionPane.showMessageDialog(this, "Nothing deleted");
            }
        }

        if (e.getSource() == backToHomeButton) {
            JOptionPane.showMessageDialog(this, "Back to home page");
        }

    }
}

public class DeleteMedicine {

    public static void main(String[] args) {
        DeleteMedicineFrame deleteFrame = new DeleteMedicineFrame();
        deleteFrame.setTitle("Delete Medicine From Database");
        deleteFrame.setVisible(true);
        deleteFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteFrame.setResizable(false);
    }
}

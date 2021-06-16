// @formatter:off
package com.medical;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;import java.awt.event.KeyEvent;import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectMedicine extends JFrame implements ActionListener
{
    private JLabel title,availableQuantityLabel,availableQuantity,perUnitPriceLabel,perUnitPrice,totalAmountLabel;
    private JTextField searchInput,quantityInput;
    private JButton searchButton,addButton,createBill;
    private JComboBox medicineList;
    private DefaultTableModel defaultTableModel;
    private JTable table;
    private JScrollPane pane;
    private int totalAmount;

    SelectMedicine()
    {
        totalAmount=0;
        setTable();

        title=new JLabel("Select Require Medicine(s)");
        perUnitPriceLabel=new JLabel("per unit price : ");
        perUnitPrice=new JLabel("");

        totalAmountLabel=new JLabel("Total Amount : ");
        searchInput=new JTextField("");
        searchButton=new JButton("Search");
        addButton=new JButton("Add");
        medicineList=new JComboBox();
        quantityInput=new JTextField("");
        availableQuantityLabel=new JLabel("available quantity : ");
        availableQuantity=new JLabel("");
        createBill=new JButton("Create Bill");


        appendListener();

        setComponentStyle();

        setLayoutManager();

        addComponent();

        setOnCloseEvent();
    }
    private void setTable()
    {
        defaultTableModel = new DefaultTableModel();
        defaultTableModel.addColumn("Name");
        defaultTableModel.addColumn("Quantity");
        defaultTableModel.addColumn("price per unit");
        defaultTableModel.addColumn("Total Price");

        table=new JTable();
        pane=new JScrollPane(table);

        table.setModel(defaultTableModel);
    }
    private void appendListener()
    {
        searchButton.addActionListener(this);
        medicineList.addActionListener(this);
        addButton.addActionListener(this);
        createBill.addActionListener(this);
        quantityInput.addKeyListener( new KeyAdapter()
        {
            public void keyPressed(KeyEvent ke)
            {
                if ((ke.getKeyChar() >= '0' && ke.getKeyChar() <= '9')|| ke.getKeyCode()==KeyEvent.VK_BACK_SPACE)
                {
                    quantityInput.setEditable(true);
                }
                else
                {
                    quantityInput.setEditable(false);
                }
            }
        });
    }

    public void actionPerformed(ActionEvent e)
    {

        switch(e.getActionCommand())
        {
            case "Search":
                SearchButtonEvent();
            break;

            case "Add" :
                AddButtonEvent();
            break;

            case "Create Bill" :
                CreateBill obj=new CreateBill();
                obj.setVisible(true);
            break;

            default:
                medicineListEvent();
            break;
        }
    }
    private void SearchButtonEvent()
    {
        CreateConn con = CreateConn.estConnection();
        Statement stmt;
        if (searchInput.getText().equals(""))
        {
            try
            {
                medicineList.removeAllItems();
                stmt = con.conn.createStatement();
                ResultSet rs = stmt.executeQuery("select name from Medicines order by name asc");
                while (rs.next())
                {
                    medicineList.addItem(rs.getString("name"));

                }
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        }
        else
        {
            try
            {
                medicineList.removeAllItems();
                PreparedStatement pstmt=con.conn.prepareStatement("SELECT name FROM Medicines where name like ? order by name asc");
                pstmt.setString(1,searchInput.getText()+"%");

                ResultSet rs = pstmt.executeQuery();
                while (rs.next())
                {
                    medicineList.addItem(rs.getString("name"));
                }
            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        }
    }

    private void AddButtonEvent()
    {
        if(Integer.valueOf(quantityInput.getText())>Integer.valueOf(availableQuantity.getText()))
        {

        }
        else {
            CreateConn con = CreateConn.estConnection();
            try
            {
                PreparedStatement pstmt = con.conn.prepareStatement("SELECT price FROM Medicines where name like ?");
                pstmt.setString(1, medicineList.getSelectedItem() + "%");
                ResultSet rs = pstmt.executeQuery();

                while (rs.next())
                {
                    String name = String.valueOf(medicineList.getSelectedItem());
                    Integer quantity = Integer.valueOf(quantityInput.getText());
                    Integer price = rs.getInt("price");
                    Integer totalPrice = rs.getInt("price") * Integer.valueOf(quantityInput.getText());
                    pstmt = con.conn.prepareStatement("insert into selectMedicine values(?,?,?,?)");
                    pstmt.setString(1, name);
                    pstmt.setInt(2, quantity);
                    pstmt.setInt(3, price);
                    pstmt.setInt(4, totalPrice);
                    pstmt.executeUpdate();
                    totalAmount = totalAmount + totalPrice;
                    totalAmountLabel.setText("Total Amount : " + totalAmount);
                    defaultTableModel.addRow(new String[]{name, String.valueOf(quantity), String.valueOf(price), String.valueOf(totalPrice)});
                }

            }
            catch (SQLException throwables)
            {
                throwables.printStackTrace();
            }
        }
    }
    private void medicineListEvent()
    {
        CreateConn con=CreateConn.estConnection();
        try
        {
            PreparedStatement pstmt=con.conn.prepareStatement("SELECT quantity,price FROM Medicines where name like ?");
            pstmt.setString(1,medicineList.getSelectedItem()+"%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next())
            {
                availableQuantity.setText(String.valueOf(rs.getInt("quantity")));
                perUnitPrice.setText(String.valueOf(rs.getInt("price")));
            }
        }
        catch (SQLException throwables)
        {
            throwables.printStackTrace();
        }
    }
    private void setComponentStyle()
    {
        setLocationAndSize();

        setColor();

        setFont();


    }
    private void setLocationAndSize()
    {
        pane.setBounds(450,400,500,200);
        title.setBounds(500, 50, 700, 50);
        searchInput.setBounds(450,150,310, 30);
        searchButton.setBounds(800,150,150,30);
        medicineList.setBounds(450,200,310,30);
        availableQuantityLabel.setBounds(800,200,200,30);
        availableQuantity.setBounds(980,200,30,30);
        perUnitPriceLabel.setBounds(800,250,250,30);
        perUnitPrice.setBounds(940,250,30,30);
        quantityInput.setBounds(800,300,150,30);
        addButton.setBounds(800,350,150,30);
        createBill.setBounds(950,610,200,30);
        totalAmountLabel.setBounds(700,610,250,30);
    }
    private void setColor()
    {
        this.getContentPane().setBackground(new Color(26,26,26));
        title.setForeground(new Color(255,255,255));
        searchInput.setBackground(new Color(38,38,38));
        searchInput.setForeground(new Color(255,255,255));
        searchInput.setCaretColor(new Color(255,255,255));
        searchButton.setBackground(new Color(38,38,38));
        searchButton.setForeground(new Color(0,255,0));
        medicineList.setBackground(new Color(38,38,38));
        medicineList.setForeground(new Color(255,255,255));
        availableQuantityLabel.setForeground(new Color(255,255,255));
        availableQuantity.setForeground(new Color(0,255,0));
        perUnitPriceLabel.setForeground(new Color(255,255,255));
        perUnitPrice.setForeground(new Color(0,255,0));
        quantityInput.setBackground(new Color(38,38,38));
        quantityInput.setForeground(new Color(255,255,255));
        quantityInput.setCaretColor(new Color(255,255,255));
        quantityInput.setBackground(new Color(38,38,38));
        table.setBackground(new Color(38,38,38));
        table.setForeground(new Color(255,255,255));
        addButton.setBackground(new Color(38,38,38));
        addButton.setForeground(new Color(0,255,0));
        totalAmountLabel.setForeground(new Color(255,255,255));
        createBill.setBackground(new Color(38,38,38));
        createBill.setForeground(new Color(0,255,0));
    }
    private void setFont()
    {
        totalAmountLabel.setFont(new Font("Vardana", Font.PLAIN,  22));
        createBill.setFont(new Font("Vardana", Font.BOLD,22));
        table.setFont(new Font("Vardana", Font.PLAIN,17));
        title.setFont(new Font("Vardana", Font.BOLD,  40));
        searchInput.setFont(new Font("Vardana", Font.PLAIN,22));
        searchButton.setFont(new Font("Vardana", Font.BOLD,22));
        medicineList.setFont(new Font("Vardana", Font.PLAIN,17));
        quantityInput.setFont(new Font("Vardana", Font.PLAIN,  22));
        availableQuantityLabel.setFont(new Font("Vardana", Font.PLAIN,  22));
        availableQuantity.setFont(new Font("Vardana", Font.PLAIN,  22));
        perUnitPriceLabel.setFont(new Font("Vardana", Font.PLAIN,  22));
        perUnitPrice.setFont(new Font("Vardana", Font.PLAIN,  22));
        addButton.setFont(new Font("Vardana", Font.BOLD,  22));
    }
    private void addComponent()
    {
        add(title);
        add(searchInput);
        add(searchButton);
        add(medicineList);
        add(quantityInput);
        add(addButton);
        add(availableQuantity);
        add(availableQuantityLabel);
        add(pane);
        add(perUnitPrice);
        add(perUnitPriceLabel);
        add(createBill);
        add(totalAmountLabel);
    }

    private void setLayoutManager()
    {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(0,0,screenSize.width, screenSize.height);
        setLayout(null);
    }
    private void setOnCloseEvent()
    {
        try
        {
            CreateConn con=CreateConn.estConnection();
            Statement stmt = con.conn.createStatement();
            stmt.executeUpdate("DELETE from selectMedicine");
        }
        catch(SQLException throwables)
        {

        }
        this.setVisible(false);
    }
}

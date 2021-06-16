// @formatter:off
package com.medical;

import javax.swing.*;
import java.awt.*;import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;import java.util.ArrayList;import java.util.Arrays;

public class CreateBill extends JFrame implements ActionListener
{
    private JTextField nameInput;
    private JLabel customerNameLabel;
    private JButton saveBillButton;
    private String dateTime;
    private String name;
    private long payAmount;
    CreateBill()
    {
        nameInput=new JTextField("");
        customerNameLabel=new JLabel("Customer Name");
        saveBillButton=new JButton("Save");

        appendListener();
        setComponentStyle();
        setLayoutManager();
        addComponent();
        setOnCloseEvent();

    }
    private void initDateTime()
    {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        dateTime=String.valueOf(dtf.format(now));
    }
    private void saveBill()
    {
        try
        {
            CreateConn con=CreateConn.estConnection();
            Statement stmt = con.conn.createStatement();
            initName();
            initDateTime();
            ResultSet rs=stmt.executeQuery("select * from selectMedicine");
            File bill = new File("C:\\Bills\\"+name+" "+dateTime+".txt");
            bill.createNewFile();
            FileWriter billWriter = new FileWriter("C:\\Bills\\"+name+" "+dateTime+".txt");
            billWriter.write("Name :"+name+"\t\t\t\t"+"Date : "+dateTime+"\n");
            ArrayList<String> headersList = new ArrayList<>(Arrays.asList("Name","Quantity","Price per unit","Total Price"));
            ArrayList<ArrayList<String>> rowsList =new ArrayList<>();
            ArrayList<String> tempList=new ArrayList<>();
            while(rs.next())
            {
                String name=rs.getString("name");
                Integer quantity = rs.getInt("quantity");
                Integer price = rs.getInt("price");
                Integer totalPrice = rs.getInt("totalPrice");
                payAmount=payAmount+totalPrice;

                tempList.add(name);
                tempList.add(String.valueOf(quantity));
                tempList.add(String.valueOf(price));
                tempList.add(String.valueOf(totalPrice));
                rowsList.add(new ArrayList<>(tempList));
                tempList.clear();
            }
            Board board = new Board(75);
            String tableString = board.setInitialBlock(new Table(board, 75, headersList, rowsList).tableToBlocks()).build().getPreview();
            billWriter.write(tableString);
            billWriter.write("\t\t\t\t\t\t\tTotal Amount : "+payAmount);
            billWriter.close();
            stmt.executeUpdate("DELETE from selectMedicine");
            ProcessBuilder openBill = new ProcessBuilder("Notepad.exe", "C:\\Bills\\"+name+" "+dateTime+".txt");
            openBill.start();
        }
        catch(SQLException throwables)
        {

        }
        catch(IOException e)
        {

        }
    }
    private void initName()
    {
        name=nameInput.getText();
    }
    private void appendListener()
    {
        saveBillButton.addActionListener(this);
    }
    public void actionPerformed(ActionEvent e)
    {
        saveBill();
    }
    private void setComponentStyle()
    {
        setLocationAndSize();

        setColor();

        setFont();
    }
    private void setLocationAndSize()
    {
        customerNameLabel.setBounds(30,50,310,30);
        nameInput.setBounds(200,50,310, 30);
        saveBillButton.setBounds(200,100,150,30);
    }
    private void setColor()
    {
        this.getContentPane().setBackground(new Color(26,26,26));
        customerNameLabel.setForeground(new Color(0,255,0));
        nameInput.setBackground(new Color(38,38,38));
        nameInput.setForeground(new Color(255,255,255));
        nameInput.setCaretColor(new Color(255,255,255));
        saveBillButton.setBackground(new Color(38,38,38));
        saveBillButton.setForeground(new Color(0,255,0));
    }
    private void setFont()
    {
        customerNameLabel.setFont(new Font("Vardana", Font.PLAIN,  22));
        nameInput.setFont(new Font("Vardana", Font.PLAIN,  22));
        saveBillButton.setFont(new Font("Vardana", Font.BOLD,  22));
    }
    private void addComponent()
    {
        add(nameInput);
        add(customerNameLabel);
        add(saveBillButton);
    }
    private void setLayoutManager()
    {
        this.setBounds(600,300,550,250);
        setLayout(null);
    }
    private void setOnCloseEvent()
    {
        this.setVisible(false);
    }
}

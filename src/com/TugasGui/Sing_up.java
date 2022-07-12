package com.TugasGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Sing_up extends JFrame{
    private JPanel Panel;
    private JTextField txt_name;
    private JTextField txt_email;
    private JPasswordField txt_password;
    private JPasswordField txt_confirm;
    private JButton btn_cancel;
    private JButton btn_register;
    private String title;
    db_connect connection = new db_connect();
    public Sing_up(String title) {
        this.setTitle(title);
        this.title = title;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(Panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Login login = new Login(title);
                dispose();
            }
        });
        btn_register.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });
    }
    private void registerUser() {
        String name = txt_name.getText();
        String email = txt_email.getText();
        String pwd = String.valueOf(txt_password.getPassword());
        String confpwd = String.valueOf(txt_confirm.getPassword());

        if(name.isEmpty() || email.isEmpty() || pwd.isEmpty()){
            error();
            return;
        }

        if(!pwd.equals(confpwd)){
           error();
            return;
        }

        user = addUserToDatabase(name,email,pwd);
        if(user != null){
            Login login = new Login(title);
            dispose();
        }
        else {
            JOptionPane.showMessageDialog(this,
                    "Failed to register new user",
                    "Try again",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    public  User user;
    private User addUserToDatabase(String name, String email, String pwd) {
        User user = null;
        try{
            Connection conn = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            Statement stmt = conn.createStatement();
            String sql = "Insert into users (Name,email,password,level) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,email);
            preparedStatement.setString(3,pwd);
            preparedStatement.setString(4,"user");

            int addedRows = preparedStatement.executeUpdate();
            if(addedRows > 0 ){
                user = new User();
                user.name = name;
                user.email = email;
                user.password = pwd;
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


    private void error(){
        JOptionPane.showMessageDialog(this,"Please enter all fields","Try again",JOptionPane.ERROR_MESSAGE);
        reset();
    }
    private void reset(){
        txt_name.setText("");
        txt_email.setText("");
        txt_password.setText("");
        txt_confirm.setText("");
    }
}

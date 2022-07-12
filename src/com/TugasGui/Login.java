package com.TugasGui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class Login extends JFrame{
    private JPanel Panel;
    private JTextField txt_email;
    private JPasswordField txt_password;
    private JButton btn_sing_up;
    private JButton btn_login;
    private String title;
    db_connect connection = new db_connect();

    Login(String title){
        this.setTitle(title);
        this.title = title;
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(Panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        btn_sing_up.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                Sing_up sing_up = new Sing_up(title);
            }
        });
        btn_login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = txt_email.getText();
                String pwd = String.valueOf(txt_password.getPassword());

                user = (User) getAuthenticatedUser(email,pwd);
                if(user != null){
                   dispose();
                   DashBoard dashBoard = new DashBoard(user.level);
                }else{
                    JOptionPane.showMessageDialog(Login.this,
                            "Email or Password Invalid",
                            "Try again",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    public User user;
    private Object getAuthenticatedUser(String email, String pwd) {
        User user = null;
        try{
            Connection con = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            // Connected to database successfully...

            Statement stmt = con.createStatement();
            String sql = "SELECT * FROM users WHERE email=? AND password=?";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, email);
            preparedStatement.setString(2, pwd);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                user = new User();
                user.name = resultSet.getString("Name");
                user.email = resultSet.getString("email");
                user.password = resultSet.getString("password");
                user.level = resultSet.getString("level");
            }
            stmt.close();
            con.close();

        }catch(Exception e){
            e.printStackTrace();
        }
        return user;
    }
}

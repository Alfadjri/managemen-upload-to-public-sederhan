package com.TugasGui;



import com.toedter.calendar.JDateChooser;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.*;
import java.text.SimpleDateFormat;


public class AddMovie extends JFrame {
    private JPanel Panel;
    private JTextField txt_name;
    private JTextField txt_duration;
    private JTextArea txt_Deskription;
    private JComboBox cb_Director;
    private JComboBox cb_Writers;
    private JComboBox cb_Categorie;
    private JButton btn_upload;
    private JTextArea txt_file;
    private JButton btn_cancel;
    private JButton btn_sumbit;
    private JPanel pn_date;
    private  String title;
    db_connect connection = new db_connect();
    JDateChooser dateChooser = new JDateChooser();

    AddMovie(String title){
        this.setTitle(title);
        this.title = title;
       this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(Panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        cb_Categorie.setEditable(false);
        getCategory();
        getWriter();
        getDirector();
        dateChooser.setDateFormatString("dd-MM-yyyy");
        pn_date.add(dateChooser);

        txt_file.setEditable(false);
        cb_Writers.setEditable(true);
        cb_Director.setEditable(true);
        btn_cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
                DashBoard dashBoard = new DashBoard("Admin");
            }
        });
        btn_sumbit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String name,Release,Duration,Deskrisi,Director,Writers,Categorie,image,id_movie = null;
                SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
                boolean kondisi = true;
                int countWriter = 0;
                int countDirector = 0;
                int countMovie = 0;
                name = String.valueOf(txt_name.getText());
                Release = String.valueOf(dateFormat.format(dateChooser.getDate()));
                Duration = String.valueOf(txt_duration.getText());
                Deskrisi = String.valueOf(txt_Deskription.getText());
                Director = (String) cb_Director.getSelectedItem();
                Writers = (String) cb_Writers.getSelectedItem();
                Categorie = (String) cb_Categorie.getSelectedItem();
                image = String.valueOf(txt_file.getText());

            do {
                countDirector = cekValue("drirector", Director);
                if (countDirector == 0) {
                    insertValue("drirector",Director);
                } else {
                    Director = getValue("drirector", Director);
                    kondisi = false;
                }
            } while (kondisi);
            kondisi = true;
            do {
                countWriter = cekValue("writers", Writers);
                if (countWriter == 0) {
                    insertValue("writers",Writers);
                } else {
                    Writers = getValue("writers", Writers);
                    kondisi = false;
                }
            } while (kondisi);
            kondisi = true;
            do{
                countMovie = cekMovie(name);
                if (countMovie == 0){
                    insertMovie(name,Release,Duration,Deskrisi,image);
                }else{
                    id_movie = getMovie(name);
                    kondisi = false;
                }
            }while (kondisi);
                String categorie = getValue("category",Categorie);
                insertData(id_movie,Director,Writers,categorie);
                dispose();
                DashBoard dashBoard = new DashBoard("Admin");
            }
        });


        btn_upload.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                getFile();
            }
        });

    }

    private void insertData(String id_movie,String Director,String Writers,String Categorie) {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
             String sql = "Insert into d_movie (id_l_movie,m_director,m_writers,m_category) VALUES (?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, id_movie);
            preparedStatement.setString(2, Director);
            preparedStatement.setString(3, Writers);
            preparedStatement.setString(4, Categorie);
            preparedStatement.executeUpdate();
            sql = "Select * from d_movie where id_l_movie='"+ id_movie +"'";
            ResultSet rs = preparedStatement.executeQuery(sql);
            String id = null;
            while(rs.next()){
                id = String.valueOf(rs.getString("id_l_movie"));
            }
            sql = "Insert into movie (id_movie,reting,m_publish) value (?,?,?)";
            preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,id);
            preparedStatement.setString(2, String.valueOf(0));
            preparedStatement.setString(3,"hide");
            preparedStatement.executeUpdate();
            preparedStatement.close();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }
    private int cekMovie(String Value) {
        Connection conn = null;
        int countDirector = 0;
        String id = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
            String director = "Select * from list_movie where m_name = '" + Value + "' ";
            PreparedStatement cekStatement = conn.prepareStatement(director);
            ResultSet rs = cekStatement.executeQuery();
            while (rs.next()) {
                countDirector++;
            }
            conn.close();
            stmt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
      return  countDirector;
    }
    private int cekValue(String table , String Value) {
        Connection conn = null;
        int countDirector = 0;
        String id = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
            String director = "Select * from "+table+" where name = '" + Value + "' ";
            PreparedStatement cekStatement = conn.prepareStatement(director);
            ResultSet rs = cekStatement.executeQuery();
            while (rs.next()) {
                countDirector++;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
      return  countDirector;
    }
    private String getMovie(String Value){
        Connection conn = null;
        String id = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
            String list = "Select * from list_movie where m_name = '" + Value + "' ";
            PreparedStatement cekStatement = conn.prepareStatement(list);
            ResultSet rs = cekStatement.executeQuery();
            while (rs.next()) {
                id = String.valueOf(rs.getString("id"));
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  id;
    }
    private String getValue(String table, String Value){
        Connection conn = null;
        int countDirector = 0;
        String id = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
            String director = "Select * from "+table+" where name = '" + Value + "' ";
            PreparedStatement cekStatement = conn.prepareStatement(director);
            ResultSet rs = cekStatement.executeQuery();
            while (rs.next()) {
                id = String.valueOf(rs.getString("id"));
            }
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  id;
    }
    private void insertValue(String table , String value){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
            String sql = "Insert into "+table+" (name) VALUES (?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,value);
            preparedStatement.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void insertMovie(String name,String release, String duration, String deskrisi, String image){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            Statement stmt = conn.createStatement();
            String sql = "Insert into list_movie (m_name,m_released,m_duration,m_deskripsi,m_image) VALUES (?,?,?,?,?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1,name);
            preparedStatement.setString(2,release);
            preparedStatement.setString(3,duration);
            preparedStatement.setString(4,deskrisi);
            preparedStatement.setString(5,image);
            preparedStatement.executeUpdate();
            stmt.close();
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void getFile(){
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        File f = fileChooser.getSelectedFile();
        String fileName = f.getAbsolutePath();
        txt_file.setText(fileName);
    }
    private void getDirector(){
         try{
            Connection con = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            String query = "Select * from drirector" ;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next() ){
                String name = String.valueOf(rs.getString("name"));
                cb_Director.addItem(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getWriter(){
         try{
            Connection con = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            String query = "Select * from writers" ;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next() ){
                String name = String.valueOf(rs.getString("name"));
                cb_Writers.addItem(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void getCategory() {
        try{
            Connection con = DriverManager.getConnection(connection.databases, connection.user, connection.pwd);
            String query = "Select * from category" ;
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next() ){
                String name = String.valueOf(rs.getString("name"));
                cb_Categorie.addItem(name);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

package com.TugasGui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class DetailMovie extends  JFrame{
    private JPanel Panel;
    private JPanel panel_gambar;
    private JTextArea txt_deskripsion;
    private JButton btn_staus;
    private JButton btn_close;
    private JLabel txt_judul;
    private JLabel txt_gender;
    private JLabel txt_penulis;
    private JLabel txt_director;
    private JLabel JImage;
    private JButton btn_delete;
    private ImageIcon Imageicon;
    db_connect connect = new db_connect();
    String status,query,name,gambar,deskription,gender,wrieter,director;
    List<String> category = new ArrayList<>();
    List<String> penulis = new ArrayList<>();
    List<String> direktur = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    DetailMovie(String user, String lbl){
        try{
            Connection con = DriverManager.getConnection(connect.databases,connect.user,connect.pwd);
            Statement statement = con.createStatement();
            query = "Select l.m_name as name ," +
                    " l.m_released as 'release', " +
                    "l.m_duration as duration , " +
                    "l.m_deskripsi as deskripsi , " +
                    "l.m_image as image," +
                    "c.name as category," +
                    "w.name as writes," +
                    "dr.name as drirecto," +
                    "m.m_publish as status " +
                    "from movie m " +
                    "join d_movie d on m.id_movie = d.id_l_movie " +
                    "join list_movie l on l.id = d.id_l_movie " +
                    "join category c on c.id = d.m_category " +
                    "JOIN writers w on w.id = d.m_writers " +
                    "join drirector dr on dr.id = d.m_director where m.id_movie ='"+lbl+"'" +
                    "GROUP BY category";
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                name =  String.valueOf(rs.getString("name"));
                status = String.valueOf(rs.getString("status"));
                gambar = String.valueOf(rs.getString("image"));
                deskription = String.valueOf(rs.getString("deskripsi"));
                category.add(String.valueOf(rs.getString("category")));
                penulis.add(String.valueOf(rs.getString("writes")));
                direktur.add(String.valueOf(rs.getString("drirecto")));

            }
            con.close();
            rs.close();
            statement.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.dispatchEvent(new WindowEvent(this,WindowEvent.WINDOW_CLOSING));

        txt_deskripsion.setEditable(false);


        this.setTitle(name);
        txt_judul.setText(name);
        btn_staus.setText(status);
        txt_deskripsion.setText(deskription);
        gender = getList(category);
        director = getList(direktur);
        wrieter = getList(penulis);
        txt_gender.setText(gender);
        txt_director.setText(director);
        txt_penulis.setText(wrieter);
//        Image
        Imageicon = new ImageIcon(gambar);
        Image Image = Imageicon.getImage();
        Image newImage = Image.getScaledInstance(150,250,java.awt.Image.SCALE_SMOOTH);
        Imageicon = new ImageIcon(newImage);
        JImage.setIcon(Imageicon);
        JImage.setText("");
        this.setContentPane(Panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        btn_close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        if(user == "user"){
            btn_staus.setVisible(false);
            btn_delete.setVisible(false);
        }

        btn_delete.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                query = "DELETE from movie where id_movie = " + lbl;
                Connection con = null;
                try {
                    con = DriverManager.getConnection(connect.databases,connect.user,connect.pwd);
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query);
                    con.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });
        btn_staus.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(btn_staus.getText().equals("hide") ){
                    query = "UPDATE movie set m_publish = 'show' where id_movie =" + lbl;
                }else{
                     query = "UPDATE movie set m_publish = 'hide' where id_movie =" + lbl;
                }
                Connection con = null;
                try {
                    con = DriverManager.getConnection(connect.databases,connect.user,connect.pwd);
                    Statement statement = con.createStatement();
                    statement.executeUpdate(query);
                    con.close();
                    statement.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
                dispose();
            }
        });


    }

    private String getList(List data){
        HashSet<String> value = new HashSet<>(data);
        String res = String.join(",",value);
        return res;
    }
}

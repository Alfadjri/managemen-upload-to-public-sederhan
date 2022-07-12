package com.TugasGui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class DashBoard extends JFrame{
    private JPanel Panel;
    private JTextField txt_pencarian;
    private JButton btn_add;
    private JTable tbl_List;
    private JButton btn_cari;
    private JPanel Table;
    private JTable tbl_list;
    private JScrollPane panel;
    private String title;
    private String level;
    db_connect connection = new db_connect();
    String pencarian;
    DashBoard(String level){

        this.setTitle("List Movie Reating");
        this.title = title;
        this.level = String.valueOf(level);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setContentPane(Panel);
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
        if(this.level.equals("user")){
            btn_add.setVisible(false);
            getTable("user","");
            btn_cari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pencarian = String.valueOf(txt_pencarian.getText());
                tbl_List.repaint();
                getTable("user",pencarian);
            }
            });
        }else{
           getTable("Admin","");
            btn_cari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pencarian =  String.valueOf(txt_pencarian.getText());
                tbl_List.repaint();
                getTable("Admin",pencarian);
            }
            });
        }
        btn_add.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                AddMovie add =new AddMovie(title);
                dispose();
            }
        });
    }

        private void getTable(String user , String pencarian){
        String[] list_table = new String[0];
        String query = null;
        tbl_List = new JTable();
        ButtonColumn buttonColumn;

        int no = 1;
        try {
            Connection con = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            if(user == "Admin" ){
                list_table = new String[]{"no","Nama movie","Renting","Status","Aksi"};
                if(pencarian != null){
                    query = "Select  l.m_name as name , " +
                            "m.reting as reting," +
                            "m.m_publish as status, " +
                            "l.id as id  " +
                            "from movie m " +
                            "JOIN d_movie d on d.id_l_movie = m.id_movie " +
                            "JOIN list_movie l on d.id_l_movie = l.id " +
                            "where l.m_name like '%"+pencarian+"%'"+
                            "GROUP BY name";
                }else{
                    query = "Select l.m_name as name , " +
                            "m.reting as reting," +
                            "m.m_publish as status, " +
                            "l.id as id  " +
                            "from movie m " +
                            "JOIN d_movie d on d.id_l_movie = m.id_movie " +
                            "JOIN list_movie l on d.id_l_movie = l.id"+
                            "GROUP BY name";
                }
            } else 
            if(user == "user"){
                list_table = new String[]{"no","Nama movie","Renting","Aksi"};
                if(pencarian != null){
                    query = "Select  l.m_name as name , " +
                            "m.reting as reting," +
                            "m.m_publish as status," +
                            "l.id as id  " +
                            "from movie m " +
                            "JOIN d_movie d on d.id_l_movie = m.id_movie " +
                            "JOIN list_movie l on d.id_l_movie = l.id " +
                            "where m.m_publish = 'show' " +
                            "and  l.m_name like '%"+pencarian+"%'" +
                            "GRoup by name";
                }else{
                    query = "Select  l.m_name as name , " +
                            "m.reting as reting," +
                            "m.m_publish as status," +
                            "l.id as id  " +
                            "from movie m " +
                            "JOIN d_movie d on d.id_l_movie = m.id_movie " +
                            "JOIN list_movie l on d.id_l_movie = l.id " +
                            "where m.m_publish = 'show'" +
                            "group by name";
                }
            }
            DefaultTableModel table = new DefaultTableModel(null,list_table);
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next())
            {
                String name = String.valueOf(rs.getString("name"));
                String reting = String.valueOf(rs.getString("reting"));
                String id = String.valueOf(rs.getString("id"));
                if(user == "Admin"){
                    String status = String.valueOf(rs.getString("status"));
                    String data[]  = {String.valueOf(no), name, reting, status, id};
                    table.addRow(data);
                }
                if(user == "user"){
                    String data[] = {String.valueOf(no), name, reting, id};
                    table.addRow(data);
                }
                no++;
            }
            tbl_list.setModel(table);
            tbl_List.setRowHeight(30);
            if(user == "Admin"){
                   panel = new JScrollPane(tbl_List);
                   Table.add(panel);
                   buttonColumn = new ButtonColumn(tbl_list, 4,user);
            }else{
                   panel = new JScrollPane(tbl_List);
                   Table.add(panel);
                   buttonColumn = new ButtonColumn(tbl_list, 3,user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

class ButtonColumn extends AbstractCellEditor
        implements TableCellRenderer, TableCellEditor, ActionListener
    {
        JTable table;
        JButton renderButton;
        JButton editButton;
        String text;
        String user;

        public ButtonColumn(JTable table, int column,String user)
        {
            super();
            this.user = user;
            this.table = table;
            renderButton = new JButton();

            editButton = new JButton();
            editButton.setFocusPainted( false );
            editButton.addActionListener( this );

            TableColumnModel columnModel = table.getColumnModel();
            columnModel.getColumn(column).setCellRenderer( this );
            columnModel.getColumn(column).setCellEditor( this );
        }

        public Component getTableCellRendererComponent(
            JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
        {
            if (hasFocus)
            {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }
            else if (isSelected)
            {
                renderButton.setForeground(table.getSelectionForeground());
                 renderButton.setBackground(table.getSelectionBackground());
            }
            else
            {
                renderButton.setForeground(table.getForeground());
                renderButton.setBackground(UIManager.getColor("Button.background"));
            }

            renderButton.setText( (value == null) ? "" : "Detail" );
            return renderButton;
        }

        public Component getTableCellEditorComponent(
            JTable table, Object value, boolean isSelected, int row, int column)
        {
            text = (value == null) ? "" : value.toString();
            editButton.setText( text );
            return editButton;
        }

        public Object getCellEditorValue()
        {
            return text;
        }

        public void actionPerformed(ActionEvent e)
        {
            fireEditingStopped();
            DetailMovie detailMovie = new DetailMovie(user,e.getActionCommand());
        }
    }

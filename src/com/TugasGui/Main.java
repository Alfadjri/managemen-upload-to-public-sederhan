package com.TugasGui;


import java.sql.*;

public class Main {
    static Connection con = null;
    static db_connect connection = new db_connect();
    static final String tilte = "Managemen Data";
    public static void main(String[] args) {
        boolean create = createDatabase();
        int number = 0;
        try {
            con = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            String sql = "Select * from category";
            PreparedStatement ps=con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                number++;
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
       if (number == 0){
           dump_user();
           insertCateogry();
       }


            Login login = new Login(tilte);
    }


    private static void dump_user(){

        try {
            con = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            Statement stmt = con.createStatement();
            String user = "Insert into users (Name,email,password,level) VALUES ('user','user@email.com','12345678','user')";
            String admin = "Insert into users (Name,email,password,level) VALUES ('admin','admin@email.com','12345678','admin')";
            stmt.executeUpdate(user);
            stmt.executeUpdate(admin);
            stmt.close();
            con.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private static void insertCateogry() {

        final String[] category = {
                "action","adventure","comedy","Crime and mystery","Fantasy","Historical","Horror","Romance","Satire","Science fiction",
                "Speculative","Thriller","Western"
        };
        try{
            con = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            Statement stmt = con.createStatement();
            String sql = "Insert into category (Name) VALUES (?)";
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            for (String name : category ) {
                preparedStatement.setString(1,name);
                preparedStatement.executeUpdate();
            }
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean createDatabase() {
        try{
            con = DriverManager.getConnection(connection.SQL_Server,connection.user,connection.pwd);
            Statement statement = con.createStatement();
            String database = "Create database if not exists db_bioskop";
            statement.executeUpdate(database);
            statement.close();
            con.close();

            con = DriverManager.getConnection(connection.databases,connection.user,connection.pwd);
            statement = con.createStatement();
            String table_user = "Create Table if not exists users (" +
                    "id INT( 10 ) NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                    "name varchar(255) not null," +
                    "email varchar(255) not null unique," +
                    "password varchar(255) not null," +
                    "level enum('admin','user') not null " +
                    ")";
            String table_category = "create table if not EXISTS category(" +
                    "id int(3) not null PRIMARY key UNIQUE AUTO_INCREMENT," +
                    "name varchar(255) not null" +
                    ")";
            String table_director = "create table if not EXISTS drirector(" +
                    "id int(3) not null primary key UNIQUE AUTO_INCREMENT," +
                    "name varchar(255) not null" +
                    ")";
            String table_writers = "create table if not EXISTS writers(" +
                    "id int(3) not null primary key UNIQUE AUTO_INCREMENT," +
                    "name varchar(255) not null" +
                    ")";
            String table_list_movie = "create table if not EXISTS list_movie(" +
                    "id int(3) PRIMARY key NOT NULL UNIQUE AUTO_INCREMENT," +
                    "m_name varchar(255)  NOT NULL," +
                    "m_released varchar(255)," +
                    "m_duration varchar(255)," +
                    "m_deskripsi text  NOT NULL," +
                    "m_image varchar(255)  NOT NULL" +
                    ")";
            String table_d_movie = "Create table if not exists d_movie (" +
                    "id_l_movie int(3)  not null ," +
                    "m_director int(255)  NOT NULL," +
                    "m_writers int(255)  NOT NULL," +
                    "m_category int(3) NOT NULL," +
                    "CONSTRAINT fk_m_id_l_movie Foreign key (id_l_movie) references list_movie(id) on delete Cascade,"+
                    "CONSTRAINT fk_m_writers FOREIGN KEY (m_writers) REFERENCES writers (id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_m_category FOREIGN KEY (m_category) REFERENCES category (id) ON DELETE CASCADE," +
                    "CONSTRAINT fk_m_director FOREIGN KEY (m_director) REFERENCES drirector (id) ON DELETE CASCADE" +
                    ");";
            String table_movie = "create table if not EXISTS  movie (" +
                    "id_movie int(3) PRIMARY key not null," +
                    "reting int(1)," +
                    "m_publish enum('show','hide') not null," +
                    "CONSTRAINT fk_id_movie FOREIGN key (id_movie) REFERENCES d_movie (id_l_movie)" +
                    ")";
            statement.executeUpdate(table_user);
            statement.executeUpdate(table_category);
            statement.executeUpdate(table_director);
            statement.executeUpdate(table_writers);
            statement.executeQuery(table_list_movie);
            statement.executeUpdate(table_d_movie);
            statement.executeUpdate(table_movie);
            statement.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}

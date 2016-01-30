/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author cesar
 */
public class ConexaoMySQL {

    public Connection get() throws SQLException {

        String url = "jdbc:mysql://localhost:3306/mysql";
        String username = "root";
        String password = "";

        System.out.println("Connecting database...");

        return DriverManager.getConnection(url, username, password);

    }

}

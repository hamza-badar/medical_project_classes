// @formatter:off
package com.medical;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class CreateConn
{
    static CreateConn obj;
    protected Connection conn;
    private CreateConn()
    {
        try
        {
            Class.forName("org.sqlite.JDBC");
            conn=DriverManager.getConnection("jdbc:sqlite:C:\\sqlite\\medical.db");
        }
        catch(ClassNotFoundException e)
        {
            System.out.println();
        }
        catch(SQLException e)
        {
            System.out.println(e);
        }
    }
    public static CreateConn estConnection()
    {
        if(obj==null)
        {
            obj=new CreateConn();
        }
        return obj;
    }
}

package Gobang.Database;


import java.sql.*;

public class Mysql {
    // JDBC 驱动名及数据库 URL
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost:3306/gobang";

    // 数据库的用户名与密码，需要根据自己的设置
    static final String USER = "root";
    static final String PASS = "123456";
    public static Connection conn = null;
    public static Statement stmt = null;

    //get st
    public  static  Statement getStmt(){
        return stmt;
    }
    //连接数据库
    public static void Connect() {
        try{
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 打开链接
            System.out.println("连接数据库...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // 执行查询
            System.out.println(" 实例化Statement对象...");
            stmt = conn.createStatement();


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    //插入排行榜
    public static void insert(int count,String name){
        try{
            String sql;
            sql="insert into gobang (name,count) values('"+name+"','"+count+"')";
            stmt.execute(sql);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    ///关闭连接
    public void CloseConnect(){
        try{
            stmt.close();
            conn.close();
        }catch(SQLException se){
            // 处理 JDBC 错误
            se.printStackTrace();
        }catch(Exception e){
            // 处理 Class.forName 错误
            e.printStackTrace();
        }finally{
            // 关闭资源
            try{
                if(stmt!=null) stmt.close();
            }catch(SQLException se2){
            }// 什么都不做
            try{
                if(conn!=null) conn.close();
            }catch(SQLException se){
                se.printStackTrace();
            }
        }

    }
}

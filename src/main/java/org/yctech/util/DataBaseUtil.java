package org.yctech.util;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DataBaseUtil {

    static ResourceUtil resourceUtil = new ResourceUtil("database");

    /**
     * 获取数据库链接
     * @return
     */
    public static Connection getconn() {
        String driver = resourceUtil.getResource("sql.driver");
        String url = resourceUtil.getResource("sql.url");
        String user = resourceUtil.getResource("sql.user");
        String password = resourceUtil.getResource("sql.password");
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return conn;
    }


    public void callProcedure(String procedure, Connection conn) throws SQLException {
        CallableStatement call = conn.prepareCall("{call " + procedure + "}");
        call.execute();
        call.close();
    }

    /**
     * 调用存储过程执行指定作业
     *
     * @param conn
     * @param jobName
     * @return
     * @throws Exception
     */
    public int startJob(Connection conn, String jobName) throws Exception {
        CallableStatement cstmt = conn.prepareCall("{call  runJob(?,?)}");
        cstmt.setString(1, jobName);
        cstmt.registerOutParameter(2, java.sql.Types.INTEGER);
        cstmt.execute();
        int status = cstmt.getInt(2);
        cstmt.close();
        return status;
    }


    public void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}

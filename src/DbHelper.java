import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbHelper
{

    //Create a singleton
    private static DbHelper instance = null;
    private Connection conn = null;


    //Database Constants
    private final String USERNAME = "week1";
    private final String PASSWORD = "Week1@Solstic3";
    private final String CONN_STRING =
            "jdbc:mysql://localhost/week1";


    //All singletons have a private connector
    private DbHelper() {
    }


    //Return Singleton
    public static DbHelper getInstance() {
        if (instance == null) {
            instance = new DbHelper();
        }
        return instance;
    }


    //Open a db connection
    private boolean openConnection()
    {
        try {
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
            return true;

        }
        catch (SQLException e) {
            System.err.println(e);
            return false;
        }

    }


    //Return connection
    public Connection getConnection()
    {
        if (conn == null) {
            if (openConnection()) {
                //System.out.println("Connection opened");
                return conn;
            } else {
                return null;
            }
        }
        return conn;
    }


    //Close connection
    public void close() {
        //System.out.println("Closing connection");
        try {
            conn.close();
            conn = null;
        } catch (Exception e) {
            System.err.println(e);
        }
    }
}
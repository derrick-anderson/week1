import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class dbConnection
{

    //Create a singleton
    private static Connection conn = null;


    //Database Constants
    private static final String USERNAME = "week1";
    private static final String PASSWORD = "Week1@Solstic3";
    private static final String CONN_STRING = "jdbc:mysql://localhost/week1";
    private static String truncateSql = "TRUNCATE TABLE stock_quotes";
    private static String insertSql = "INSERT INTO stock_quotes (date, symbol, price, volume) VALUES (?,?,?,?)";


    // Singleton Connection Constructor
    private static Connection dbConnection()
    {
        try{
            conn = DriverManager.getConnection(CONN_STRING, USERNAME, PASSWORD);
        }
        catch(SQLException e){
            System.err.println(e.getNextException());
        }
        finally{
            return conn;
        }
    }


    // Public Access Method
    public static Connection getConnection()
    {
        if (conn == null) {
            conn = dbConnection();
            return conn;
        }
        return conn;
    }


    //Method for truncating table before additions
    public static void truncateTable(){
        conn = getConnection();
        try{
            conn.prepareStatement(truncateSql).execute();
        }
        catch(SQLException e){
            printSqlError(e);
        }

    }

    //todo: Add a method for the batch insert to the table.
    // Method to do bulk insert to table.
    public static void insertToTable(List<StockQuote> list_quotes){
        conn = getConnection();
        try{
            PreparedStatement insertStatement  = conn.prepareStatement(insertSql);
            for( StockQuote bean :list_quotes ){

                // Assign values from beans to the batch statement
                insertStatement.setTimestamp(1, bean.getDate());
                insertStatement.setString(2, bean.getSymbol());
                insertStatement.setBigDecimal(3, bean.getPrice());
                insertStatement.setInt(4, bean.getVolume());
                insertStatement.addBatch();
            }
            insertStatement.executeBatch();
            System.out.println("Success importing Stock Data to DB!");
        }
        catch(SQLException e){
            printSqlError(e);
            System.err.println("Issue Writing Quotes to DB!");
        }
    }

    //todo: Add a method for the query for the stocks

    //todo: Add a method to return a list of the Symbols in the table

    //todo: Add a method to return the earliest date in the table

    //todo: Add a method to return the latest date in the table


    // Close connection method called at the end of main
    public static void close() {

        try {
            conn.close();
            conn = null;
        }
        catch (SQLException e) {
            printSqlError(e);
        }
    }

    // Sql Error handling
    private static void printSqlError(SQLException e){
        System.err.println("Error Code : " + e.getErrorCode());
        System.err.println("SQL State : " + e.getSQLState());
        System.err.println("Message : " + e.getNextException());
    }
}
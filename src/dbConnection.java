import java.sql.*;
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
    private static String summarySql = "SELECT symbol, DATE_FORMAT(date, ?) as date,  max(price)" +
            " as max_price, min(price) as min_price, sum(volume) as trade_volume," +
            " (SELECT price FROM stock_quotes WHERE symbol = ? AND DATE_FORMAT(date, ?) = ? " +
            " order by date DESC LIMIT 1) as closing_price ," +
            " (SELECT price FROM stock_quotes WHERE symbol = ? AND DATE_FORMAT(date, ?) = ? " +
            " order by date ASC LIMIT 1) as opening_price " +
            " from stock_quotes WHERE symbol = ? AND DATE_FORMAT(date, ?) = ?" +
            " group by symbol, DATE_FORMAT(date, ?);";

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

    //todo: Add a method for the query for the summary
    // Method that queries table for summary and returns summary bean.
    public static StockSummary getSummary(String ticker, String date, String dateFormat){

        conn = getConnection();
        StockSummary bean = new StockSummary();

        try{
            PreparedStatement summaryCall = conn.prepareStatement(summarySql);

            summaryCall.setString(1, dateFormat);
            summaryCall.setString(2, ticker);
            summaryCall.setString(3, dateFormat);
            summaryCall.setString(4, date);
            summaryCall.setString(5, ticker);
            summaryCall.setString(6, dateFormat);
            summaryCall.setString(7, date);
            summaryCall.setString(8, ticker);
            summaryCall.setString(9, dateFormat);
            summaryCall.setString(10, date);
            summaryCall.setString(11, dateFormat);

            ResultSet rs = summaryCall.executeQuery();

            rs.next();

            bean.setTicker(rs.getString("symbol"));
            bean.setVolume(rs.getInt("trade_volume"));
            bean.setOpen_price(rs.getBigDecimal("opening_price"));
            bean.setLow_price(rs.getBigDecimal("min_price"));
            bean.setHigh_price(rs.getBigDecimal("max_price"));
            bean.setClosing_price(rs.getBigDecimal("closing_price"));

            rs.close();

        }
        catch(SQLException e){
            printSqlError(e);
            System.err.println("Issue Writing Quotes to DB!");
        }
        finally{
            return bean;
        }
    }

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
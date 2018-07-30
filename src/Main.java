import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;

import static javafx.application.Platform.exit;

public class Main {

    public static void main(String[] args) throws SQLException{

        //Process the Url
        URL input_url = null;
        try {
            input_url = new URL("https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json");
        }
        catch (Exception  e){
            System.err.println("Error in URL");
        }


        //Open a db Connection
        Connection conn = dbConnection.getConnection();

        List<StockQuote> stock_list = null;

        //Create an object mapper and use it to spit the Json into a list.
        try {
            ObjectMapper mapper = new ObjectMapper();
            stock_list = mapper.readValue(input_url, new TypeReference<List<StockQuote>>() {
            });
            System.out.println("Successfully retreived " + stock_list.size() + " quotes from URL");

            //Empty DB table before inserting new rows
            dbConnection.truncateTable();

        }
        catch(IOException io_e) {
            System.err.println("Issue Retrieving Data from URL!");
            dbConnection.close();
            exit();
        }
        catch(Exception e){
            System.err.println("Issue with Database Flush!");
            dbConnection.close();
            exit();
        }


        //todo: update this after the stock list comes from separate class.
        // Call the insertion function and pass the stock list
        dbConnection.insertToTable(stock_list);


        //Notify user of how to ask for data
        System.out.println("-------------------------------------------------------------------------------------");
        System.out.println("To use this tool you will provide a Stock symbol (4 letters) and a date to check.");
        System.out.println("You will be able to see either a daily or monthly summary of the data.");


        //This is the beginning of the main interaction loop.
        while(true) {

            //Ask for stock symbol
            //todo: verify that user input is within tickers from table

            String ticker_in = InputHelper.getInput("Please provide a 4 letter stock ticker symbol: (eg. 'AAPL')").toUpperCase();

            if( ticker_in.length() != 4){
                System.err.println("Not 4 letters, try again!");
                continue;
            }


            //Ask for monthly vs daily summary
            //todo: add prepared statement for monthly
            System.out.println("Would you like a daily or a monthly summary of your stock?");
            System.out.println("1) Daily Summary");
            System.out.println("2) Monthly Summary");
            int summary_option = InputHelper.getIntegerInput("");

            if( (summary_option < 1) || (summary_option > 2)){
                System.err.println("You didn't enter 1 or 2, try again");
                continue;
            }


            //Ask for date
            String date_in_m = null;
            String date_in_d = null;
            switch( summary_option ){
                case 1:
                    date_in_d = InputHelper.getInput("Please provide a date in the format 'YYYY-mm-dd'");
                    break;
                case 2:
                    date_in_m = InputHelper.getInput("Please provide a date in the format 'YYYY-mm'");
                    break;
            }

            CallableStatement summary = null;
            switch( summary_option ){
                case 1:
                    summary = conn.prepareCall("{call get_daily_summary(?,?)}");
                    summary.setString(1, ticker_in);
                    summary.setString(2, date_in_d);
                    break;

                case 2:
                    summary = conn.prepareCall("{call get_monthly_summary(?,?)}");
                    summary.setString(1, ticker_in);
                    summary.setString(2, date_in_m);
                    break;
            }


            //Prepare call and execute
            ResultSet rs = summary.executeQuery();

            //Assign values to summary object
            StockSummary summaryBean = new StockSummary();


            while(rs.next()) { // Advance to the 1 record
                summaryBean.setTicker(rs.getString("symbol"));
                summaryBean.setVolume(rs.getInt("trade_volume"));
                summaryBean.setOpen_price(rs.getBigDecimal("opening_price"));
                summaryBean.setLow_price(rs.getBigDecimal("min_price"));
                summaryBean.setHigh_price(rs.getBigDecimal("max_price"));
                summaryBean.setClosing_price(rs.getBigDecimal("closing_price"));
            }

            rs.close();

            //Call the summary object summarize
            summaryBean.summary(summary_option);

            //Ask the user if they would like to check on another stock!
            String answer = InputHelper.getInput("Would you like to check on another stock (Y/N)?");
            if(! answer.toUpperCase().equals("Y")){
                break;
            }
            else{
                continue;
            }
        }
        dbConnection.close();

    }
}

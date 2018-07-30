import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.List;

public class InputHelper {

    //Constructor
    public InputHelper() {
    }


    //Get input helper method
    public static String getInput(String prompt) {
        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        System.out.println(prompt);
        System.out.flush();

        try {
            return stdin.readLine();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    // Get number (double)
    public static double getDoubleInput(String prompt) throws NumberFormatException {
        String input = getInput(prompt);
        return Double.parseDouble(input);
    }


    // Get number (integer)
    public static int getIntegerInput(String prompt) throws NumberFormatException {
        String input = getInput(prompt);
        return Integer.parseInt(input);
    }

    //todo: Adding a method to pull data from url and return a list of StockQuotes
    public static List<StockQuote> getStockQuotes(){
        List<StockQuote> stock_list = null;
        try{
            URL input_url = new URL("https://bootcamp-training-files.cfapps.io/week1/week1-stocks.json");
            ObjectMapper mapper = new ObjectMapper();
            stock_list = mapper.readValue(input_url, new TypeReference<List<StockQuote>>() {});
        }
        catch(Exception e){
            System.err.println(e.getMessage());
        }
        finally {
            return stock_list;
        }
    }

}
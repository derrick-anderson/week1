import java.io.BufferedReader;
import java.io.InputStreamReader;

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
}
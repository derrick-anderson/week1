import java.math.BigDecimal;

public class StockSummary {

    private String ticker;
    private BigDecimal open_price;
    private BigDecimal low_price;
    private BigDecimal high_price;
    private BigDecimal closing_price;
    private Integer volume;


    public StockSummary() {
    }

    public String getTicker() {
        return ticker;
    }

    public void setTicker(String ticker) {
        this.ticker = ticker;
    }

    public BigDecimal getOpen_price() {
        return open_price;
    }

    public void setOpen_price(BigDecimal open_price) {
        this.open_price = open_price;
    }

    public BigDecimal getLow_price() {
        return low_price;
    }

    public void setLow_price(BigDecimal low_price) {
        this.low_price = low_price;
    }

    public BigDecimal getHigh_price() {
        return high_price;
    }

    public void setHigh_price(BigDecimal high_price) {
        this.high_price = high_price;
    }

    public BigDecimal getClosing_price() {
        return closing_price;
    }

    public void setClosing_price(BigDecimal closing_price) {
        this.closing_price = closing_price;
    }

    public Integer getVolume() {
        return volume;
    }

    public void setVolume(Integer volume) {
        this.volume = volume;
    }

    //todo: Add a new Summary format to this
    public void summary(int option){
        /*System.out.println("Stock : " + this.ticker);
        System.out.println("Volume : " + this.volume);
        System.out.println("Opening Price : " + this.open_price);
        System.out.println("Low Price : " + this.low_price);
        System.out.println("High Price : " + this.high_price);
        System.out.println("Closing Price : " + this.closing_price);
        System.out.println("TESTING");*/
        if(option == 1){
            System.out.println("Daily Summary for Stock : " + this.ticker);
        }else if(option ==2) {
            System.out.println("Monthly Summary for Stock : " + this.ticker);
        }
        System.out.println(String.format("%60s", "").replace(" ", "-"));
        System.out.println(String.format("|%-10s|%-15s|%-10s|%-10s|%-15s|", "Volume", "Opening Price", "Low Price", "High Price", "Closing Price"));
        System.out.println(String.format("|%,-10d|%,-15.2f|%,-10.2f|%,-10.2f|%,-15.2f|", this.volume, this.open_price, this.low_price, this.high_price, this.closing_price));
    }
}

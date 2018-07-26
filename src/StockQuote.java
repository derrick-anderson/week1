import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;


public class StockQuote {

    private String symbol;
    private BigDecimal price;
    private int volume;
    private String date;
    private Time time;

    public StockQuote() {

    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public Timestamp getDate() {
        return Timestamp.valueOf(date.replace("T", " ").replace("+0000", ""));
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "StockQuote{" +
                "symbol='" + symbol + '\'' +
                ", price=" + price +
                ", volume=" + volume +
                //", dateString='" + dateString + '\'' +
                ", date=" + date +
                '}';
    }
}

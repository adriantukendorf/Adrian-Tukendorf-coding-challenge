package bitcoin;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

public class PortfolioPerformance {

    private static final List<Price> PRICES = List.of(
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 1, 5, 0, 0), new BigDecimal("35464.53")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 2, 5, 0, 0), new BigDecimal("35658.76")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 3, 5, 0, 0), new BigDecimal("36080.06")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 3, 13, 0, 0), new BigDecimal("37111.11")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 6, 5, 0, 0), new BigDecimal("38041.47")),
            new Price(LocalDateTime.of(2021, Month.SEPTEMBER, 7, 5, 0, 0), new BigDecimal("34029.61")));

    private static final List<Transaction> TRANSACTIONS = List.of(
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 1, 9, 0, 0), new BigDecimal("0.012")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 1, 15, 0, 0), new BigDecimal("-0.007")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 4, 9, 0, 0), new BigDecimal("0.017")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 5, 9, 0, 0), new BigDecimal("-0.01")),
            new Transaction(LocalDateTime.of(2021, Month.SEPTEMBER, 7, 9, 0, 0), new BigDecimal("0.1")));

    // Complete this method to return a list of daily portfolio values with one record for each day from the 01-09-2021-07-09-2021 in ascending date order
    public static List<DailyPortfolioValue> getDailyPortfolioValues() {
        //Start and end date of the operation
        var startDate = LocalDate.of(2021, 9, 1);
        var endDate = LocalDate.of(2021, 9, 7);

        List<DailyPortfolioValue> PERFORMANCE = new ArrayList<>();

        Price latestBitcoinPriceTime = null;
        BigDecimal accountBalance = BigDecimal.valueOf(0.00000);
        MathContext mc = new MathContext(9);

        for (LocalDate date = startDate; date.isBefore(endDate) || date.isEqual(endDate); date = date.plusDays(1)) {
            int priceChangesSameDay = 0;

            //Calculating the latest price of Bitcoin for the specific date
            try {
                for (Price price : PRICES) {
                    if (price.effectiveDate().toLocalDate().isEqual(date)) {
                        latestBitcoinPriceTime = price;
                        priceChangesSameDay++;
                        if (priceChangesSameDay > 1) {
                            if (price.effectiveDate().compareTo(latestBitcoinPriceTime.effectiveDate()) < 0) {
                                latestBitcoinPriceTime = price;
                            }
                        }
                    }
                }

            } catch (Exception error1) {
                System.out.println("Error: price calculation");
            }

            //Calculating transactions value per day
            try {
                for (Transaction transaction : TRANSACTIONS) {
                    if (transaction.effectiveDate().toLocalDate().isEqual(date)) {
                        accountBalance = accountBalance.add(transaction.numberOfBitcoins(), mc);
                    }
                }
            } catch (Exception error2) {
                System.out.println("Error: transactions calculations");
            }

            //Visualisation of the Bitcoin price for a specific day
            System.out.println("Date:" + date + " BTC:" + accountBalance);

                //Daily portfolio value calculation accountBalance*latestPrice
                BigDecimal performance = accountBalance.multiply(latestBitcoinPriceTime.price(), mc);

            //Adding calculated values to the list that is to be returned
            PERFORMANCE.add(new DailyPortfolioValue(date, performance));
        }


        return PERFORMANCE;
    }
}

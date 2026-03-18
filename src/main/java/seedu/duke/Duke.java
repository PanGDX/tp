package seedu.duke;

import java.util.Arrays;
import java.util.List;

public class Duke {
    /**
     * Main entry-point for the Duke application.
     */
    public static void main(String[] args) {
        String logo = " ____        _        \n"
                + "|  _ \\ _   _| | _____ \n"
                + "| | | | | | | |/ / _ \\\n"
                + "| |_| | |_| |   <  __/\n"
                + "|____/ \\__,_|_|\\_\\___|\n";

        System.out.println("Hello from\n" + logo);

        Storage storage = new Storage("data/ledger.txt");
        TransactionsList transactionList = new TransactionsList(storage);

        ExchangeRateStorage exchangeRateStorage = new ExchangeRateStorage("data/exchange-rates.json");
        LiveExchangeRateService liveExchangeRateService = new LiveExchangeRateService();

        ExchangeRateData rateData;
        try {
            rateData = exchangeRateStorage.load();
        } catch (RuntimeException e) {
            System.out.println("Warning: Could not load cached exchange rates.");
            return;
        }

        CurrencyConverter converter = new CurrencyConverter(rateData);
        transactionList.setCurrencyConverter(converter);

        List<String> supportedCurrencies = Arrays.asList("EUR", "SGD", "USD");

        try {
            ExchangeRateData liveData = liveExchangeRateService.fetchLatest("EUR", supportedCurrencies);
            exchangeRateStorage.save(liveData);
            converter.updateRates(liveData);
            System.out.println("Live exchange rates refreshed for " + liveData.getDate() + ".");
        } catch (RuntimeException e) {
            System.out.println("Using cached exchange rates from " + converter.getRateDate() + ".");
        }

        Parser parser = new Parser(transactionList, converter, exchangeRateStorage, liveExchangeRateService);
        parser.start();

        System.out.println("--- Transaction Manager Exited ---");
    }
}

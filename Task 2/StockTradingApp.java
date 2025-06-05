import java.io.*;
import java.util.*;

class Stock {
    String symbol;
    double price;

    public Stock(String symbol, double price) {
        this.symbol = symbol;
        this.price = price;
    }
}

class Transaction {
    String type; 
    String stockSymbol;
    int quantity;
    double price;

    public Transaction(String type, String stockSymbol, int quantity, double price) {
        this.type = type;
        this.stockSymbol = stockSymbol;
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public String toString() {
        return type + " " + quantity + " of " + stockSymbol + " at $" + price;
    }
}

class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    List<Transaction> transactions = new ArrayList<>();
    double cash = 10000.0;

    public void buyStock(String symbol, int qty, double price) {
        double totalCost = qty * price;
        if (cash >= totalCost) {
            cash -= totalCost;
            holdings.put(symbol, holdings.getOrDefault(symbol, 0) + qty);
            transactions.add(new Transaction("BUY", symbol, qty, price));
            System.out.println("Bought " + qty + " of " + symbol);
        } else {
            System.out.println("Not enough cash.");
        }
    }

    public void sellStock(String symbol, int qty, double price) {
        int owned = holdings.getOrDefault(symbol, 0);
        if (owned >= qty) {
            cash += qty * price;
            holdings.put(symbol, owned - qty);
            transactions.add(new Transaction("SELL", symbol, qty, price));
            System.out.println("Sold " + qty + " of " + symbol);
        } else {
            System.out.println("Not enough stock to sell.");
        }
    }

    public void showPortfolio(Map<String, Stock> market) {
        System.out.println("\n--- Portfolio ---");
        System.out.println("Cash: $" + String.format("%.2f", cash));
        double totalValue = cash;
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            Stock stock = market.get(entry.getKey());
            if (stock != null) {
                double value = entry.getValue() * stock.price;
                totalValue += value;
                System.out.println(entry.getKey() + ": " + entry.getValue() + " shares worth $" + value);
            }
        }
        System.out.println("Total Portfolio Value: $" + String.format("%.2f", totalValue));
    }

    public void showTransactions() {
        System.out.println("\n--- Transaction History ---");
        for (Transaction t : transactions) {
            System.out.println(t);
        }
    }

    public void saveToFile(String filename) {
        try (PrintWriter out = new PrintWriter(new FileWriter(filename))) {
            out.println(cash);
            for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
                out.println(entry.getKey() + " " + entry.getValue());
            }
            for (Transaction t : transactions) {
                out.println("T " + t.type + " " + t.stockSymbol + " " + t.quantity + " " + t.price);
            }
            System.out.println("Portfolio saved to " + filename);
        } catch (IOException e) {
            System.out.println("Error saving portfolio.");
        }
    }

    public void loadFromFile(String filename) {
        try (Scanner sc = new Scanner(new File(filename))) {
            holdings.clear();
            transactions.clear();
            cash = Double.parseDouble(sc.nextLine());
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                if (line.startsWith("T ")) {
                    String[] t = line.split(" ");
                    transactions.add(new Transaction(t[1], t[2], Integer.parseInt(t[3]), Double.parseDouble(t[4])));
                } else {
                    String[] parts = line.split(" ");
                    holdings.put(parts[0], Integer.parseInt(parts[1]));
                }
            }
            System.out.println("Portfolio loaded from " + filename);
        } catch (Exception e) {
            System.out.println("Error loading portfolio.");
        }
    }
}

public class StockTradingApp {
    static Map<String, Stock> market = new HashMap<>();

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Portfolio portfolio = new Portfolio();

        // Sample market data
        market.put("AAPL", new Stock("AAPL", 175.30));
        market.put("GOOG", new Stock("GOOG", 2805.67));
        market.put("TSLA", new Stock("TSLA", 725.15));
        market.put("MSFT", new Stock("MSFT", 310.23));

        while (true) {
            System.out.println("\n--- Stock Trading Simulator ---");
            System.out.println("1. View Market");
            System.out.println("2. Buy Stock");
            System.out.println("3. Sell Stock");
            System.out.println("4. View Portfolio");
            System.out.println("5. View Transactions");
            System.out.println("6. Save Portfolio");
            System.out.println("7. Load Portfolio");
            System.out.println("8. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();

            switch (choice) {
                case 1:
                    System.out.println("\n--- Market Data ---");
                    for (Stock s : market.values()) {
                        System.out.println(s.symbol + ": $" + s.price);
                    }
                    break;
                case 2:
                    System.out.print("Enter stock symbol to buy: ");
                    String buySym = sc.next().toUpperCase();
                    if (market.containsKey(buySym)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.buyStock(buySym, qty, market.get(buySym).price);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;
                case 3:
                    System.out.print("Enter stock symbol to sell: ");
                    String sellSym = sc.next().toUpperCase();
                    if (market.containsKey(sellSym)) {
                        System.out.print("Enter quantity: ");
                        int qty = sc.nextInt();
                        portfolio.sellStock(sellSym, qty, market.get(sellSym).price);
                    } else {
                        System.out.println("Stock not found.");
                    }
                    break;
                case 4:
                    portfolio.showPortfolio(market);
                    break;
                case 5:
                    portfolio.showTransactions();
                    break;
                case 6:
                    System.out.print("Enter filename to save: ");
                    String saveFile = sc.next();
                    portfolio.saveToFile(saveFile);
                    break;
                case 7:
                    System.out.print("Enter filename to load: ");
                    String loadFile = sc.next();
                    portfolio.loadFromFile(loadFile);
                    break;
                case 8:
                    System.out.println("Exiting simulator...");
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }
}










/*
OUTPUT:-

W:\GUI\Task 2>javac StockTradingApp.java

W:\GUI\Task 2>java StockTradingApp

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 1

--- Market Data ---
MSFT: $310.23
GOOG: $2805.67
AAPL: $175.3
TSLA: $725.15

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 2
Enter stock symbol to buy: AAPL
Enter quantity: 10
Bought 10 of AAPL

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 4

--- Portfolio ---
Cash: $8247.00
AAPL: 10 shares worth $1753.0
Total Portfolio Value: $10000.00

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 3
Enter stock symbol to sell: AAPL
Enter quantity: 5
Sold 5 of AAPL

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 5

--- Transaction History ---
BUY 10 of AAPL at $175.3
SELL 5 of AAPL at $175.3

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 6
Enter filename to save: portfile.txt
Portfolio saved to portfile.txt

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 7
Enter filename to load: portfile.txt
Portfolio loaded from portfile.txt

--- Stock Trading Simulator ---
1. View Market
2. Buy Stock
3. Sell Stock
4. View Portfolio
5. View Transactions
6. Save Portfolio
7. Load Portfolio
8. Exit
Enter choice: 8
Exiting simulator...
*/
package ePortfolio;
/**
 * The Greater class for all investments in the portfolio
 * @author Yousif Jamal
 */
public class Investment{
    protected String investmentType;
    protected String symbol;
    protected String name;
    protected int quantity;
    protected double price;
    protected double bookValue;

    /**
     * Generic Investment constructor
     */
    public Investment(String type, String ticker, String fundName, int initialAmount, double initialPrice){
        
        investmentType = type;
        symbol = ticker;
        name = fundName;
        quantity = initialAmount;
        price = initialPrice;
        bookValue = initialAmount * initialPrice;
    }

    /**
     * Preforms equality operation based on symbol argument
     *  */
    public boolean equals(String tickerArgument){
        
        if (symbol.compareToIgnoreCase(tickerArgument) == 0){
            return true;
        }
        return false;
    }

    /**
     * String version of investment printed out on multi-lines
     */
    public String toString(){
        
        return investmentType.toUpperCase()+"\n"+ symbol.toUpperCase() + ": "+ name.toUpperCase() + "\nQuantity: "+quantity+"\nPrice: "+price+"\nBook Value: "+bookValue;
    }

    /**Writes out particulars of an investment - intended for file storage */
    public String writeInvestment(){
        return "type = "+investmentType+"\nsymbol = "+symbol+"\nname = "+name+"\nquantity = "+quantity+"\nprice = "+price+"\nbookvalue = "+bookValue;
    }

    /**sets bookvalue - useful for reading file values, etc. */
    public void setBookvalue(double inputBook){
        bookValue = inputBook;
    }
    /**
     * quantity of investment 
     */
    public int getQuantity(){
        return quantity;
    }

    /** Returns price of investment */
    public double getPrice(){
        return price;
    }

    /**Changes price to whatever the argument is */
    public void setPrice(double newPrice){
        price = newPrice;
    }
    
    /**Returns ticker for stock or mutual fund */
    public String getTicker(){
        return symbol;
    }

    /**returns tokenized string to compare keywords against for search */
    public String[] tokenizeName(){   
        return name.split("[, . ; ! ? :]", 0);
    }

    /**Placeholder function to be overwritten by subclasses */
    public void buy(int boughtQuantity, double newPrice){
    }

    /**Placeholder function to be overwritten by subclasses */
    public double sell(int soldQuantity, double sellingPrice){
        return -1;
    }

    /**Placeholder function to be overwritten by subclasses */
    public double getGains(){
        return -1;
    }

    public String getName(){
        return name;
    }
}
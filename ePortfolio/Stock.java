package ePortfolio;
/**Stock specific class
 * extends the investment class and overrides functions pertaining to the way buying/selling and calculating gain is done.
 */
public class Stock extends Investment {
    private static double commission;

    /**Regular Constructor - just defines a commission value and super's all if the Investment's constructor args
     * Updates bookvalue since that is affected by commission value
     */
    public Stock(String type, String ticker, String companyName, int initialAmount, double initialPrice){
        super(type, ticker, companyName, initialAmount, initialPrice);
        commission = 9.99;
        bookValue = initialAmount * initialPrice + commission;
    }

    /**Buys a quantity of a stock
     * adds buy-value to book cost
     * treats the buy price of the stock as the new price of the stock and updates it accordingly
     */
    @Override//Due to the fact Stocks & Mutual funds dont have the same buying algo
    public void buy(int boughtQuantity, double newPrice){
        price = newPrice;
        quantity += boughtQuantity;

        bookValue += newPrice*boughtQuantity + commission;
    }

    /**Sells a quantity of the stock
     * updates book cost to a proportion of stock sold or to 0 if all the investment is liquidated
     * returns a double that is used to account for realized gains in the portfolio class
     */
    @Override//Due to the fact Stocks & Mutual funds dont have the same selling algo
    public double sell(int soldQuantity, double sellingPrice){
        double quantityRatio;

        if (soldQuantity < quantity){
            quantityRatio = ((double)quantity -(double)soldQuantity)/ (double)quantity;
            price = sellingPrice;
            bookValue = bookValue*(quantityRatio);
            quantity -= soldQuantity;
            return (sellingPrice * soldQuantity) - commission - bookValue*(quantityRatio);
        }else{
            quantity = 0;
            return ((sellingPrice*soldQuantity) - bookValue - commission);
        }
    }

    /**Gets paper value of how much your stock would sell for now vs your book cost
     * Accounts for commission value
     */
    @Override
    public double getGains(){
        return price*quantity - bookValue - commission; 
    }

}

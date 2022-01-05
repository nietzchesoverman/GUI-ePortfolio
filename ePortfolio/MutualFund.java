package ePortfolio;
/**Mutual fund specific class
 * extends the investment class and overrides functions pertaining to the way buying/selling and calculating gain is done.
 */
public class MutualFund extends Investment{
    private static int redemptionFee;

    /**Regular Constructor - just defines a redemptionFee value and super's all if the Investment's constructor args */
    public MutualFund(String type, String ticker, String fundName, int initialAmount, double initialPrice){
        super(type, ticker, fundName, initialAmount, initialPrice);
        redemptionFee = 45;
    }

    /**Buys a quantity of a mutual fund
     * adds buy-value to book cost
     * treats the buy price of the mutual fund as the new price of the fund and updates it accordingly
     */
    @Override //Due to the fact Stocks & Mutual funds dont have the same buying algo
    public void buy(int boughtQuantity, double newPrice){
        price = newPrice;
        quantity += boughtQuantity;

        bookValue += newPrice*boughtQuantity;
    }

    /**Sells a quantity of the mutual fund
     * updates book cost to a proportion of mutual fund sold or to 0 if all the investment is liquidated
     * returns a double that is used to account for realized gains in the portfolio class
     */
    @Override //Due to the fact Stocks & Mutual funds dont have the same selling algo
    public double sell(int soldQuantity, double sellingPrice){
        
        double quantityRatio;

        if (soldQuantity < quantity){
            quantityRatio = ((double)quantity - (double)soldQuantity)/ (double)quantity;
            price = sellingPrice;
            bookValue = bookValue*(quantityRatio);
            quantity -= soldQuantity;
            return soldQuantity*sellingPrice - bookValue*quantityRatio - redemptionFee;
        }else{
            quantity = 0;
            return sellingPrice*soldQuantity - bookValue - redemptionFee;
        }
    }

    /**Gets paper value of how much your mutual fund would sell for now vs your book cost
     * Accounts for redemption fee value
     */
    @Override
    public double getGains(){
        return price*quantity - bookValue - redemptionFee;
    }
}

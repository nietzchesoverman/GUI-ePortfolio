package ePortfolio;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashMap;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import javax.swing.*;
/**Preforms all portfolio operations the user may initiate. Tracks the greater investment list and 
 * handles I/O of both userInput and command line files.
 */
public class Portfolio{
    Scanner scannerObj = new Scanner(System.in);
    public static ArrayList<Investment> investmentList = new ArrayList<Investment>();
    public static HashMap<String, ArrayList<Integer>> nameTokenHash = new HashMap<String, ArrayList<Integer>>();
    protected double realizedGains;

    /**Initializes realized gains to 0 for the creation of a new portfolio */
    public Portfolio(){
        realizedGains = 0;
    }

    /**Buys an investment in the following order:
     * 1) Prompts for market ticker and name
     * 2) Checks ticker against current investments and executes buy operaton if it exits after pulling that investment out locally
     * 3) If investment doesnt exist, gathers particulars and creates a new subclass
     * 4) sets or adds investment into investmentList ArrayList*/
    public void buy(String inputInvest, String inputTicker, String investName, double inputPrice, int inputQuantity) throws Exception{
        String investmentVehicle;
        String ticker;
        String name;
        double price;
        int quantity;
        int i;
        
        
        investmentVehicle = inputInvest;
        ticker = inputTicker;
        
        for (i = 0; i < investmentList.size(); i++){
            if (investmentList.get(i).equals(ticker)){
                price = inputPrice;
                quantity = inputQuantity;
                scannerObj.nextLine();
                if (price <= 0 || quantity <= 0){
                    throw new Exception("You cannot enter zero amounts for quantity or price.\n");
                }
                Investment updateBuy = investmentList.get(i);                                       //Unknown whether stock or Mutual Fund so pull investment
                updateBuy.buy(quantity, price);                                                     //Subclasses should override buy function accordingly
                investmentList.set(i, updateBuy);   
                hashNames();
                return;
            }
        }
        //If investment doesnt exist, perform collection of particulars
        name = investName;
        price = inputPrice;
        quantity = inputQuantity; 
        
        if (price <= 0 || quantity <= 0){
            throw new Exception("You cannot enter zero amounts for quantity or price.\n");
        }

        if (investmentVehicle.compareToIgnoreCase("stock") == 0){               //check investment type and create object accordingly
            Stock newBuy = new Stock(investmentVehicle, ticker, name, quantity, price);        
            investmentList.add(newBuy);

        }else if (investmentVehicle.compareToIgnoreCase("mutual fund") == 0 || investmentVehicle.compareToIgnoreCase("mutualfund") == 0){
            MutualFund newBuy = new MutualFund(investmentVehicle, ticker, name, quantity, price);
            investmentList.add(newBuy);
        }else{                                                                 //Error check invalid entries
            throw new Exception("Error: Invalid Investment Vehicle - Please enter stock or mutual fund");
        }
        hashNames();
    }

    /**Sells an Investment in the following process:
     * 1) Ask for ticker and compare with Investment ArrayList
     * 2) Ask for quantity and price to sell at - error check the quantity
     * 3) Grab investment out and rely on subclass overriding of sell method to properly sell quantity of investment at dictated price
     * 4) Set the investment back into the ArrayList or delete it if it was completely liquidated*/
    public void sell(String inputTicker, int inputQuant, double inputPrice) throws Exception{ 
        String ticker;
        int quantity;
        double price;
        int i;

        ticker = inputTicker;

        for (i = 0; i < investmentList.size(); i++){
            if (investmentList.get(i).equals(ticker)){              //If you find the ticker in investments
                Investment soldInvestment = investmentList.get(i);
                quantity = inputQuant;
                if (quantity > soldInvestment.getQuantity() ){                      //Error check selling more than having
                    throw new Exception("Error - Cant sell more than you have\n");
                }
                price = inputPrice;
                if (price <= 0 || quantity <= 0){
                    throw new Exception("\nYou cannot enter zero amounts for quantity or price.");
                }
                realizedGains+= soldInvestment.sell(quantity, price);
                if (soldInvestment.getQuantity() == 0){                                  //Account for full liquidation
                    investmentList.remove(i);
                }else{
                    investmentList.set(i, soldInvestment);                               //Set new investment specs at that index
                }
                hashNames();
                return;
            }
        }
        throw new Exception("Error - Stock/Mutual Fund not found in holdings.\n");
    }

    /**Updates all Investment prices - propmts user for them all
     * 1) Loop through each investment asking the user to enter in new price for the market ticker
     * 2) Execute a setprice function in each investment */
    public void update(){
        int i;
        double newPricing;

        for (i = 0; i < investmentList.size(); i++){
            System.out.print("Enter updated price for "+investmentList.get(i).getTicker()+": ");
            newPricing = scannerObj.nextDouble();
            scannerObj.nextLine();
            investmentList.get(i).setPrice(newPricing);
        }
    }
    /**Calculates total gains by accounting for unrealized gains only per each stock */
    public double getGain(){
        int i;
        double totalGains;

        totalGains = 0;
        for (i = 0; i < investmentList.size(); i++){
            totalGains += investmentList.get(i).getGains();
        }

        return totalGains;
    }
    /**Search Function with up to 3 fields - Ticker, Keyword set, price range
     * Compare tokenized keywords with hashed keywords.
     * add that to a list and then only retain indicies in the list that match with the ticker and price range parameters
     */
    public void search(String inputSymbol, String inputKeywords, String inputRange){
        ArrayList<Integer> matchedIndex = new ArrayList<Integer>();
        String[] tokenizedKeywords;
        String[] rangeParameters = new String[3];
        String ticker;
        String userInput;
        int i;

        //First ask for all the fields.
        ticker = inputSymbol;
        userInput = inputKeywords;
        tokenizedKeywords = tokenizeUserInput(userInput);
        userInput = inputRange;
        rangeParameters = parseRange(userInput);
    
        hashNames();
        //now that our names are hashed, grab the tokenized keywords, lowercase them, then compare against lowercased hashednames
        for (i = 0; i < tokenizedKeywords.length; i++){
            if (nameTokenHash.containsKey(tokenizedKeywords[i].toLowerCase())){       //If it has the lowercase key
                if (matchedIndex.isEmpty()){                                          //If matched indicies are empty
                    matchedIndex = nameTokenHash.get(tokenizedKeywords[i].toLowerCase()); // make matched indicies the first tokenized keyword
                }else{                                                                    
                    matchedIndex.retainAll(nameTokenHash.get(tokenizedKeywords[i].toLowerCase())); //only keep matched indexes common between all keywords
                }
            }
        }
        //Now that we have index of hash names, lets get index of symbols
        if (ticker.compareToIgnoreCase("") != 0){                                           //If ticker aint empty
            if (matchedIndex.size() == 0){                                    //Then if index is empty
                matchedIndex = findSymbolIndicies(ticker);                  //make matched indicies the matchedTickers
            }else{
                matchedIndex.retainAll(findSymbolIndicies(ticker));         //else, retain only found symbol indicies
            }
        }

        //Ticker & Name conditions done - range condition is the final one
        if (!(rangeParameters == null)){                                    //if range params exist
            if (matchedIndex.size() == 0){
                matchedIndex = findRangeIndicies(rangeParameters);
            }else{
                matchedIndex.retainAll(findRangeIndicies(rangeParameters));
            }
        }
        
        //Print out all matched investments - account for matched Index still being empty because of no input or none found
        if (tokenizedKeywords[0].compareToIgnoreCase("") == 0 && rangeParameters == null && ticker.compareToIgnoreCase("") == 0){
            for (i = 0; i < investmentList.size(); i++){
                GUI.searchText.append("\n"+investmentList.get(i).toString()+"\n");               //print all investments if no search params
            }
        }else if (matchedIndex.size() == 0){
            GUI.searchText.append("NO RESULTS FOUND\n");             //No search results found if matched index empty
        }else{
            for (i = 0; i < matchedIndex.size(); i++){
                GUI.searchText.append("\n"+investmentList.get(matchedIndex.get(i)).toString()+"\n"); //Else only print out matched indicies
            }
        }
    }
    /**Computes matched indicies for input price-range parameters obtained from parseRange()
     *  returning them as an ArrayList */
    private ArrayList<Integer> findRangeIndicies(String[] rangeParam){
        ArrayList<Integer> matchedIndicies = new ArrayList<Integer>();
        int i;
        double lowerEnd;
        double upperEnd;

        if (rangeParam[2].compareToIgnoreCase("") == 0){                          //If third value empty can deduce its either greater than or empty
            lowerEnd = Double.parseDouble(rangeParam[0]);
            if (rangeParam[1].compareToIgnoreCase("equal") == 0){                 //if Equal add all equal values to matched index
                for (i = 0; i < investmentList.size(); i++){
                    if (investmentList.get(i).getPrice() == lowerEnd){          //Parse a double and if its equal then add it as matched index
                        matchedIndicies.add(i);
                    }
                }
            }else{                                                                              
                for (i = 0; i < investmentList.size(); i++){
                    if (investmentList.get(i).getPrice() >= lowerEnd){
                        matchedIndicies.add(i);
                    }
                }
            }
        }else if (rangeParam[1].compareToIgnoreCase("less") == 0){                     
            upperEnd = Double.parseDouble(rangeParam[2]);
            for (i = 0; i < investmentList.size(); i++){
                if (investmentList.get(i).getPrice() <= upperEnd){
                    matchedIndicies.add(i);
                }
            }
        }else{
            lowerEnd = Double.parseDouble(rangeParam[0]);
            upperEnd = Double.parseDouble(rangeParam[2]);
            for (i = 0; i < investmentList.size(); i++){
                if (investmentList.get(i).getPrice() >= lowerEnd && investmentList.get(i).getPrice() <= upperEnd){
                    matchedIndicies.add(i);
                }
            }
        }

        return matchedIndicies;
    }

    /**Returns Integer ArrayList that contains all indicies of the input ticker  */
    private ArrayList<Integer> findSymbolIndicies(String ticker){
        ArrayList<Integer> matchedTickers = new ArrayList<Integer>();
        int i;

        for (i = 0; i < investmentList.size(); i++){
            if (ticker.compareToIgnoreCase(investmentList.get(i).getTicker()) == 0){    //If we find our ticker in this index
                matchedTickers.add(i);                                                  //Add the index
            }
        }
        return matchedTickers;
    }

    /**Quick function to tokenize userInput for the inputed keywords */
    private String[] tokenizeUserInput(String userInput){
        return userInput.split("[, . ; ! ? :]", 0);
    }

    /**splits the price range input into its component parts - that is:
     * - the number inputed
     * - the greater than or less than or between or equal logic 
     * - the upper range, which gets calculated only if its between and lower than logic
     * Returns this all in a string array of length 3 [lower, logic, upper]
     */
    private String[] parseRange(String inputedRange){
        String lowerLimitOrSingleNumber;
        String[] tokenizedRange;
        String[] parsedInput = new String[3];

        //check the presence of -, and location of -
        if (inputedRange.compareToIgnoreCase("") == 0){       //make the string array null if "" entered
            return null;
        }else if (inputedRange.charAt(0) == '-'){              //if first char is the -, then its a less than
            tokenizedRange = inputedRange.split("-", 0);
            parsedInput[0] = "";
            parsedInput[1] = "less";
            parsedInput[2] = tokenizedRange[1];
        }else if (inputedRange.charAt(inputedRange.length() - 1) == '-'){    //if last char is the -, then its a greater than
            tokenizedRange = inputedRange.split("-", 0);
            parsedInput[0] = tokenizedRange[0];
            parsedInput[1] = "greater";
            parsedInput[2] = "";
        }else if (inputedRange.indexOf('-') == -1){                     //Equality if no - found
            tokenizedRange = inputedRange.split("-", 0);
            parsedInput[0] = tokenizedRange[0];
            parsedInput[1] = "equal";
            parsedInput[2] = "";
        }else{                                                      //Only other option is now a range 
            tokenizedRange = inputedRange.split("-", 0);
            parsedInput[0] = tokenizedRange[0];
            parsedInput[1] = "range";
            parsedInput[2] = tokenizedRange[1];
        }
        return parsedInput;
    }

    /**When called this function loops through the names of each investment and splits them,
     * then hashes them into the hashmap by key, if key already exists, it adds its index to the ArrayList present at 
     * that key */
    private void hashNames(){
        String[] tokenizedName;
        ArrayList<Integer> temp = new ArrayList<Integer>();
        int i;
        int j;

        for (i = 0; i < investmentList.size(); i++){                            //for each investment tokenize its name and store this 
            tokenizedName = investmentList.get(i).tokenizeName();
            for (j = 0; j < tokenizedName.length; j++){                         //for every word in the name
                if (nameTokenHash.containsKey(tokenizedName[j])){               //If already mapped
                    if (!(nameTokenHash.get(tokenizedName[j]).contains(i))){    //If the element isnt already in the mapped array
                        temp = nameTokenHash.get(tokenizedName[j]);             //Pull the integer array out
                        temp.add(i);                                            //add this investment's index into it
                        nameTokenHash.put(tokenizedName[j], temp);              //put the integer array back at hash key
                    }
                }else{                                                          //NEW MAPPING
                    temp.clear();                                               //Clear arraylist for it
                    temp.add(i);                                                //Add new index
                    nameTokenHash.put(tokenizedName[j].toLowerCase(), temp);    //Add new integer array at this new lowercase key
                }
            }
        }
    }
    /**Loads investments and tosses them into a portfolio - provided a filename to read from */
    public void loadInvestments(String filename){
        String investmentType;
        String symbol;
        String name;
        int quantity;
        double price;
        double bookVal;
        try{
            File f = new File(filename);
            Scanner fileScanner = new Scanner(f);

            while (fileScanner.hasNextLine()){                      //Because i formatted this, if theres a next line- there's also 6 more
                investmentType = fileScanner.nextLine().split(" = ")[1];
                symbol = fileScanner.nextLine().split(" = ")[1];
                name = fileScanner.nextLine().split(" = ")[1];
                quantity = Integer.parseInt(fileScanner.nextLine().split(" = ")[1]);
                price = Double.parseDouble(fileScanner.nextLine().split(" = ")[1]);
                bookVal = Double.parseDouble(fileScanner.nextLine().split(" = ")[1]);

                if (investmentType.compareToIgnoreCase("stock") == 0){
                    Stock newStock = new Stock(investmentType, symbol, name, quantity, price);
                    newStock.setBookvalue(bookVal);
                    investmentList.add(newStock);
                }else{
                    MutualFund newFund = new MutualFund(investmentType, symbol, name, quantity, price);
                    newFund.setBookvalue(bookVal);
                    investmentList.add(newFund);
                }
                fileScanner.nextLine();         //account for the newline gap
            }
            fileScanner.close();
        }catch(Exception e){
            System.out.println("Error loading investments - file may be empty");
        }
    }

    /**Saves investments from a portfolio and writes them to a file - provided filename from cmd Line */
    public void saveInvestments(String filename){
        int i;
        try{
            PrintWriter fileWriter = new PrintWriter(filename, "UTF-8");
            for (i = 0; i < investmentList.size(); i++){
                fileWriter.println(investmentList.get(i).writeInvestment());
                fileWriter.print('\n');
            }
            fileWriter.close();
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("Error saving investments");
        }
    }

}
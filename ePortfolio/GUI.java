package ePortfolio;

import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import javax.swing.*;

import ePortfolio.Investment;
import ePortfolio.Main;
import ePortfolio.Portfolio;
/**Master GUI Class, contains built-in listeners for buttons, a master listener for the menu switching and handles some validity checking not 
 * pertaining to GUI objects. Uses CardLayout to switch between windows, initializers are all kept private as this class is meant to handle all things GUI
 * acting as the essential front-end wherein the others are back-end
 */
public class GUI extends JFrame{
    private JPanel currentDisplay = new JPanel(new CardLayout());
    private static int investmentIndex;
    private static String functionString;
    /**Search text made protected as Portfolio search function can easily print into the JtextArea */
    protected static JTextArea searchText;

    /**Standard constructor for the GUI- builds and initializes the welcome screen */
    public GUI(){
        initializeWelcome();
    }

    /**Switches between cards/different display windows
     * initializes each respective window too
     */
    private class MasterListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            if (e.getActionCommand() == "Buy"){
                CardLayout c1 = (CardLayout)(currentDisplay.getLayout());
                initializeBuyInterface();
                c1.show(currentDisplay, "Buy");
            }else if (e.getActionCommand() == "Sell"){
                CardLayout c2 = (CardLayout)(currentDisplay.getLayout());
                initializeSellInterface();
                c2.show(currentDisplay, "Sell");
            }else if (e.getActionCommand() == "Update"){
                CardLayout c3 = (CardLayout)(currentDisplay.getLayout());
                initializeUpdateInterface();
                c3.show(currentDisplay, "Update");
            }else if (e.getActionCommand() == "Get Gain"){
                CardLayout c4 = (CardLayout)(currentDisplay.getLayout());
                initializeGainInterface();
                c4.show(currentDisplay, "Get Gain");
            }else if (e.getActionCommand() == "Search"){
                CardLayout c5 = (CardLayout)(currentDisplay.getLayout());
                initializeSearchInterface();
                c5.show(currentDisplay, "Search");
            }else if(e.getActionCommand() == "Save & Quit"){
                if (!(Main.fileName.isEmpty())){             //Adds save functionality
                    Main.ePortfolio.saveInvestments(Main.fileName);
                }
                System.exit(0);
            }
        }
    }

    private void initializeWelcome(){
        JMenuBar mainMenu = new JMenuBar();
        JPanel menuScreen = new JPanel();
        JMenu commands = new JMenu("Commands");
        JMenuItem buy = new JMenuItem("Buy");
        JMenuItem sell = new JMenuItem("Sell");
        JMenuItem update = new JMenuItem("Update");
        JMenuItem gain = new JMenuItem("Get Gain");
        JMenuItem search = new JMenuItem("Search");
        JMenuItem quit = new JMenuItem("Save & Quit");
        JLabel welcome = new JLabel("Welcome to ePortfolio - your family owned, tried and true investment manager\n Select an option from the commands menu above!");

        setTitle("ePortfolio - Welcome");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setBackground(Color.LIGHT_GRAY);
        add(currentDisplay);

        welcome.setLayout(new BorderLayout());

        //Add remainder of listeners for each menu option
        buy.addActionListener(new MasterListener());
        sell.addActionListener(new MasterListener());
        update.addActionListener(new MasterListener());
        gain.addActionListener(new MasterListener());
        search.addActionListener(new MasterListener());
        quit.addActionListener(new MasterListener());

        commands.add(buy);
        commands.add(sell);
        commands.add(update);
        commands.add(gain);
        commands.add(search);
        commands.add(quit);
        mainMenu.add(commands);
        setJMenuBar(mainMenu);
        menuScreen.add(welcome, BorderLayout.CENTER);
        currentDisplay.add(menuScreen, "Welcome");

    }

    private void initializeBuyInterface(){
        String[] investmentTypes = {"Stock", "Mutual Fund"};
        JPanel buyScreen = new JPanel(new GridLayout(2, 1, 0, 20));
        JPanel topInputArea = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 25, 25));
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 5, 10));
        JPanel bottomOutputArea = new JPanel(new BorderLayout());
        JLabel typeLabel = new JLabel("Type: ");
        JComboBox type = new JComboBox(investmentTypes);
        JLabel symbolLabel = new JLabel("Symbol: ");
        JTextField symbol = new JTextField();
        JLabel nameLabel = new JLabel("Name: ");
        JTextField name = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity: ");
        JTextField quantity = new JTextField();
        JLabel priceLabel = new JLabel("Price: ");
        JTextField price = new JTextField();
        JButton reset = new JButton("Reset");
        JButton submit = new JButton("Buy");
        JTextArea outputText = new JTextArea();
        JScrollPane scrolledOutputText = new JScrollPane(outputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JLabel outputLabel = new JLabel("Messages: ");
        JLabel panelTitle = new JLabel("~Buying an Investment~");

        //work input panel in input area first
        
        inputPanel.add(typeLabel);
        inputPanel.add(type);
        inputPanel.add(symbolLabel);
        inputPanel.add(symbol);
        inputPanel.add(nameLabel);
        inputPanel.add(name);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantity);
        inputPanel.add(priceLabel);
        inputPanel.add(price);

        //Work button panel in input area
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                symbol.setText("");
                name.setText("");
                quantity.setText("");
                price.setText("");
            }
        });

        submit.addActionListener(new ActionListener(){              //in-text listeners such that inheriting changing different features is easier
            public void actionPerformed(ActionEvent e){
                try{
                    Main.ePortfolio.buy((String)type.getSelectedItem(), symbol.getText(), name.getText(), Double.parseDouble(price.getText()),Integer.parseInt(quantity.getText()));
                    outputText.setText("Successfully bought "+quantity.getText()+" of "+symbol.getText()+" for "+price.getText()+"\n\n"+Portfolio.investmentList.get(Portfolio.investmentList.size() - 1).toString());
                }catch(Exception b){
                    outputText.setText(b.getMessage());
                }
            }
        });
        reset.setBounds(0, 0, 75, 50);
        submit.setBounds(0, 0, 75, 50);
        buttonPanel.add(reset);
        buttonPanel.add(submit);

        //Bottom output area 
        outputText.setEditable(false);
        scrolledOutputText.setBounds(0, 0, 300, 400);
        bottomOutputArea.setBounds(10, 10, 300, 400);
        bottomOutputArea.add(scrolledOutputText, BorderLayout.CENTER);
        bottomOutputArea.add(outputLabel, BorderLayout.NORTH);

        //Linking JPanels
        buyScreen.setSize(800, 600);
        topInputArea.add(panelTitle, BorderLayout.NORTH);
        topInputArea.add(inputPanel, BorderLayout.WEST);
        topInputArea.add(buttonPanel, BorderLayout.EAST);
        buyScreen.add(topInputArea);
        buyScreen.add(bottomOutputArea);

        currentDisplay.add(buyScreen, "Buy");
    }

    private void initializeSellInterface(){
        JPanel sellScreen = new JPanel(new GridLayout(2, 1, 0, 20));
        JPanel topInputArea = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 25, 25));
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 20, 65));
        JPanel bottomOutputArea = new JPanel(new BorderLayout());
        JLabel symbolLabel = new JLabel("Symbol: ");
        JTextField symbol = new JTextField();
        JLabel quantityLabel = new JLabel("Quantity: ");
        JTextField quantity = new JTextField();
        JLabel priceLabel = new JLabel("Price: ");
        JTextField price = new JTextField();
        JButton reset = new JButton("Reset");
        JButton submit = new JButton("Sell");
        JTextArea outputText = new JTextArea();
        JScrollPane scrolledOutputText = new JScrollPane(outputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JLabel outputLabel = new JLabel("Messages: ");
        JLabel panelTitle = new JLabel("~Selling an Investment~");

        //work input panel in input area first
        symbol.setSize(200, 50);
        quantity.setSize(200, 50);
        price.setSize(200, 50);

        inputPanel.add(symbolLabel);
        inputPanel.add(symbol);
        inputPanel.add(quantityLabel);
        inputPanel.add(quantity);
        inputPanel.add(priceLabel);
        inputPanel.add(price);

        //Work button panel in input area
        reset.setSize( 75, 50);
        submit.setSize( 75, 50);
        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                symbol.setText("");
                quantity.setText("");
                price.setText("");
            }
        });
        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){             //values inputed into portfolio methods
                try{
                    Main.ePortfolio.sell(symbol.getText(), Integer.parseInt(quantity.getText()), Double.parseDouble(price.getText()));
                    outputText.setText("Successfully sold "+quantity.getText()+" of "+symbol.getText()+" at $"+price.getText());
                }catch (Exception b){
                    outputText.setText(b.getMessage());
                }
            }
        });
        buttonPanel.add(reset);
        buttonPanel.add(submit);

        //Bottom output area 
        outputText.setEditable(false);
        scrolledOutputText.setBounds(0, 0, 300, 400);
        bottomOutputArea.setBounds(10, 10, 300, 400);
        bottomOutputArea.add(scrolledOutputText, BorderLayout.CENTER);
        bottomOutputArea.add(outputLabel, BorderLayout.NORTH);

        //Linking JPanels
        sellScreen.setSize(800, 600);
        topInputArea.add(panelTitle, BorderLayout.NORTH);
        topInputArea.add(inputPanel, BorderLayout.WEST);
        topInputArea.add(buttonPanel, BorderLayout.EAST);
        sellScreen.add(topInputArea);
        sellScreen.add(bottomOutputArea);

        currentDisplay.add(sellScreen, "Sell");
    }

    private void initializeUpdateInterface(){
        JPanel updateScreen = new JPanel(new GridLayout(2, 1, 0, 20));
        JPanel topInputArea = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(3, 1, 25, 25));
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 20, 65));
        JPanel bottomOutputArea = new JPanel(new BorderLayout());
        JLabel symbolLabel = new JLabel("Symbol: ");
        JTextArea symbol = new JTextArea();
        JLabel investmentLabel = new JLabel("Name: ");
        JTextArea investmentName = new JTextArea();
        JLabel priceLabel = new JLabel("Price: ");
        JTextField price = new JTextField();
        JButton prev = new JButton("Previous");
        JButton next = new JButton("Next");
        JButton save = new JButton("Save");
        JTextArea outputText = new JTextArea();
        JScrollPane scrolledOutputText = new JScrollPane(outputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JLabel outputLabel = new JLabel("Messages: ");
        JLabel panelTitle = new JLabel("~Updating an Investment~");
        
        
        investmentIndex = 0;
        

        //work input panel in input area first
        symbol.setEditable(false);
        symbol.setBackground(Color.LIGHT_GRAY);
        symbol.setText(Portfolio.investmentList.get(investmentIndex).getTicker().toUpperCase());
        investmentName.setEditable(false);
        investmentName.setBackground(Color.LIGHT_GRAY);
        investmentName.setText(Portfolio.investmentList.get(investmentIndex).getName());

        inputPanel.add(symbolLabel);
        inputPanel.add(symbol);
        inputPanel.add(investmentLabel);
        inputPanel.add(investmentName);
        inputPanel.add(priceLabel);
        inputPanel.add(price);

        //Work button panel in input area
        prev.setEnabled(false);
        prev.addActionListener(new ActionListener(){                            //checks position based on arraylist indicies
            public void actionPerformed(ActionEvent e){                         //enables/disables buttons respectively
                investmentIndex--;  
                symbol.setText(Portfolio.investmentList.get(investmentIndex).getTicker().toUpperCase());
                investmentName.setText(Portfolio.investmentList.get(investmentIndex).getName());
                if (investmentIndex == Portfolio.investmentList.size() - 1){
                    next.setEnabled(false);
                }else{
                    next.setEnabled(true);
                }
                if (investmentIndex == 0){
                    prev.setEnabled(false);
                }else{
                    prev.setEnabled(true);
                }
            }
        });
        next.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                investmentIndex++;
                symbol.setText(Portfolio.investmentList.get(investmentIndex).getTicker().toUpperCase());
                investmentName.setText(Portfolio.investmentList.get(investmentIndex).getName());
                if (investmentIndex == 0){                  //enable/disable button based on index of array list respectively
                    prev.setEnabled(false);
                }else{
                    prev.setEnabled(true);
                }
                if (investmentIndex == Portfolio.investmentList.size() - 1){
                    next.setEnabled(false);
                }else{
                    next.setEnabled(true);
                }
            }
        });
        save.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){             //Grabs on-screen info and saves it to the investment
                try{
                    if (Double.parseDouble(price.getText()) < 0){
                        throw new Exception("New price cant be less than 0");
                    }
                    Investment updateInvestment = Portfolio.investmentList.get(investmentIndex);
                    updateInvestment.setPrice(Double.parseDouble(price.getText()));
                    Portfolio.investmentList.set(investmentIndex, updateInvestment);
                    outputText.setText("Successfully updated "+symbol.getText()+"\n\n"+ Portfolio.investmentList.get(investmentIndex).toString());
                }catch(Exception b){
                    outputText.setText(b.getMessage());
                }
            }
        });
        buttonPanel.add(prev);
        buttonPanel.add(next);
        buttonPanel.add(save);

        //Bottom output area 
        outputText.setEditable(false);
        scrolledOutputText.setBounds(0, 0, 300, 400);
        bottomOutputArea.setBounds(10, 10, 300, 400);
        bottomOutputArea.add(scrolledOutputText, BorderLayout.CENTER);
        bottomOutputArea.add(outputLabel, BorderLayout.NORTH);

        //Linking JPanels
        updateScreen.setSize(800, 600);
        topInputArea.add(panelTitle, BorderLayout.NORTH);
        topInputArea.add(inputPanel, BorderLayout.WEST);
        topInputArea.add(buttonPanel, BorderLayout.EAST);
        updateScreen.add(topInputArea);
        updateScreen.add(bottomOutputArea);

        currentDisplay.add(updateScreen, "Update");
    }

    private void initializeGainInterface(){
        JPanel gainScreen = new JPanel(new BorderLayout());
        JPanel topInputArea = new JPanel(new BorderLayout());
        JPanel bottomOutputArea = new JPanel(new BorderLayout());
        JLabel panelTitle = new JLabel("~Getting Portfolio & Individual Gain on Investment~");
        JTextField calculatedGain = new JTextField();
        JTextArea outputText = new JTextArea();
        JScrollPane scrolledOutputText = new JScrollPane(outputText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JLabel outputLabel = new JLabel("Messages: ");
        int i;

        calculatedGain.setEditable(false);                                      //No listeners here, can easily calculate all needed info and display it
        calculatedGain.setSize(150, 150);
        calculatedGain.setText("Total Gain: "+Main.ePortfolio.getGain());
        
        outputText.setText("Gain for "+Portfolio.investmentList.get(0).getTicker()+" = "+Portfolio.investmentList.get(0).getGains()+"\n");
        for (i = 1; i < Portfolio.investmentList.size(); i++){
            outputText.append("Gain for "+Portfolio.investmentList.get(i).getTicker()+" = "+Portfolio.investmentList.get(i).getGains()+"\n");
        }
    
        outputText.setEditable(false);
        scrolledOutputText.setBounds(0, 0, 300, 400);
        bottomOutputArea.setBounds(10, 10, 300, 400);
        bottomOutputArea.add(scrolledOutputText, BorderLayout.CENTER);
        bottomOutputArea.add(outputLabel, BorderLayout.NORTH);

        topInputArea.add(panelTitle, BorderLayout.NORTH);
        topInputArea.add(calculatedGain, BorderLayout.CENTER);
        gainScreen.add(topInputArea, BorderLayout.NORTH);
        gainScreen.add(bottomOutputArea, BorderLayout.CENTER);

        currentDisplay.add(gainScreen, "Get Gain");
    }

    private void initializeSearchInterface(){
        JPanel searchScreen = new JPanel(new GridLayout(2, 1, 0, 20));
        JPanel topInputArea = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new GridLayout(2, 1, 25, 25));
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 5, 10));
        JPanel bottomOutputArea = new JPanel(new BorderLayout());
        JLabel symbolLabel = new JLabel("Symbol: ");
        JTextField symbol = new JTextField();
        JLabel nameLabel = new JLabel("Name Keywords: ");
        JTextField name = new JTextField();
        JLabel lowLabel = new JLabel("Low Price: ");
        JTextField lowPrice = new JTextField();
        JLabel highLabel = new JLabel("High Price: ");
        JTextField highPrice = new JTextField();
        JButton reset = new JButton("Reset");
        JButton submit = new JButton("Search");
        searchText = new JTextArea();
        functionString = new String();
        JScrollPane scrolledOutputText = new JScrollPane(searchText, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        JLabel outputLabel = new JLabel("Search Results: ");
        JLabel panelTitle = new JLabel("~Search for an Investment~");
        

        inputPanel.add(symbolLabel);
        inputPanel.add(symbol);
        inputPanel.add(nameLabel);
        inputPanel.add(name);
        inputPanel.add(lowLabel);
        inputPanel.add(lowPrice);
        inputPanel.add(highLabel);
        inputPanel.add(highPrice);

        reset.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    symbol.setText("");
                    name.setText("");
                    lowPrice.setText("");
                    highPrice.setText("");
                }catch(Exception b){
                    searchText.setText(b.getMessage());
                }
            }
        });

        submit.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    if (lowPrice.getText().isBlank()){                          //some extra parsing for code reuse later on based on how i implemented the search funciton and how it expects input
                        if (highPrice.getText().isBlank()){                     //this is easier than revamping an entirely new search-function to work with GUI 
                            functionString = "";
                        }else{
                            if (0 > Double.parseDouble(highPrice.getText())){
                                throw new Exception("search price cannot be negative");
                            }
                            functionString = "-"+highPrice.getText();
                        }
                    }else{                                                  //use exceptions to handle invalid values
                        if (highPrice.getText().isBlank()){
                            if (Double.parseDouble(lowPrice.getText()) < 0 ){
                                throw new Exception("search price cannot be negative");
                            }
                            functionString = lowPrice.getText()+"-";
                        }else{
                            if (Double.parseDouble(lowPrice.getText()) > Double.parseDouble(highPrice.getText())){
                                throw new Exception("Lower range price cannot be greater than higher range price");
                            }
                            if (Double.parseDouble(lowPrice.getText()) < 0 || 0 > Double.parseDouble(highPrice.getText())){
                                throw new Exception("search price cannot be negative");
                            }
                            functionString = lowPrice.getText()+"-"+highPrice.getText();
                        }
                    }
                    searchText.setText("Following Matched Investments:\n");
                    Main.ePortfolio.search(symbol.getText(), name.getText(), functionString);
                }catch(Exception b){
                    searchText.setText(b.getMessage());
                }
            }
        });
        buttonPanel.add(reset);
        buttonPanel.add(submit);

        //Bottom output area 
        searchText.setEditable(false);
        scrolledOutputText.setBounds(0, 0, 300, 400);
        bottomOutputArea.setBounds(10, 10, 300, 400);
        bottomOutputArea.add(scrolledOutputText, BorderLayout.CENTER);
        bottomOutputArea.add(outputLabel, BorderLayout.NORTH);

        //Linking JPanels
        searchScreen.setSize(800, 600);
        topInputArea.add(panelTitle, BorderLayout.NORTH);
        topInputArea.add(inputPanel, BorderLayout.WEST);
        topInputArea.add(buttonPanel, BorderLayout.EAST);
        searchScreen.add(topInputArea);
        searchScreen.add(bottomOutputArea);

        currentDisplay.add(searchScreen, "Search");
    }
}

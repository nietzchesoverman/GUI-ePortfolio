##########################################
###############READ ME####################
##########################################
By: Yousif Jamal/ github.com/nietzchesoverman
---------
OVERVIEW
---------

Purpose: This program seeks to create an interactive e-portfolio capable of tracking any number amount both stocks and mutual funds, this should be dynamic
meaning it can be altered. The program now supports saving/loading investments to and from a source file respectively. This program now has a robust GUI.

Assumptions/Limitations: 
- Fixed Commission/ Redemption Fee for stocks and mutual funds respectively.
- Assumes user launches from commandline and not with an executable
- Currency is unspecified
- GUI is intuitive but not the prettiest

-------
FOLDERS
-------
Javadocs: The associated javadocs containing interactive HTML versions of source files

ePortfolio: This is the Package folder containing .java files

stocks.txt: Test file provided for file I/O and quick testing of GUI without having to manually enter

-----------
USER GUIDE
-----------
Compilation: Can be done by running the terminal command "javac ePortfolio/*.java" while in the directory that contains the ePortfolio package.

Running: Can be accomplished by launching the Main class using the following command: "java ePortfolio.Main filename.txt" while in the directory that contains the ePortfolio package.
"filename.txt" need not be in a directory or exist - as it will just save to that file upon exiting if you select the "Save & Exit" drop down menu option

----------
TEST-PLAN
----------

Buy();
	-Ensure function doesn't break or create new instances of the stock/mutual fund class when entering in the same ticker, function shouldn't ask for same ticker's
	company/mutual fund name as it was given this information previously.
	-Ensure function doesn't accept negative or 0 prices/quantities
	-Buying price updates the stock.price instance variable of that specific object. 
	-Ensure all information is placed in the proper instance variables, with book cost calculated properly.
	-Ensure that constructor properly instantiates the object and it is placed in the appropriate Stock / Mutual Fund arrayList in the Portfolio class
Sell();
	-Function should appropriately decrement quantity and bookcost when executed
	-Function shouldn't accept 0 values for either price or value
	-Function should update stock price to whatever price the user just made their sale at
	-gain should be calculated and stored within the instance variable when stock or mutual fund was sold at that specific price
	-If all of an investment is sold it should be deleted from its respective arrayList with the gain recorded
Update();
	-Function should iterate over both stocks and mutual funds prompting user for newprice on all their respective arrayList elements
	-Ensure assumption isn't made that stockList length is the same as the Mutual fund List length.
	-input negative price or quantity value and observe handling
	-new price should be reflected in each investment vehicle's private price variable using a mutator.
getGain();
	-Should ensure that price is the most recent price when making the "hypothetical sale"
	-Should test using gain of empty profile - result should be 0
	-ensure price affecting getGain is updated for both sells, buys, and updates.
search();
	-Search for stock using no parameters
	-Search for stock using combination of 2 parameters
	-Search for stock using combination of all 3 parameters
	-Search for stock using one parameter that is true and one that is false- ensuring an "all or nothing" search is executed and 
	the search doesn't just return true upon the first true parameter
	-Search for an investment with multiple keywords to see that proper tokenization is taking place
	-Search using invalid price numbers
	-Search for element that isnt in the investment arrayList. Should provide appropriate output stating investment wasn't found
saveInvestments();
	-Ensure it overwrites any file in the directory with the same name/file extension combo (essentially updating a save)
	-Ensure it creates a file in the appropriate directory with command line argument's name/extension if the file doesn't exist
loadInvestments();
	-Ensure when loading that bookvalue is the same as the source file's and doesn't get altered to price*quantity as that's what the constructor would do
	 here we're esentially ensuring that one of the helperfunctions acts on bookvalue - this would effect the gain of the investment, as gain would be set to
	 0 automatically if all investments were just instantiated solely with the constructor- there may have been previous gains that are being left out now
	-Call search with no parameters to ensure that all the investments show up and have the same proper values as the ones in the source files.
GUI Testing
	-Ensure that text field items are locked in place such that absurd values dont end up breaking your GUI
	-Blank entries on JTextFields or Text areas shouldn't break the program when actionEvents are fired
	-Resizing window shouldn't break the program or GUI look
	-Save&Close should write information into same file it read from, clicking (x) button should just exit the program
------------
IMPROVEMENTS
------------
-Flesh out a master listener class so that we don't have 10 classes being made for all the buttons/action events in the program
-More time spent making the GUI more appealing to the eye, more rigourous testing such that one can ensure it doesn't glitch out
-Cardlayout method for switching windows seems intensive (constantly having all those elements rendered or working cant be good for memory/ computational load) - use more windows and dispose?
-use more OOP principles - Portfolio can preform less of the legwork and spread it out across another helper class/function
-Be able to read .csv files and get real-time updates from website like yahoo finance that actually provide historical stock data
-Better UI: Flesh out the neatness and spacing between different displays, allow user to pick their currency, allow for cross portability w bank account investment funds
-Be able to calculate %gain of overall portfolio instead of just the dollar gain of your investments. Account for Realized + Unrealized Gains when performing getGains
Essentially account for Profit/Loss on overall portfolio (including money you've pulled out as cash) vs Profit/Loss on investments only



package ePortfolio;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.awt.event.KeyAdapter;
import javax.swing.*;
/**Handles Main intitialization of the GUI as well as handling Portfolio and Filename variables for portfolio functions and saving
 * command line argument- if no file found, it will save program input to same command line argument file upon exiting
 */
public class Main {
    public static Portfolio ePortfolio = new Portfolio();
    public static String fileName;
    public static void main(String[] args){
        
        String optionSelected;
        fileName = args[0];
        Scanner scannerObj = new Scanner(System.in);
        GUI mainFrame = new GUI();

        mainFrame.setVisible(true);

        if (!(fileName.isEmpty())){
            ePortfolio.loadInvestments(fileName);
        }
        scannerObj.close();
    }
}

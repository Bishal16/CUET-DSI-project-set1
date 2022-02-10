package com.company;


import java.io.*;
import java.util.*;

/**
 *
 * @author Mahathir Bishal
 */


public class Main {

    public static List<Map> inventory = new ArrayList();
    public static double AccountBalance;


    static void clear_console(){
        //Clears Screen in java
        try {
            if (System.getProperty("os.name").contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {}
    }

    static void addProduct(){
        String productName;
        double buyPrice;
        double sellPrice;
        double productProfit = 0.0;
        int available = 0;
        boolean deleted = false;

        Scanner input = new Scanner(System.in);

        // getting product details
        System.out.println("Enter Product details :\n");
        System.out.print("\tProduct name : ");
        productName = input.nextLine();

        System.out.print("\tBuy price : ");
        buyPrice = input.nextDouble();

        System.out.print("\tSell price : ");
        sellPrice = input.nextDouble();


        //checking if the product is already added
        boolean duplicate = false;
        for (int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).get("productName").equals(productName)) {
                System.out.println("\""+productName+"\" is already in the inventory list!! ");
                duplicate = true;
                break;
            }
        }

        //adding product to inventory
        if (!duplicate) {
            Map product = new HashMap();

            product.put("productName",productName);
            product.put("buyPrice",buyPrice);
            product.put("sellPrice",sellPrice);
            product.put("productProfit",productProfit);
            product.put("available",available);
            product.put("deleted",deleted);

            inventory.add(product);
            System.out.println("\""+productName +"\" added to inventory list.");
        }
    }

    static void deleteProduct(){
        System.out.print("\n\tEnter product name to delete : ");

        Scanner input = new Scanner(System.in);
        String delete =  input.nextLine();
        Boolean prodFound = false;

        for (int i = 0; i < inventory.size(); i++){
            String name = inventory.get(i).get("productName").toString();
            Boolean deleted = (Boolean)inventory.get(i).get("deleted");
            if (name.equals(delete) && !deleted){
                inventory.get(i).put("deleted", true);
                prodFound = true;
                System.out.println("\""+ delete +"\" deleted from inventory.");
                break;
            }
        }
        if (!prodFound){
            System.out.println("\""+ delete +"\" Not Found in inventory!!");
        }
    }

    static void buyProduct(){
        System.out.print("\n\tEnter product name to buy : ");
        Scanner input = new Scanner(System.in);
        String buy =  input.nextLine();

        System.out.print("\tEnter product quantity : ");
        int quantity =  input.nextInt();

        boolean prodFound = false;
        for (int i = 0; i < inventory.size(); i++){
            if (inventory.get(i).get("productName").toString().equals(buy)) {
                double bPrice = (double) inventory.get(i).get("buyPrice");

                //check account balance before buying
                if (bPrice*quantity > AccountBalance)
                    System.out.println("Not enough balance to buy.\nBuy fail ");
                else {
                    int prev = (int) inventory.get(i).get("available");
                    inventory.get(i).put("available", prev + quantity);
                    AccountBalance = AccountBalance - (quantity * bPrice);
                    System.out.println("Product buy successfull!!");
                }
                prodFound = true;
                break;
            }
        }
        if (!prodFound)
            System.out.println("Product not found in inventory. \nBuy fail.");
    }

    static void sellProduct(){
        //clear_console();
        System.out.print("\n\tEnter product name to sell : ");
        Scanner input = new Scanner(System.in);
        String sell =  input.nextLine();

        System.out.print("\tEnter product quantity : ");
        int quantity =  input.nextInt();

        boolean prodFound = false;
        for (int i = 0; i < inventory.size(); i++){
            if (inventory.get(i).get("productName").toString().equals(sell)) {
                int avail = (int) inventory.get(i).get("available");

                //checking availability before selling
                if (quantity > avail)
                    System.out.println("Not enough product available.\nsell fail ");
                else {
                    int prvQty = (int) inventory.get(i).get("available");
                    double prvProfit = (double) inventory.get(i).get("productProfit");
                    double sPrice = (double) inventory.get(i).get("sellPrice");
                    double bPrice = (double) inventory.get(i).get("buyPrice");

                    inventory.get(i).put("available", prvQty - quantity);
                    inventory.get(i).put("productProfit", prvProfit + (sPrice - bPrice) * quantity);
                    AccountBalance = AccountBalance + (quantity * sPrice);
                    System.out.println("Product sell successfull!!");
                }
                prodFound = true;
                break;
            }
        }
        if (!prodFound)
            System.out.println("Product not found in inventory. \nsell fail.");
    }
    //this function used for two scenarios 1.show only inventory item with available amount
                                         //2.show items with profit and total profit including deleted product
    static void listProduct(boolean productOnly){
        System.out.println("inventory : ");
        double totalProf = 0.0;

        // print table header
        if(productOnly) {
            System.out.format("%20s%15s", "Product Name", "Available\n");
            System.out.format("%20s%15s", "------------", "---------\n");
        }
        else{
            System.out.format("%20s%15s%10s","Product Name","Available", "Profit\n");
            System.out.format("%20s%15s%10s","------------","---------", "------\n");
        }

        //print table row
        for (int i = 0; i < inventory.size(); i++){
            totalProf = totalProf + (double) inventory.get(i).get("productProfit");
            if ((boolean)inventory.get(i).get("deleted") )
                continue;
            if(productOnly){
                System.out.format("%20s%15s",
                        inventory.get(i).get("productName"),
                        inventory.get(i).get("available") +"\n");
            }
            else {
                System.out.format("%20s%15d%10s",
                        inventory.get(i).get("productName"),
                        inventory.get(i).get("available"),
                        inventory.get(i).get("productProfit")+"\n");
            }
        }

        if(productOnly){
            System.out.format("%35s","--------------------------\n");
        }
        else{
            System.out.format("%45s","--------------------------------------\n");
            System.out.format("%45s", "total profit (including deleted) = "+totalProf+"\n");
        }

    }

    static void seeAccount(){
        System.out.println("\n\tAvailable account balance: " + AccountBalance);
    }

    static void options(){
        System.out.println("\n1.Add a product\n"
                + "2.Delete product\n"
                + "3.Buy product\n"
                + "4.Sell product\n"
                + "5.View inventory\n"
                + "6.Show available balance\n"
                + "0.Exit");
        System.out.print("Enter (0-6) : ");
    }

    static void subOption(String option){
        while(true) {
            clear_console();
            listProduct(true);
            System.out.println("\n1."+option+" another product\n" +
                    "2.Go to main menu\n" +
                    "Enter (1-2) : ");
            Scanner input = new Scanner(System.in);

            int subOption = input.nextInt();
            if (subOption == 1 && option=="Add")
                addProduct();
            else if (subOption == 1 && option=="Delete")
                deleteProduct();
            else if (subOption == 1 && option=="Sell")
                sellProduct();
            else if (subOption == 1 && option=="Buy")
                buyProduct();
            else if (subOption == 2) {
                break;
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("Shop ledger v.2 started.\n");
        Main Inventory = new Main();
        //--------------------------------------------------
        //retrieving previously saved data from file into inventory list variable
        try {
            FileInputStream fis = new FileInputStream("savedData.tmp");
            ObjectInputStream ois = new ObjectInputStream(fis);
            Inventory.inventory  = (List<Map>) ois.readObject();
            ois.close();

            FileInputStream fis2 = new FileInputStream("savedAccBal.tmp");
            ObjectInputStream ois2 = new ObjectInputStream(fis2);
            Inventory.AccountBalance  = (double) ois2.readObject();
            System.out.println(Inventory.AccountBalance);
            ois2.close();
        }
        catch(Exception e) {
            System.out.println("read failed");
        }
        //---------------------------------------------------


        while (true){
            Inventory.clear_console();
            Inventory.options();

            Scanner input = new Scanner(System.in);
            int option = input.nextInt();
            if(option < 0 || option > 6){
                System.out.println("Warning : Enter between 0-6!!");
                continue;
            }
            switch(option) {
                case 1:
                    Inventory.addProduct();
                    subOption("Add"); // created an extra function to avoid code redundancy
                    break;
                case 2:
                    Inventory.listProduct(true);//show inventory before deleting a product
                    Inventory.deleteProduct();
                    subOption("Delete");
                    break;
                case 3:
                    Inventory.listProduct(true);
                    Inventory.buyProduct();
                    subOption("Buy");
                    break;
                case 4:
                    Inventory.listProduct(true);
                    Inventory.sellProduct();
                    subOption("Sell");
                    break;
                case 5:
                    Inventory.listProduct(false);
                    System.out.println("Press Enter key to continue...");
                    Scanner s = new Scanner(System.in);
                    s.nextLine();
                    break;
                case 6:
                    Inventory.seeAccount();
                    System.out.println("Press Enter key to continue...");
                    Scanner t = new Scanner(System.in);
                    t.nextLine();
                    break;
                case 0:
                    //saving inventory info to file
                    try {
                        FileOutputStream fos = new FileOutputStream("savedData.tmp");
                        ObjectOutputStream oos = new ObjectOutputStream(fos);
                        oos.writeObject(Inventory.inventory);
                        oos.close();

                        FileOutputStream fos2 = new FileOutputStream("savedAccBal.tmp");
                        ObjectOutputStream oos2 = new ObjectOutputStream(fos2);
                        oos2.writeObject(Inventory.AccountBalance);
                        oos2.close();
                    }
                    catch(Exception e) {
                        System.out.println("save failed");
                    }

                    //
                    System.out.println("Program Terminated and Data saved.");
                    System.exit(0);
            }
        }
    }
}

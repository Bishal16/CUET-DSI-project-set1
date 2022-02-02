package com.company;


import java.io.IOException;
import java.util.*;

/**
 *
 * @author Mahathir Bishal
 */


public class Main {

    private static List<Map> inventory = new ArrayList();
    public static double AccountBalance = 1000;


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
        clear_console();
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

        //productProfit = sellPrice - buyPrice;

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
        //System.out.println(inventory);
        //System.out.println(inventory.get(0).get("productName"));


        optionCaller();

    }
    static void deleteProduct(){
        clear_console();
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

        optionCaller();
    }
    static void buyProduct(){
        clear_console();
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
            System.out.println("Product not found in inventory. \nBuy fail2.");

        optionCaller();
    }
    static void sellProduct(){
        clear_console();
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

        optionCaller();
    }
    static void listProduct(){
        clear_console();
        System.out.println("inventory : ");
        double totalProf = 0.0;

        System.out.format("%20s%15s%10s","Product Name","Available", "Profit\n");
        System.out.format("%20s%15s%10s","------------","---------", "------\n");

        for (int i = 0; i < inventory.size(); i++){
            totalProf = totalProf + (double) inventory.get(i).get("productProfit");
            if ((boolean)inventory.get(i).get("deleted") )
                continue;
            System.out.format("%20s%15d%10s",
                    inventory.get(i).get("productName"),
                    inventory.get(i).get("available"),
                    inventory.get(i).get("productProfit")+"\n");
        }
        System.out.format("%45s","-------------------------------------\n");
        System.out.format("%45s", "total profit (including deleted) = "+totalProf+"\n");
        optionCaller();
    }
    static void seeAccount(){
        clear_console();
        System.out.println("\n\tAvailable account balance: " + AccountBalance);
        optionCaller();
    }

    static void optionCaller(){
        System.out.println("\n1.Add a product\n"
                + "2.Delete product\n"
                + "3.Buy product\n"
                + "4.Sell product\n"
                + "5.View inventory\n"
                + "6.Show available balance\n"
                + "0.Exit");
        System.out.print("Enter (0-6) : ");
        Scanner input = new Scanner(System.in);
        int option = input.nextInt();
        switch(option) {
            case 1:
                addProduct();
                break;
            case 2:
                deleteProduct();
                break;
            case 3:
                buyProduct();
                break;
            case 4:
                sellProduct();
                break;
            case 5:
                listProduct();
                break;
            case 6:
                seeAccount();
                break;
            case 0:
                System.out.println("Program Terminated.");
                System.exit(0);
        }
    }


    public static void main(String[] args) {
        System.out.println("Start\n");
        clear_console();
        optionCaller();
    }
}

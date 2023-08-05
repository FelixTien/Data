import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

class HW3{
    public static void main(String args[]) throws FileNotFoundException{
        ArrayList<Coffee> coffees = new ArrayList<Coffee>();
        ArrayList<Tea> teas = new ArrayList<Tea>();

        readInventory(coffees, teas);
        System.out.println("Display Inventory");
        display(coffees, teas);
        System.out.println();

        ArrayList<Order> orders = new ArrayList<Order>();
        readOrder(coffees, teas, orders);
        System.out.println("Display Total Sales");
        totalSales(orders);
        System.out.println();

        updateInventory(coffees, teas);
        System.out.println("Inventory has been updated!!");
    }
    public static void readInventory(ArrayList<Coffee> coffees, ArrayList<Tea> teas) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("inventory.csv"));
        sc.useDelimiter("\n");
        sc.next();
        while (sc.hasNext()) {
            String temp[] = sc.next().split(",");
            if(temp[0].equals("Coffee")){
                coffees.add(new Coffee(temp[1], temp[2], Double.parseDouble(temp[3]), Double.parseDouble(temp[4]), temp[5]));
            }else{
                teas.add(new Tea(temp[1], temp[2], Double.parseDouble(temp[3]), Integer.parseInt(temp[4]), temp[5]));
            }
        }
        sc.close();
    }
    public static void display(ArrayList<Coffee> coffees, ArrayList<Tea> teas) throws FileNotFoundException{
        for(int i = 0; i < coffees.size(); i++){
            System.out.println(coffees.get(i).printDetails());
        }
        for(int i = 0; i < teas.size(); i++){
            System.out.println(teas.get(i).printDetails());            
        }
    }
    public static void readOrder(ArrayList<Coffee> coffees, ArrayList<Tea> teas, ArrayList<Order> orders) throws FileNotFoundException{
        Scanner sc = new Scanner(new File("order.csv"));
        sc.useDelimiter("\n");
        sc.next();
        while (sc.hasNext()) {
            String temp[] = sc.next().split(",");
            if(temp[1].equals("Coffee")){
                for(int i = 0; i < coffees.size(); i++){
                    if(temp[2].equals(coffees.get(i).name)){
                        orders.add(new Order(Integer.parseInt(temp[0]), coffees.remove(i), Integer.parseInt(temp[3])));
                        break;
                    }
                }
            }else{
                for(int i = 0; i < teas.size(); i++){
                    if(temp[2].equals(teas.get(i).name)){
                        orders.add(new Order(Integer.parseInt(temp[0]), teas.remove(i), Integer.parseInt(temp[3])));
                        break;
                    }
                }
            }
        }
        sc.close();
    }
    public static void totalSales(ArrayList<Order> orders){
        if(orders.size() > 0){
            int id = orders.get(0).orderID;
            double price = 0.0;
            int i = 0;
            while(i <= orders.size()){
                if(i == orders.size()){
                    System.out.println("The total price of order " + id + " is " + price);
                }
                else if(orders.get(i).orderID == id){
                    price += orders.get(i).getOrderTotal();
                }else{
                    System.out.println("The total price of order " + id + " is " + price);
                    id = orders.get(i).orderID;
                    price = orders.get(i).getOrderTotal();
                }
                i++;
            }
        }
    }
    public static void updateInventory(ArrayList<Coffee> coffees, ArrayList<Tea> teas){
        try (FileWriter fileWriter = new FileWriter("info.csv")) {
            fileWriter.write("Type,Name,Size,Price,Caffeine,Origin\n");
            for(int i = 0; i < coffees.size(); i++){
                fileWriter.write(coffees.get(i).getDetails() + "\n");
            }
            for(int i = 0; i < teas.size(); i++){
                fileWriter.write(teas.get(i).getDetails() + "\n");
            }
        }catch(IOException e){}
    }
}
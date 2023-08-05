abstract class Beverage {
    String name;
    String size;
    double price;

    Beverage(String name, String size, double price){
        this.name = name;
        this.size = size;
        this.price = price;
    }

    abstract String getDetails();
}

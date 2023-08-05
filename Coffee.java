class Coffee extends Beverage {
    double caffeineContent;
    String origin;

    Coffee(String name, String size, double price, double caffeineContent, String origin){
        super(name, size, price);
        this.caffeineContent = caffeineContent;
        this.origin = origin;
    }

    String getDetails(){
        return "Coffee," + this.name + "," + this.size + "," + this.price + "," + this.caffeineContent + "," + this.origin;
    }
    String printDetails(){
        return "Coffee\t" + this.name + "\t" + this.size + "\t" + this.price + "\t" + this.caffeineContent + "\t" + this.origin;
    }
}

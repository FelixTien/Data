import java.util.Random;

class Tea extends Beverage {
    double caffeineContent;
    String origin;
    int steepingTime;
    String flavor;

    Tea(String name, String size, double price, double caffeineContent, String origin){
        super(name, size, price);
        this.caffeineContent = caffeineContent;
        this.origin = origin;

        Random r = new Random();
        this.steepingTime = r.nextInt(90)+10;
        this.flavor = this.name.split(" ")[0];
    }

    String getDetails(){
        return "Tea," + this.name + "," + this.size + "," + this.price + "," + this.caffeineContent + "," + this.origin;
    }
    String printDetails(){
        return "Tea\t" + this.name + "\t" + this.size + "\t" + this.price + "\t" + this.steepingTime + "\t" + this.flavor;
    }
}

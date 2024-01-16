import java.util.Arrays;
import java.util.List;

class Car{
    private int id;
    private String name;
    private String brand;
    public Car(int id, String name, String brand){
        this.id = id;
        this.name = name;
        this.brand = brand;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "id = " + this.id + " name = " + this.name + " brand";
    }
}

public class Test {
    public static void main(String args[]){
        Car c1 = new Car(10, "Prius", "Toyota");
        Car c2 = new Car(25, "Wish", "Toyota");
        Car c3 = new Car(15, "RAV4", "Toyota");
        List<Car> list = Arrays.asList(c1, c2, c3);
        list.sort((a, b) -> b.getId() - a.getId());
        System.err.println(list);
    }
}

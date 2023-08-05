class Order {
    int orderID;
    Beverage beverage;
    int quantity;

    Order(int orderID, Beverage beverage, int quantity){
        this.orderID = orderID;
        this.beverage = beverage;
        this.quantity = quantity;
    }

    double getOrderTotal(){
        return Math.round((this.beverage.price * this.quantity)*100.0)/100.0;
    }
}
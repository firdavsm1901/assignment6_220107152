package com.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

interface Beverage {
    double cost();
    String getDescription();
}

class BasicBeverage implements Beverage {
    private final String name;
    private final double basePrice;

    public BasicBeverage(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    @Override
    public double cost() {
        return basePrice;
    }

    @Override
    public String getDescription() {
        return name;
    }
}

abstract class BeverageDecorator implements Beverage {
    protected Beverage beverage;

    public BeverageDecorator(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public String getDescription() {
        return beverage.getDescription();
    }
}

class Topping extends BeverageDecorator {
    private final String toppingName;
    private final double toppingPrice;

    public Topping(Beverage beverage, String toppingName, double toppingPrice) {
        super(beverage);
        this.toppingName = toppingName;
        this.toppingPrice = toppingPrice;
    }

    @Override
    public double cost() {
        return beverage.cost() + toppingPrice;
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", " + toppingName;
    }
}

public class Main {
    private static final List<Beverage> beverages = new ArrayList<>();
    static {
        beverages.add(new BasicBeverage("Coffee", 2.5));
        beverages.add(new BasicBeverage("Tea", 2.0));
        beverages.add(new BasicBeverage("Latte", 3.0));
        beverages.add(new BasicBeverage("Cappuccino", 3.5));
        beverages.add(new BasicBeverage("Espresso", 2.0));
    }

    private static final List<Topping> toppings = new ArrayList<>();
    static {
        toppings.add(new Topping(beverages.get(0), "Caramel Syrup", 0.5));  
        toppings.add(new Topping(beverages.get(0), "Whipped Cream", 0.75)); 
        toppings.add(new Topping(beverages.get(0), "Vanilla Syrup", 0.4)); 
        toppings.add(new Topping(beverages.get(0), "Marshmallows", 0.8)); 

        toppings.add(new Topping(beverages.get(1), "Honey", 0.3)); 
        toppings.add(new Topping(beverages.get(1), "Lemon Slice", 0.25)); 
        toppings.add(new Topping(beverages.get(1), "Mint Leaves", 0.2)); 

        toppings.add(new Topping(beverages.get(2), "Chocolate Powder", 0.6)); 
        toppings.add(new Topping(beverages.get(2), "Cinnamon Powder", 0.3)); 
        toppings.add(new Topping(beverages.get(2), "Whipped Cream", 0.75)); 

        toppings.add(new Topping(beverages.get(3), "Cinnamon Powder", 0.3)); 
        toppings.add(new Topping(beverages.get(3), "Chocolate Shavings", 0.5)); 
        toppings.add(new Topping(beverages.get(3), "Whipped Cream", 0.75)); 

        toppings.add(new Topping(beverages.get(4), "Vanilla Extract", 0.4)); 
        toppings.add(new Topping(beverages.get(4), "Caramel Drizzle", 0.6)); 
        toppings.add(new Topping(beverages.get(4), "Whipped Cream", 0.75)); 
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Select beverage:");
        for (int i = 0; i < beverages.size(); i++) {
            System.out.println((i + 1) + ". " + beverages.get(i).getDescription());
        }
        int beverageChoice = scanner.nextInt();
        scanner.nextLine();

        Beverage beverage = beverages.get(beverageChoice - 1);

        List<Topping> selectedToppings = new ArrayList<>();
        while (true) {
            System.out.println();
            System.out.println("Add topping (Enter '0' to finish):");
            for (int i = 0, j = 1; i < toppings.size(); i++) {
                Topping topping = toppings.get(i);
                if (topping.getDescription().startsWith(beverage.getDescription())) {
                    System.out.println((j++) + ". " + topping.getDescription());
                }
            }
            
            int toppingChoice = scanner.nextInt();
            scanner.nextLine();
            if (toppingChoice == 0) {
                break;
            }
            if (toppingChoice > 0 && toppingChoice <= toppings.size()) {
                Topping selectedTopping = toppings.get(toppingChoice - 1);
                if (!selectedToppings.contains(selectedTopping)) {
                    selectedToppings.add(selectedTopping);
                } else {
                    System.out.println();;
                    System.out.println("This topping is already added. Please choose another topping.");
                    System.out.println();
                }
            } else {
                System.out.println("Invalid topping choice. Please try again.");
            }
        }

        double finalCost = beverage.cost();
        for (Topping topping : selectedToppings) {
            finalCost += topping.cost() - beverage.cost();
        }

        System.out.println();
        System.out.println("Final cost: $" + finalCost);

        scanner.close();
    }
}

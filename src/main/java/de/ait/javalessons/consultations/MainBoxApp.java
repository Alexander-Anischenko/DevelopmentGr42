package de.ait.javalessons.consultations;

public class MainBoxApp {
    public static void main(String[] args) {
        Box<String> stringBox = new Box<>("Hello");
        System.out.println(stringBox.getValue());
        stringBox.setValue("World");
        System.out.println(stringBox.getValue());

        Box<Integer>  intBox = new Box<>(10);
        System.out.println(intBox.getValue());
        intBox.setValue(20);
        System.out.println(intBox.getValue());

        Box<Boolean>   boolBox = new Box<>(true);
        System.out.println(boolBox.getValue());
        boolBox.setValue(false);
        System.out.println(boolBox.getValue());
    }
}

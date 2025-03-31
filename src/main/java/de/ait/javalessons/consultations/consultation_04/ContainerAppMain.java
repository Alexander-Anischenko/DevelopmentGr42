package de.ait.javalessons.consultations.consultation_04;

import de.ait.homeWork.HomeWork_06.TVProgram;
import de.ait.javalessons.consultations.ContainerApp;

import java.util.ArrayList;
import java.util.List;

public class ContainerAppMain {
    public static void main(String[] args) {
        ContainerApp<String> stringContainerApp = new ContainerApp<>();
        stringContainerApp.add("Hello");
        stringContainerApp.add("World");
        System.out.println(stringContainerApp.get(0));
        System.out.println(stringContainerApp.get(1));

        ContainerApp<Integer> integerContainerApp = new ContainerApp<>();
        integerContainerApp.add(10);
        integerContainerApp.add(20);
        System.out.println(integerContainerApp.get(0));
        System.out.println(integerContainerApp.get(1));

        ContainerApp<TVProgram> tvContainerApp = new ContainerApp<>();
        tvContainerApp.add(new TVProgram("TV-5", "Sport news", 100, true, 4));
        tvContainerApp.add(new TVProgram("TV-6", "Sport news", 100, true, 4));
        System.out.println(tvContainerApp.get(0));
        System.out.println(tvContainerApp.get(1));

        List<String> list = List.of("Hello", "World");
        System.out.println(list.get(0));
        System.out.println(list.get(1));
    }
}

package de.ait.javalessons.consultations;

import java.util.ArrayList;
import java.util.List;

public class ContainerApp<T> implements Container<T> {
    private  List<T> elements = new ArrayList<>();

    @Override
    public void add(T element) {
        elements.add(element);

    }

    @Override
    public T get(int index) {
        return elements.get(index);
    }
}

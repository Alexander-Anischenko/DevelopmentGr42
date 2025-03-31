package de.ait.javalessons.consultations;

public interface Container<T> {

    void add(T element);
    T get(int index);
}

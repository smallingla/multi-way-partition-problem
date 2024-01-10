package org.example;

import java.util.ArrayList;
import java.util.List;

public class Subset implements Comparable<Subset>{

    int sum;
    int index;
    List<Integer> elements;

    Subset(int index) {
        this.index = index;
        this.sum = 0;
        this.elements = new ArrayList<>();
    }

    void addElement(int element) {
        elements.add(element);
        sum += element;
    }

    public int compareTo(Subset other) {
        return Integer.compare(this.sum, other.sum);
    }

    @Override
    public String toString() {
        return "Subset{" +
                "sum=" + sum +
                ", index=" + index +
                ", elements=" + elements +
                '}';
    }
}

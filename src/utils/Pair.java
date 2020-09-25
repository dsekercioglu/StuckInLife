package utils;

import java.util.Objects;

public class Pair<T> implements Comparable {

    T T;
    Comparable C;

    public Pair(T o, Comparable c) {
        T = o;
        C = c;
    }

    public T getObject() {
        return T;
    }

    public Comparable getComparable() {
        return C;
    }

    @Override
    public int compareTo(Object o) {
        return C.compareTo(((Pair) o).C);
    }

    @Override
    public String toString() {
        return "Pair{" +
                "T=" + T +
                ", C=" + C +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pair<?> pair = (Pair<?>) o;
        return Objects.equals(T, pair.T) &&
                Objects.equals(C, pair.C);
    }

    @Override
    public int hashCode() {
        return Objects.hash(T, C);
    }
}

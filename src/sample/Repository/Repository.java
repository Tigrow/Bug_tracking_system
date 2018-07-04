package sample.Repository;

import java.util.List;

public interface Repository<T> {
    T add(T item);

    void add(Iterable<T> items);

    T update(T item);

    void remove(T item);

    void remove(String specification);

    List<T> query(String specification);
}

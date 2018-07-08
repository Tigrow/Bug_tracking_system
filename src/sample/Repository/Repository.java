package sample.Repository;

import java.util.List;

public interface Repository<T> {
    T add(T item);

    T update(T item);

    void remove(T item);

    List<T> query(String specification);
}

package sample.Repository;

import java.io.File;
import java.util.List;

public interface Repository<T> {
    void add(T item);

    void add(Iterable<T> items);

    void update(T item);

    void remove(T item);

    void remove(String specification);

    List<T> query(String specification);
}

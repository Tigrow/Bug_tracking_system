package sample.Repository;

import sample.Task;

import java.util.List;

public class TaskRepository implements Repository<Task> {
    @Override
    public Task add(Task item) {
        return null;
    }

    @Override
    public void add(Iterable<Task> items) {

    }

    @Override
    public Task update(Task item) {
        return null;
    }

    @Override
    public void remove(Task item) {

    }

    @Override
    public void remove(String specification) {

    }

    @Override
    public List<Task> query(String specification) {
        return null;
    }
}

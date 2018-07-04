package sample;

public class Project {
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Project(String name) {
        this.name = name;
    }

    public Project() {
    }

    public Project(int id, String name) {
        this.id = id;
        this.name = name;
    }
}

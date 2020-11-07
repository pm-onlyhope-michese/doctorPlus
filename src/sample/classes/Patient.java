package sample.classes;

public class Patient {
    private int id;
    private String first_name;
    private String last_name;

    public Patient(int id, String first_name, String last_name) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFull_name() {
        return first_name + ' ' + last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public String getFirst_name() {
        return first_name;
    }

    @Override
    public String toString() {
        return id + ") " + first_name + ' ' + last_name;
    }
}

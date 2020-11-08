package sample.classes;

public class Doctor {
    private int id;
    private String first_name;
    private String last_name;

    public Doctor(int id, String first_name, String last_name) {
        this.id = id;

        this.first_name = first_name;
        this.last_name = last_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public int getId() {
        return id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getFull_name() {
        return first_name + ' ' + last_name;
    }
}

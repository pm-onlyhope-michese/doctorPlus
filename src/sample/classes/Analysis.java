package sample.classes;

public class Analysis {
    private int id;
    private String title;
    private String description;
    private String date;
    private String typeA;

    public String getDate() {
        return date;
    }

    public String getTypeA() {
        return this.typeA;
    }

    private String result;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public int getId() {
        return id;
    }

    public Analysis(int id, String title, String description, String date, String typeA, String result) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.result = result;
        this.typeA = typeA;
        this.date = date;
    }
}

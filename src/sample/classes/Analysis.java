package sample.classes;

public class Analysis {
    private int id;
    private String title;
    private String description;
    private String date;
    private String type;
    private String result;

    public Analysis(int id, String title, String description, String date, String type, String result) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.result = result;
        this.type = type;
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return this.type;
    }

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
}

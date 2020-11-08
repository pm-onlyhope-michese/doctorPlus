package sample.classes;

public class AnalysisNameList {
    private int id;
    private String title;
    private String type;

    public AnalysisNameList(int id, String title, String type) {
        this.id = id;
        this.title = title;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return title;
    }
}

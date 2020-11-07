package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.AnalysisNameList;
import sample.classes.Doctor;
import sample.classes.Patient;
import sample.model.baseModel.DocumentationExcel;
import sample.model.baseModel.DocumentationWord;
import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelPrintDocumentation extends Model {
    private final String pathFullStatisticWord;
    private final String pathFullStatisticExcel;
    private final String pathAnalyzesWord;
    private final String pathAnalyzesExcel;
    private final String pathPatientsWord;
    private final String pathPatientsExcel;

    public ModelPrintDocumentation() {
        this.pathFullStatisticWord = "fullStatistic/word/";
        this.pathFullStatisticExcel = "fullStatistic/excel/";
        this.pathAnalyzesWord = "analyzes/word/";
        this.pathAnalyzesExcel = "analyzes/excel/";
        this.pathPatientsWord = "patients/word/";
        this.pathPatientsExcel = "patients/excel/";
    }

    public ObservableList<Patient> getAllPatientsByDoctorId(int doctor_id) {
        ObservableList<Patient> result = FXCollections.observableArrayList();
        String sql = "select patient_id, first_name, last_name from doctors_patients " +
                "inner join patients using(patient_id) " +
                "where doctor_id = " + doctor_id;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                result.add(new Patient(resultSet.getInt("patient_id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    public ObservableList<AnalysisNameList> getAllAnalysisNames() {
        ObservableList<AnalysisNameList> result = FXCollections.observableArrayList();
        String sql = "select `analysis_namelist_id`, `title`, `type` from analysis_namelist";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                result.add(new AnalysisNameList(resultSet.getInt("analysis_namelist_id"),
                        resultSet.getString("title"), resultSet.getString("type")));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    public void printAnalyzes(Patient patient) {
        List<ArrayList<String>> table = getAllAnalyzesByPatientId(patient.getId());
        DocumentationWord documentationWord = new DocumentationWord();
        documentationWord.createH1("Анализы пациента");
        documentationWord.createParagraph("Пациент: " + patient.getFull_name());
        documentationWord.createParagraph("Всего анализов: " + (table.size() - 1));
        documentationWord.createTable(table);
        documentationWord.output(pathPatientsWord + "Пациент. " + patient.getFull_name());
        documentationWord.closeDocumentation();

        DocumentationExcel document = new DocumentationExcel(patient.getFull_name());
        document.renderTable(table);
        document.output(pathPatientsExcel + "Пациент. " + patient.getFull_name());
        document.closeDocumentation();
    }

    public void printPatientsByAnalysis(int analysisId) {
        List<String> descriptions = getDescriptionsAnalysisByAnalysisNameListId(analysisId);
        String title = descriptions.get(0);
        String description = descriptions.get(1);
        String type = descriptions.get(2);
        int count = getCountAllAnalysisByAnalysisNameListId(analysisId);

        DocumentationWord documentationWord = new DocumentationWord();
        documentationWord.createH1("Статистика сдачи анализа пациентами клиники");
        documentationWord.createParagraph("Анализ: " + title);

        if (!description.equals("")) {
            documentationWord.createParagraph("Описание: " + description);
        }

        documentationWord.createParagraph("Всего анализов: " + (count - 1));

        if (type.equals("string")) {

            List<String> resultAndCountAnalysis = getResultAndCountAnalysisByGroupResultAndByAnalysisNameListId(analysisId);
            documentationWord.createParagraph(resultAndCountAnalysis.get(0) +
                    ": " + resultAndCountAnalysis.get(1));

        } else if (type.equals("integer")) {
            documentationWord.createParagraph("Средний результат: " +
                    getAvgAnalysisResultByGroupResultAndByAnalysisNameListId(analysisId));
        }
        List<ArrayList<String>> table = getAllAnalyzesByAnalysisId(analysisId);
        documentationWord.createTable(table);
        documentationWord.output(pathAnalyzesWord + "Анализ. " + title);
        documentationWord.closeDocumentation();

        DocumentationExcel document = new DocumentationExcel(title);
        document.renderTable(table);
        document.output(pathAnalyzesExcel + "Анализ. " + title);
        document.closeDocumentation();
    }

    public void printFullStatistic(Doctor doctor) {
        DocumentationWord documentationWord = new DocumentationWord();
        documentationWord.createH1("Статистические данные клиники");
        documentationWord.createParagraph("Доктор: " + doctor.getFull_name());
        documentationWord.createParagraph("");

        documentationWord.createParagraph("Всего пациентов: " + countAllPatients());
        documentationWord.createParagraph("Всего пациентов у вас: "
                + countAllPatientsByDoctor(doctor.getId()));
        documentationWord.createParagraph("");

        documentationWord.createParagraph("Всего анализов: " + countRowAllAnalyzes());
        documentationWord.createParagraph("Всего анализов сдали ваши пациенты: "
                + countRowsAllAnalyzesByDoctor(doctor.getId()));
        documentationWord.createParagraph("");

        List<ArrayList<String>> allAnalysis = countRowsAnalysis();
        List<ArrayList<String>> allAnalysisByDoctor = countRowsAnalysisByDoctor(doctor.getId());

        documentationWord.createH2("Общая статистика сдачи анализов по больнице");
        documentationWord.createTable(allAnalysis);
        documentationWord.createParagraph("");

        documentationWord.createH2("Статистика сдачи анализов вашими пациентами");
        documentationWord.createTable(allAnalysisByDoctor);
        documentationWord.createParagraph("");
        documentationWord.output(pathFullStatisticWord + "Полная статистика. " + doctor.getFull_name());
        documentationWord.closeDocumentation();

        DocumentationExcel document = new DocumentationExcel("Все анализы");
        document.renderTable(allAnalysis);
        document.addSheet(doctor.getFull_name());
        document.renderTable(allAnalysisByDoctor);
        document.output(pathFullStatisticExcel + "Полная статистика. " + doctor.getFull_name());
        document.closeDocumentation();
    }

    private List<ArrayList<String>> getAllAnalyzesByPatientId(int patient_id) {
        List<ArrayList<String>> result = new ArrayList<>();

        ArrayList<String> title = new ArrayList<>();
        title.add("Id");
        title.add("Название");
        title.add("Дата");
        title.add("Результат");
        result.add(title);

        String sql = "select analysis_id, updatedate_at, `result`, title, description, `type` from analysis\n" +
                "inner join analysis_namelist using(analysis_namelist_id)\n" +
                "where patient_id = " + patient_id;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(Integer.toString(resultSet.getInt("analysis_id")));
                row.add(resultSet.getString("title"));
                row.add(resultSet.getString("updatedate_at"));
                row.add(resultSet.getString("result"));
                result.add(row);
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private List<ArrayList<String>> getAllAnalyzesByAnalysisId(int analysisId) {
        List<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> rowTitle = new ArrayList<>();
        rowTitle.add("Id");
        rowTitle.add("Имя");
        rowTitle.add("Фамилия");
        rowTitle.add("Результат");
        result.add(rowTitle);

        String sql = "select first_name, last_name, analysis_id, `result` from analysis\n" +
                "inner join patients using(patient_id)\n" +
                "where analysis_namelist_id = " + analysisId + " " +
                "order by analysis_id";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(Integer.toString(resultSet.getInt("analysis_id")));
                row.add(resultSet.getString("first_name"));
                row.add(resultSet.getString("last_name"));
                row.add(resultSet.getString("result"));
                result.add(row);
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private List<String> getDescriptionsAnalysisByAnalysisNameListId(int analysisNameListId) {
        List<String> result = new ArrayList<>();
        String sql = "select title, description, `type` from analysis_namelist where analysis_namelist_id = " + analysisNameListId;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result.add(resultSet.getString("title"));
                result.add(resultSet.getString("description"));
                result.add(resultSet.getString("type"));
            }
            resultSet.close();
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private int getCountAllAnalysisByAnalysisNameListId(int analysisNameListId) {
        int result = 0;
        String sql = "select count(*) as count from analysis\n" +
                "where analysis_namelist_id = " + analysisNameListId;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private List<String> getResultAndCountAnalysisByGroupResultAndByAnalysisNameListId(int analysisNameListId) {
        List<String> result = new ArrayList<>();
        String sql = "select `result`, count(*) as count from analysis\n" +
                "where analysis_namelist_id = " + analysisNameListId + " " +
                "group by `result`";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result.add(resultSet.getString("result"));
                result.add(Integer.toString(resultSet.getInt("count")));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private double getAvgAnalysisResultByGroupResultAndByAnalysisNameListId(int analysisNameListId) {
        double result = 0.0;
        String sql = "select avg(result) as avg from analysis\n" +
                "where analysis_namelist_id = " + analysisNameListId + "\n" +
                "group by `result`";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = Double.parseDouble(resultSet.getString("avg"));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private List<ArrayList<String>> countRowsAnalysis() {
        List<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        title.add("Название анализа");
        title.add("Количество сданных анализов");
        result.add(title);
        String sql = "select `title`, count(*) as count from analysis_namelist " +
                "left join analysis using(analysis_namelist_id) " +
                "group by `title` " +
                "order by analysis_namelist_id";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(resultSet.getString("title"));
                row.add(Integer.toString(resultSet.getInt("count")));
                result.add(row);
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private List<ArrayList<String>> countRowsAnalysisByDoctor(int doctorId) {
        List<ArrayList<String>> result = new ArrayList<>();
        ArrayList<String> title = new ArrayList<>();
        title.add("Название анализа");
        title.add("Количество сданных анализов");
        result.add(title);
        String sql = "select `title`, count(*) as count from analysis_namelist\n" +
                "left join analysis using(analysis_namelist_id)\n" +
                "left join doctors_patients using(patient_id)\n" +
                "where doctor_id = " + doctorId + "\n" +
                "group by `title`\n" +
                "order by analysis_namelist_id";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                ArrayList<String> row = new ArrayList<>();
                row.add(resultSet.getString("title"));
                row.add(Integer.toString(resultSet.getInt("count")));
                result.add(row);
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private int countRowAllAnalyzes() {
        int result = 0;
        String sql = "select count(*) as count from analysis";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private int countRowsAllAnalyzesByDoctor(int doctorId) {
        int result = 0;

        String sql = "select count(*) as count from analysis " +
                "inner join doctors_patients using(patient_id) " +
                "where doctor_id = " + doctorId;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private int countAllPatients() {
        int result = 0;

        String sql = "select count(*) as count from patients";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }

    private int countAllPatientsByDoctor(int doctorId) {
        int result = 0;

        String sql = "select count(*) as count from doctors_patients " +
                "where doctor_id = " + doctorId;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            if (resultSet.next()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return result;
    }
}

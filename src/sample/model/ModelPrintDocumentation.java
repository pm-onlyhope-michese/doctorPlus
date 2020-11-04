package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.Analysis;
import sample.classes.AnalysisNameList;
import sample.classes.Doctor;
import sample.classes.Patient;
import sample.model.baseModel.Documentation;
import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ModelPrintDocumentation extends Model {

    public ObservableList<Patient> getAllPatientsByDoctorId(int doctor_id) throws SQLException {
        ObservableList<Patient> patients = FXCollections.observableArrayList();
        String sql = "select patient_id, first_name, last_name from doctors_patients " +
                "inner join patients using(patient_id) " +
                "where doctor_id = " + doctor_id;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                patients.add(new Patient(resultSet.getInt("patient_id"),
                        resultSet.getString("first_name"), resultSet.getString("last_name")));
            }
        } catch (SQLException exp) {
            System.out.println(exp.getMessage());
        } finally {
            db.close();
        }

        return patients;
    }

    public ObservableList<AnalysisNameList> getAllAnalysisNames() throws SQLException {
        ObservableList<AnalysisNameList> analysisNames = FXCollections.observableArrayList();
        String sql = "select `analysis_namelist_id`, `title`, `type` from analysis_namelist";

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                analysisNames.add(new AnalysisNameList(resultSet.getInt("analysis_namelist_id"),
                        resultSet.getString("title"), resultSet.getString("type")));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return analysisNames;
    }

    public ObservableList<Analysis> getAllAnalyzesByPatientId(int patient_id) throws SQLException {
        ObservableList<Analysis> analyses = FXCollections.observableArrayList();
        String sql = "select analysis_id, updatedate_at, `result`, title, description, `type` from analysis\n" +
                "inner join analysis_namelist using(analysis_namelist_id)\n" +
                "where patient_id = " + patient_id;

        try {
            ResultSet resultSet = db.executeQuery(sql);
            while (resultSet.next()) {
                analyses.add(new Analysis(resultSet.getInt("analysis_id"),
                        resultSet.getString("title"), resultSet.getString("description"),
                        resultSet.getString("updatedate_at"), resultSet.getString("type"),
                        resultSet.getString("result")));
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return analyses;
    }

    public boolean printAnalyzes(Patient patient) {
        boolean result = true;

        try {
            ObservableList<Analysis> analyses = getAllAnalyzesByPatientId(patient.getId());

            List<ArrayList<String>> table = new ArrayList<>();
            ArrayList<String> rowTitle = new ArrayList<>();
            rowTitle.add("Id");
            rowTitle.add("Название");
            rowTitle.add("Дата");
            rowTitle.add("Результат");
            table.add(rowTitle);

            for (int counter = 0; counter < analyses.size(); counter++) {
                ArrayList<String> row = new ArrayList<>();
                row.add(Integer.toString(analyses.get(counter).getId()));
                row.add(analyses.get(counter).getTitle());
                row.add(analyses.get(counter).getDate());
                row.add(analyses.get(counter).getResult());
                table.add(row);
            }

            Documentation documentation = Documentation.getDocumentation();
            documentation.createH1("Анализы");
            documentation.createParagraph("Пациент: " + patient.getFull_name());
            documentation.createParagraph("Всего анализов: " + (table.size() - 1));
            documentation.createTable(4, table);
            documentation.output("Пациент. " + patient.getFull_name());
            documentation.closeDocumentation();
        } catch (SQLException throwables) {
            result = false;
            System.err.println(throwables.getMessage());
        }

        return result;
    }

    private List<ArrayList<String>> getAllAnalyzesByAnalysisId(int analysisId) throws SQLException {
        List<ArrayList<String>> arrayLists = new ArrayList<>();
        ArrayList<String> rowTitle = new ArrayList<>();
        rowTitle.add("Id");
        rowTitle.add("Имя");
        rowTitle.add("Фамилия");
        rowTitle.add("Результат");
        arrayLists.add(rowTitle);

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
                arrayLists.add(row);
            }
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        } finally {
            db.close();
        }

        return arrayLists;
    }

    public boolean printPatientsByAnalysis(int analysisId) {
        boolean result = true;
        try {
            String title = null;
            String description = null;
            String type = null;
            int count = 0;

            try {
                String sql = "select title, description, `type` from analysis_namelist where analysis_namelist_id = " + analysisId;
                ResultSet resultSet = db.executeQuery(sql);
                if (resultSet.next()) {
                    title = resultSet.getString("title");
                    description = resultSet.getString("description");
                    type = resultSet.getString("type");
                }
                resultSet.close();

                sql = "select count(*) as count from analysis\n" +
                        "where analysis_namelist_id = " + analysisId;
                resultSet = db.executeQuery(sql);
                if (resultSet.next()) {
                    count = resultSet.getInt("count");
                }

            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            } finally {
                db.close();
            }

            Documentation documentation = Documentation.getDocumentation();
            documentation.createH1("Пациенты");
            documentation.createParagraph("Анализ: " + title);

            if (!description.equals("")) {
                documentation.createParagraph("Описание: " + description);
            }
            documentation.createParagraph("Всего анализов: " + (count - 1));
            try {
                String sql = null;
                if (type.equals("string")) {
                    sql = "select `result`, count(*) as count from analysis\n" +
                            "where analysis_namelist_id = " + analysisId + " " +
                            "group by `result`";
                    ResultSet resultSet = db.executeQuery(sql);
                    if (resultSet.next()) {
                        documentation.createParagraph(resultSet.getString("result") +
                                ": " + resultSet.getString("count"));
                    }
                } else if (type.equals("integer")) {
                    sql = "select avg(result) as avg from analysis\n" +
                            "where analysis_namelist_id = " + analysisId + " " +
                            "group by `result`";
                    ResultSet resultSet = db.executeQuery(sql);
                    if (resultSet.next()) {
                        documentation.createParagraph("Средний результат: " + resultSet.getString("avg"));
                    }
                }
            } catch (SQLException throwables) {
                System.err.println(throwables.getMessage());
            } finally {
                db.close();
            }
            documentation.createTable(4, getAllAnalyzesByAnalysisId(analysisId));
            documentation.output("Анализ. " + title);
            documentation.closeDocumentation();
        } catch (SQLException throwables) {
            result = false;
            System.err.println(throwables.getMessage());
        }
        return result;
    }

    private List<ArrayList<String>> countRowsAnalysis() throws SQLException {
        List<ArrayList<String>> result = new ArrayList<>();
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

    private List<ArrayList<String>> countRowsAnalysisByDoctor(int doctorId) throws SQLException {
        List<ArrayList<String>> result = new ArrayList<>();
        String sql = "select `title`, count(*) as count from analysis_namelist\n" +
                "left join analysis using(analysis_namelist_id)\n" +
                "left join doctors_patients using(patient_id)\n" +
                "where doctor_id = " + doctorId + "\n" +
                "group by `title`\n" +
                "order by analysis_namelist_id";

        System.out.println(sql);
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

    private int countRowAllAnalyzes() throws SQLException {
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

    private int countRowsAllAnalyzesByDoctor(int doctorId) throws SQLException {
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

    private int countAllPatients() throws SQLException {
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

    private int countAllPatientsByDoctor(int doctorId) throws SQLException {
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

    public boolean printFullStatistic(Doctor doctor) {
        boolean result = true;

        Documentation documentation = Documentation.getDocumentation();
        documentation.createH1("Полная статистика");
        documentation.createParagraph("Доктор: " + doctor.getFull_name());
        documentation.createParagraph("");
        try {
            documentation.createParagraph("Всего пациентов: " + countAllPatients());
            documentation.createParagraph("Всего пациентов у вас: "
                    + countAllPatientsByDoctor(doctor.getId()));
            documentation.createParagraph("");

            documentation.createParagraph("Всего анализов: " + countRowAllAnalyzes());
            documentation.createParagraph("Всего анализов сдали ваши пациенты: "
                    + countRowsAllAnalyzesByDoctor(doctor.getId()));
            documentation.createParagraph("");

            List<ArrayList<String>> allAnalysis = countRowsAnalysis();
            List<ArrayList<String>> allAnalysisByDoctor = countRowsAnalysisByDoctor(doctor.getId());

            documentation.createH2("Всего");
            for (int counter = 0; counter < allAnalysis.size(); counter++) {
                documentation.createParagraph(allAnalysis.get(counter).get(0) + ": " + allAnalysis.get(counter).get(1));
            }
            documentation.createParagraph("");

            documentation.createH2("Ваши пациенты");
            for (int counter = 0; counter < allAnalysisByDoctor.size(); counter++) {
                documentation.createParagraph(allAnalysisByDoctor.get(counter).get(0) + ": " + allAnalysisByDoctor.get(counter).get(1));
            }
            documentation.createParagraph("");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            result = false;
        }
        documentation.output("Полная статистика. " + doctor.getFull_name());
        documentation.closeDocumentation();

        return result;
    }
}

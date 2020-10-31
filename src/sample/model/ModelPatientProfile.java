package sample.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import sample.classes.Analysis;
import sample.classes.AnalysisNameList;
import sample.model.baseModel.Model;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ModelPatientProfile extends Model {
    public boolean changeNamePatient(int patient_id, String firstName, String lastName) {
        boolean result = false;
        String sql = "update patients set first_name = '" + firstName + "', " +
                "last_name = '" + lastName + "' " +
                "where patient_id = " + patient_id;

        try {
            result = db.executeUpdate(sql);
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        }

        return result;
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
        }finally {
            db.close();
        }

        return analyses;
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

    public boolean addAnalysisToPatient(int patient_id, int analysisNameListId, String analysisResult) {
        boolean result = false;
        String sql = "insert into analysis (patient_id, analysis_namelist_id, result) values " +
                "(" + patient_id + ", " + analysisNameListId + ", '" + analysisResult + "')";

        try {
            result = db.executeUpdate(sql);
        } catch (SQLException exp) {
            System.err.println(exp.getMessage());
        }

        return result;
    }

    public boolean deleteAnalysisByAnalysisId(int analysis_id) {
        boolean result = false;
        String sql = "delete from analysis where analysis_id = " + analysis_id;

        try {
            result = db.executeUpdate(sql);
        } catch (SQLException exp) {
            System.err.println(exp);
        }

        return result;
    }
}

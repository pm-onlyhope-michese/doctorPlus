package sample.config;

import java.util.HashMap;

public class Config {
    public static final String urlDB = "jdbc:mysql://localhost:3306/hospital?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
    public static final String userNameDB = "root";
    public static final String passwordDB = "1234";
    public static final String pathDoctorAuthorization = "view/doctorAuthorization.fxml";
    public static final String pathDoctorRegistration = "view/doctorRegistration.fxml";
    public static final String pathPatientProfile = "view/patientProfile.fxml";
    public static final String pathPatientRegistration = "view/patientRegistration.fxml";
    public static final String pathPatientsTable = "view/patientsTable.fxml";
    public static final Integer widthApp = 731;
    public static final Integer heightApp = 456;

    public static HashMap<String, String> paths = new HashMap();
    public static void initialize() {
        paths.put(pathDoctorAuthorization, "Авторизация");
        paths.put(pathDoctorRegistration, "Регистрация");
        paths.put(pathPatientProfile, "Профиль пациента");
        paths.put(pathPatientRegistration, "Добавление нового пациента");
        paths.put(pathPatientsTable, "Пациенты");
    }
}

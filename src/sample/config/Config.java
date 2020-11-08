package sample.config;

import java.util.HashMap;

public class Config {
    public final String urlDB;
    public final String userNameDB;
    public final String passwordDB;
    public final String pathDoctorAuthorization;
    public final String pathDoctorRegistration;
    public final String pathPatientProfile;
    public final String pathPatientRegistration;
    public final String pathPatientsTable;
    public final String pathPrintDocumentation;
    public final Integer widthApp;
    public final Integer heightApp;

    private final HashMap<String, String> paths;

    public Config() {
        urlDB = "jdbc:mysql://localhost:3306/hospital?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        userNameDB = "pm-onlyhope-michese";
        passwordDB = "1234";
        pathDoctorAuthorization = "view/doctorAuthorization.fxml";
        pathDoctorRegistration = "view/doctorRegistration.fxml";
        pathPatientProfile = "view/patientProfile.fxml";
        pathPatientRegistration = "view/patientRegistration.fxml";
        pathPatientsTable = "view/patientsTable.fxml";
        pathPrintDocumentation = "view/printDocumentation.fxml";
        widthApp = 731;
        heightApp = 456;

        paths = new HashMap<>();
        paths.put(pathDoctorAuthorization, "Авторизация");
        paths.put(pathDoctorRegistration, "Регистрация");
        paths.put(pathPatientProfile, "Профиль пациента");
        paths.put(pathPatientRegistration, "Добавление нового пациента");
        paths.put(pathPatientsTable, "Пациенты");
        paths.put(pathPrintDocumentation, "Печать");
    }

    public String getPath(String key) {
        return paths.get(key);
    }
}

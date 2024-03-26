package main.Controllers.CRUDEmployees;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.Entities.Companies;
import main.Models.Entities.Employees;
import main.Models.Entities.Researches;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;

public class UpdateEmployees {
    public TextField textfieldEmployeeName;
    public TextField textfieldEmployeeSurname;
    public TextField textfieldEmployeeSalary;
    public TextField textfieldEmployeeNumber;

    public Button buttonSave;

    public Label labelNameError;
    public Label labelSurnameError;
    public Label labelSalaryError;
    public Label labelNumberError;

    public Button buttonBack;
    private Researches researches;
    private Companies companies;
    private Employees employees;

    public void setEmployees(Employees employees, Researches researches, Companies companies) {
        this.companies = companies;
        this.researches = researches;
        this.employees = employees;
        textfieldEmployeeName.setText(employees.getEmployeeName());
        textfieldEmployeeSurname.setText(employees.getEmployeeSurname());
        textfieldEmployeeSalary.setText(String.valueOf(employees.getEmployeeSalary()));
        textfieldEmployeeNumber.setText(employees.getPhoneNumber());
    }

    public void Save_Pressed(ActionEvent actionEvent) throws IOException {
        if (textfieldEmployeeName.getText().isEmpty()){
            labelNameError.setVisible(true);
        } else {
            labelNameError.setVisible(false);
        }
        if (textfieldEmployeeSurname.getText().isEmpty()){
            labelSurnameError.setVisible(true);
        } else {
            labelSurnameError.setVisible(false);
        }
        if (textfieldEmployeeSalary.getText().isEmpty() || textfieldEmployeeSalary.getText().matches("^\\D*$")){
            labelSalaryError.setVisible(true);
        } else {
            labelSalaryError.setVisible(false);
        }
        if (textfieldEmployeeNumber.getText().isEmpty()){
            labelNumberError.setVisible(true);
        } else {
            labelNumberError.setVisible(false);
        }
        if (labelNameError.isVisible() || labelSurnameError.isVisible() || labelSalaryError.isVisible()|| labelNumberError.isVisible()){
            return;
        }

        employees.setEmployeeName((textfieldEmployeeName.getText()));
        employees.setEmployeeSurname((textfieldEmployeeSurname.getText()));
        employees.setEmployeeSalary(Integer.valueOf(textfieldEmployeeSalary.getText()));
        employees.setPhoneNumber((textfieldEmployeeNumber.getText()));
        employees.setResearch(researches);

        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(employees));
        requestModel.setRequestType(RequestType.UPDATEEMPLOYEES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Cорудник успешно редактирован!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readEmployees.fxml"));
            Parent root = loader.load();
            ReadEmployees readEmployees = loader.getController();
            readEmployees.setResearches(researches, companies);
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
        }
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readEmployees.fxml"));
        Parent root = loader.load();
        ReadEmployees readEmployees = loader.getController();
        readEmployees.setResearches(researches, companies);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

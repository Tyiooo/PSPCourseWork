package main.Controllers.CRUDCompanies;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Enums.*;
import main.Models.Entities.Companies;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;

public class UpdateCompanies {
    public TextField textfieldCompanyName;
    public TextField textfieldCompanyAddress;

    public Button buttonBack;
    public Button buttonSave;
    public Label labelNameError;
    public Label labelAddressError;
    private Companies companies;

    public void setCompanies(Companies companies) {
        this.companies = companies;
        textfieldCompanyName.setText(companies.getCompanyName());
        textfieldCompanyAddress.setText(companies.getCompanyAdress());
    }


    public void Save_Pressed(ActionEvent actionEvent) throws IOException {
        if (textfieldCompanyName.getText().isEmpty()){
            labelNameError.setVisible(true);
        } else {
            labelNameError.setVisible(false);
        }
        if (textfieldCompanyAddress.getText().isEmpty()){
            labelAddressError.setVisible(true);
        } else {
            labelAddressError.setVisible(false);
        }
        if (labelNameError.isVisible() || labelAddressError.isVisible()){
            return;
        }

        companies.setCompanyName(textfieldCompanyName.getText());
        companies.setCompanyAdress(textfieldCompanyAddress.getText());
        companies.setUser(ClientSocket.getInstance().getUser());

        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(companies));
        requestModel.setRequestType(RequestType.UPDATECOMPANIES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Компания успешно редактирована!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });

            Stage stage = (Stage) buttonSave.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readCompanies.fxml"));
            Parent root = loader.load();
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка!!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
        }
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readCompanies.fxml"));
        Parent root = loader.load();
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

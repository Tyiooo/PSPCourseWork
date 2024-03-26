package main.Controllers.CRUDCompanies;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Models.Entities.Companies;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Enums.Roles;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;

public class CreateCompanies {
    public TextField textfieldCompanyName;
    public TextField textfieldCompanyAddress;
    public Button buttonCreate;
    public Label labelNameError;
    public Label labelAddressError;
    public Button buttonBack;

    public void Create_Pressed(ActionEvent actionEvent) throws IOException {
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

        Companies companies = new Companies();
        companies.setCompanyName((textfieldCompanyName.getText()));
        companies.setCompanyAdress((textfieldCompanyAddress.getText()));
        companies.setUser((ClientSocket.getInstance().getUser()));

        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(companies));
        requestModel.setRequestType(RequestType.CREATECOMPANIES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Компания успешно добавлена!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/readCompanies.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Компания с таким названием у вас уже есть!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
        }
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/readCompanies.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

package main.Controllers.CRUDUsers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;

public class CreateUsers {
    public TextField textfieldUserName;
    public TextField textfieldUserLogin;

    public TextField textfieldUserPassword;

    public Button buttonBack;

    public Button buttonCreate;
    public Label labelNameError;
    public Label labelLoginError;
    public Label labelPasswordError;

    public void Create_Pressed(ActionEvent actionEvent) throws IOException {
        if (textfieldUserName.getText().isEmpty()){
            labelNameError.setVisible(true);
        } else {
            labelNameError.setVisible(false);
        }
        if (textfieldUserLogin.getText().isEmpty()){
            labelLoginError.setVisible(true);
        } else {
            labelLoginError.setVisible(false);
        }
        if (textfieldUserPassword.getText().isEmpty()){
            labelPasswordError.setVisible(true);
        } else {
            labelPasswordError.setVisible(false);
        }
        if (labelNameError.isVisible() || labelLoginError.isVisible()|| labelPasswordError.isVisible()){
            return;
        }

        User users = new User();
        users.setName(textfieldUserName.getText());
        users.setPassword(textfieldUserPassword.getText());
        users.setLogin(textfieldUserLogin.getText());

        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(users));
        requestModel.setRequestType(RequestType.CREATEUSERS);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Пользователь успешно добавлен!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/readUsers.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Пользователь с таким логином у вас уже есть!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
        }
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/readUsers.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

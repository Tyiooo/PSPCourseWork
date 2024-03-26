package main.Controllers;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import main.Enums.*;
import main.Models.Entities.Companies;
import main.Models.Entities.PersonData;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;
import java.util.Set;
public class MenuAdmin {
    public Button buttonWatchCompanies;
    public Button buttonBack;
    public Button buttonWatchUsers;

    public void Users_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonWatchUsers.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/readUsers.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    public void Companies_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonWatchCompanies.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/readCompanies.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/login.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

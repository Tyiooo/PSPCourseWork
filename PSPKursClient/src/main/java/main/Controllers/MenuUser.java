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
public class MenuUser {
    public Button buttonWatchCompanies;
    public Button buttonWatchSex;

    public Button buttonBack;
    public Button buttonBarChart;
    public Button buttonSalary;
    public void Sex_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonWatchSex.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/sexDiagram.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    public void BarChart_Pressed(ActionEvent actionEvent) throws IOException{
        Stage stage = (Stage) buttonBarChart.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/priceDiagram.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    public void Price_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonSalary.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/salaryDiagram.fxml"));
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

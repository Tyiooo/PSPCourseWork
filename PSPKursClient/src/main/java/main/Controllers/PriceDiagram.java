package main.Controllers;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Models.Entities.Companies;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.Entities.PersonData;
import main.Models.Entities.Researches;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;


import java.io.IOException;
import java.util.List;

public class PriceDiagram {
    public Button buttonCreate;
    private List<Researches> researches;
    public Button buttonBack;

    public CategoryAxis xAxis;
    public NumberAxis yAxis;
    @FXML
    private BarChart<String, Number> barChart;;

    public void initialize() throws IOException {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Исследования");
        yAxis.setLabel("Цена");
        XYChart.Series<String, Number> series = new XYChart.Series<>();
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(ClientSocket.getInstance().getUser()));
        requestModel.setRequestType(RequestType.PRICEDIAGRAM);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);

        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            researches = new Gson().fromJson(responseModel.getResponseData(), new TypeToken<List<Researches>>() {}.getType());
            for(Researches research : researches) {
                series.getData().add(new XYChart.Data<>(research.getResearchName(), research.getResearchCost()));
//                System.out.println(research.getResearchName());
//                System.out.println(research.getResearchCost());
            }
            barChart.getData().add(series);
        }
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        User user = ClientSocket.getInstance().getUser();
        if(user.getRole().equals("Admin")){
            Stage stage = (Stage) buttonBack.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/menuadmin.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
        else {
            Stage stage = (Stage) buttonBack.getScene().getWindow();
            Parent root;
            root = FXMLLoader.load(getClass().getResource("/menuUser.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
    }

}

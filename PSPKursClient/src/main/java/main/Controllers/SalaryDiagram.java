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
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.Models.Entities.*;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;


import java.io.IOException;
import java.util.List;

public class SalaryDiagram {
    private List<Employees> employees;
    public Button buttonBack;

    @FXML
    private PieChart pieChart;;

    public void initialize() throws IOException {
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(ClientSocket.getInstance().getUser()));
        requestModel.setRequestType(RequestType.SALARYDIAGRAM);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            employees = new Gson().fromJson(responseModel.getResponseData(), new TypeToken<List<Employees>>() {}.getType());
//            for(PersonData personData1 : personData) {
//                System.out.println(personData1.getMail());
//                if(personData1.getSex().equals("М"))
//                    counterMens++;
//                else
//                    counterWomens++;
//            }
            ObservableList<PieChart.Data> employeeslist = FXCollections.observableArrayList();
            for(Employees employee : employees){
                employeeslist.add(new PieChart.Data(employee.getEmployeeName(), employee.getEmployeeSalary()));
                System.out.println(employee.getEmployeeName() + employee.getEmployeeSalary());
            }
            pieChart.setData(employeeslist);
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

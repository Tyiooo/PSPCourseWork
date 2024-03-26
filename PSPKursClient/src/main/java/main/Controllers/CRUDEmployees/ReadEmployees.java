package main.Controllers.CRUDEmployees;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.stage.Stage;
import main.Controllers.CRUDResearches.ReadResearches;
import main.Controllers.CRUDResearches.UpdateResearches;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.Entities.Companies;
import main.Models.Entities.Employees;
import main.Models.Entities.Researches;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;
import java.util.List;

public class ReadEmployees {
    public Button buttonCreate;
    private List<Employees> employees;

    public Button buttonBack;

    public Button buttonPriceDiagram;
    @FXML
    private TableView<Employees> tableView;

    @FXML
    private TableColumn<Employees, Integer> id;

    @FXML
    private TableColumn<Employees, String> name;

    @FXML
    private TableColumn<Employees, String> surname;

    @FXML
    private TableColumn<Employees, Integer> salary;
    @FXML
    private TableColumn<Employees, String> phoneNumber;

    private Companies companies;
    private Researches researches;
    public void setResearches(Researches researches, Companies companies) throws IOException {
        this.companies = companies;
        this.researches = researches;
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(researches));
        requestModel.setRequestType(RequestType.READEMPLOYEES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            employees = new Gson().fromJson(responseModel.getResponseData(), new TypeToken<List<Employees>>() {}.getType());
            ObservableList<Employees> employeesList = FXCollections.observableList(employees);
            tableView.setItems(employeesList);
        }
    }
    public void initialize() throws IOException {
        id.setCellValueFactory(new PropertyValueFactory<>("Id_employee"));
        name.setCellValueFactory(new PropertyValueFactory<>("EmployeeName"));
        surname.setCellValueFactory(new PropertyValueFactory<>("EmployeeSurname"));
        salary.setCellValueFactory(new PropertyValueFactory<>("EmployeeSalary"));
        phoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));

        ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Удалить");
        cm.getItems().add(delete);
        MenuItem update = new MenuItem("Редактировать");
        cm.getItems().add(update);

        ContextMenu cm2 = new ContextMenu();
        MenuItem create = new MenuItem("Создать");
        cm2.getItems().add(create);


        tableView.setRowFactory( tv -> {
            TableRow<Employees> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getButton() == MouseButton.SECONDARY && (! row.isEmpty()) ){
                    cm.show(tableView, event.getScreenX(), event.getScreenY());
                }
                if (event.getButton() == MouseButton.SECONDARY && (row.isEmpty()) ){
                    cm2.show(tableView, event.getScreenX(), event.getScreenY());
                }
                if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY) {
                    cm.hide();
                    cm2.hide();
                }
                if (event.getClickCount() == 1 && event.getButton() == MouseButton.PRIMARY && (row.isEmpty())) {
                    tableView.getSelectionModel().clearSelection();
                }
            });
            return row ;
        });
        delete.setOnAction(event -> {
            try {
                handleDeleteClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        update.setOnAction(event -> {
            try {
                handleUpdateClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        create.setOnAction(event -> {
            try {
                handleCreateClick();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

//    private void handleDoubleClick() throws IOException {
//        Stage stage = (Stage) buttonCreate.getScene().getWindow();
//        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
//        Parent root = loader.load();
//        ReadResearches showAccount = loader.getController();
//        showAccount.setCompany(tableView.getSelectionModel().getSelectedItem());
//        Scene newScene = new Scene(root);
//        stage.setScene(newScene);
//    }

    private void handleDeleteClick() throws IOException {
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(tableView.getSelectionModel().getSelectedItem()));
        requestModel.setRequestType(RequestType.DELETEEMPLOYEES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Сотрудник успешно удалён!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readEmployees.fxml"));
            Parent root = loader.load();
            ReadEmployees readEmployees = loader.getController();
            readEmployees.setResearches(researches, companies);
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
    }
    private void handleUpdateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateEmployees.fxml"));
        Parent root = loader.load();
        UpdateEmployees updateEmployees = loader.getController();
        updateEmployees.setEmployees(tableView.getSelectionModel().getSelectedItem(), researches, companies);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    private void handleCreateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/createEmployees.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }


    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
        Parent root = loader.load();
        ReadResearches readResearches = loader.getController();
        readResearches.setCompany(companies);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Create_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonCreate.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createEmployees.fxml"));
        Parent root = loader.load();
        CreateEmployees createEmployees = loader.getController();
        createEmployees.setResearches(researches, companies);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

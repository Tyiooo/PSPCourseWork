package main.Controllers.CRUDResearches;

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
import main.Controllers.CRUDEmployees.ReadEmployees;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.Entities.Companies;
import main.Models.Entities.Employees;
import main.Models.Entities.Researches;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public class ReadResearches {
    public Button buttonCreate;
    private List<Researches> researches;

    public Button buttonFileWriter;
    public Button buttonBack;

    public Button buttonBarChart;

    @FXML
    private TableView<Researches> tableView;

    @FXML
    private TableColumn<Researches, Integer> id;

    @FXML
    private TableColumn<Researches, String> name;

    @FXML
    private TableColumn<Researches, Integer> cost;

    @FXML
    private TableColumn<Researches, Integer> amountOfDays;

    private Companies company;
    public void setCompany(Companies company) throws IOException {
        this.company = company;
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(company));
        requestModel.setRequestType(RequestType.READRESEARCHES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            researches = new Gson().fromJson(responseModel.getResponseData(), new TypeToken<List<Researches>>() {}.getType());
            ObservableList<Researches> researchesList = FXCollections.observableList(researches);
            tableView.setItems(researchesList);
        }
    }
    public void initialize() throws IOException {
        id.setCellValueFactory(new PropertyValueFactory<>("Id_research"));
        name.setCellValueFactory(new PropertyValueFactory<>("ResearchName"));
        cost.setCellValueFactory(new PropertyValueFactory<>("ResearchCost"));
        amountOfDays.setCellValueFactory(new PropertyValueFactory<>("amountOfDays"));

        ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Удалить");
        cm.getItems().add(delete);
        MenuItem update = new MenuItem("Редактировать");
        cm.getItems().add(update);
        MenuItem count = new MenuItem("Посчитать затраты");
        cm.getItems().add(count);
        ContextMenu cm2 = new ContextMenu();
        MenuItem create = new MenuItem("Создать");
        cm2.getItems().add(create);


        tableView.setRowFactory( tv -> {
            TableRow<Researches> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
                    try {
                        handleDoubleClick();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
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
        count.setOnAction(event -> {
            try {
                handleCountClick();
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

    private void handleCountClick() throws IOException{
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(tableView.getSelectionModel().getSelectedItem()));
        requestModel.setRequestType(RequestType.COUNTRESEARCHES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        Researches researchCost = null;
        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            researchCost = new Gson().fromJson(responseModel.getResponseData(), Researches.class);
        }
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Цена");
            alert.setHeaderText(null);
            alert.setContentText("Стоимость исследования : " + researchCost.getResearchCost());
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
            Parent root = loader.load();
            ReadResearches readResearches = loader.getController();
            readResearches.setCompany(company);
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
    }
    private void handleDoubleClick() throws IOException {
        Stage stage = (Stage) buttonCreate.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readEmployees.fxml"));
        Parent root = loader.load();
        ReadEmployees readEmployees = loader.getController();
        readEmployees.setResearches(tableView.getSelectionModel().getSelectedItem(), company);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    private void handleDeleteClick() throws IOException {
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(tableView.getSelectionModel().getSelectedItem()));
        requestModel.setRequestType(RequestType.DELETERESEARCHES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Исследование успешно удалёно!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
            Parent root = loader.load();
            ReadResearches readResearches = loader.getController();
            readResearches.setCompany(company);
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
    }
    private void handleUpdateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateResearches.fxml"));
        Parent root = loader.load();
        UpdateResearches updateResearches = loader.getController();
        updateResearches.setResearches(tableView.getSelectionModel().getSelectedItem(), company);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    private void handleCreateClick() throws IOException {
        Stage stage = (Stage) buttonCreate.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/createResearches.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void FileWrite_Pressed(ActionEvent actionEvent) throws IOException{
        FileWriter writer = new FileWriter("PriceResearches.txt", false);
        writer.write("Общая стоимость исследований\t\n\n");
        for(Researches research : researches){
            Request requestModel = new Request();
            requestModel.setRequestMessage(new Gson().toJson(research));
            requestModel.setRequestType(RequestType.COUNTRESEARCHES);
            ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
            ClientSocket.getInstance().getOut().flush();

            String answer = ClientSocket.getInstance().getIn().readLine();
            Response responseModel = new Gson().fromJson(answer, Response.class);

            if (responseModel.getResponseStatus() == ResponseStatus.OK) {
                research = new Gson().fromJson(responseModel.getResponseData(), Researches.class);
            }
            writer.write("Название : " + research.getResearchName() +
                    " | Стоимость : " + research.getResearchCost() +"\n");
        }
        writer.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Записать в файл");
        alert.setHeaderText(null);
        alert.setContentText("Стоимости исследований успешно записаны в файл!");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {

                });
        Stage stage = (Stage) buttonFileWriter.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
        Parent root = loader.load();
        ReadResearches readResearches = loader.getController();
        readResearches.setCompany(company);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/readCompanies.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Create_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonCreate.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createResearches.fxml"));
        Parent root = loader.load();
        CreateResearches createResearches = loader.getController();
        createResearches.setCompany(company);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

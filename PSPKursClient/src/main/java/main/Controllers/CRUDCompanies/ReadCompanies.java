package main.Controllers.CRUDCompanies;

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
import main.Models.Entities.Companies;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.Entities.User;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;


import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ReadCompanies {
    public Button buttonCreate;
    private List<Companies> companies;
    public Button buttonBack;
    public Button buttonFileWriter;

    @FXML
    private TableView<Companies> tableView;

    @FXML
    private TableColumn<Companies, Integer> id;

    @FXML
    private TableColumn<Companies, String> name;

    @FXML
    private TableColumn<Companies, String> address;


    public void initialize() throws IOException {
        id.setCellValueFactory(new PropertyValueFactory<>("Id_company"));
        name.setCellValueFactory(new PropertyValueFactory<>("CompanyName"));
        address.setCellValueFactory(new PropertyValueFactory<>("CompanyAdress"));
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(ClientSocket.getInstance().getUser()));
        requestModel.setRequestType(RequestType.READCOMPANIES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            companies = new Gson().fromJson(responseModel.getResponseData(), new TypeToken<List<Companies>>() {}.getType());
            ObservableList<Companies> companieslist = FXCollections.observableList(companies);
            tableView.setItems(companieslist);
        }
        ContextMenu cm = new ContextMenu();
        MenuItem delete = new MenuItem("Удалить");
        cm.getItems().add(delete);
        MenuItem update = new MenuItem("Редактировать");
        cm.getItems().add(update);

        ContextMenu cm2 = new ContextMenu();
        MenuItem create = new MenuItem("Создать");
        cm2.getItems().add(create);


        tableView.setRowFactory( tv -> {
            TableRow<Companies> row = new TableRow<>();
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
    private void handleDoubleClick() throws IOException {
        Stage stage = (Stage) buttonCreate.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
        Parent root = loader.load();
        ReadResearches readResearches = loader.getController();
        readResearches.setCompany(tableView.getSelectionModel().getSelectedItem());
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    private void handleDeleteClick() throws IOException {
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(tableView.getSelectionModel().getSelectedItem()));
        requestModel.setRequestType(RequestType.DELETECOMPANIES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Компания успешно удалёна!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/readCompanies.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
    }
    private void handleUpdateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateCompanies.fxml"));
        Parent root = loader.load();
        UpdateCompanies updateCompanies = loader.getController();
        updateCompanies.setCompanies(tableView.getSelectionModel().getSelectedItem());
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    private void handleCreateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/createCompanies.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void FileWrite_Pressed(ActionEvent actionEvent) throws IOException{
        FileWriter writer = new FileWriter("Companies.txt", true);
        //writer.write("Компании\n\n");
        for(int i = 0; i < companies.size();i++){
            writer.write("Название : " + companies.get(i).getCompanyName() +
                    " | Адрес : " + companies.get(i).getCompanyAdress() +"\n");
        }
        writer.close();
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Подтверждение");
        alert.setHeaderText(null);
        alert.setContentText("Компании успешно записаны в файл!");
        alert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .ifPresent(response -> {

                });
        Stage stage = (Stage) buttonFileWriter.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/readCompanies.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
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

    public void Create_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/createCompanies.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

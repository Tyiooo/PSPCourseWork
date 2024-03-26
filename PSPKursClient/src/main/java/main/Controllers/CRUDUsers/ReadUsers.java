package main.Controllers.CRUDUsers;

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
import main.Models.Entities.User;
import main.Enums.RequestType;
import main.Enums.ResponseStatus;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;


import java.io.IOException;
import java.util.List;

public class ReadUsers {
    public Button buttonCreate;
    private List<User> users;
    public Button buttonBack;

    @FXML
    private TableView<User> tableView;

    @FXML
    private TableColumn<User, Integer> id;

    @FXML
    private TableColumn<User, String> name;
    @FXML
    private TableColumn<User, String> login;

    @FXML
    private TableColumn<User, String> password;


    public void initialize() throws IOException {
        id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        login.setCellValueFactory(new PropertyValueFactory<>("Login"));
        name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        password.setCellValueFactory(new PropertyValueFactory<>("Password"));
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(ClientSocket.getInstance().getUser()));
        requestModel.setRequestType(RequestType.READUSERS);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            users = new Gson().fromJson(responseModel.getResponseData(), new TypeToken<List<User>>() {}.getType());
            ObservableList<User> userList = FXCollections.observableList(users);
            tableView.setItems(userList);
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
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
//                if (event.getClickCount() == 2 && (! row.isEmpty()) ) {
//
//                    try {
//                        handleDoubleClick();
//                    } catch (IOException e) {
//                        throw new RuntimeException(e);
//                    }
//                }
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

    private void handleDeleteClick() throws IOException {
        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(tableView.getSelectionModel().getSelectedItem()));
        requestModel.setRequestType(RequestType.DELETEUSERS);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Пользователь успешно удалён!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonCreate.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/readUsers.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        }
    }
    private void handleUpdateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/updateUsers.fxml"));
        Parent root = loader.load();
        UpdateUsers updateUsers = loader.getController();
        updateUsers.setUsers(tableView.getSelectionModel().getSelectedItem());
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    private void handleCreateClick() throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/createUsers.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/menuadmin.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Create_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/createUsers.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

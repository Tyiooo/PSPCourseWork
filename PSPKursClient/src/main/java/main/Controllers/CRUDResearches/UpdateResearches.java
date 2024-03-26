package main.Controllers.CRUDResearches;

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
import main.Models.Entities.Researches;
import main.Models.TCP.Request;
import main.Models.TCP.Response;
import main.Utility.ClientSocket;

import java.io.IOException;

public class UpdateResearches {
    public TextField textfieldResearchName;
    public TextField textfieldResearchCost;
    public TextField textfieldResearchTime;
    public Button buttonSave;
    public Label labelNameError;
    public Label labelCostError;
    public Label labelTimeError;

    public Button buttonBack;
    private Companies company;
    private Researches researches;

    public void setResearches(Researches researches, Companies company) {
        this.company = company;
        this.researches = researches;
        textfieldResearchName.setText(researches.getResearchName());
        textfieldResearchCost.setText(String.valueOf(researches.getResearchCost()));
        textfieldResearchTime.setText(String.valueOf(researches.getAmountOfDays()));
    }

    public void Save_Pressed(ActionEvent actionEvent) throws IOException {
        if (textfieldResearchName.getText().isEmpty()){
            labelNameError.setVisible(true);
        } else {
            labelNameError.setVisible(false);
        }
        if (textfieldResearchCost.getText().isEmpty() || textfieldResearchCost.getText().matches("^\\D*$")){
            labelCostError.setVisible(true);
        } else {
            labelCostError.setVisible(false);
        }
        if (textfieldResearchTime.getText().isEmpty()){
            labelTimeError.setVisible(true);
        } else {
            labelTimeError.setVisible(false);
        }
        if (labelNameError.isVisible() || labelCostError.isVisible()|| labelTimeError.isVisible()){
            return;
        }

        researches.setResearchName((textfieldResearchName.getText()));
        researches.setResearchCost(Integer.valueOf((textfieldResearchCost.getText())));
        researches.setAmountOfDays(Integer.valueOf(textfieldResearchTime.getText()));
        researches.setCompany(company);

        Request requestModel = new Request();
        requestModel.setRequestMessage(new Gson().toJson(researches));
        requestModel.setRequestType(RequestType.UPDATERESEARCHES);
        ClientSocket.getInstance().getOut().println(new Gson().toJson(requestModel));
        ClientSocket.getInstance().getOut().flush();

        String answer = ClientSocket.getInstance().getIn().readLine();
        Response responseModel = new Gson().fromJson(answer, Response.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Подтверждение");
            alert.setHeaderText(null);
            alert.setContentText("Исследование успешно редактировано!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
            Stage stage = (Stage) buttonSave.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
            Parent root = loader.load();
            ReadResearches readResearches = loader.getController();
            readResearches.setCompany(company);
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка!");
            alert.setHeaderText(null);
            alert.setContentText("Ошибка!");
            alert.showAndWait()
                    .filter(response -> response == ButtonType.OK)
                    .ifPresent(response -> {

                    });
        }
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) buttonBack.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/readResearches.fxml"));
        Parent root = loader.load();
        ReadResearches readResearches = loader.getController();
        readResearches.setCompany(company);
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}

package hu.petrik.varganorbert_javafxrestclientdolgozat;

import com.google.gson.Gson;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class ListOrszagController extends Controller{
    @FXML
    private Button insertButton;
    @FXML
    private Button updateButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<Orszag> orszagTable;
    @FXML
    private TableColumn<Orszag, String> nevCol;
    @FXML
    private TableColumn<Orszag, Integer> lakossagCol;
    @FXML
    private TableColumn<Orszag, String> tagallamCol;

    @FXML
    private void initialize() {
        nevCol.setCellValueFactory(new PropertyValueFactory<>("nev"));
        lakossagCol.setCellValueFactory(new PropertyValueFactory<>("lakossag"));
        tagallamCol.setCellValueFactory(new PropertyValueFactory<>("EU_tag"));
        Platform.runLater(() -> {
            try {
                loadOrszagFromServer();
            } catch (IOException e) {
                error("Couldn't get data from server", e.getMessage());
                Platform.exit();
            }
        });
    }

    private void loadOrszagFromServer() throws IOException {
        Response response = RequestHandler.get(App.BASE_URL);
        String content = response.getContent();
        Gson converter = new Gson();
        Orszag[] orszagok = converter.fromJson(content, Orszag[].class);
        orszagTable.getItems().clear();
        for (Orszag orszag : orszagok) {
            orszagTable.getItems().add(orszag);
        }
    }

    @FXML
    public void insertClick(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("create-orszag-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Create Orszag");
            stage.setScene(scene);
            stage.show();
            insertButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            stage.setOnCloseRequest(event -> {
                insertButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                try {
                    loadOrszagFromServer();
                } catch (IOException e) {
                    error("An error occurred while communicating with the server");
                }
            });
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        int selectedIndex = orszagTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Válasszon ki egy elemet");
            return;
        }
        Orszag selected = orszagTable.getSelectionModel().getSelectedItem();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("update-orszag-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 640, 480);
            Stage stage = new Stage();
            stage.setTitle("Ország frissítése");
            stage.setScene(scene);
            UpdateOrszagController controller = fxmlLoader.getController();
            controller.setOrszag(selected);
            stage.show();
            insertButton.setDisable(true);
            updateButton.setDisable(true);
            deleteButton.setDisable(true);
            stage.setOnHidden(event -> {
                insertButton.setDisable(false);
                updateButton.setDisable(false);
                deleteButton.setDisable(false);
                try {
                    loadOrszagFromServer();
                } catch (IOException e) {
                    error("An error occurred while communicating with the server");
                }
            });
        } catch (IOException e) {
            error("Could not load form", e.getMessage());
        }
    }

    @FXML
    public void deleteClick(ActionEvent actionEvent) {
        int selectedIndex = orszagTable.getSelectionModel().getSelectedIndex();
        if (selectedIndex == -1) {
            warning("Válasszon ki egy elemet");
            return;
        }

        Orszag selected = orszagTable.getSelectionModel().getSelectedItem();
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setHeaderText(String.format("Are you sure you want to delete %s?", selected.getNev()));
        Optional<ButtonType> optionalButtonType = confirmation.showAndWait();
        if (optionalButtonType.isEmpty()) {
            System.err.println("Unknown error occurred");
            return;
        }
        ButtonType clickedButton = optionalButtonType.get();
        if (clickedButton.equals(ButtonType.OK)) {
            String url = App.BASE_URL + "/" + selected.getId();
            try {
                RequestHandler.delete(url);
                loadOrszagFromServer();
            } catch (IOException e) {
                error("An error occurred while communicating with the server");
            }
        }
    }
}

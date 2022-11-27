package hu.petrik.varganorbert_javafxrestclientdolgozat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;

public class CreateOrszagController extends Controller {
    @FXML
    private TextField nevField;
    @FXML
    private TextField tagallamField;
    @FXML
    private Spinner<Integer> lakossagField;
    @FXML
    private Button submitButton;

    @FXML
    private void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000000, 10000,100);
        lakossagField.setValueFactory(valueFactory);
    }

    @FXML
    public void submitClick(ActionEvent actionEvent) {
        String nev = nevField.getText().trim();
        String tagallam = tagallamField.getText().trim();
        int lakossag = lakossagField.getValue();
        if (nev.isEmpty()) {
            warning("This information is required");
            return;
        }
        if (tagallam.isEmpty()) {
            warning("This information is required");
            return;
        }
        Orszag newOrszag = new Orszag(0, nev,lakossag,tagallam);
        Gson converter = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        String json = converter.toJson(newOrszag);
        try {
            Response response = RequestHandler.post(App.BASE_URL, json);
            if (response.getResponseCode() == 201) {
                warning("Ország hozzáadva!");
                nevField.setText("");
                tagallamField.setText("");
                lakossagField.getValueFactory().setValue(100000);
            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

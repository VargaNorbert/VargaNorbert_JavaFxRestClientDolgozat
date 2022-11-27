package hu.petrik.varganorbert_javafxrestclientdolgozat;

import com.google.gson.Gson;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class UpdateOrszagController extends Controller {
    @FXML
    private TextField nevField;
    @FXML
    private TextField tagallamField;
    @FXML
    private Spinner<Integer> lakossagField;
    @FXML
    private Button updateButton;

    private Orszag orszag;

    public void setOrszag(Orszag orszag) {
        this.orszag = orszag;
        nevField.setText(this.orszag.getNev());
        tagallamField.setText(this.orszag.getEu_tag());
        lakossagField.getValueFactory().setValue(this.orszag.getLakossag());
    }

    @FXML
    private void initialize() {
        SpinnerValueFactory.IntegerSpinnerValueFactory valueFactory =
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100000000, 10000,100);
        lakossagField.setValueFactory(valueFactory);
    }

    @FXML
    public void updateClick(ActionEvent actionEvent) {
        String nev = nevField.getText().trim();
        int lakossag = lakossagField.getValue();
        String tagallam = tagallamField.getText().trim();
        if (nev.isEmpty()) {
            warning("This information is required");
            return;
        }
        if (tagallam.isEmpty()) {
            warning("This information is required");
            return;
        }
        // TODO: validate email format
        this.orszag.setNev(nev);
        this.orszag.setLakossag(lakossag);
        this.orszag.setEu_tag(orszag.tagallamConverter(tagallam));
        Gson converter = new Gson();
        String json = converter.toJson(this.orszag);
        try {
            String url = App.BASE_URL + "/" + this.orszag.getId();
            Response response = RequestHandler.put(url, json);
            if (response.getResponseCode() == 200) {
                Stage stage = (Stage) this.updateButton.getScene().getWindow();
                stage.close();
            } else {
                String content = response.getContent();
                error(content);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

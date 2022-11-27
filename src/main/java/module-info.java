module hu.petrik.varganorbert_javafxrestclientdolgozat {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens hu.petrik.varganorbert_javafxrestclientdolgozat to javafx.fxml;
    exports hu.petrik.varganorbert_javafxrestclientdolgozat;
}
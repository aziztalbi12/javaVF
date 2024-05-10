package controllers;

import com.twilio.Twilio;
import entities.Category;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import services.CategoryService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.lang.Integer.parseInt;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class CreateCategoryController {

    CategoryService cs = new CategoryService();

    @FXML
    private TextField descTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField quantiteTF;
    public static final String ACCOUNT_SID = "ACc2e181aeeaf7326ae8a8652257917fe0";
    public static final String AUTH_TOKEN = "a71640bb3642ee5ff492502676ea81c6";
    private static final String FROM_NUMBER = "+12176706796";

    static {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    public void sendSms(String to, String message) {
        Message sms = Message.creator(
                new PhoneNumber(to),  // To number
                new PhoneNumber(FROM_NUMBER),  // From Twilio number
                message
        ).create();
        System.out.println("Sent message SID: " + sms.getSid());
    }
    @FXML
    void Create(ActionEvent event) throws SQLException, IOException {
        String dsc = descTF.getText();
        String nom = nomTF.getText();
        String quantite = quantiteTF.getText();

        if (dsc.isEmpty() || nom.isEmpty() || quantite.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else if (!isValidString(dsc) || !isValidString(nom) || !isValidInt(quantite) ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid information.");
            alert.showAndWait();
        } else {
            int quantiteInt = parseInt(quantite);
            Category cat = new Category(quantiteInt,nom,dsc);
            cs.add(cat);
            String phoneNumber="+216 29862411";
            String message = "Hello, you've ADDED a new categorie  " + cat.getNom() + "!";

            //sendSms(phoneNumber, message);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Crée");
            alert.setHeaderText(null);
            alert.setContentText("Categorie Crée !");
            alert.getButtonTypes().clear(); // Remove existing button types
            alert.getButtonTypes().add(ButtonType.OK); // Add only OK button type
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
            Parent root = loader.load();
            Scene scene = quantiteTF.getScene();
            if (scene != null) {
                Stage currentStage = (Stage) scene.getWindow();
                currentStage.close();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }

    }

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = quantiteTF.getScene();
            if (scene != null) {
                Stage currentStage = (Stage) scene.getWindow();
                currentStage.close();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isValidInt(String value) {
        // Check if the value is a valid integer
        return value.matches("-?\\d+");
    }

    private boolean isValidString(String name) {
        // Check if the name contains only letters and has length between 2 and 50
        return name.matches("^[a-zA-Z0-9_ ]*$");
    }


}

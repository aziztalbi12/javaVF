package controllers;

import entities.Category;
import entities.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import services.CategoryService;
import services.ProductService;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.net.URL;
import java.util.Properties;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class CreateProduct implements Initializable {

    ProductService ps = new ProductService();


    @FXML
    private ComboBox<Category> categoryCB;

    @FXML
    private TextField descTF;

    @FXML
    private TextField nomTF;

    @FXML
    private TextField prixTF;

    private final CategoryService categoryService = new CategoryService();



    @FXML
    void Create(ActionEvent event) throws SQLException, IOException {

        String dsc = descTF.getText();
        String nom = nomTF.getText();
        String prix = prixTF.getText();
        String selectedCategoryName = String.valueOf(categoryCB.getValue()); // Get selected category name


        if (dsc.isEmpty() || nom.isEmpty() || prix.isEmpty() ||  selectedCategoryName == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please fill all the fields");
            alert.showAndWait();
        } else if (!isValidString(dsc) || !isValidString(nom) || !isValidInt(prix)) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Please enter valid information.");
            alert.showAndWait();
        } else {
            int quantiteInt = parseInt(prix);
            Product prod = new Product(quantiteInt,nom,dsc,(categoryCB.getValue()).getId());
            ps.add(prod);

            // Load the HTML template
            String htmlTemplate = loadHtmlTemplate();

            // Replace the placeholder with the certification name
            String htmlBody = htmlTemplate.replace("{{product}}", prod.getNom() );
            String userAddress ="aziztalbi043@gmail.com";
            // Send the email with HTML content
            sendEmail(userAddress, "adding done", htmlBody);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Crée");
            alert.setHeaderText(null);
            alert.setContentText("Categorie Crée !");
            alert.getButtonTypes().clear(); // Remove existing button types
            alert.getButtonTypes().add(ButtonType.OK); // Add only OK button type
            alert.showAndWait();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
            Parent root = loader.load();
            Scene scene = nomTF.getScene();
            if (scene != null) {
                Stage currentStage = (Stage) scene.getWindow();
                currentStage.close();
            }
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
        }
    }

    public void sendEmail(String toEmail, String subject, String htmlBody) {
        final String username = "aziztalbi043@gmail.com"; // your email
        final String password = "deibtvstouymjrcy"; // your password

        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(javax.mail.Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);

            // Create a MimeBodyPart to hold the HTML content
            MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(htmlBody, "text/html");

            // Create a Multipart object to hold the HTML content and attachments (if any)
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(htmlPart);

            // Set the content of the message to the Multipart object
            message.setContent(multipart);

            Transport.send(message);
            System.out.println("Email sent successfully!");
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email: " + e.getMessage());
            showAlert("Email Error", "Failed to Send Email", "Could not send the email to: " + toEmail, Alert.AlertType.ERROR);
        }
    }

    public void showAlert(String title, String header, String content, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }


    private String loadHtmlTemplate() { return "<!DOCTYPE html>\n" +
            "<html lang=\"en\">\n" +
            "<head>\n" +
            "    <meta charset=\"UTF-8\">\n" +
            "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
            "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">\n" +
            "    <title>Certification Details</title>\n" +
            "    <style>\n" +
            "        body {\n" +
            "            font-family: Arial, sans-serif;\n" +
            "            margin: 0;\n" +
            "            padding: 0;\n" +
            "            background-color: #f4f4f4;\n" +
            "        }\n" +
            "\n" +
            "        .container {\n" +
            "            max-width: 600px;\n" +
            "            margin: 20px auto;\n" +
            "            padding: 20px;\n" +
            "            background-color: #fff;\n" +
            "            border-radius: 8px;\n" +
            "            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);\n" +
            "        }\n" +
            "\n" +
            "        h1 {\n" +
            "            color: #333;\n" +
            "            text-align: center;\n" +
            "        }\n" +
            "\n" +
            "        p {\n" +
            "            color: #666;\n" +
            "            line-height: 1.6;\n" +
            "        }\n" +
            "\n" +
            "        strong {\n" +
            "            font-weight: bold;\n" +
            "        }\n" +
            "\n" +
            "        .signature {\n" +
            "            text-align: center;\n" +
            "            margin-top: 20px;\n" +
            "        }\n" +
            "    </style>\n" +
            "</head>\n" +
            "<body>\n" +
            "<div class=\"container\">\n" +
            "    <h1>you added a product!</h1>\n" +
            "    <p>Dear Admin,</p>\n" +
            "    <p>You have been successfully added a product:</p>\n" +
            "    <p><strong>Product  Name:</strong> {{product}}</p>\n" +
            "    <p>Thank you for choosing our platform.</p>\n" +
            "    <p class=\"signature\">Best regards,<br>The Admin Team</p>\n" +
            "</div>\n" +
            "</body>\n" +
            "</html>\n";
    }

    @FXML
    void GoBack(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/productList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = prixTF.getScene();
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
        if (value == null) {
            return false;
        }
        return value.matches("-?\\d+");  // Correctly matches integers including negative numbers
    }

    private boolean isValidString(String name) {
        if (name == null) {
            return false;
        }
        return name.matches("^[a-zA-Z\\s]{2,50}$");  // Ensures that the string contains only letters and spaces
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            List<Category> categories = categoryService.read();
            ObservableList<Category> categoryList = FXCollections.observableArrayList(categories);

            // Set custom cell factory to render only category names
            categoryCB.setCellFactory(param -> new ListCell<Category>() {
                @Override
                protected void updateItem(Category item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setText(null);
                    } else {
                        setText(item.getNom()); // Render only category name
                    }
                }
            });

            // Set converter to display only category names in the combo box
            categoryCB.setConverter(new StringConverter<Category>() {
                @Override
                public String toString(Category category) {
                    return category == null ? null : category.getNom();
                }

                @Override
                public Category fromString(String string) {
                    return null; // Not needed in this case
                }
            });

            categoryCB.setItems(categoryList); // Set category objects in the combo box
        } catch (SQLException e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }


}

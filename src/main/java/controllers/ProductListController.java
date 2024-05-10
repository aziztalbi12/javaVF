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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import services.CategoryService;
import services.ProductService;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ProductListController implements Initializable {

    private static ProductListController instance;

    ProductService ps = new ProductService();

    @FXML
    private TableColumn<?, ?> categoryIdCol;

    @FXML
    private TableColumn<?, ?> descCol;

    @FXML
    private TableColumn<?, ?> nomCol;

    @FXML
    private TableColumn<?, ?> prixCol;

    @FXML
    private TableView<Product> tabProd;

    private List<Product> ProductList;




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        instance = this;
        try {
            ListeProducts();
        } catch (SQLException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void ListeProducts() throws SQLException {

        ProductService ps = new ProductService();

        // Initialize table columns
        nomCol.setCellValueFactory(new PropertyValueFactory<>("nom"));
        categoryIdCol.setCellValueFactory(new PropertyValueFactory<>("category_id"));
        prixCol.setCellValueFactory(new PropertyValueFactory<>("prix"));
        descCol.setCellValueFactory(new PropertyValueFactory<>("description"));

        boolean deleteColumnExists = false;
        boolean ModifyColumnExists = false;

        for (TableColumn column : tabProd.getColumns()) {
            if (column.getText().equals("Action")) {
                deleteColumnExists = true;
                break;
            }
        }

        if (!deleteColumnExists) {
            TableColumn<Product, Void> deleteColumn = new TableColumn<>("Action");
            deleteColumn.setCellFactory(column -> {
                return new TableCell<Product, Void>() {
                    private final Button deleteButton = new Button("Delete");

                    {
                        deleteButton.setOnAction(event -> {
                            Product p = getTableView().getItems().get(getIndex());
                            ProductService ps = new ProductService();
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Delete Product");
                            alert.setHeaderText("Are you sure you want to delete this Product?");
                            alert.setContentText("This action cannot be undone.");
                            Optional<ButtonType> result = alert.showAndWait();
                            if (result.isPresent() && result.get() == ButtonType.OK) {
                                try {
                                    System.out.println(p);
                                    ps.delete(p);

                                    refreshTable();
                                } catch (SQLException ex) {
                                    Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
                                }
                            } else {

                                alert.close();
                            }

                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(deleteButton);
                        }
                    }
                };
            });

            tabProd.getColumns().add(deleteColumn);
        }

        if (!ModifyColumnExists) {
            TableColumn<Product, Void> modifyColumn = new TableColumn<>("Update");
            modifyColumn.setCellFactory(column -> {
                return new TableCell<Product, Void>() {
                    private final Button modifyButton = new Button("Modify");

                    {
                        modifyButton.setOnAction(event -> {
                            Product selectedProd = getTableView().getItems().get(getIndex());
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/modifyProduct.fxml"));
                            Parent root;
                            try {
                                root = loader.load();
                                ModifyProductController controller = loader.getController();
                                controller.initData(selectedProd);
                                Scene scene = modifyButton.getScene();
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
                        });
                    }

                    @Override
                    protected void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(modifyButton);
                        }
                    }
                };
            });

            tabProd.getColumns().add(modifyColumn);
        }

        // Load voyages from the database
        List<Product> list = ps.read();
        System.out.println(list);
        ObservableList<Product> observableList = FXCollections.observableArrayList(list);
        tabProd.setItems(observableList);

    }

    public void refreshTable() {
        try {
            ProductList = new ProductService().read();
            tabProd.setItems(FXCollections.observableArrayList(ProductList));
        } catch (SQLException ex) {
            Logger.getLogger(CategoryListController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    void Create(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/createProduct.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabProd.getScene();
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



    @FXML
    void goToStat(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/stat.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabProd.getScene();
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

    @FXML
    void Go(ActionEvent event) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/categoryList.fxml"));
        Parent root;
        try {
            root = loader.load();
            Scene scene = tabProd.getScene();
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

    public void goToMaps(ActionEvent actionEvent) {
        Stage mapStage = new Stage();
        WebView webView = new WebView();
        WebEngine webEngine = webView.getEngine();

        double latitude = 36.899500;
        double longitude = 10.189658;

        // HTML content with embedded Google Map using your API key
        String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
                        <style type="text/css">
                            html { height: 100% }
                            body { height: 100%; margin: 0; padding: 0 }
                            #map_canvas { height: 100% }
                        </style>
                        <script type="text/javascript" src="https://maps.googleapis.com/maps/api/js?key=AIzaSyC9SaD0tZsCcoIlUSc9r6zQnZKD6vl3z94"></script>
                        <script type="text/javascript">
                            function initialize() {
                                var mapOptions = {
                                    center: new google.maps.LatLng(36.862499, 10.195556), // Central point between the two locations
                                    zoom: 13,
                                    mapTypeId: google.maps.MapTypeId.ROADMAP
                                };
                                var map = new google.maps.Map(document.getElementById("map_canvas"), mapOptions);
                                
                                // Coordinates for Esprit faculties
                                var locations = [
                                    {lat: 36.899500, lng: 10.189658, title: 'Esprit Ariana'}, // Approximate location for Esprit Ariana
                                    {lat: 36.86667, lng: 10.195556, title: 'Esprit Charguia'}  // Approximate location for Esprit Charguia
                                ];
                                
                                // Create markers for each location
                                locations.forEach(function(location) {
                                    var marker = new google.maps.Marker({
                                        position: new google.maps.LatLng(location.lat, location.lng),
                                        map: map,
                                        title: location.title
                                    });
                                });
                            }
                        </script>
                    </head>
                    <body onload="initialize()">
                        <div id="map_canvas" style="width:100%; height:100%"></div>
                    </body>
                    </html>
                    """;

        webEngine.loadContent(html, "text/html");

        mapStage.setScene(new Scene(webView, 600, 500));
        mapStage.setTitle("Google Map");
        mapStage.show();
    }
}

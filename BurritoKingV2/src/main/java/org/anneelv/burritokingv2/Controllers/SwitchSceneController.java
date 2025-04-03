package org.anneelv.burritokingv2.Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;
import java.util.Objects;

/*
 * Controller class responsible for switching scenes and initializing controllers.
 */
public class SwitchSceneController {

    /* For scenes that changes the whole stage*/
    public void signup(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/anneelv/burritokingv2/signup.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void login(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/org/anneelv/burritokingv2/login.fxml")));
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void dashboard(ActionEvent event, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/dashboard.fxml"));
        Parent root = loader.load();

        DashboardController dashboardController = loader.getController();
        dashboardController.initUserSession(session);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void upgradeVIP(MouseEvent event, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/upgradeVIP.fxml"));
        Parent root = loader.load();

        UpgradeVIPController upgradeVIPController = loader.getController();
        upgradeVIPController.initUserSession(session);

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void checkout(ActionEvent event, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/checkout.fxml"));
        Parent root = loader.load();

        CheckoutController checkoutController = loader.getController();
        checkoutController.initUserSession(session);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void finalizingOrder(ActionEvent event, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/finalizingOrder.fxml"));
        Parent root = loader.load();

        FinalizingOrderController finalizingOrderController = loader.getController();
        finalizingOrderController.initUserSession(session);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void pointRedemption(ActionEvent event, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/pointRedemption.fxml"));
        Parent root = loader.load();

        PointRedemptionController pointRedemptionController = loader.getController();
        pointRedemptionController.initUserSession(session);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void collectOrder(ActionEvent event, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/collectOrder.fxml"));
        Parent root = loader.load();

        CollectOrderController collectOrderController = loader.getController();
        collectOrderController.initUserSession(session);

        Stage stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }



    /* For scenes that changes part of the stage from Dashboard*/
    public void profile(StackPane contentArea, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/profile.fxml"));
        Parent root = loader.load();

        ProfileController profileController = loader.getController();
        profileController.initUserSession(session);

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void foodMenu(StackPane contentArea, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/foodMenu.fxml"));
        Parent root = loader.load();

        FoodMenuController foodMenuController = loader.getController();
        foodMenuController.initUserSession(session);

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void order(StackPane contentArea, UserSession session) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/order.fxml"));
        Parent root = loader.load();

        OrderController orderController = loader.getController();
        orderController.initUserSession(session);

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }

    public void pointHistory(StackPane contentArea, UserSession session) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/anneelv/burritokingv2/pointHistory.fxml"));
        Parent root = loader.load();

        PointHistoryController pointHistoryController = loader.getController();
        pointHistoryController.initUserSession(session);

        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(root);
    }
}

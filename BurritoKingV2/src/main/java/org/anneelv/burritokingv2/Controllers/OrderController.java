package org.anneelv.burritokingv2.Controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import org.anneelv.burritokingv2.Database.OrdersDb;
import org.anneelv.burritokingv2.Models.FinalizedOrder;
import org.anneelv.burritokingv2.Models.FoodItem;
import org.anneelv.burritokingv2.Models.UserSession;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
* Controller class for managing Order scene and functionalities
* */
public class OrderController extends ControllerInit {
    private UserSession session;
    private OrdersDb ordersDB;
    private final SwitchSceneController switchSceneController = new SwitchSceneController();
    private ObservableList<FinalizedOrder> ongoingOrderList;
    private ObservableList<FinalizedOrder> completeOrderList;
    private List<FinalizedOrder> orderList;
    private List<FinalizedOrder> awaitingCollectionOrders;

    @FXML
    private TableView<FinalizedOrder> ongoingOrderTable;
    @FXML
    private TableColumn<FinalizedOrder, Integer> orderIDCol;
    @FXML
    private TableColumn<FinalizedOrder, String> itemsCol;
    @FXML
    private TableColumn<FinalizedOrder, String> quantityCol;
    @FXML
    private TableColumn<FinalizedOrder, String> statusCol;
    @FXML
    private TableColumn<FinalizedOrder, Double> totalPriceCol;
    @FXML
    private TableColumn<FinalizedOrder, CheckBox> ongoingCheckboxCol;
    @FXML
    private TableView<FinalizedOrder> pastOrderTable;
    @FXML
    private TableColumn<FinalizedOrder, Integer> completeOrderIDCol;
    @FXML
    private TableColumn<FinalizedOrder, String> orderDateCol;
    @FXML
    private TableColumn<FinalizedOrder, String> orderTimeCol;
    @FXML
    private TableColumn<FinalizedOrder, String> completeItemsCol;
    @FXML
    private TableColumn<FinalizedOrder, String> completeQuantityCol;
    @FXML
    private TableColumn<FinalizedOrder, String> completeStatusCol;
    @FXML
    private TableColumn<FinalizedOrder, Double> completeTotalPriceCol;
    @FXML
    private TableColumn<FinalizedOrder, CheckBox> completeCheckboxCol;
    @FXML
    private CheckBox orderDateTimeCheckbox;
    @FXML
    private CheckBox orderItemsQtyCheckbox;
    @FXML
    private CheckBox orderTotalPriceCheckbox;
    @FXML
    private CheckBox orderStatusCheckbox;

    @Override
    public void initUserSession(UserSession userSession) {
        this.session = userSession;
        ordersDB = new OrdersDb();
        // Initialize the UI with the user information
        initializeUI();
    }

    @Override
    protected void initializeUI(){
        if (session != null) {
            setOngoingOrderList();
            setCompleteOrderList();
            session.printOrder();
        }
    }

    /*
     * Sets up the ongoing order list from order with status "Await for Collection"
     */
    private void setOngoingOrderList(){
        ongoingOrderList = FXCollections.observableArrayList();
        orderList = new ArrayList<>();
        orderList = session.getOrderHistory();
        awaitingCollectionOrders = orderList.stream()
                .filter(order -> order.getStatus() == FinalizedOrder.OrderStatus.AWAIT_FOR_COLLECTION)
                .toList();
        if (awaitingCollectionOrders.isEmpty()){
            System.out.println("No order found");
            ongoingOrderTable.setItems(FXCollections.observableArrayList());
        } else {
            for (FinalizedOrder orders : awaitingCollectionOrders){
                setOngoingOrderObservableList(orders);
            }
            FXCollections.sort(ongoingOrderList);
            associateOngoingOrderDataToColumn(ongoingOrderList);
        }
        ongoingOrderTable.refresh();
    }

    /*
     * Sets up the complete order list from order with status "Collected" and "Cancelled"
     */
    private void setCompleteOrderList(){
        completeOrderList = FXCollections.observableArrayList();
        orderList = new ArrayList<>();
        orderList = session.getOrderHistory();
        List<FinalizedOrder> completeCollectionOrders = orderList.stream()
                .filter(order -> order.getStatus() != FinalizedOrder.OrderStatus.AWAIT_FOR_COLLECTION)
                .toList();
        if (completeCollectionOrders.isEmpty()){
            pastOrderTable.setItems(FXCollections.observableArrayList());
        } else {
            for (FinalizedOrder orders : completeCollectionOrders){
                setCompleteOrderObservableList(orders);
            }
            FXCollections.sort(completeOrderList);
            associateCompleteOrderDataToColumn(completeOrderList);
        }
        pastOrderTable.refresh();
    }

    /*
     * Associates data from the ongoing order list to table columns in ongoing order table
     */
    private void associateOngoingOrderDataToColumn(ObservableList<FinalizedOrder> orders){
        orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        itemsCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getItems().stream()
                        .map(FoodItem::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));
        itemsCol.setCellFactory(new MultilineCellFactory());
        quantityCol.setCellValueFactory(cellData -> {
            List<FoodItem> items = cellData.getValue().getItems();
            StringBuilder quantities = new StringBuilder();
            for (FoodItem item : items) {
                quantities.append(item.getQuantity()).append("\n");
            }
            return new SimpleStringProperty(quantities.toString());
        });
        quantityCol.setCellFactory(new MultilineCellFactory());
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        statusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        ongoingCheckboxCol.setCellValueFactory(new PropertyValueFactory<>("select"));

        ongoingOrderTable.setItems(orders);
    }

    /*
     * Associates data from the complete order list to table columns in past order table.
     */
    private void associateCompleteOrderDataToColumn(ObservableList<FinalizedOrder> orders){
        completeOrderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("placedDate"));
        orderTimeCol.setCellValueFactory(new PropertyValueFactory<>("placedTime"));
        completeItemsCol.setCellValueFactory(cellData -> new SimpleStringProperty(
                cellData.getValue().getItems().stream()
                        .map(FoodItem::getName)
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("")
        ));
        completeItemsCol.setCellFactory(new MultilineCellFactory());
        completeQuantityCol.setCellValueFactory(cellData -> {
            List<FoodItem> items = cellData.getValue().getItems();
            StringBuilder quantities = new StringBuilder();
            for (FoodItem item : items) {
                quantities.append(item.getQuantity()).append("\n");
            }
            return new SimpleStringProperty(quantities.toString());
        });
        completeQuantityCol.setCellFactory(new MultilineCellFactory());
        completeTotalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        completeStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));
        completeCheckboxCol.setCellValueFactory(new PropertyValueFactory<>("select"));

        pastOrderTable.setItems(orders);
    }

    /*
    * Adds a finalized order with status "Await for Collection" to ongoing observable list
    * */
    public void setOngoingOrderObservableList(FinalizedOrder order){
        ongoingOrderList.add(order);
    }

    /*
    * Adds a finalized order with status "Collected" or "Cancelled" to past order observable list
    * */
    public void setCompleteOrderObservableList(FinalizedOrder order){
        completeOrderList.add(order);
    }

    /*
    * Handles the action when the cancel button is clicked
    * */
    public void cancelBtnOnAction(){
        boolean anyOrderSelected = false;
        for (FinalizedOrder order : awaitingCollectionOrders) {
            if (order.getSelect().isSelected()) {
                anyOrderSelected = true;
                break;
            }
        }

        if (anyOrderSelected){
            cancelOrderConfirmation();
        } else {
            noOrderSelectedAlert();
        }
    }

    /*
     * Displays a confirmation dialog for cancelling orders.
     */
    private void cancelOrderConfirmation() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Cancel Orders");
        alert.setHeaderText("Are you sure you want to cancel the selected orders?");
        alert.setContentText("This action cannot be undone.");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            for (FinalizedOrder order : awaitingCollectionOrders) {
                if (order.getSelect().isSelected()) {
                    order.getSelect().setSelected(false);
                    order.setStatus("Cancelled");
                    ordersDB.updateOrderStatus(order.getOrderID(), "Cancelled");
                    completeOrderList.add(order);
                }
            }
            session.setCollectedPoint();
            setOngoingOrderList();
            setCompleteOrderList();
            ongoingOrderTable.refresh();
        }
    }

    /*
     * Displays an alert when no order is selected.
     */
    private void noOrderSelectedAlert(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Order Selection Alert");
        alert.setHeaderText(null);
        alert.setContentText("Please select an order first.");
        alert.showAndWait();
    }

    /*
     * Displays a warning when too many orders are selected for collection.
     */
    private void tooMuchOrderSelectedAlert(){
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Collect Orders");
        alert.setHeaderText(null);
        alert.setContentText("Please select one order at a time to collect.");
        alert.showAndWait();
    }

    /*
     * Handles the action when the collect button is clicked.
     */
    public void collectBtnOnAction(ActionEvent event) throws IOException {
        int orderCount = 0;
        for (FinalizedOrder order : awaitingCollectionOrders) {
            if (order.getSelect().isSelected()) {
                orderCount ++;
                session.setOrderToCollect(order);
            }
        }

        if (orderCount == 1){
            switchSceneController.collectOrder(event, session);
        } else if (orderCount > 1){
            tooMuchOrderSelectedAlert();
        }else {
            noOrderSelectedAlert();
        }

    }

    /*
     * Handles the action when the export button is clicked.
     */
    public void exportBtnOnAction(){
        int orderCount = 0;
        List<FinalizedOrder> selectedOrder = new ArrayList<>();
        for (FinalizedOrder order : completeOrderList) {
            if (order.getSelect().isSelected()) {
                orderCount ++;
                selectedOrder.add(order);
            }
        }

        if (orderCount < 1){
            noOrderSelectedAlert();
        } else {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save CSV File");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
            File file = fileChooser.showSaveDialog(pastOrderTable.getScene().getWindow());

            if (file != null) {
                exportOrdersToCSV(file, selectedOrder);
            }
        }
    }

    /*
     * Exports orders to a CSV file.
     */
    private void exportOrdersToCSV(File file, List<FinalizedOrder> orders){
        try (FileWriter writer = new FileWriter(file)) {
            // Write header
            writer.append("Order ID,");
            if (orderDateTimeCheckbox.isSelected()) {
                writer.append("Order Date,");
                writer.append("Order Time,");
            }
            if (orderItemsQtyCheckbox.isSelected()) {
                writer.append("Order Item,");
                writer.append("Order Qty,");
            }
            if (orderTotalPriceCheckbox.isSelected()) writer.append("Total Price,");
            if (orderStatusCheckbox.isSelected()) writer.append("Status,");
            writer.append("\n");

            // Write data
            for (FinalizedOrder order : orders) {
                if (orderItemsQtyCheckbox.isSelected()){
                    for (FoodItem item : order.getItems()) {
                        writer.append(String.valueOf(order.getOrderID())).append(",");
                        if (orderDateTimeCheckbox.isSelected()) {
                            writer.append(order.getPlacedDate()).append(",");
                            writer.append(order.getPlacedTime()).append(",");
                        }
                        if (orderItemsQtyCheckbox.isSelected()) {
                            writer.append(item.getName()).append(",");
                            writer.append(String.valueOf(item.getQuantity())).append(",");
                        }
                        if (orderTotalPriceCheckbox.isSelected()) writer.append(Double.toString(order.getTotalPrice())).append(",");
                        if (orderStatusCheckbox.isSelected()) writer.append(order.getStatus().toString()).append(",");
                        writer.append("\n");
                    }
                } else {
                    writer.append(String.valueOf(order.getOrderID())).append(",");
                    if (orderDateTimeCheckbox.isSelected()) {
                        writer.append(order.getPlacedDate()).append(",");
                        writer.append(order.getPlacedTime()).append(",");
                    }
                    if (orderTotalPriceCheckbox.isSelected()) writer.append(Double.toString(order.getTotalPrice())).append(",");
                    if (orderStatusCheckbox.isSelected()) writer.append(order.getStatus().toString()).append(",");
                    writer.append("\n");
                }
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText("Failed to export orders: " + e.getMessage());
            alert.showAndWait();
        }
    }
}

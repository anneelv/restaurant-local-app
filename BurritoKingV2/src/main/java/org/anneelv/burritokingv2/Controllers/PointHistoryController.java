package org.anneelv.burritokingv2.Controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.anneelv.burritokingv2.Models.Customer;
import org.anneelv.burritokingv2.Models.FinalizedOrder;
import org.anneelv.burritokingv2.Models.UserSession;

import java.util.List;

/*
 * Controller class for managing PointHistory scene and functionalities.
 */
public class PointHistoryController extends ControllerInit {
    private UserSession session;
    private ObservableList<FinalizedOrder> completeOrderList;

    @FXML
    private Label collectedPointLabel;
    @FXML
    private TableView<FinalizedOrder> pointHistoryTable;
    @FXML
    private TableColumn<FinalizedOrder,Integer> orderIDCol;
    @FXML
    private TableColumn<FinalizedOrder,String> orderDateCol;
    @FXML
    private TableColumn<FinalizedOrder,String> orderTimeCol;
    @FXML
    private TableColumn<FinalizedOrder,Double> totalPriceCol;
    @FXML
    private TableColumn<FinalizedOrder,Integer> pointsUsedCol;
    @FXML
    private TableColumn<FinalizedOrder,Integer> pointsCollectedCol;

    @Override
    public void initUserSession(UserSession userSession) {
        this.session = userSession;

        // Initialize the UI with the user information
        initializeUI();
    }

    @Override
    protected void initializeUI() {
        if (session != null) {
            Customer customer = session.getCustomer();
            setCollectedOrders();
            collectedPointLabel.setText(String.valueOf(customer.getPointsCollected()));
        }
    }

    /*
     * Retrieves collected orders from the user session and populates the point history table.
     */
    public void setCollectedOrders(){
        completeOrderList = FXCollections.observableArrayList();
        List<FinalizedOrder> orderHistory = session.getOrderHistory();
        // Filter collected orders
        List<FinalizedOrder> collectedOrders = orderHistory.stream()
                .filter(order -> order.getStatus() == FinalizedOrder.OrderStatus.COLLECTED ||
                        ((order.getStatus() == FinalizedOrder.OrderStatus.AWAIT_FOR_COLLECTION) && (order.getPointsUsed() > 0)))
                .toList();

        if (collectedOrders.isEmpty()){
            // If no collected orders, clear the table
            pointHistoryTable.setItems(FXCollections.observableArrayList());
        } else {
            // Populate the table with collected orders
            for (FinalizedOrder order : collectedOrders){
                setCompleteOrderObservableList(order);
            }
            FXCollections.sort(completeOrderList);
            associateCompleteOrderDataToColumn(completeOrderList);
        }
    }

    /*
     * Associates data from the complete order list to table columns.
     */
    private void associateCompleteOrderDataToColumn(ObservableList<FinalizedOrder> orders) {
        orderIDCol.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        orderDateCol.setCellValueFactory(new PropertyValueFactory<>("placedDate"));
        orderTimeCol.setCellValueFactory(new PropertyValueFactory<>("placedTime"));
        totalPriceCol.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        pointsUsedCol.setCellValueFactory(new PropertyValueFactory<>("pointsUsed"));
        pointsCollectedCol.setCellValueFactory(new PropertyValueFactory<>("pointsCollected"));

        setCustomCellFactory(orderIDCol);
        setCustomCellFactory(orderDateCol);
        setCustomCellFactory(orderTimeCol);
        setCustomCellFactory(totalPriceCol);
        setCustomCellFactory(pointsUsedCol);
        setCustomCellFactory(pointsCollectedCol);

        pointHistoryTable.setItems(orders);
    }

    /*
     * Sets a custom cell factory for a table column to highlight orders
     * that are still waiting to be collected but used points.
     */
    private <T> void setCustomCellFactory(TableColumn<FinalizedOrder, T> column) {
        column.setCellFactory(col -> new TableCell<FinalizedOrder, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    FinalizedOrder order = getTableView().getItems().get(getIndex());
                    if (order.getStatus() == FinalizedOrder.OrderStatus.AWAIT_FOR_COLLECTION) {
                        setStyle("-fx-background-color: orange;");
                    } else {
                        setStyle("");
                    }
                }
            }
        });
    }

    /*
     * Adds a finalized order to the complete order list.
     */
    public void setCompleteOrderObservableList(FinalizedOrder order){
        completeOrderList.add(order);
    }

}

package org.anneelv.burritokingv2.Controllers;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;
import org.anneelv.burritokingv2.Models.FinalizedOrder;

/*
 * A factory for creating table cells that display multiline text.
 */
public class MultilineCellFactory implements Callback<TableColumn<FinalizedOrder, String>, TableCell<FinalizedOrder, String>> {
    @Override
    public TableCell<FinalizedOrder, String> call(TableColumn<FinalizedOrder, String> param) {
        return new TableCell<FinalizedOrder, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    // Clear the cell if the item is null or empty
                    setText(null);
                } else {
                    // Replace comma separator with newline for multiline display
                    setText(item.replace(", ", "\n"));
                }
            }
        };
    }
}

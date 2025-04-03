module org.anneelv.burritokingv2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires javafx.graphics;


    opens org.anneelv.burritokingv2 to javafx.fxml;
    exports org.anneelv.burritokingv2;
    exports org.anneelv.burritokingv2.Controllers;
    exports org.anneelv.burritokingv2.Models;
    opens org.anneelv.burritokingv2.Controllers to javafx.fxml;
}
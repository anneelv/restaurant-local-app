package org.anneelv.burritokingv2.Controllers;

import org.anneelv.burritokingv2.Models.UserSession;

import java.io.IOException;


/*
 * Abstract class providing initialization methods for controller classes.
 */
abstract class ControllerInit {

    /*
     * Initializes the user session of each class that implements this
     */
    abstract void initUserSession(UserSession session) throws IOException;

    /*
     * Initializes the UI for each controllers that implements this
     */
    abstract void initializeUI();
}

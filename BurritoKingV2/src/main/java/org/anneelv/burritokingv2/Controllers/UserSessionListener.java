package org.anneelv.burritokingv2.Controllers;

import org.anneelv.burritokingv2.Models.Customer;

/*
 * Interface for listening to updates in user sessions.
 */
public interface UserSessionListener {
    /*
     * Callback method invoked when a user session is updated.
     */
    void onUserSessionUpdated(Customer updatedCustomer);
}

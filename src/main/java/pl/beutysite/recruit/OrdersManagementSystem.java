package pl.beutysite.recruit;

import pl.beutysite.recruit.orders.Order;

public interface OrdersManagementSystem {

    public void createOrder(int itemId, int customerId, OrderFlag... flags);
    public Order fetchNextOrder();
}

package pl.beutysite.recruit;

import pl.beutysite.recruit.orders.DiscountedOrder;
import pl.beutysite.recruit.orders.InternationalOrder;
import pl.beutysite.recruit.orders.Order;
import pl.beutysite.recruit.orders.PriorityOrder;

import java.math.BigDecimal;
import java.util.*;

public class OrdersManagementSystemImpl implements OrdersManagementSystem {

    //external systems
    private final TaxOfficeAdapter taxOfficeAdapter;
    private final ItemsRepository itemsRepository;

    private Set<Order> ordersQueue = new LinkedHashSet<>();
    private Order newOrder = null;

    public OrdersManagementSystemImpl(TaxOfficeAdapter taxOfficeAdapter, ItemsRepository itemsRepository) {
        this.taxOfficeAdapter = taxOfficeAdapter;
        this.itemsRepository = itemsRepository;
    }

    @Override
    public void createOrder(int itemId, int customerId, OrderFlag... flags) {

        //fetch price and calculate discount and taxes
        BigDecimal itemPrice = itemsRepository.fetchItemPrice(itemId);

        //Create order and add in queue
        OrderFlag flag = flags[0];
        switch (flag) {
            case STANDARD: newOrder = new Order(itemId,customerId,itemPrice); break;
            case PRIORITY: newOrder = new PriorityOrder(itemId,customerId,itemPrice); break;
            case INTERNATIONAL: newOrder = new InternationalOrder(itemId,customerId,itemPrice); break;
            case DISCOUNTED: newOrder = new DiscountedOrder(itemId,customerId,itemPrice); break;
        }

        ordersQueue.add(newOrder);

        // TODO: 01.03.17 should refactor it with change of use Iterator on Stream API and Lyambdas Java8
        //JIRA-18883 Fix priority orders not always being fetched first

        if (OrderFlag.PRIORITY.equals(flag)) {
            while (fetchNextOrder()!=newOrder) { // TODO: 01.03.17 Must fix this bug because we cannot compare to objects with != operator
                ordersQueue.remove(newOrder);
                newOrder=new PriorityOrder(itemId,customerId,itemPrice);
                ordersQueue.add(newOrder);
            }
            ordersQueue.add(newOrder);
        }


        //send tax due amount
        taxOfficeAdapter.registerTax(newOrder.getTax());
    }

    @Override
    public Order fetchNextOrder() {
        return ordersQueue.iterator().next();
    }
}

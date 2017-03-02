package pl.beutysite.recruit;

import pl.beutysite.recruit.orders.*;

import java.math.BigDecimal;
import java.util.*;

import static pl.beutysite.recruit.OrderFlag.*;

public class OrdersManagementSystemImpl implements OrdersManagementSystem {

    //external systems
    private final TaxOfficeAdapter taxOfficeAdapter;
    private final ItemsRepository itemsRepository;

    private Deque<Order> ordersQueue = new ArrayDeque<>();
    private Order newOrder = null;

    public OrdersManagementSystemImpl(TaxOfficeAdapter taxOfficeAdapter, ItemsRepository itemsRepository) {
        this.taxOfficeAdapter = taxOfficeAdapter;
        this.itemsRepository = itemsRepository;
    }
    @Override
    public void createOrder(int itemId, int customerId, OrderFlag... flags) {

        //fetch price and calculate discount and taxes
        BigDecimal itemPrice = itemsRepository.fetchItemPrice(itemId);

        List<OrderFlag> orderFlags = Arrays.asList(flags);

        if(orderFlags.size() == 1) {
            switch (orderFlags.get(0)) {
                case STANDARD:
                    newOrder = new Order(itemId, customerId, itemPrice, orderFlags);
                    break;
                case PRIORITY:
                    newOrder = new PriorityOrder(itemId, customerId, itemPrice, orderFlags);
                    break;
                case INTERNATIONAL:
                    newOrder = new InternationalOrder(itemId, customerId, itemPrice, orderFlags);
                    break;
                case DISCOUNTED:
                    newOrder = new DiscountedOrder(itemId, customerId, itemPrice, orderFlags);
                    break;
            }
        } else if(orderFlags.contains(STANDARD))
            newOrder = new Order(itemId, customerId, itemPrice, orderFlags);
        else
            newOrder = new SpecialOrder(itemId, customerId, itemPrice, orderFlags);

        if(orderFlags.contains(PRIORITY))
            ordersQueue = sortQueueByPriority(ordersQueue);
        else
            ordersQueue.offerLast(newOrder);

        //send tax due amount
        taxOfficeAdapter.registerTax(newOrder.getTax());
    }


    @Override
    public Order fetchNextOrder() {
        return ordersQueue.pollFirst();
    }


    private Deque<Order> sortQueueByPriority(Deque<Order> ordersQueue){
        Deque<Order> queuePriority = new ArrayDeque<>();
        Deque<Order> queueNoPriority = new ArrayDeque<>();

        ordersQueue.offerLast(newOrder);

        for (Order order : ordersQueue) {
            if (order.getOrderFlags().contains(PRIORITY))
                queuePriority.offerLast(order);
            else
                queueNoPriority.offerLast(order);
        }

        ordersQueue = new ArrayDeque<>();

        Deque<Order> finalOrdersQueue = ordersQueue;
        queuePriority.forEach(a -> finalOrdersQueue.offerLast(a));
        Deque<Order> finalOrdersQueue1 = ordersQueue;
        queueNoPriority.forEach(a -> finalOrdersQueue1.offerLast(a));
        return ordersQueue;
    }
}

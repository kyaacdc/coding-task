package pl.beutysite.recruit.service;

import pl.beutysite.recruit.orders.enums.OrderFlag;
import pl.beutysite.recruit.utils.TaxOfficeAdapter;
import pl.beutysite.recruit.orders.*;
import pl.beutysite.recruit.repositories.ItemsRepository;

import java.math.BigDecimal;
import java.util.*;

import static pl.beutysite.recruit.orders.enums.OrderFlag.*;

public class OrdersManagementSystemImpl implements OrdersManagementSystem {

    //external systems
    private final TaxOfficeAdapter taxOfficeAdapter;
    private final ItemsRepository itemsRepository;

    private Deque<Order> ordersQueue;
    private Order newOrder = null;

    public OrdersManagementSystemImpl(TaxOfficeAdapter taxOfficeAdapter, ItemsRepository itemsRepository) {
        this.taxOfficeAdapter = taxOfficeAdapter;
        this.itemsRepository = itemsRepository;
        ordersQueue = new ArrayDeque<>();
    }

    @Override
    public void createOrder(int itemId, int customerId, OrderFlag... flags) {

        List<OrderFlag> orderFlags = Arrays.asList(flags);

        newOrder = getOrderInstance(itemId, customerId, orderFlags);

        if (orderFlags.contains(PRIORITY))
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


    private Deque<Order> sortQueueByPriority(Deque<Order> ordersQueue) {
        Deque<Order> queuePriority = new ArrayDeque<>();
        Deque<Order> queueNoPriority = new ArrayDeque<>();

        ordersQueue.offerLast(newOrder);

        for (Order order : ordersQueue) {
            if (order.getOrderFlags().contains(PRIORITY))
                queuePriority.offerLast(order);
            else
                queueNoPriority.offerLast(order);
        }

        queueNoPriority.forEach(queuePriority::offerLast);

        return queuePriority;
    }

    private Order getOrderInstance(int itemId, int customerId, List<OrderFlag> orderFlags) {

        //fetch price and calculate discount and taxes
        BigDecimal itemPrice = itemsRepository.fetchItemPrice(itemId);

        if (orderFlags.size() == 1) {
            switch (orderFlags.get(0)) {
                case STANDARD:
                    return new Order(itemId, customerId, itemPrice, orderFlags);
                case PRIORITY:
                    return new PriorityOrder(itemId, customerId, itemPrice, orderFlags);
                case INTERNATIONAL:
                    return new InternationalOrder(itemId, customerId, itemPrice, orderFlags);
                case DISCOUNTED:
                    return new DiscountedOrder(itemId, customerId, itemPrice, orderFlags);
                default:
                    return new SpecialOrder(itemId, customerId, itemPrice, orderFlags);
            }
        }
        return new SpecialOrder(itemId, customerId, itemPrice, orderFlags);
    }
}

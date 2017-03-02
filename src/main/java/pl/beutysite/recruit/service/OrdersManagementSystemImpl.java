package pl.beutysite.recruit.service;

import pl.beutysite.recruit.orders.enums.OrderFlag;
import pl.beutysite.recruit.utils.TaxOfficeAdapter;
import pl.beutysite.recruit.orders.*;
import pl.beutysite.recruit.repositories.ItemsRepository;

import java.math.BigDecimal;
import java.util.*;

import static pl.beutysite.recruit.orders.enums.OrderFlag.*;

public class OrdersManagementSystemImpl implements OrdersManagementSystem {

    private final TaxOfficeAdapter taxOfficeAdapter;
    private final ItemsRepository itemsRepository;

    private Deque<Order> ordersQueue;
    private Order newOrder = null;

    public OrdersManagementSystemImpl(TaxOfficeAdapter taxOfficeAdapter, ItemsRepository itemsRepository) {
        this.taxOfficeAdapter = taxOfficeAdapter;
        this.itemsRepository = itemsRepository;
        ordersQueue = new ArrayDeque<>();
    }

    /**
     *   Method createOrder is create new Order and add it in
     *   queue by FIFO with sorting by priority if need. Also
     *   this method register tax in taxOfficeAdapter
     *   @param itemId with int type - item ID
     *   @param customerId with int type - customer ID
     *   @param flags with OrderFlag type - is vararg parameter
     *                for set order types
     */
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

    /**
     *   Method fetchNextOrder() get new Order from
     *   orders queue
     *   @return Order next object in queue of orders
     */
    @Override
    public Order fetchNextOrder() {
        return ordersQueue.pollFirst();
    }


    /**
     *   Method sortQueueByPriority is sort all queue
     *   of orders by FIFO and by priority
     *   @param ordersQueue with Deque<Order> type - this
     *                      queue that created only by FIFO
     *                      without sort by Priority
     *   @return Deque<Order> sorted queue by priority
     */
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

    /**
     *   Method getOrderInstance is choose instance type
     *   by OrderFlag and return new instance of Order for
     *   add into queue
     *   @param itemId with int type - item ID
     *   @param customerId with int type - customer ID
     *   @param orderFlags with List<OrderFlag> type - is parameter
     *                for set order types and choose type of Order
     *                     instanse
     *   @return Order that than we can add into queue
     */
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

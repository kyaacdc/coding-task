package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

public class Order {
    private final int itemId;
    private final int customerId;
    private final BigDecimal price;
    private List<OrderFlag> orderFlags;

    //for performance reasons lets pre-calculate it in constructor
    private int preCalculatedHashCode = 0;

    private static Random random = new Random();

    public Order(int itemId, int customerId, BigDecimal price, List<OrderFlag> orderFlags) {
        this.itemId = itemId;
        this.customerId = customerId;
        this.price = price;
        this.orderFlags = orderFlags;
        preCalculatedHashCode = random.nextInt();
    }

    public void process() {
        SeriousEnterpriseEventBus seeb = SeriousEnterpriseEventBusLookup.seeb;
        seeb.sendEvent("Order processing started");
        seeb.sendEvent("Initiate shipment");
        seeb.sendEvent("Order processing finished");
    }

    public BigDecimal getTotalAmount() {
        return price.add(getTax());
    }

    public int getItemId() {
        return itemId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public BigDecimal getTax() {
        //calculating standard tax - 23.5%
        return TaxCalculationsHelper.getPercentagePart(getPrice(), new BigDecimal("23.5"));
    }

    public List<OrderFlag> getOrderFlags() {
        return orderFlags;
    }

    public void setOrderFlags(List<OrderFlag> orderFlags) {
        this.orderFlags = orderFlags;
    }

    public int getPreCalculatedHashCode() {
        return preCalculatedHashCode;
    }

    public void setPreCalculatedHashCode(int preCalculatedHashCode) {
        this.preCalculatedHashCode = preCalculatedHashCode;
    }

    @Override
    public int hashCode() {
        return preCalculatedHashCode;
    }

}

package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
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

    /**
     * Method process should for execute/sent events: started,
     * checking, init shipment, and finalize
     */
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

    /**
     * Method getPrice needed for get price of order, that should for
     * simple standart order, by base price that situated in super class.'
     * And also get price depend from enum type of OrderFlag that can be with
     * STANDARD value
     *
     * @return BigDecimal value of price with include Percentage
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * Method getTax is override superClass and needed
     * for calculate tax value of order
     * OrderFlag must have value, such as PRIORITY,'
     * DISCOUNTED or STANDARD values and not contain INTERNATIONAL value
     * the tax will be get from super class and calculating like
     * standard tax - 23.5%
     *
     * @return BigDecimal value of tax with getPercentagePart
     * discount
     */
    public BigDecimal getTax() {
        return TaxCalculationsHelper.getPercentagePart(getPrice(), new BigDecimal("23.5"))
                .round(new MathContext(4, RoundingMode.CEILING));
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

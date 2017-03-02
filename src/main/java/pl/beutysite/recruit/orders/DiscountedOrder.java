package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;
import java.math.BigDecimal;
import java.util.List;

public class DiscountedOrder extends Order {

    public DiscountedOrder(int itemId, int customerId, BigDecimal price,List<OrderFlag> orderFlags) {
        super(itemId, customerId, price, orderFlags);
    }

    /**
     *   Method getPrice needed for get price of order, that should for
     *   standard discounted - 11%, by base price that situated in super class.
     *  @return BigDecimal value of price with substracted standart
     *  discount
     */
    @Override
    public BigDecimal getPrice() {
        return TaxCalculationsHelper.subtractPercentage(super.getPrice(), new BigDecimal("11"));
    }

    /**
     *   Method process should for execute/sent events: started,
     *   checking, init shipment, and finalize
     */
    @Override
    public void process() {
        SeriousEnterpriseEventBus seeb = SeriousEnterpriseEventBusLookup.seeb;
        seeb.sendEvent("Order processing started");

        seeb.sendEvent("Run fraud detection and revenue integrity check");

        seeb.sendEvent("Initiate shipment");
        seeb.sendEvent("Order processing finished");
    }
}

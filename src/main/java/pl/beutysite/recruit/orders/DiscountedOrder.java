package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;
import java.math.BigDecimal;
import java.util.List;

public class DiscountedOrder extends Order {

    public DiscountedOrder(int itemId, int customerId, List<OrderFlag> orderFlags, BigDecimal price) {
        super(itemId, customerId, orderFlags, price);
    }

    @Override
    public BigDecimal getPrice() {
        //subtracting standard discount - 11%
        return TaxCalculationsHelper.subtractPercentage(super.getPrice(), new BigDecimal("11"));
    }

    @Override
    public void process() {
        SeriousEnterpriseEventBus seeb = SeriousEnterpriseEventBusLookup.seeb;
        seeb.sendEvent("Order processing started");

        seeb.sendEvent("Run fraud detection and revenue integrity check");

        seeb.sendEvent("Initiate shipment");
        seeb.sendEvent("Order processing finished");
    }
}

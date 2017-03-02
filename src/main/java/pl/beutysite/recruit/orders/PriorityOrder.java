package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.OrderFlag;
import pl.beutysite.recruit.SeriousEnterpriseEventBus;
import pl.beutysite.recruit.SeriousEnterpriseEventBusLookup;
import pl.beutysite.recruit.TaxCalculationsHelper;

import java.math.BigDecimal;
import java.util.List;

public class PriorityOrder extends Order {

    public PriorityOrder(int itemId, int customerId, BigDecimal price,List<OrderFlag> orderFlags) {
        super(itemId, customerId, price, orderFlags);
    }

    /**
     *   Method getPrice needed for get price of order, that should be
     *   priority, by base price that situated in super class.
     *   This method increase you price on 1,5 %
     *  @return BigDecimal value of price with add Percentage.
     *  discount
     */
    @Override
    public BigDecimal getPrice() {
        //adding priority order fee - 1.5%
        return TaxCalculationsHelper.addPercentage(super.getPrice(),new BigDecimal("1.5"));
    }

    /**
     *   Method process should for execute/sent events: started,
     *   checking, init shipment, and finalize
     */
    public void process() {
        SeriousEnterpriseEventBus seeb = SeriousEnterpriseEventBusLookup.seeb;
        seeb.sendEvent("Order processing started");

        seeb.sendEvent("*** This is priority order, hurry up! ***");

        seeb.sendEvent("Initiate shipment");
        seeb.sendEvent("Order processing finished");
    }
}

package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.orders.enums.OrderFlag;
import pl.beutysite.recruit.service.SeriousEnterpriseEventBus;
import pl.beutysite.recruit.service.SeriousEnterpriseEventBusLookup;
import pl.beutysite.recruit.utils.TaxCalculationsHelper;

import java.math.BigDecimal;
import java.util.List;

public class InternationalOrder extends Order {

    public InternationalOrder(int itemId, int customerId, BigDecimal price,List<OrderFlag> orderFlags) {
        super(itemId, customerId, price, orderFlags);
    }

    /**
     *   Method getTax is override superClass and
     *   needed for calculate tax value of order, that should be
     *   international type with 11% of tax, by calculated price.
     *  @return BigDecimal value of tax with getPercentagePart
     *  discount
     */
    @Override
    public BigDecimal getTax() {
        //calculating international tax - 15.0% 
        return TaxCalculationsHelper.getPercentagePart(getPrice(), new BigDecimal("15.0"));
    }

    /**
     *   Method process should for execute/sent events: started,
     *   checking, init shipment, and finalize
     */
    @Override
    public void process() {
        SeriousEnterpriseEventBus seeb = SeriousEnterpriseEventBusLookup.seeb;
        seeb.sendEvent("Order processing started");

        seeb.sendEvent("Dispatch translated order confirmation email");

        seeb.sendEvent("Initiate shipment");
        seeb.sendEvent("Order processing finished");
    }
}

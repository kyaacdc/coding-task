package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;
import java.math.BigDecimal;
import java.util.List;

//TODO will have more taxes calculated
public class InternationalOrder extends Order {

    public InternationalOrder(int itemId, int customerId, BigDecimal price,List<OrderFlag> orderFlags) {
        super(itemId, customerId, price, orderFlags);
    }

    //Get value of tax from base price
    @Override
    public BigDecimal getTax() {
        //calculating international tax - 15.0% 
        return TaxCalculationsHelper.getPercentagePart(getPrice(), new BigDecimal("15.0"));
    }

    @Override
    public void process() {
        SeriousEnterpriseEventBus seeb = SeriousEnterpriseEventBusLookup.seeb;
        seeb.sendEvent("Order processing started");

        seeb.sendEvent("Dispatch translated order confirmation email");

        seeb.sendEvent("Initiate shipment");
        seeb.sendEvent("Order processing finished");
    }
}

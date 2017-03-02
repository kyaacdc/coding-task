package pl.beutysite.recruit.orders;

import pl.beutysite.recruit.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import static pl.beutysite.recruit.OrderFlag.*;
import static pl.beutysite.recruit.TaxCalculationsHelper.*;

public class SpecialOrder extends Order{

    public SpecialOrder(int itemId, int customerId, List<OrderFlag> orderFlags, BigDecimal price) {
        super(itemId, customerId, orderFlags, price);
    }

    @Override
    public BigDecimal getPrice() {

        BigDecimal priceDISCOUNTED = subtractPercentage(super.getPrice(), new BigDecimal("11"));
        BigDecimal pricePRIORITY = addPercentage(super.getPrice(), new BigDecimal("1.5"));
        BigDecimal priceINTERNATIONAL = super.getPrice();

        if(getOrderFlags().containsAll(Arrays.asList(OrderFlag.values())))
            return getMiddlePercentageAll(priceDISCOUNTED, pricePRIORITY, priceINTERNATIONAL);
        else if(getOrderFlags().contains(DISCOUNTED) && getOrderFlags().contains(PRIORITY))
            return getMiddlePercentage(priceDISCOUNTED, pricePRIORITY);
        else if(getOrderFlags().contains(DISCOUNTED) && getOrderFlags().contains(INTERNATIONAL))
            return getMiddlePercentage(priceDISCOUNTED, priceINTERNATIONAL);
        else if(getOrderFlags().contains(PRIORITY) && getOrderFlags().contains(INTERNATIONAL))
            return getMiddlePercentage(pricePRIORITY, priceINTERNATIONAL);
        else if(getOrderFlags().contains(DISCOUNTED))
            return priceDISCOUNTED;
        else if(getOrderFlags().contains(PRIORITY))
            return pricePRIORITY;
        else if(getOrderFlags().contains(INTERNATIONAL))
            return priceINTERNATIONAL;
        else
            return super.getPrice();
    }

    @Override
    public BigDecimal getTax() {
        if(getOrderFlags().contains(INTERNATIONAL))
            return TaxCalculationsHelper.getPercentagePart(getPrice(), new BigDecimal("15.0"));
        return super.getTax();
    }
}

package pl.beutysite.recruit;

import java.math.BigDecimal;

public interface ItemsRepository {

    public BigDecimal fetchItemPrice(int itemId);
}

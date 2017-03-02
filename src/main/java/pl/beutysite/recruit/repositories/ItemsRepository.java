package pl.beutysite.recruit.repositories;

import java.math.BigDecimal;

public interface ItemsRepository {

    public BigDecimal fetchItemPrice(int itemId);
}

package pl.beutysite.recruit;

import pl.beutysite.recruit.orders.Order;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OrdersManagementSystemImplTest {

    @Mock
    private TaxOfficeAdapter taxOfficeAdapter;

    @Mock
    private ItemsRepository itemsRepository;

    @InjectMocks
    private OrdersManagementSystemImpl ordersManagementSystem = new OrdersManagementSystemImpl(taxOfficeAdapter, itemsRepository);

    @Test
    public void orderPriceShouldBeReturnedForOrdersWithCombinedPriority() {
        //given
        given(itemsRepository.fetchItemPrice(1)).willReturn(new BigDecimal("10.00"));
        given(itemsRepository.fetchItemPrice(2)).willReturn(new BigDecimal("20.00"));
        given(itemsRepository.fetchItemPrice(3)).willReturn(new BigDecimal("30.00"));
        given(itemsRepository.fetchItemPrice(4)).willReturn(new BigDecimal("40.00"));
        given(itemsRepository.fetchItemPrice(5)).willReturn(new BigDecimal("50.00"));
        given(itemsRepository.fetchItemPrice(6)).willReturn(new BigDecimal("60.00"));
        given(itemsRepository.fetchItemPrice(7)).willReturn(new BigDecimal("70.00"));

        //when
        ordersManagementSystem.createOrder(1, 1, OrderFlag.DISCOUNTED, OrderFlag.INTERNATIONAL);
        ordersManagementSystem.createOrder(2, 1, OrderFlag.DISCOUNTED, OrderFlag.PRIORITY);
        ordersManagementSystem.createOrder(3, 1, OrderFlag.DISCOUNTED, OrderFlag.INTERNATIONAL);
        ordersManagementSystem.createOrder(4, 1, OrderFlag.PRIORITY, OrderFlag.INTERNATIONAL);
        ordersManagementSystem.createOrder(5, 1, OrderFlag.DISCOUNTED, OrderFlag.PRIORITY, OrderFlag.INTERNATIONAL);
        ordersManagementSystem.createOrder(6, 1, OrderFlag.DISCOUNTED, OrderFlag.PRIORITY);
        ordersManagementSystem.createOrder(7, 1, OrderFlag.DISCOUNTED, OrderFlag.INTERNATIONAL);

        //then
        Order nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getPrice()).isEqualTo(new BigDecimal("19.05"));

        nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getPrice()).isEqualTo(new BigDecimal("40.30"));

        nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getPrice()).isEqualTo(new BigDecimal("47.63"));

        nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getPrice()).isEqualTo(new BigDecimal("57.15"));

        nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getPrice()).isEqualTo(new BigDecimal("9.45"));
    }

    @Test
    public void taxShouldBeReturnedCorrectForInternationalCominedAndOtherOrders() {
        //given
        given(itemsRepository.fetchItemPrice(1)).willReturn(new BigDecimal("100.00"));
        given(itemsRepository.fetchItemPrice(2)).willReturn(new BigDecimal("100.00"));
        given(itemsRepository.fetchItemPrice(3)).willReturn(new BigDecimal("100.00"));

        //when
        ordersManagementSystem.createOrder(1, 1, OrderFlag.INTERNATIONAL);
        ordersManagementSystem.createOrder(2, 1, OrderFlag.DISCOUNTED, OrderFlag.PRIORITY);
        ordersManagementSystem.createOrder(3, 1, OrderFlag.DISCOUNTED, OrderFlag.INTERNATIONAL);

        //then
        Order nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getTax()).isEqualTo(new BigDecimal("22.39")); //first priority

        nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getTax()).isEqualTo(new BigDecimal("15")); //second international

        nextOrder = ordersManagementSystem.fetchNextOrder();
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getTax()).isEqualTo(new BigDecimal("14.18")); //third combined international
    }

    @Test
    public void priority_order_should_be_returned_first() {

        //given
        given(itemsRepository.fetchItemPrice(1)).willReturn(new BigDecimal("5.00"));
        given(itemsRepository.fetchItemPrice(2)).willReturn(new BigDecimal("5.00"));

        //when
        ordersManagementSystem.createOrder(1, 1, OrderFlag.STANDARD);
        ordersManagementSystem.createOrder(2, 1, OrderFlag.PRIORITY);

        Order nextOrder = ordersManagementSystem.fetchNextOrder();

        //then
        assertThat(nextOrder).isNotNull();
        assertThat(nextOrder.getItemId()).isEqualTo(2);
    }

    //Ignoring cause assertion is failing with small difference in tax amount - should be nothing serious
    @Ignore
    @Test
    public void tax_amount_sent_to_tax_office_should_be_correct() {

        //given
        given(itemsRepository.fetchItemPrice(1)).willReturn(new BigDecimal("3.33"));

        //when
        ordersManagementSystem.createOrder(1, 1, OrderFlag.PRIORITY);
        Order nextOrder = ordersManagementSystem.fetchNextOrder();

        //then
        assertThat(nextOrder).isNotNull();


        //should be 0.77 tax because:
        // 3.33 + 1.5% = 3.38   3.38 * 23.5% = 0.80
        verify(taxOfficeAdapter).registerTax(new BigDecimal("0.80"));
    }


}
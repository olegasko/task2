package ua.skillsup.practice;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Spy;
import org.mockito.runners.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(MockitoJUnitRunner.class)
public class ExampleServiceImplTest {

    public static final String PROPER_TITLE = "proper title";
    public static final BigDecimal PRICE_FOR_ROUND  = BigDecimal.valueOf(16.568);
    public static final BigDecimal PRICE_LESS_THAN_LOWER_LIMIT = BigDecimal.valueOf(14.49);
    public static final BigDecimal NULL = null;
    public static final BigDecimal PROPER_PRICE = BigDecimal.valueOf(15);
    public static final String TITLE_NOT_UNIQUE = "Title not unique";
    public static final String TOO_LONG_TITLE = "Too long title - 11111111111111";

    @Spy
    ExampleDaoImpl dao;

    ExampleService service;

    @Before
    public void before() {
        service = new ExampleServiceImpl(dao);
    }

    @Test
    public void shouldRoundWhenPriceHasMoreDecimalPoints() {
        service.addNewItem(PROPER_TITLE, PRICE_FOR_ROUND);
        verify(dao).store(any());
    }

    @Test
    public void shouldNotStoreWhenPriceLessThenLowerLimit() {
        service.addNewItem(PROPER_TITLE, PRICE_LESS_THAN_LOWER_LIMIT);
        verify(dao, times(0)).store(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenPriceIsNull() {
        service.addNewItem(PROPER_TITLE, NULL);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionWhenTitleIsNotUnique() {
        service.addNewItem(TITLE_NOT_UNIQUE, PROPER_PRICE);
        service.addNewItem(TITLE_NOT_UNIQUE, PROPER_PRICE);
    }

    @Test
    public void shouldStoreWhenTitleLengthInRange() {
        service.addNewItem(PROPER_TITLE, PROPER_PRICE);
        verify(dao, times(1)).store(any());
    }

    @Test
    public void shouldNotStoreWhenTitleLengthOutOfRange() {
        service.addNewItem(TOO_LONG_TITLE, PROPER_PRICE);
        verify(dao, times(0)).store(any());
    }
}

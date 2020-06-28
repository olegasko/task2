package ua.skillsup.practice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

public class ExampleServiceImpl implements ExampleService {

    private ExampleDaoImpl dao;
    private ExampleEntity entity;

    public ExampleServiceImpl(ExampleDaoImpl dao, ExampleEntity entity) {
        this.dao = dao;
        this.entity = entity;
    }

    @Override
    public void addNewItem(String title, BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException("Price shouldn't be null");
        }
        if (title.length() < 3 || title.length() > 20) {
            return;
        }
        if (price.scale() > 2) {
            price = price.setScale(2, RoundingMode.HALF_UP);
        }
        if (price.longValue() < 15) {
            return;
        }
        this.entity.setId(new Random().nextLong());
        this.entity.setTitle(title);
        this.entity.setDateIn(Instant.now());
        this.entity.setPrice(price);
        if(!dao.store(entity)) {
            throw new IllegalArgumentException("Title is should be unique");
        }
    }

    /**
     * Prepare storage statistic of items average prices per day they were added.
     * In case no items were added in concrete day - the day shouldn't be present in the final result
     *
     * @return {@link Map} of statistic results, where key is the day when items were stored, and
     * the value is actual average cost of all items stored during that day
     */

    @Override
    public Map<LocalDate, BigDecimal> getStatistic() {
        Map<LocalDate, Double> groupedEntities
                = dao
                .findAll()
                .stream()
                .collect(
                        Collectors.groupingBy(entity -> LocalDate.ofInstant(entity.getDateIn(), ZoneId.systemDefault()),
                                Collectors.averagingDouble(entity -> entity.getPrice().doubleValue())));
        return groupedEntities
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> BigDecimal.valueOf(entry.getValue())));
    }
}

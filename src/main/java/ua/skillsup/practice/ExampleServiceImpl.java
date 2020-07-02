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

    public ExampleServiceImpl(ExampleDaoImpl dao) {
        this.dao = dao;
    }

    @Override
    public void addNewItem(String title, BigDecimal price) {
        if(price == null) {
            throw new IllegalArgumentException("Price shouldn't be null");
        }
        if (title.length() < 3 || title.length() > 20) {
            throw new IllegalArgumentException("Tittle length should be from 3 to 20");
        }
        if (price.scale() > 2) {
            price = price.setScale(2, RoundingMode.HALF_UP);
        }
        if (price.longValue() < 15) {
            throw new IllegalArgumentException("Price should be 15 or more");
        }
        ExampleEntity entity = new ExampleEntity();
        entity.setId(new Random().nextLong());
        entity.setTitle(title);
        entity.setDateIn(Instant.now());
        entity.setPrice(price);
        if(!dao.store(entity)) {
            throw new IllegalArgumentException("Title is should be unique");
        }
    }

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

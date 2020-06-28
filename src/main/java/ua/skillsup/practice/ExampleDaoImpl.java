package ua.skillsup.practice;

import java.util.ArrayList;
import java.util.List;

public class ExampleDaoImpl implements ExampleDao {

    List<ExampleEntity> entities = new ArrayList<>();

    @Override
    public boolean store(ExampleEntity entity) throws ExampleNetworkException {
        if (!entities.contains(entity)) {
            entities.add(entity);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<ExampleEntity> findAll() throws ExampleNetworkException {
        return entities;
    }
}

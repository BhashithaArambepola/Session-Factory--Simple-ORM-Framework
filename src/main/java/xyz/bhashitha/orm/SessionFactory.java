package xyz.bhashitha.orm;

import xyz.bhashitha.orm.annotation.Entity;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class SessionFactory {

    private final List<Class<?>> entityClassList =new ArrayList<>();
    private Connection connection;

    public SessionFactory addAnnotatedClass(Class<?> entityClass){
        if (entityClass.getDeclaredAnnotation(Entity.class)== null){
            throw new RuntimeException("Invalid Entity Class");
        }
        entityClassList.add(entityClass);
        return this;
    }

    public SessionFactory setConnection(Connection connection){
        this.connection=connection;
        return this;
    }




}

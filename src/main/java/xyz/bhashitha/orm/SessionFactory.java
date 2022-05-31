package xyz.bhashitha.orm;

import xyz.bhashitha.orm.annotation.Entity;
import xyz.bhashitha.orm.annotation.Id;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


/**
 * SessionFactory is the starting point of the ORM
 *
 * @author bhashitha
 * @since 1.0.0
 */

public class SessionFactory {

    private final List<Class<?>> entityClassList = new ArrayList<>();
    private Connection connection;

    /**
     * Add classes that have been annotated with <code>@Entity</code> annotation
     *
     * @param entityClass
     * @return SessionLibrary
     * @throws RuntimeException if the class is not annotated with <code>@Entity</code> annotation
     */

    public SessionFactory addAnnotatedClass(Class<?> entityClass) {
        if (entityClass.getDeclaredAnnotation(Entity.class) == null) {
            throw new RuntimeException("Invalid Entity Class");
        }
        entityClassList.add(entityClass);
        return this;
    }

    /**
     * Set the connection
     *
     * @param connection
     * @return
     */

    public SessionFactory setConnection(Connection connection) {
        this.connection = connection;
        return this;
    }

    /**
     * Validate whether everything work ok
     *
     * @return SessionFactory
     * @throws RuntimeException
     */

    public SessionFactory build() {
        if (this.connection == null) {
            throw new RuntimeException("Failed to build without a connection");
        }
        return this;
    }

    /**
     * Bootstrap the ORM framework and create table
     *
     * @throws SQLException
     */
    public void bootstrap() throws SQLException {
        for (Class<?> entity : entityClassList) {
            String tableName = entity.getDeclaredAnnotation(Entity.class).value();
            if (tableName.trim().isEmpty()) tableName = entity.getSimpleName();

            List<String> columns = new ArrayList<>();
            String primaryKey = null;

            Field[] fields = entity.getDeclaredFields();
            for (Field field : fields) {
                Id primaryKeyField = field.getDeclaredAnnotation(Id.class);
                if (primaryKeyField != null) {
                    primaryKey = field.getName();
                    continue;
                }

                String columnName = field.getName();
                columns.add(columnName);
            }
            if (primaryKey == null) throw new RuntimeException("Entity without a primary key");

            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS ").append(tableName).append("(");
            for (String column : columns) {
                sb.append(column).append(" VARCHAR(255),");
            }
            sb.append(primaryKey).append(" VARCHAR(255) PRIMARY KEY)");
            System.out.println(sb);
            Statement stm = connection.createStatement();
            stm.execute(sb.toString());
        }
    }


}

package xyz.bhashitha.orm;

import xyz.bhashitha.orm.annotation.Entity;
import xyz.bhashitha.orm.annotation.Id;

import java.sql.Connection;
import java.sql.DriverManager;


class SessionFactoryTest {
    public static void main(String[] args) throws Exception {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dep_session?createDatabaseIfNotExist=true", "root", "mysql");

        new SessionFactory()
                .addAnnotatedClass(Teacher.class)
                .addAnnotatedClass(Student.class)
                .setConnection(connection)
                .build()
                .bootstrap();
    }
}


@Entity
class Teacher{
    @Id
    private String id;
    private String name;
    private String address;
}
@Entity
class Student{
    @Id
    private String registrationIndex;
    private String name;
    private String grade;
}
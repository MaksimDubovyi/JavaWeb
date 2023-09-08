package step.learning.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class User {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private int age;
    private String avatar;
    private String PasswordHash;

    public User() {
        setId(null);
    }

    public User(String firstName, String lastName, String email,String phone, String avatar, String PasswordHash,int age) {
        this.firstName =firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.avatar = avatar;
        this.PasswordHash = PasswordHash;
        this.age = age;
    }

    public User(ResultSet resultSet) throws SQLException {

        setId(UUID.fromString( resultSet.getString("id")));
        setFirstName(resultSet.getString("firstName"));
        setLastName(resultSet.getString("lastName"));
        setEmail(resultSet.getString("email"));
        setPhone(resultSet.getString("phone"));
        setAge(resultSet.getInt("age"));
        setAvatar(resultSet.getString("avatar"));
        setPasswordHash(resultSet.getString("PasswordHash"));
    }

    @Override
    public String toString() {
        return String.format("{id: '%s', firstName '%s', lastName: %s email: %s ,phone: '%s', age '%d'}",
                                getId(), getFirstName(), getLastName(),  getEmail() ,  getPhone(),  getAge());
    }



    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPasswordHash() {
        return PasswordHash;
    }

    public void setPasswordHash(String passwordHash) {
        PasswordHash = passwordHash;
    }
}

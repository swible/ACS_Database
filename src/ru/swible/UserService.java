package ru.swible;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import ru.swible.pojo.User;

import java.util.List;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    private DBService dbService;

    public UserService(DBService dbService) {
        this.dbService = dbService;
    }

    public User findById(Long id) {
        try (Connection connection = dbService.getSql2o().open()) {
            return connection.createQuery("select id, login, firstName, sureName, lastName from user " +
                    "where id = :id")
                    .addParameter("id", id)
                    .executeAndFetchFirst(User.class);
        } catch (Exception e) {
            logger.error("load user error id = " + id, e);
            throw new RuntimeException(e);
        }
    }

    public User add(User userFromClient) {
        String sql = "insert into user (login, firstName, sureName, lastName) " +
                "values (:login, :firstName, :sureName, :lastName)";
        try (Connection connection = dbService.getSql2o().open()) {
            Long id = connection.createQuery(sql)
                    .addParameter("login", userFromClient.getLogin())
                    .addParameter("firstName", userFromClient.getFirstName())
                    .addParameter("sureName", userFromClient.getSureName())
                    .addParameter("lastName", userFromClient.getLastName())
                    .executeUpdate()
                    .getKey(Long.class);


            logger.info("Added user: {}", userFromClient);
            userFromClient.setId(id);
            return userFromClient;
        } catch (Exception e) {
            logger.error("add user error", e);
            throw new RuntimeException(e);
        }
    }

    public User update(Long id, User userFromClient) {
        User user = findById(id);

        String login = userFromClient.getLogin();
        String firstName = userFromClient.getFirstName();
        String sureName = userFromClient.getSureName();
        String lastName = userFromClient.getLastName();

        if (login != null) {
            user.setLogin(login);
        }
        if (firstName != null) {
            user.setFirstName(firstName);
        }
        if (sureName != null) {
            user.setSureName(sureName);
        }
        if (lastName != null) {
            user.setLastName(lastName);
        }

        String sql = "update user set login = :login, firstName = :firstName, sureName = :sureName, lastName = :lastName " +
                "where id = :id";
        try (Connection connection = dbService.getSql2o().open()) {
            connection.createQuery(sql)
                    .addParameter("id", user.getId())
                    .addParameter("login", user.getLogin())
                    .addParameter("firstName", user.getFirstName())
                    .addParameter("sureName", user.getSureName())
                    .addParameter("lastName", user.getLastName())
                    .executeUpdate();
        } catch (Exception e) {
            logger.error("change user error", e);
            throw new RuntimeException(e);
        }
        return user;
    }


    public void delete(Long id) {
        String sql = "delete from user where id = :id";
        try (Connection connection = dbService.getSql2o().open()) {
            connection.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
        } catch (Exception e) {
            logger.error("change user error", e);
            throw new RuntimeException(e);
        }
    }

    public List findAll() {
        try (Connection connection = dbService.getSql2o().open()) {
            return connection.createQuery("select id, login, firstName, sureName, lastName from user")
                    .executeAndFetch(User.class);
        } catch (Exception e) {
            logger.error("load users error", e);
            throw new RuntimeException(e);
        }
    }
}
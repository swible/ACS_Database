package ru.swible;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import ru.swible.pojo.SqlExecuteResult;
import ru.swible.pojo.User;

public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static DBService dbService;
    private static UserService userService;
    private static ObjectMapper om = new ObjectMapper();

    public static void main(String[] args) {
        dbService = new DBService();
        userService = new UserService(dbService);


        // Start embedded server at this port
        spark.Spark.port(8080);
        spark.Spark.staticFileLocation("/");

        // Main Page, welcome
        //spark.Spark.get("/", (request, response) -> "Welcome");

        // POST - Add an user
        spark.Spark.post("/user", (request, response) -> {

            User userFromClient = om.readValue(request.body(), User.class);

            User user = userService.add(userFromClient);

            response.status(201); // 201 Created
            response.header("Content-Type", "application/json");
            return om.writeValueAsString(user);
        });

        spark.Spark.post("/sqlExecute", (request, response) -> {
            String sql = request.body();
            response.header("Content-Type", "application/json");
            try (Connection connection = dbService.getSql2o().open()) {
                connection.createQuery(sql)
                        .executeUpdate();
                SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
                sqlExecuteResult.setOk(true);
                return om.writeValueAsString(sqlExecuteResult);
            } catch (Exception e) {
                logger.error("execute sql error", e);
                SqlExecuteResult sqlExecuteResult = new SqlExecuteResult();
                sqlExecuteResult.setOk(false);
                sqlExecuteResult.setErrorText(e.getMessage());
                return om.writeValueAsString(sqlExecuteResult);
            }
        });

        // GET - Give me user with this id
        spark.Spark.get("/user/:id", (request, response) -> {
            User user = userService.findById(new Long(request.params(":id")));
            if (user != null) {
                response.header("Content-Type", "application/json");
                return om.writeValueAsString(user);
            } else {
                response.status(404); // 404 Not found
                return om.writeValueAsString("user not found");
            }
        });

        // Get - Give me all users
        spark.Spark.get("/user", (request, response) -> {
            response.header("Content-Type", "application/json");
            return om.writeValueAsString(userService.findAll());
        });

        // PUT - Update user
        spark.Spark.put("/user/:id", (request, response) -> {
            String id = request.params(":id");
            Long idLong = Long.valueOf(id);
            User user = userService.findById(idLong);
            if (user != null) {
                User userFromClient = om.readValue(request.body(), User.class);
                User userUpdated = userService.update(idLong, userFromClient);
                response.header("Content-Type", "application/json");
                return om.writeValueAsString(userUpdated);
            } else {
                response.status(404);
                return om.writeValueAsString("user not found");
            }
        });

        // DELETE - delete user
        spark.Spark.delete("/user/:id", (request, response) -> {
            String id = request.params(":id");
            Long idLong = Long.valueOf(id);
            User user = userService.findById(idLong);
            if (user != null) {
                userService.delete(idLong);
                return om.writeValueAsString("user with id " + id + " is deleted!");
            } else {
                response.status(404);
                return om.writeValueAsString("user not found");
            }
        });
    }
}

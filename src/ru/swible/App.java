package ru.swible;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.ResultSetHandler;
import ru.swible.pojo.SqlResult;
import ru.swible.pojo.SqlSelectResult;
import ru.swible.pojo.User;

import java.sql.ResultSetMetaData;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

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
                SqlResult sqlExecuteResult = new SqlResult();
                sqlExecuteResult.setOk(true);
                return om.writeValueAsString(sqlExecuteResult);
            } catch (Exception e) {
                logger.error("execute sql error", e);
                SqlResult sqlExecuteResult = new SqlResult();
                sqlExecuteResult.setOk(false);
                sqlExecuteResult.setErrorText(e.getMessage());
                return om.writeValueAsString(sqlExecuteResult);
            }
        });

        spark.Spark.post("/sqlSelect", (request, response) -> {
            String sql = request.body();
            response.header("Content-Type", "application/json");
            try (Connection connection = dbService.getSql2o().open()) {
                List<String> colNames = new LinkedList<>();
                AtomicBoolean haveMetadata = new AtomicBoolean();
                List<List<String>> result = connection.createQuery(sql)
                        .executeAndFetch((ResultSetHandler<List<String>>) rs ->  {
                            if (!haveMetadata.get()) {
                                ResultSetMetaData metaData = rs.getMetaData();
                                int columnCount = metaData.getColumnCount();
                                for (int i = 1; i <= columnCount; i++) {
                                    colNames.add(metaData.getColumnLabel(i));
                                }
                                haveMetadata.set(true);
                            }
                            List<String> rowResult = new LinkedList<>();
                            for (int i = 1; i <= colNames.size(); i++) {
                               rowResult.add(rs.getString(i));
                            }
                            return rowResult;
                        });
                SqlSelectResult sqlExecuteResult = new SqlSelectResult();
                sqlExecuteResult.setOk(true);
                sqlExecuteResult.setColNames(colNames);
                sqlExecuteResult.setColValues(result);
                return om.writeValueAsString(sqlExecuteResult);
            } catch (Exception e) {
                logger.error("execute sql error", e);
                SqlResult sqlResult = new SqlResult();
                sqlResult.setOk(false);
                sqlResult.setErrorText(e.getMessage());
                return om.writeValueAsString(sqlResult);
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

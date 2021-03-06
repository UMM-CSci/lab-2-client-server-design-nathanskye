package umm3601;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import umm3601.user.UserController;
import umm3601.todo.TodoController;

import java.io.IOException;

import static spark.Spark.*;

public class Server {
    public static void main(String[] args) throws IOException {
        staticFiles.location("/public");
        Gson gson = new Gson();
        UserController userController = new UserController();
        TodoController todoController = new TodoController();

        // Simple example route
        get("/hello", (req, res) -> "Hello world!");

        // Redirects for the "home" page
        redirect.get("", "/");
        redirect.get("/", "/index.html");

        // Redirect for the "about" page
        redirect.get("/about", "/about.html");

        // Redirect for the Users Form
        redirect.get("/users", "/users.html");

        // Redirector for the Todos form
        redirect.get("/todo", "/todo.html");

        // For a question mark question
        get("/kittens", (req, res) -> {
            return "meow";
        });

        // List users
        get("api/users", (req, res) -> {
            res.type("application/json");
            return wrapInJson("users", gson.toJsonTree(userController.listUsers(req.queryMap().toMap())));
        });

        // See specific user
        get("api/users/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return gson.toJson(userController.getUser(id));
        });

        //
        get("api/todos", (req, res) -> {
            res.type("application/json");
            return wrapInJson("todos", gson.toJsonTree(todoController.listTodos(req.queryMap().toMap())));
        });

        // See specific todo
        get("api/todos/:id", (req, res) -> {
            res.type("application/json");
            String id = req.params("id");
            return gson.toJson(todoController.getTodo(id));
        });
    }

    public static JsonObject wrapInJson(String name, JsonElement jsonElement) {
        JsonObject result = new JsonObject();
        result.add(name, jsonElement);
        return result;
    }

}

package demo.todos.resource;

import demo.todos.features.create.command.CreateTodo;
import demo.todos.features.delete.command.DeleteTodo;
import demo.todos.features.findByUser.query.FindTodosByUser;
import demo.todos.features.updateStatus.command.UpdateStatusCommand;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;

import java.net.URI;
import java.net.URISyntaxException;

@Path("/todos")
@RequiredArgsConstructor(onConstructor = @__(@Inject))
@Produces(MediaType.APPLICATION_JSON) // Automatically converts return objects to JSON
@Consumes(MediaType.APPLICATION_JSON)
public class TodoResource {

    private final Poshtar poshtar;

    @GET
    @Path("/user/{id}")
    public Response findTodosByUser(@PathParam("id") Long userId) {
        var response = poshtar.send(new FindTodosByUser(userId));
        return Response.ok(response).build();
    }

    @POST
    public Response createTodo(CreateTodo command) throws URISyntaxException {
        poshtar.send(command);
        // Returning 201 Created with a URI
        return Response.created(new URI("/api/todos")).build();
    }

    @DELETE
    @Path("/{userId}/{todoId}")
    public Response deleteTodo(
            @PathParam("userId") Long userId,
            @PathParam("todoId") Long todoId) throws URISyntaxException {
        var command = new DeleteTodo(userId, todoId);
        poshtar.send(command);
        return Response.created(new URI("")).build();
    }

    @PUT
    public Response updateTodoStatus(UpdateStatusCommand command) {
        poshtar.send(command);
        return Response.ok().build();

    }
}

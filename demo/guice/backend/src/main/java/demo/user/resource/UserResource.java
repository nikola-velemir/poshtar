package demo.user.resource;

import jakarta.inject.Inject;
import demo.user.features.activate.command.ActivateUserCommand;
import demo.user.features.getById.query.GetUserByIdQuery;
import demo.user.features.login.query.LoginQuery;
import demo.user.features.register.command.RegisterCommand;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import org.nikola.velemir.poshtar.core.mediator.Poshtar;

@Path("/users")
@RequiredArgsConstructor(onConstructor = @__(@Inject)) // This is the magic line
@Produces(MediaType.APPLICATION_JSON) // Automatically converts return objects to JSON
@Consumes(MediaType.APPLICATION_JSON) // Automatically parses incoming JSON to Objects
public class UserResource {

    private final Poshtar poshtar;

    @GET
    @Path("/{id}")
    public Response findUser(@PathParam("id") Long id) {
        var response = poshtar.send(new GetUserByIdQuery(id));
        return Response.ok(response).build();
    }
    @POST
    @Path("/login")
    public Response login(LoginQuery loginQuery){
        var response = poshtar.send(loginQuery);
        return Response.ok(response).build();
    }
    @POST
    @Path("/register")
    public Response register(RegisterCommand command) {
        poshtar.send(command);
        return Response.status(Response.Status.CREATED).build();
    }

    @PUT
    @Path("/activate/{name}")
    public Response activate(@PathParam("name") String username) {
        poshtar.send(new ActivateUserCommand(username));
        return Response.ok().build();
    }
}

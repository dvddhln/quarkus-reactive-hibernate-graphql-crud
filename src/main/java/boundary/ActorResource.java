package boundary;

import entity.Actor;
import entity.ActorMovieEntity;
import entity.Movie;
import entity.dto.ActorDTO;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;

@GraphQLApi
public class ActorResource {

    @Query("allActors")
    @Description("Get all Actors")
    public Uni<List<ActorDTO>> getAllActors() {
        return Actor.getAllActors()
                .onItem().transform(ActorDTO::from);
    }

    @Query
    @Description("Get an actor")
    public Uni<ActorDTO> getActor(@Name("actorId") long id) {
        return Actor.findByActorId(id).onItem().transform(ActorDTO::from);
    }

    public Uni<List<Movie>> movies(@Source ActorDTO actor) {
        return ActorMovieEntity.getMoviesByActorQuery(actor.id).onItem().transform(actorMovieEntity ->
                actorMovieEntity.movie).collect().asList();
    }

    @Mutation
    @Description("Add movie to actor")
    public Uni<ActorDTO> addMovieToActor(@Name("movieId") long movieId, @Name("actorId") long actorId) {
        return Actor.addMovieToActor(movieId, actorId).onItem().transform(ActorDTO::from);
    }
}

package boundary;

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
public class MovieResource {

    @Query("allMovies")
    @Description("Get all Movies")
    public Uni<List<Movie>> getAllMovies() {
        return Movie.getAllMovies();
    }

    @Query
    @Description("Get a movie")
    public Uni<Movie> getMovie(@Name("movieId") long id) {
        return Movie.findByMovieId(id);
    }

    public Uni<List<ActorDTO>> actors(@Source Movie movie) {
        return ActorMovieEntity.getActorsByMovieQuery(movie)
                .onItem()
                .transform(actorMovieEntity ->
                        ActorDTO.from(actorMovieEntity.actor))
                .collect().asList();
    }

    @Mutation
    @Description("Create a movie")
    public Uni<Movie> createMovie(Movie movie) {
        return Movie.addMovie(movie);
    }

    @Mutation
    @Description("Update a movie")
    public Uni<Movie> updateMovie(@Name("movieId") long id, Movie movie) {
        return Movie.updateMovie(id, movie);
    }

    @Mutation
    @Description("Delete a movie")
    public Uni<Boolean> deleteMovie(@Name("movieId") long id) {
        return Movie.deleteMovie(id);
    }

    @Mutation
    @Description("Add actor to movie")
    public Uni<Movie> addActorToMovie(@Name("movieId") long movieId, @Name("actorId") long actorId) {
        return Movie.addActorToMovie(movieId, actorId);
    }

}
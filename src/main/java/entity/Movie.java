package entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.json.bind.annotation.JsonbDateFormat;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Getter
public class Movie extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String title;
    public String director;

    @JsonbDateFormat("yyyy-MM-dd")
    public LocalDate releaseDate;


    public static Uni<List<Movie>> getAllMovies() {
        return Movie
                .listAll(Sort.by("releaseDate"))
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));

    }

    public static Uni<Movie> findByMovieId(Long id) {
        return findById(id);
    }

    public static Uni<Movie> updateMovie(Long id, Movie movie) {
        return Panache
                .withTransaction(() -> findByMovieId(id)
                        .onItem().ifNotNull()
                        .transform(entity -> {
                            entity.title = movie.title;
                            return entity;
                        })
                        .onFailure().recoverWithNull());
    }

    public static Uni<Movie> addMovie(Movie movie) {
        return Panache
                .withTransaction(movie::persist)
                .replaceWith(movie)
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .transform(t -> new IllegalStateException(t));
    }


    public static Uni<Boolean> deleteMovie(Long id) {
        return Panache.withTransaction(() -> deleteById(id));
    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

    public static Uni<Movie> addActorToMovie(Long movieId, Long actorId) {

        Uni<Movie> movie = findById(movieId);
        Uni<Actor> actor = Actor.findByActorId(actorId);

        Uni<Tuple2<Actor, Movie>> movieActorUni = Uni.combine()
                .all().unis(actor, movie).asTuple();

        return Panache
                .withTransaction(() -> movieActorUni
                        .onItem().ifNotNull()
                        .transform(entity -> {

                            if (entity.getItem2() == null || entity.getItem1() == null) {
                                return null;
                            }
                            return ActorMovieEntity.builder()
                                    .actor(entity.getItem1())
                                    .movie(entity.getItem2()).build();

                        })
                        .onItem().call(actorMovieEntity -> actorMovieEntity.persist())
                        .onItem().transform(actorMovieEntity -> actorMovieEntity.movie));

    }
}

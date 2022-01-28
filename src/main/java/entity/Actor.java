package entity;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Uni;
import io.smallrye.mutiny.tuples.Tuple2;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.Duration;
import java.util.Collections;
import java.util.List;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Getter
public class Actor extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    public String name;


    public static Uni<List<Actor>> getAllActors() {
        return Actor
                .listAll()
                .ifNoItem()
                .after(Duration.ofMillis(10000))
                .fail()
                .onFailure()
                .recoverWithUni(Uni.createFrom().<List<PanacheEntityBase>>item(Collections.EMPTY_LIST));

    }

    public static Uni<Actor> findByActorId(Long id) {
        return findById(id);
    }

    public static Uni<Actor> addMovieToActor(Long movieId, Long actorId) {

        Uni<Actor> actor = findById(actorId);
        Uni<Movie> movie = Movie.findByMovieId(movieId);

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
                        .onItem().transform(actorMovieEntity -> actorMovieEntity.actor));

    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }
}

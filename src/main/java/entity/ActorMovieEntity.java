package entity;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import io.smallrye.mutiny.Multi;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.json.bind.annotation.JsonbTransient;
import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Cacheable
@Table(indexes = {
        @Index(name = "movie_id_index", columnList = "movie_id"),
        @Index(name = "actor_id_index", columnList = "actor_id"),
        @Index(name = "movie_actor_index_index", columnList = "movie_id, actor_id"),
},
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"movie_id", "actor_id"})
        })
@NamedQueries(value = {@NamedQuery(name = "ActorMovieEntity.getByMovieId",
        query = "SELECT c FROM ActorMovieEntity c JOIN FETCH c.actor where c.movie.id = ?1"),
        @NamedQuery(name = "ActorMovieEntity.getByActorId",
                query = "SELECT c FROM ActorMovieEntity c JOIN FETCH c.movie where c.actor.id = ?1")})
@Getter
public class ActorMovieEntity extends PanacheEntityBase {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonbTransient
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Movie movie;

    @OneToOne(fetch = FetchType.LAZY)
    @JsonbTransient
    @OnDelete(action = OnDeleteAction.CASCADE)
    public Actor actor;


    public static Multi<ActorMovieEntity> getEntityByMovieAndActor(Long movie, Long actor) {
        return stream("movie.id = ?1 and actor.id = ?2", movie, actor);
    }

    public static Multi<ActorMovieEntity> getActorsByMovieQuery(Movie movie) {
        return stream("#ActorMovieEntity.getByMovieId", movie.id);
    }

    public static Multi<ActorMovieEntity> getMoviesByActorQuery(Long actorId) {
        return stream("#ActorMovieEntity.getByActorId", actorId);
    }

    public String toString() {
        return this.getClass().getSimpleName() + "<" + this.id + ">";
    }

}

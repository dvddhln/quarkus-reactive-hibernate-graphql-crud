package entity.dto;

import entity.Movie;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Name;

import javax.json.bind.annotation.JsonbDateFormat;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Name("MovieResponse")
public class MovieDTO {

    public Long id;
    public String title;
    public String director;

    @JsonbDateFormat("yyyy-MM-dd")
    public LocalDate releaseDate;

    public static MovieDTO from(Movie movie) {
        return MovieDTO
                .builder()
                .id(movie.id)
                .releaseDate(movie.releaseDate)
                .title(movie.title)
                .director(movie.director)
                .build();
    }

    public static List<MovieDTO> from(List<Movie> actor) {
        return actor.stream().map(MovieDTO::from)
                .collect(Collectors.toList());

    }
}

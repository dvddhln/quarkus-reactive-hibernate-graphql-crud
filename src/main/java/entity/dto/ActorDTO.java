package entity.dto;


import entity.Actor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.eclipse.microprofile.graphql.Name;

import java.util.List;
import java.util.stream.Collectors;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Name("ActorResponse")
public class ActorDTO {

    public String name;
    public Long id;
    
    public static ActorDTO from(Actor actor) {
        return ActorDTO
                .builder()
                .id(actor.id)
                .name(actor.name)
                .build();
    }

    public static List<ActorDTO> from(List<Actor> actor) {
        return actor.stream().map(ActorDTO::from)
                .collect(Collectors.toList());

    }
}

package guru.springframework.commands;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitOfMeasureCommand {
    private Long id;
    private String description;
}

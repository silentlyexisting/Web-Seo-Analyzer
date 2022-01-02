package hexlet.code;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.Instant;


@Getter
@Setter
@RequiredArgsConstructor
@Entity
public class Url extends Model {
    @Id
    private long id;
    @NonNull
    private String name;
    @WhenCreated
    private Instant createdAt;
}

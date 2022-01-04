package hexlet.code.domain;

import io.ebean.Model;
import io.ebean.annotation.WhenCreated;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.time.Instant;
import java.util.List;


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
    @OneToMany(cascade = CascadeType.ALL)
    private List<UrlCheck> urlChecks;
}

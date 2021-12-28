package hexlet.code;

import io.ebean.Model;
import io.ebean.config.JsonConfig;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Url extends Model {
    @Id
    private long id;
    private String name;
    private JsonConfig.DateTime createdAt;
}

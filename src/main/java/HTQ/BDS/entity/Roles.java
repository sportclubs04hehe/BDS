package HTQ.BDS.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Entity
@Table(name = "_role")
@Data
public class Roles {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 50, nullable = false, unique = true)
    @NotBlank
    private String name;

    public static String ADMIN = "ADMIN";
    public static String USER = "USER";

}

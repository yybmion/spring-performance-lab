package performance.lab2.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    private String name;

    // @BatchSize(size = 100)  application.yml 에 적용하는 방식과 같음
    @OneToMany(mappedBy = "team")
    private List<Member> members = new ArrayList<>();

    public static Team create(Long teamId, String name, List<Member> members) {
        Team team = new Team();
        team.teamId = teamId;
        team.name = name;
        team.members = members;

        return team;
    }
}

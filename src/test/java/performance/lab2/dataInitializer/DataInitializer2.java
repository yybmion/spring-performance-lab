package performance.lab2.dataInitializer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import performance.lab2.LabApplication2;
import performance.lab2.domain.Member;
import performance.lab2.domain.Team;

@SpringBootTest(classes = LabApplication2.class)
@ActiveProfiles("test2")
public class DataInitializer2 {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    static final int TEAMS_PER_BATCH = 100;
    static final int MEMBERS_PER_TEAM = 1000;
    static final int EXECUTE_COUNT = 10;

    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    @Test
    void initialize() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        for (int i = 0; i < EXECUTE_COUNT; i++) {
            final int batchIndex = i;
            executorService.submit(() -> {
                try {
                    insertBatch(batchIndex);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                    System.out.println("Remaining batches: " + latch.getCount());
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }

    void insertBatch(int batchIndex) {
        transactionTemplate.executeWithoutResult(status -> {
            for (int i = 0; i < TEAMS_PER_BATCH; i++) {
                Team team = Team.create(
                        null,
                        "Team_" + batchIndex + "_" + i,
                        new ArrayList<>()
                );
                entityManager.persist(team);

                for (int j = 0; j < MEMBERS_PER_TEAM; j++) {
                    Member member = Member.create(
                            null,
                            "Member_" + batchIndex + "_" + i + "_" + j,
                            team
                    );
                    entityManager.persist(member);
                    team.getMembers().add(member);
                }

                if (i % 10 == 0) {
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        });
    }
}

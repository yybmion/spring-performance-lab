package performance.lab.dataInitialize;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.support.TransactionTemplate;
import performance.lab.domain.Article;
import performance.lab.domain.User;

@SpringBootTest
@ActiveProfiles("test")
public class DataInitializer {
    @PersistenceContext
    EntityManager entityManager;

    @Autowired
    TransactionTemplate transactionTemplate;

    static final int USERS_PER_BATCH = 100;
    static final int ARTICLES_PER_USER = 100;
    static final int EXECUTE_COUNT = 1000;


    CountDownLatch latch = new CountDownLatch(EXECUTE_COUNT);

    @Test
    void initialize() throws InterruptedException{
        ExecutorService executorService = Executors.newFixedThreadPool(10);

        for(int i=0;i<EXECUTE_COUNT;i++){
            final int batchIndex = i;
            executorService.submit(() -> {
                try{
                    insertBatch(batchIndex);
                }catch (Exception e){
                    e.printStackTrace();
                }finally{
                    latch.countDown();
                    System.out.println("Remaining batches: " + latch.getCount());
                }
            });
        }

        latch.await();
        executorService.shutdown();
    }
    void insertBatch(int batchIndex){
        transactionTemplate.executeWithoutResult(status -> {
            for(int i=0;i<USERS_PER_BATCH;i++){
                User user = User.create(
                        null,
                        "User_" + batchIndex + "_" + i
                );
                entityManager.persist(user);

                for(int j=0;j<ARTICLES_PER_USER;j++){
                    Article article = Article.create(
                            null,
                            "Content_" + j,
                            user
                    );
                    entityManager.persist(article);
                }

                if(i%10==0){
                    entityManager.flush();
                    entityManager.clear();
                }
            }
        });
    }
}

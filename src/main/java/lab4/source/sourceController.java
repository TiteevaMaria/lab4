package lab4.source;

import com.hazelcast.core.HazelcastInstance;
import lab4.Train;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.Time;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@org.springframework.stereotype.Controller
public class sourceController
{
    final HazelcastInstance hazelcastInstance;
    private BlockingQueue<Train> queue;

    public sourceController(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance)
    {
        this.hazelcastInstance = hazelcastInstance;
        this.queue = hazelcastInstance.getQueue("trainsQueue");
        new Thread(() ->
        {
            try
            {
                trainsGenerator();
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }).start();
    }

    public void trainsGenerator() throws InterruptedException
    {
        Random random = new Random();
        while (true)
        {
            Thread.sleep(12000);
            Train train = new Train();
            train.name = String.valueOf(random.nextInt(10000));
            train.time = new Time(System.currentTimeMillis() + random.nextInt(600000));
            System.out.println("Created train " + train);
            queue.add(train);
        }
    }
}

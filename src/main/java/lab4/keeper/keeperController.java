package lab4.keeper;

import com.hazelcast.core.HazelcastInstance;
import lab4.Train;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

@org.springframework.stereotype.Controller
public class keeperController 
{
    final HazelcastInstance hazelcastInstance; 
    private BlockingQueue<Train> queue; 
    private Set<Train> trainsSet; 

    
    public keeperController(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) 
	{
        this.hazelcastInstance = hazelcastInstance;
        this.queue = hazelcastInstance.getQueue("trainsQueue");
        this.trainsSet = hazelcastInstance.getSet("trainsSet");
        fromQueueToSet();
    }

    public void fromQueueToSet() 
	{
        new Thread(() -> {
            while (true) 
			{
                if (!queue.isEmpty()) 
				{
                    for (Train train : queue)
					{
                        System.out.println("Train " + train);
                        trainsSet.add(queue.poll());
                        hazelcastInstance.<String>getTopic("newTrain").publish(""); 
                    }
                    System.out.println("set " + Arrays.toString(trainsSet.toArray()));
                }

                try 
				{
                    Thread.sleep(2000); 
                } 
				catch (InterruptedException e) 
				{
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

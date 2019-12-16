package lab4.keeper;

import com.hazelcast.core.HazelcastInstance;
import lab4.DBUtils;
import lab4.Train;
import org.springframework.beans.factory.annotation.Qualifier;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

@org.springframework.stereotype.Controller
public class keeperController
{
    final HazelcastInstance hazelcastInstance;
    private BlockingQueue<Train> queue;

    public keeperController(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance)
    {
        this.hazelcastInstance = hazelcastInstance;
        this.queue = hazelcastInstance.getQueue("trainsQueue");
        try
        {
            DBUtils.connectDB();
        }
        catch (ClassNotFoundException | SQLException e)
        {
            e.printStackTrace();
        }
        fromQueueToDB();
    }

    public void fromQueueToDB()
    {
        new Thread(() -> {
            try
            {
                DBUtils.createDB();
            }
            catch (ClassNotFoundException | SQLException e)
            {
                e.printStackTrace();
            }
            while (true)
            {
                if (!queue.isEmpty())
                {
                    for (Train train : queue)
                    {
                        try
                        {
                            DBUtils.writeDB(queue.poll());
                        }
                        catch (SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
                hazelcastInstance.<String>getTopic("newTrain").publish("");
                try
                {
                    Thread.sleep(10000);
                } catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

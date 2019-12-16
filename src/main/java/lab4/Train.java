package lab4;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;


public class Train implements Serializable
{
    public String name = "";
    public Timestamp time = new Timestamp(0);

    @Override
    public String toString()
    {
        return "Train{" + "name='" + name + '\'' + ", time=" + time + '}';
    }
}

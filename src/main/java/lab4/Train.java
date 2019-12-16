package lab4;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;


public class Train implements Serializable
{
    public String name = "";
    public Time time = new Time(0);

    @Override
    public String toString()
    {
        return "Train{" + "name='" + name + '\'' + ", time=" + time + '}';
    }
}

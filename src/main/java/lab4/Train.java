package lab4;

import java.io.Serializable;
import java.util.Date;

public class Train implements Serializable 
{
    public String name = "";
    public Date date = new Date();

    @Override
    public String toString()
	{
        return "Train{" + "name='" + name + '\'' + ", date=" + date + '}';
    }
}

package lab4.keeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"lab4.keeper"})
public class keeper 
{
    public static void main(String[] args) 
	{
        SpringApplication.run(keeper.class, args);
    }
}

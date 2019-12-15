package lab4.pointer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication(scanBasePackages = {"lab4.pointer"})
@EnableWebSocket
public class pointer 
{
    public static void main(String[] args)
	{
        SpringApplication.run(pointer.class, args);
    }
}

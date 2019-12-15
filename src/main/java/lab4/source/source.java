package lab4.source;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication(scanBasePackages = {"lab4.source"})
public class source
 {

    public static void main(String[] args) 
	{
        SpringApplication.run(source.class, args);
    }
}

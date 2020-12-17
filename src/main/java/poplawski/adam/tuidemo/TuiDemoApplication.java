package poplawski.adam.tuidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableFeignClients
@EnableSwagger2
public class TuiDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TuiDemoApplication.class, args);
    }

}

package shop.ecoswapshop;

import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EcoswapshopApplication {

    public static void main(String[] args) {
        // .env 파일 로드
//        Dotenv dotenv = Dotenv.load();
        SpringApplication.run(EcoswapshopApplication.class, args);
    }
    @Bean
    public Hibernate5Module hibernate5Module() {
        return new Hibernate5Module();
    }
}

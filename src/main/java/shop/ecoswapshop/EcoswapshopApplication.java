package shop.ecoswapshop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcoswapshopApplication {

    public static void main(String[] args) {

        Hello hello = new Hello();
        hello.setName("Lim");
        String name = hello.getName();
        System.out.println("name = " + name);

        SpringApplication.run(EcoswapshopApplication.class, args);
    }

}

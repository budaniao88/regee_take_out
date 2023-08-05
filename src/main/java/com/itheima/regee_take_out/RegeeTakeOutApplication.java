package com.itheima.regee_take_out;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Slf4j
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
public class RegeeTakeOutApplication {

    public static void main(String[] args) {
        SpringApplication.run(RegeeTakeOutApplication.class, args);
        log.info("启动成功");

    }

}

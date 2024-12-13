package com.financial.system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.financial.common.security.annotation.EnableCustomConfig;
import com.financial.common.security.annotation.EnableFinancialFeignClients;

/**
 * 系统模块
 * 
 * @author ruoyi
 */
@EnableCustomConfig
@EnableFinancialFeignClients
@SpringBootApplication
public class FinancialSystemApplication
{
    public static void main(String[] args)
    {
        SpringApplication.run(FinancialSystemApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统模块启动成功   ლ(´ڡ`ლ)ﾞ  \n" +
                " .-------.       ____     __        \n" +
                " |  _ _   \\      \\   \\   /  /    \n" +
                " | ( ' )  |       \\  _. /  '       \n" +
                " |(_ o _) /        _( )_ .'         \n" +
                " | (_,_).' __  ___(_ o _)'          \n" +
                " |  |\\ \\  |  ||   |(_,_)'         \n" +
                " |  | \\ `'   /|   `-'  /           \n" +
                " |  |  \\    /  \\      /           \n" +
                " ''-'   `'-'    `-..-'              ");
    }
}

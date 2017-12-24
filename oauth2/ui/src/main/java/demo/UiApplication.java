package demo;

import com.google.common.collect.Maps;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@SpringBootApplication
@EnableZuulProxy
@EnableOAuth2Sso
@EnableOAuth2Client
@Controller
public class UiApplication extends WebSecurityConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(UiApplication.class, args);
    }

    @GetMapping("/hello")
    @ResponseBody
    public String response() {
        return "<h1>Hello World</h1>";
    }

    @GetMapping("/get-supplier/{id}")
    @ResponseBody
    public Map<String, String> getSupplier(@PathVariable("id") int id) {
        Map<String, String> supplier = new HashMap<>();
        supplier.put("companyName", "name" + id);
        supplier.put("email", "email@user" + id + ".com");
        return supplier;
    }

    @GetMapping("/get-suppliers")
    @ResponseBody
    public Map<String, Object> getSuppliers() {
        List<Map<String, String>> suppliers = IntStream.range(0, 3).mapToObj(id -> {
            Map<String, String> supplier = new HashMap<>();
            supplier.put("id", Integer.toString(id));
            supplier.put("companyName", "name" + id);
            supplier.put("email", "email@user" + id + ".com");
            return supplier;
        }).collect(Collectors.toList());
        HashMap<String, Object> map = Maps.newHashMap();
        map.put("content", suppliers);
        return map;

    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        CookieCsrfTokenRepository csrfTokenRepository = CookieCsrfTokenRepository.withHttpOnlyFalse();
        http
                .logout().and()
                .authorizeRequests()
                .antMatchers("/static/js/index.html"
                        , "/static/js/home.html"
                        , "/home.html"
                        , "/"
                        , "/login"
                        , "/get-supplier*"
                        , "/implicit"
                        , "/implicit.html").permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf()
                .csrfTokenRepository(csrfTokenRepository);
        // @formatter:oln
    }
}


package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class GreetingController {

    private boolean isSchedule;

    @Autowired
    private SimpMessagingTemplate template;

    @MessageMapping("/hello")
    @SendTo("/topic/greetings")
    public Greeting greeting(HelloMessage message) throws Exception {
        Thread.sleep(1000);
        return new Greeting("Hello, " + message.getName() + "!");
    }

    @RequestMapping(path="/hello")
    public String greet() throws Exception {
        //String text = "[" + getTimestamp() + "]:" + greeting;
        //this.template.convertAndSend("/topic/greetings", text);

        //Thread.sleep(1000); // simulated delay

        this.template.convertAndSend("/topic/greetings", new Greeting("Hello " + new Date().getTime()));

        return "done";
    }

    @RequestMapping(path="/toggleschedule")
    public void toggleSchedule() {
        this.isSchedule = ! this.isSchedule;
    }

    @Scheduled(fixedDelay=5000)
    public void scheduleGreet() throws Exception {
         if(isSchedule) {
             Thread.sleep(1000);
             this.template.convertAndSend("/topic/greetings", new Greeting("Hello " + new Date().getTime()));
         }
    }
}

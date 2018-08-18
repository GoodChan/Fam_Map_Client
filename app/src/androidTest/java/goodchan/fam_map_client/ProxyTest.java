package goodchan.fam_map_client;

import org.junit.Test;
import goodchan.fam_map_client.Requests.RegisterRequest;
import static org.junit.Assert.*;

public class ProxyTest {
    Proxy proxy = new Proxy("192.168.254.52", "8080"); // my laptop's IP address

    @Test
    public void register() {
        RegisterRequest registerRequest = new RegisterRequest("clientUser", "client", "client@client.com",
                "firstanme", "lastname", "f");
        proxy.register(registerRequest);
    }

    @Test
    public void logIn() {
    }

    @Test
    public void getEvents() {
    }

    @Test
    public void getPeople() {
    }
}
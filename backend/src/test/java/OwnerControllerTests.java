import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import springboot.Application;
import springboot.DTO.OwnerDTO;
import springboot.DTO.ShopDTO;
import springboot.Repository.OwnerRepository;
import springboot.Repository.ShopRepository;
import springboot.config.WebSecurity;
import springboot.controller.OwnerController;
import springboot.controller.ShopController;
import springboot.filters.JWTAuthenticationFilter;
import springboot.filters.JWTAuthorizationFilter;
import springboot.model.Item;
import springboot.model.Owner;
import springboot.model.Shop;
import springboot.Repository.ItemRepository;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.patch;
import static org.mockito.Mockito.spy;
import java.util.Arrays;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)

public class OwnerControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private OwnerRepository ownerRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private OwnerController ownerController = spy(new OwnerController(ownerRepository, shopRepository, bCryptPasswordEncoder));

    @Autowired
    private WebApplicationContext wac;

    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private JWTAuthorizationFilter jwtAuthorizationFilter;

    private AuthenticationManager authenticationManager;

    @Autowired
    private WebSecurity webSecurity;

    private Shop s1;
    private Shop s2;
    private Item i1;
    private Item i2;
    private Owner admin;
    private OwnerDTO owner;
    private ObjectMapper mapper = new ObjectMapper();
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private String token="";
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.admin = new Owner("ADMIN", "ADMIN", "ADMIN@gmail.com", "Pass4Admin");
        admin.setId(69);
        this.owner = new OwnerDTO((long) 1, "owner", "own", "email", "password");

    }

//    @Test
//    @WithMockUser(value="ADMIN")
//    public void testGet() throws Exception {
//        doReturn(Optional.of(s1)).when(shopRepository).findById(Mockito.anyLong());
//        MvcResult res = mockMvc.perform(get("/api/public/shop").param("shopId", String.valueOf(420))  )
//                .andReturn();
//
//        String content = res.getResponse().getContentAsString();
//
//        Shop resShop = mapper.readValue(content, Shop.class);
//
//        assert(resShop.getId() == s1.getId());
//    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testCreateAccept() throws Exception {
        String ownerStr = mapper.writeValueAsString(owner);
        MvcResult res = mockMvc.perform(post("/api/owner").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr)).andReturn();

        int status = res.getResponse().getStatus();
        assert(status == 200);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testCreateReject() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());

        String ownerStr = mapper.writeValueAsString(owner);
        MvcResult res = mockMvc.perform(post("/api/owner").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr)).andReturn();

        MvcResult res2 = mockMvc.perform(post("/api/owner").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr)).andReturn();

        int status = res2.getResponse().getStatus();
        assert(status == 400);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testEditAccept() throws Exception {

        String ownerStr = mapper.writeValueAsString(owner);
        MvcResult res = mockMvc.perform(post("/api/owner").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr)).andReturn();

        OwnerDTO owner = new OwnerDTO(admin.getId(), "owner", "own", "email", "password");
        String ownerStr2 = mapper.writeValueAsString(owner);
        MvcResult res2 = mockMvc.perform(post("/api/owner/edit").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr2)).andReturn();

        int status = res.getResponse().getStatus();
        assert(status == 200);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testEditReject() throws Exception {

        OwnerDTO owner = new OwnerDTO((long) 5, "owner", "own", "email", "password");
        String ownerStr = mapper.writeValueAsString(owner);
        MvcResult res = mockMvc.perform(post("/api/owner/edit").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr)).andReturn();

        int status = res.getResponse().getStatus();
        assert(status == 400);
    }

    @Test
    @WithMockUser(value = "ADMIN")
    public void testDelete() throws Exception{
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());

        String ownerStr = mapper.writeValueAsString(owner);
        MvcResult res = mockMvc.perform(post("/api/owner").contentType(APPLICATION_JSON_UTF8)
                .content(ownerStr)).andReturn();

        MvcResult res2 = mockMvc.perform(delete("/api/owner").param("ownerId", String.valueOf(admin.getId()))).andReturn();

        int status = res2.getResponse().getStatus();
        assert(status == 200);
    }

//    @Test
//    @WithMockUser(value = "ADMIN")
//    public void testLogin() throws Exception{
//        //doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
//
//        String ownerStr = mapper.writeValueAsString(owner);
//        MvcResult res = mockMvc.perform(post("/api/owner").contentType(APPLICATION_JSON_UTF8)
//                .content(ownerStr)).andReturn();
//
//        OwnerDTO login = new OwnerDTO(null, null, null, owner.getEmail(), owner.getPassword());
//        String loginStr = mapper.writeValueAsString(login);
//        MvcResult res2 = mockMvc.perform(post("/api/login").contentType(APPLICATION_JSON_UTF8)
//                .content(loginStr)).andReturn();
//
//        int status = res2.getResponse().getStatus();
//        assert(status == 200);
//    }

}

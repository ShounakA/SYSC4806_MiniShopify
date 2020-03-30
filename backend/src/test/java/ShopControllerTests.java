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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import springboot.Application;
import springboot.DTO.ShopDTO;
import springboot.Repository.OwnerRepository;
import springboot.Repository.ShopRepository;
import springboot.config.WebSecurity;
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
public class ShopControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private OwnerRepository ownerRepository;

    @MockBean
    private ItemRepository itemRepository;

    @InjectMocks
    private ShopController shopController = spy(new ShopController(shopRepository, ownerRepository, itemRepository));

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
    private ObjectMapper mapper = new ObjectMapper();
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private String token="";
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.admin = new Owner("ADMIN", "ADMIN", "ADMIN@gmail.com", "Pass4Admin");
        admin.setId(69);
        ArrayList<String> tempTags = new ArrayList<String>();
        tempTags.add("tag1");
        tempTags.add("tag2");

        this.i1 = new Item ("toothbrush","use it lol", 1, 1, s1);
        this.i2 = new Item ("toothpaste","hi", 2, 1, s1);
        ArrayList<Item> tempItems = new ArrayList<Item>();
        tempItems.add(i1);
        tempItems.add(i2);
        this.s1 = new Shop("Test Shop", "test", tempTags, admin, tempItems);
        this.s1.setId(420);

        s2 = new Shop("s2", "asdasd", Arrays.asList("test", "test13122"), admin,  new ArrayList(Arrays.asList(i2)));
        s2.setId(4000);
        admin.setOwnedShops(new ArrayList(Arrays.asList(s1, s2)));


    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testGet() throws Exception {
        doReturn(Optional.of(s1)).when(shopRepository).findById(Mockito.anyLong());
        MvcResult res = mockMvc.perform(get("/api/public/shop").param("shopId", String.valueOf(420))  )
                .andReturn();

        String content = res.getResponse().getContentAsString();

        Shop resShop = mapper.readValue(content, Shop.class);

        assert(resShop.getId() == s1.getId());
    }
    @Test
    @WithMockUser(value="ADMIN")
    public void testCreate() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
        ShopDTO oldShop = new ShopDTO((long) 21, "newShop1", "new desc2", Arrays.asList("new"), admin.getId(), null);
        String oldShopStr = mapper.writeValueAsString(oldShop);
        MvcResult res = mockMvc.perform(post("/api/shop").contentType(APPLICATION_JSON_UTF8)
                .content(oldShopStr)).andReturn();

        String content = res.getResponse().getContentAsString();

        Shop newShop = mapper.readValue(content, Shop.class);

        verify(shopRepository).save(any());
        assert (newShop.getName().equals("newShop1"));
        assert(newShop.getDescription().equals("new desc2"));
        assert (newShop.getProducts().isEmpty());
        assert(newShop.getOwner().getFirstName().equals(admin.getFirstName()));
    }

    @Test
    @WithMockUser(value = "ADMIN")
    public void testPatch() throws Exception{
        doReturn(Optional.of(s2)).when(shopRepository).findById(Mockito.anyLong());

        ShopDTO oldShop = new ShopDTO((long) 21, "newShop", "new desc", null, null, null);

        String oldShopStr = mapper.writeValueAsString(oldShop);

        MvcResult res = mockMvc.perform(post("/api/shop/edit").contentType(APPLICATION_JSON_UTF8)
                .content(oldShopStr)).andReturn();

        String content = res.getResponse().getContentAsString();

        Shop newShop = mapper.readValue(content, Shop.class);

        verify(shopRepository).save(any());
        assert (newShop.getName().equals("newShop"));
        assert(newShop.getDescription().equals("new desc"));
        assert (newShop.getProducts() != null);
        assert (newShop.getTags() != null);
        assert (newShop.getOwner().getId() == admin.getId());
    }
    @Test
    @WithMockUser(value = "ADMIN")
    public void testDelete() throws Exception{
        doReturn(Optional.of(s1)).when(shopRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
        MvcResult res = mockMvc.perform(delete("/api/shop").param("shopId", String.valueOf(420)).param("ownerId", String.valueOf(12)))
                .andReturn();

        String content = res.getResponse().getContentAsString();
        verify(shopRepository).delete(any());
        assert(content.equals("Shop ID: " + s1.getId() + " deleted"));
    }

}

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;
import springboot.Application;
import springboot.DTO.ItemDTO;
import springboot.Repository.OwnerRepository;
import springboot.Repository.ShopRepository;
import springboot.config.WebSecurity;
import springboot.controller.CartController;
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
import static org.mockito.Mockito.spy;
import java.util.Arrays;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@AutoConfigureMockMvc
@SpringBootTest(classes = Application.class)
@RunWith(SpringRunner.class)

public class CartControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ShopRepository shopRepository;

    @MockBean
    private OwnerRepository ownerRepository;

    @MockBean
    private ItemRepository itemRepository;

    @InjectMocks
    private CartController cartController = spy(new CartController(ownerRepository, shopRepository, itemRepository));

    @Autowired
    private WebApplicationContext wac;

    private JWTAuthenticationFilter jwtAuthenticationFilter;

    private JWTAuthorizationFilter jwtAuthorizationFilter;

    private AuthenticationManager authenticationManager;

    @Autowired
    private WebSecurity webSecurity;

    private Owner admin;
    private Shop shop;
    private Item i1;
    private Item i2;
    private ObjectMapper mapper = new ObjectMapper();
    public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
    private String token="";
    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        this.admin = new Owner("ADMIN", "ADMIN", "ADMIN@gmail.com", "Pass4Admin");
        admin.setId(100);

        ArrayList<String> tempTags = new ArrayList<String>();
        tempTags.add("tag1");
        tempTags.add("tag2");
        this.i1 = new Item ("toothbrush","use it lol", 10, 2, shop);
        i1.setId(4);
        this.i2 = new Item ("toothpaste","hi", 10, 1, shop);
        i2.setId(5);
        ArrayList<Item> tempItems = new ArrayList<Item>();
        tempItems.add(i1);
        tempItems.add(i2);

        shop = new Shop("s2", "asdasd", Arrays.asList("toothpaste", "bathroom"), admin,  tempItems);
        shop.setId(3);
        admin.setOwnedShops(new ArrayList(Arrays.asList(shop)));
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testGetCart() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());

        MvcResult res = mockMvc.perform(get("/api/cart").param("ownerId", String.valueOf(100)))
                .andReturn();

        int status = res.getResponse().getStatus();

        assert(status == 200);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testAddItem() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(shop)).when(shopRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(i1)).when(itemRepository).findById(Mockito.anyLong());

        ItemDTO item = new ItemDTO((long) 4, "", "", 5, 0, (long) 3);
        String itemStr = mapper.writeValueAsString(item);
        MvcResult res = mockMvc.perform(post("/api/cart/add").contentType(APPLICATION_JSON_UTF8)
                .content(itemStr).param("ownerId", String.valueOf(100))).andReturn();

        int status = res.getResponse().getStatus();
        assert(status == 200);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testRemoveItem() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(shop)).when(shopRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(i1)).when(itemRepository).findById(Mockito.anyLong());

        ItemDTO item = new ItemDTO((long) 4, "", "", 5, 0, (long) 3);
        String itemStr = mapper.writeValueAsString(item);
        MvcResult resAdd = mockMvc.perform(post("/api/cart/add").contentType(APPLICATION_JSON_UTF8)
                .content(itemStr).param("ownerId", String.valueOf(100))).andReturn();

        MvcResult resDel = mockMvc.perform(post("/api/cart/remove").contentType(APPLICATION_JSON_UTF8)
                .content(itemStr).param("ownerId", String.valueOf(100))).andReturn();

        int status = resDel.getResponse().getStatus();
        assert(status == 200);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testEditItem() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(shop)).when(shopRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(i1)).when(itemRepository).findById(Mockito.anyLong());

        ItemDTO item = new ItemDTO((long) 4, "", "", 5, 0, (long) 3);
        String itemStr = mapper.writeValueAsString(item);
        MvcResult resAdd = mockMvc.perform(post("/api/cart/add").contentType(APPLICATION_JSON_UTF8)
                .content(itemStr).param("ownerId", String.valueOf(100))).andReturn();

        item = new ItemDTO((long) 4, "", "", 4, 0, (long) 3);
        MvcResult resEd = mockMvc.perform(post("/api/cart/edit").contentType(APPLICATION_JSON_UTF8)
                .content(itemStr).param("ownerId", String.valueOf(100))).andReturn();

        int status = resEd.getResponse().getStatus();
        assert(status == 200);
    }

    @Test
    @WithMockUser(value="ADMIN")
    public void testUpdatedCart() throws Exception {
        doReturn(Optional.of(admin)).when(ownerRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(shop)).when(shopRepository).findById(Mockito.anyLong());
        doReturn(Optional.of(i1)).when(itemRepository).findById(Mockito.anyLong());

        ItemDTO item = new ItemDTO((long) 4, "", "", 5, 0, (long) 3);
        String itemStr = mapper.writeValueAsString(item);

        ItemDTO item2 = new ItemDTO((long) 5, "", "", 5, 0, (long) 3);
        String item2Str = mapper.writeValueAsString(item);

        MvcResult resAdd = mockMvc.perform(post("/api/cart/add").contentType(APPLICATION_JSON_UTF8)
                .content(itemStr).param("ownerId", String.valueOf(100))).andReturn();

        MvcResult resAdd2 = mockMvc.perform(post("/api/cart/add").contentType(APPLICATION_JSON_UTF8)
                .content(item2Str).param("ownerId", String.valueOf(100))).andReturn();

        MvcResult resTotal = mockMvc.perform(get("/api/cart/total").contentType(APPLICATION_JSON_UTF8)
                .param("ownerId", String.valueOf(100))).andReturn();

        int status = resTotal.getResponse().getStatus();
        String total = resTotal.getResponse().getContentAsString();

        assert(status == 200);
        assert(total.equals("Total: 20.0"));
    }
}

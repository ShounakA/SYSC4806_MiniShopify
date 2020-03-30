import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import springboot.Application;
import springboot.Repository.OwnerRepository;
import springboot.model.Owner;

import java.util.List;

@AutoConfigureMockMvc
@ContextConfiguration(classes={Application.class})
@RunWith(SpringJUnit4ClassRunner.class)
@DataJpaTest
public class JPAOwnerRepositoryTests {

    @Autowired
    private OwnerRepository ownerRepository;

    private Owner userJohn, userTom;

    @Before
    public void init() {
        userJohn = new Owner("John", "Doe", "john@doe.com", "password");
        ownerRepository.save(userJohn);

        userTom = new Owner("Tom", "Doe", "tom@doe.com", "password");
        ownerRepository.save(userTom);
    }

    @Test
    public void givenFirstName_getCorrect() {

        List<Owner> results = ownerRepository.findByFirstName(userJohn.getFirstName());
        Assert.assertEquals(1, results.size());
    }

    @Test
    public void givenLastName_getCorrect() {

        List<Owner> results = ownerRepository.findByLastName("Doe");
        Assert.assertEquals(2, results.size());
    }

    @Test
    public void givenEmailAndPassword_getCorrect() {

        Owner results = ownerRepository.findByEmailAndPassword(userJohn.getEmail(), userJohn.getPassword());
        Assert.assertEquals(results, userJohn);
    }
}
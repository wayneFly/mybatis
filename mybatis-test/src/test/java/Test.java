import com.llc.App;
import com.llc.service.UserService;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>
 * Test
 * </p>
 *
 * @author llc
 * @desc
 * @since 2021-02-19 17:02
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class Test {

    @Autowired
    private UserService userService;

    @org.junit.Test
    public void insert() {
        int aaa = userService.insert(1, "aaa", 3);
        System.out.println(aaa);

    }

}

package search;

import com.aylien.textapi.responses.TaxonomyCategory;
import model.User;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertFalse;

public class NewsFinderTest {

    NewsFinder newsFinder;

    @Before
    public void setup() {
        newsFinder = new BingNewsFinder();

    }

    @Test
    public void test1() throws Exception {
        User user = new User("Jon", "Doe", "user@email.com");
        ArrayList<String> haveToAppear = new ArrayList<>();
        ArrayList<String> cantAppear = new ArrayList<>();
        ArrayList<String> blacklistDomains = new ArrayList<>();
        ArrayList<String> queries = new ArrayList<>();
        haveToAppear.add("IAB19");
        haveToAppear.add("IAB19-10");
        cantAppear.add("IAB1-6");
        blacklistDomains.add("ft.com");
        queries.add("apple iphone");
        queries.add("apple");

        newsFinder.Init(haveToAppear,cantAppear,blacklistDomains,queries,user);
        newsFinder.Start();
        List<RelevantNews> results = newsFinder.GetResults();
        assertFalse(results.isEmpty());

        System.out.println(newsFinder.PrintResults());
    }
}
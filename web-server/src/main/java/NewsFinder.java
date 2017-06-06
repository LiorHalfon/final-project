import com.aylien.textapi.TextAPIException;
import model.User;
import java.util.ArrayList;
import java.util.List;

public interface NewsFinder {

    void Init(ArrayList<String> haveToAppearCategories,
              ArrayList<String> cantAppearCategories,
              ArrayList<String> blacklistDomains,
              ArrayList<String> queries,
              User user);
    List<RelevantNews> Start() throws Exception;
}

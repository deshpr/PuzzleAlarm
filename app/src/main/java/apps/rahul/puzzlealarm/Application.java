package apps.rahul.puzzlealarm;

/**
 * Created by Rahul on 7/23/2016.
 */

import java.util.ArrayList;
import java.util.List;

public class Application {

    public static final String TAG = "PUZZLE_APP";

    public static List<String> supportedContentProviders = new ArrayList<String>()
    {{
       add(new String("Facebook"));
       add("Instagram");
       add("OneDrive");
    }};
    public static List<IContentProvider> loggedInProviders = new ArrayList<IContentProvider>();

}

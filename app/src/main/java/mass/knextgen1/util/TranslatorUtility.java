package mass.knextgen1.util;

/**
 * Created by Turtle II on 4/16/2015.
 */
public class TranslatorUtility {
    public static String getLanguageSuffix(int languageIndex) {
        switch(languageIndex) {
            case 0:
                return "en";
            case 1:
                return "es";
            case 2:
                return "fr";
            default:
                return "en";
        }
    }
}

package vibefuze.utils;

import java.util.Collection;
import java.util.List;

public class TextUtil {
    public static String normalize(String data) {
        return java.text.Normalizer.normalize(
                        data.toLowerCase()
                        .replace('ł', 'l'),
                        java.text.Normalizer.Form.NFD)
                        .replaceAll("[^\\p{ASCII}]", "")
                        .replaceAll("\\(.*\\)", "")
                        .replaceAll("[^a-zA-Z0-9]", "");
    }

    public static boolean compare(String s1, String s2){
        return TextUtil.normalize(s1).equals(TextUtil.normalize(s2));
    }

    public static boolean compare(String s1, Collection<String> s2){
        return s2.stream()
                .anyMatch(s -> TextUtil.normalize(s1).equals(TextUtil.normalize(s)));
    }
}

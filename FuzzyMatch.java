
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class FuzzyMatch {

  /*
   * t0 = [SORTED_INTERSECTION]
   * t1 = [SORTED_INTERSECTION] + [SORTED_REST_OF_STRING1]
   * t2 = [SORTED_INTERSECTION] + [SORTED_REST_OF_STRING2]
   *
   * outcome = max(t0,t1,t2)
   *
   */
  public static int getRatio(String s1, String s2) {
    if (s1.length() >= s2.length()) {
      // We need to swap s1 and s2
      String temp = s2;
      s2 = s1;
      s1 = temp;
    }

    // Get alpha numeric characters
    s1 = escapeString(s1).toLowerCase();
    s2 = escapeString(s2).toLowerCase();

    //split the string by space and store words in sets
    final Set<String> set1 = new HashSet<>();
    set1.addAll(Arrays.asList(s1.split(" ")));
    final Set<String> set2 = new HashSet<>();
    set2.addAll(Arrays.asList(s2.split(" ")));

    // Find out difference of sets set1 and intersection of set1,set2
    final SetView<String> intersection = Sets.intersection(set1, set2);
    final TreeSet<String> sortedIntersection = Sets.newTreeSet(intersection);
    final SetView<String> restOfSet1 = Sets.symmetricDifference(set1,
        intersection);
    final SetView<String> restOfSet2 = Sets.symmetricDifference(set2,
        intersection);

    // Sort it
    final TreeSet<String> sortedRestOfSet1 = Sets.newTreeSet(restOfSet1);
    final TreeSet<String> sortedRestOfSet2 = Sets.newTreeSet(restOfSet2);

    final StringBuilder sb0 = new StringBuilder();
    sortedIntersection.forEach((s) -> {
      sb0.append(" ").append(s);
    });
    final String t0 = sb0.toString().trim();

    final StringBuilder sb1 = new StringBuilder();
    Sets.union(sortedIntersection, sortedRestOfSet1).forEach((s) -> {
      sb1.append(" ").append(s);
    });
    final String t1 = sb1.toString().trim();

    final StringBuilder sb2 = new StringBuilder();
    Sets.union(intersection, sortedRestOfSet2).forEach((s) -> {
      sb2.append(" ").append(s);
    });
    final String t2 = sb2.toString().trim();

    final int amt1 = calculateLevensteinDistance(t0, t1);
    final int amt2 = calculateLevensteinDistance(t0, t2);
    final int amt3 = calculateLevensteinDistance(t1, t2);

    return Math.max(Math.max(amt1, amt2), amt3);
  }

  public static int calculateLevensteinDistance(String s1, String s2) {
    int distance = StringUtils.getLevenshteinDistance(s1, s2);
    double ratio = ((double) distance) / (Math.max(s1.length(), s2.length()));
    return 100 - new Double(ratio * 100).intValue();
  }

  public static String escapeString(String token) {
    final Pattern p = Pattern.
        compile("[^\\w+]", Pattern.UNICODE_CHARACTER_CLASS);
    return p.matcher(token).replaceAll(" ");
  }

  public static void main(String[] args) {
    final long start = System.currentTimeMillis();
    // A score of 100 means complete match.
    System.out.println(getRatio("web services as a software",
        "software as a services"));
    System.out.println(getRatio("CSK vs RCB", "RCB vs CSK"));
    System.out.println(
        getRatio("software-as-a-service", "software as a service"));
    System.out.println(getRatio("Microsoft's deal with skype",
        "Microsoft skype deal"));
    System.out.println(getRatio("apple is good", "Google is best apple is"));
    final long end = System.currentTimeMillis();
    System.out.println("Executed in " + (end - start) + " ms");
  }

}

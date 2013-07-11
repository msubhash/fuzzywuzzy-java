import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;

public class FuzzyMatch {

	/*
	 * t0 = [SORTED_INTERSECTION] 
	 * t1 = [SORTED_INTERSECTION] + [SORTED_REST_OF_STRING1] 
	 * t2 = [SORTED_INTERSECTION] + [SORTED_REST_OF_STRING2]
	 * 
	 * outcome = max(t0,t1,t2)
	 * 
	 */
	
	public static int getRatio(String s1, String s2, boolean debug) {
		
		if (s1.length() >= s2.length()) {		
			// We need to swap s1 and s2		
			String temp = s2;
			s2 = s1;
			s1 = temp;			
		}

		// Get alpha numeric characters
		
		s1 = escapeString(s1);
		s2 = escapeString(s2);
		
		s1 = s1.toLowerCase();
		s2 = s2.toLowerCase();
		
		
		Set<String> set1 = new HashSet<String>();
		Set<String> set2 = new HashSet<String>();
		
		//split the string by space and store words in sets
		StringTokenizer st1 = new StringTokenizer(s1);		
		while (st1.hasMoreTokens()) {
			set1.add(st1.nextToken());
		}

		StringTokenizer st2 = new StringTokenizer(s2);		
		while (st2.hasMoreTokens()) {
			set2.add(st2.nextToken());
		}
		
		SetView<String> intersection = Sets.intersection(set1, set2);
		
		TreeSet<String> sortedIntersection = Sets.newTreeSet(intersection);

		if (debug) {
		    System.out.print("Sorted intersection --> ");
		for (String s:sortedIntersection) 
			System.out.print(s + " ");
		}
		
		// Find out difference of sets set1 and intersection of set1,set2
		
		SetView<String> restOfSet1 = Sets.symmetricDifference(set1, intersection);
		
		// Sort it
		
		TreeSet<String> sortedRestOfSet1 = Sets.newTreeSet(restOfSet1);
		
		SetView<String> restOfSet2 = Sets.symmetricDifference(set2, intersection);
		TreeSet<String> sortedRestOfSet2 = Sets.newTreeSet(restOfSet2);
		
		if (debug) {
		System.out.print("\nSorted rest of 1 --> ");
		for (String s:sortedRestOfSet1) 
			System.out.print(s + " ");
		
		System.out.print("\nSorted rest of 2 -->");
		for (String s:sortedRestOfSet2) 
			System.out.print(s + " ");
		}
		
		String t0 = "";
		String t1 = "";
		String t2 = "";
		
		for (String s:sortedIntersection) {
			t0 = t0 + " " + s;			
		}
		t0 = t0.trim();
		
		Set<String> setT1 = Sets.union(sortedIntersection, sortedRestOfSet1);
		for (String s:setT1) {
			t1 = t1 + " " + s;			
		}
		t1 = t1.trim();
		
		Set<String> setT2 = Sets.union(intersection, sortedRestOfSet2);		
		for (String s:setT2) {
			t2 = t2 + " " + s;			
		}
		
		t2 = t2.trim();
		
		
		int amt1 = calculateLevensteinDistance(t0, t1);
		int amt2 = calculateLevensteinDistance(t0, t2);
		int amt3 = calculateLevensteinDistance(t1, t2);
		
		if (debug) {
			System.out.println();
			System.out.println("t0 = " + t0 + " --> " + amt1);
			System.out.println("t1 = " + t1 + " --> " + amt2);
			System.out.println("t2 = " + t2 + " --> " + amt3);
			System.out.println();
		}
		
		
		int finalScore = Math.max(Math.max(amt1, amt2), amt3);
		return finalScore;	
	}
	
	public static int calculateLevensteinDistance(String s1, String s2) {
		int distance = StringUtils.getLevenshteinDistance(s1, s2);
		double ratio = ((double) distance) / (Math.max(s1.length(), s2.length()));
		return 100 - new Double(ratio*100).intValue();		
	}
	
	public static String escapeString(String token) {

		StringBuffer s = new StringBuffer(token.length());

		CharacterIterator it = new StringCharacterIterator(token);
		for (char ch = it.first(); ch != CharacterIterator.DONE; ch = it.next()) {
			switch (ch) {
			// '-,)(!`\":/][?;~><
			case '\'':
			case '/':
			case '\\':
			case '-':
			case ',':
			case ')':
			case '(':
			case '!':
			case '`':
			case '\"':
			case ':':
			case ']':
			case '[':
			case '?':
			case ';':
			case '~':
			case '<':
			case '>':
				s.append(" ");
				break;
			default:
				s.append(ch);
				break;
			}
		}

		token = s.toString();
		return token;
	}

	public static void main(String[] args) {        
		boolean debug = false;
		// A score of 100 means complete match.
		System.out.println(getRatio("web services as a software", "software as a services", debug));
		System.out.println(getRatio("CSK vs RCB", "RCB vs CSK", debug));
		System.out.println(getRatio("software-as-a-service", "software as a service", debug));
		System.out.println(getRatio("Microsoft's deal with skype", "Microsoft skype deal", debug));
		System.out.println(getRatio("apple is good", "Google is best apple is", debug));
    }
}

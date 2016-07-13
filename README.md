fuzzywuzzy-java
===============

Java implementation of famous fuzzy wuzzy algorithm -- http://seatgeek.com/blog/dev/fuzzywuzzy-fuzzy-string-matching-in-python

Dependent Libararies:

Google guava - For intersection and union operations on collections
Apache commons-lang - To Levenstein Distance implementation

funnywuzzy java implementation calculates the match score of input strings s1, s2

Results (Compared Strings (S1, S2) and % Matches found)

"CSK vs RCB", "RCB vs CSK"   -----------> 100% match

"web services as a software", "software as a services" -----------> 100% match

"software-as-a-service", "software as a service" -----------> 100% match

"Microsoft's deal with skype", "Microsoft skype deal" -----------> 100% match

"apple is good", "Google is best apple is" -----------> 62% match

===========================================================================
LICENSE - MIT

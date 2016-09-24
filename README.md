fuzzywuzzy-java
===============

Java implementation of famous fuzzy wuzzy algorithm -- http://seatgeek.com/blog/dev/fuzzywuzzy-fuzzy-string-matching-in-python

## Dependencies

* Google Guava - For intersection and union operations on collections
* Apache commons-lang - Levenstein Distance implementation

## Build & Run

```
javac -cp guava-14.0.1.jar:commons-lang3-3.1.jar FuzzyMatch.java
java -cp .:guava-14.0.1.jar:commons-lang3-3.1.jar FuzzyMatch
```

## Results

You should see something like:

```
100
100
100
100
62
```

Which means:

```
"CSK vs RCB", "RCB vs CSK" --> 100% match

"web services as a software" vs "software as a services" --> 100% match

"software-as-a-service" vs "software as a service" --> 100% match

"Microsoft's deal with skype" vs "Microsoft skype deal" --> 100% match

"apple is good" vs "Google is best apple is" --> 62% match
```

===========================================================================
LICENSE - MIT

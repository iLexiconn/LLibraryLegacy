package net.ilexiconn.llibrary.common.json;

import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.stream.JsonReader;

public class SubstitutionJsonReader extends JsonReader {
    public static final String DEFAULT_SUBSTITUTIONS_KEY = "substitutions";

    public static final String DEFAULT_SUBSTITUTION_PREFIX = "#";

    private final String substitutionsKey;

    private final String substitutionPrefix;

    private final int substitutionPrefixLength;

    private final Map<String, String> substitutions = new HashMap<String, String>();

    private boolean needsSubstitutionStart = true;

    private boolean hasSubsitutions = true;

    private boolean readingSubstitutions;

    private String nextKey;

    public SubstitutionJsonReader(Reader in) {
        this(in, DEFAULT_SUBSTITUTIONS_KEY, DEFAULT_SUBSTITUTION_PREFIX);
    }

    public SubstitutionJsonReader(Reader in, String substitutionsKey, String substitutionPrefix) {
        super(in);
        this.substitutionsKey = substitutionsKey;
        this.substitutionPrefix = substitutionPrefix;
        substitutionPrefixLength = substitutionPrefix.length();
    }

    @Override
    public void endObject() throws IOException {
        super.endObject();
        if (readingSubstitutions) {
            readingSubstitutions = false;
            nextKey = null;
        }
    }

    @Override
    public String nextName() throws IOException {
        String name = super.nextName();
        if (needsSubstitutionStart) {
            if (name.equals(substitutionsKey)) {
                readingSubstitutions = true;
            } else {
                hasSubsitutions = false;
            }
            needsSubstitutionStart = false;
        } else if (readingSubstitutions) {
            nextKey = name;
        }
        return name;
    }

    @Override
    public String nextString() throws IOException {
        String string = super.nextString();
        if (readingSubstitutions) {
            substitutions.put(nextKey, string);
        } else if (hasSubsitutions) {
            if (string.indexOf(substitutionPrefix) == 0) {
                String key = string.substring(substitutionPrefixLength);
                String substitution = substitutions.get(key);
                if (substitution != null) {
                    string = substitution;
                }
            }
        }
        return string;
    }
}

package com.karlhammar.ontometrics.plugins;

public class ParserConfiguration {
    public static enum ImportStrategy {ALLOW_IMPORTS, IGNORE_IMPORTS};

    // default strategy
    private ImportStrategy importStrategy = ImportStrategy.IGNORE_IMPORTS;

    public ImportStrategy getImportStrategy() {
        return importStrategy;
    }

    public ParserConfiguration setImportStrategy(ImportStrategy is) {
        this.importStrategy = is;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if(this == o)
            return true;
        else if(null == o)
            return false;
        else if(! (o instanceof ParserConfiguration))
            return false;

        return importStrategy == ((ParserConfiguration) o).importStrategy;
    }
}

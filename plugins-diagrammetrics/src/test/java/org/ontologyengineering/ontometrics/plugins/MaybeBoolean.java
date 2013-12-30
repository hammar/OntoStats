package org.ontologyengineering.ontometrics.plugins;

public class MaybeBoolean {
    static enum Option {MAYBE, JUST_TRUE, JUST_FALSE};

    Option o;

    public MaybeBoolean (Boolean b) {
        if(b.booleanValue()) {
            this.o = Option.JUST_TRUE;
        } else {
            this.o = Option.JUST_FALSE;
        }
    }

    public MaybeBoolean (Option o) {
        this.o = o;
    }

    public boolean isMaybe() {
        return Option.MAYBE == this.o;
    }

    public boolean isJust() {
        return !isMaybe();
    }

    public Option get() {
        return o;
    }
}
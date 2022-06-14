package com.program.shell;

import java.util.List;

public class ParsingInfo<T> {
    public int found;
    public int parsed;
    public int failed;
    public int alreadyAdded;
    public List<T> result;

    public int getFound() {
        return found;
    }

    public void setFound(int found) {
        this.found = found;
    }

    public int getParsed() {
        return parsed;
    }

    public void setParsed(int parsed) {
        this.parsed = parsed;
    }

    public int getFailed() {
        return failed;
    }

    public void setFailed(int failed) {
        this.failed = failed;
    }

    public int getAlreadyAdded() {
        return alreadyAdded;
    }

    public void setAlreadyAdded(int alreadyAdded) {
        this.alreadyAdded = alreadyAdded;
    }

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }
}

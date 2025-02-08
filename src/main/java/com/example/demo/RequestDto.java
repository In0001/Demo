package com.example.demo;

public class RequestDto {
    private String path;
    private int n;

    public RequestDto() {}

    public RequestDto(String path, int n) {
        this.path = path;
        this.n = n;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }
}

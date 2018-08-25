package com.credolabs.genericsvuploader.csv;

import org.springframework.web.multipart.MultipartFile;

public class GenericFileUploadRequest {
    private MultipartFile file;
    private String driver;
    private String url;
    private String user;
    private String password;
    private ProcessEnum process;

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ProcessEnum getProcess() {
        return process;
    }

    public void setProcess(ProcessEnum process) {
        this.process = process;
    }

    @Override
    public String toString() {
        return "GenericFileUploadRequest{" +
                "file=" + file +
                ", driver='" + driver + '\'' +
                ", url='" + url + '\'' +
                ", user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", process=" + process +
                '}';
    }
}

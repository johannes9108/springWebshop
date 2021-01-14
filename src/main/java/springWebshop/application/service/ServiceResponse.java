package springWebshop.application.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ServiceResponse<T> {
    private boolean sucessful;
    private List<T> responseObjects;
    private int currentPage;
    private int totalPages;
    private int totalItems;
    private List<String> errorMessages;

    public ServiceResponse() {
        this.responseObjects = new ArrayList<T>();
        this.errorMessages = new ArrayList<>();
    }

    public ServiceResponse(List<T> responseObjects, List<String> errorMessages) {
        this.responseObjects = responseObjects;
        this.errorMessages = errorMessages;
        this.sucessful = CollectionUtils.isEmpty(errorMessages);
    }

    public void addErrorMessage(String error) {
        sucessfulFalse();
        this.errorMessages.add(error);
    }

    public void addResponseObject(T object) {
        sucessfulTrue();
        this.responseObjects.add(object);
    }

    public void setResponseObjects(List<T> objects) {
        sucessfulTrue();
        this.responseObjects = objects;
    }

    public void setErrorMessages(List<String> errorMessages) {
        if (!errorMessages.isEmpty()) sucessfulFalse();
        this.errorMessages = errorMessages;
    }

    private void sucessfulTrue() {
        this.sucessful = true;
    }

    private void sucessfulFalse() {
        this.sucessful = false;
    }
    @Override
    public String toString() {
        return "ServiceResponse{" +
                "sucessful=" + sucessful +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalItems=" + totalItems +
                ", errorMessages=" + errorMessages +
                '}';
    }
}


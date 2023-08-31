package org.example.web.dto;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotEmpty;

public class Book {

    private Integer id;

    @NotEmpty(message = "Autor's name cannot be empty.")
    private String author;
    @NotEmpty(message = "Title book cannot be empty.")
    private String title;

    @Digits(integer = 4, fraction = 0, message = "Size no more than 4 characters." )
    private Integer size;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    @Override
    public String toString(){
      return "Book{"+"id="+id+", author='"+author+'\''+", title='"+title+'\''+", size="+size+'\'';
    }
}

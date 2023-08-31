package org.example.web.dto;

import javax.validation.constraints.Pattern;

public class BookRegexToRemove {

  @Pattern(regexp="(?:id:[0-9]+|author:[a-zA-Z0-9]+|title:[a-zA-Z0-9]+|size:[0-9]+)", message="In the template for deletion, before the : sign, specify the field by which to delete")
  private String regexValue;

  public BookRegexToRemove() {
  }

  public BookRegexToRemove ( String regexValue ) {
    this.regexValue = regexValue;
  }

  public String getRegexValue() {
    return regexValue;
  }

  public void setRegexValue( String regexValue ) {
    this.regexValue = regexValue;
  }

  @Override
  public String toString(){
    return "BookRegexToRemove{value='"+ getRegexValue() +'\'' + "}";
  }
}

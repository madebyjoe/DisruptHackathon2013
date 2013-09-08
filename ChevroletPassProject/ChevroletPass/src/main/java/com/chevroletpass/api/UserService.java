package com.chevroletpass.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created on 9/7/13.
 */
public class UserService implements IUserService {

  private static final String BASE_URL = "http://blog/example.com";

  @Override
  public ArrayList<User> getAllUsers() {
    // The URL for making the GET request
    final String url = BASE_URL + "/users.json";
    //This is where we get the RestTemplate and add the message converters
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
    //This is where we call the exchange method and process the response
    ResponseEntity<UserContainer> responseEntity = restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(), UserContainer.class);
    return responseEntity.getBody().getUsers();
  }

//  private ArrayList<User> foo() {
//    ResponseEntity<UserContainer> responseEntity = restTemplate.exchange(url, HttpMethod.GET, getRequestEntity(), UserContainer.class);
//    UserContainer uc = responseEntity.getBody();
//    if (null != uc) {
//      ArrayList<User> users = uc.getUsers();
//      return users;
//    } else {
//      return null;
//    }
//  }


  @Override
  public User getUserById(long id) {
    // The URL for making the GET request
    final String url = BASE_URL + "/user?userId={id}";
    //This is where we get the RestTemplate and add the message converters
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

    return restTemplate.getForObject(url, User.class, id);
  }

  @Override
  public boolean deleteUserById(long id) {
    final String url = BASE_URL + "/user?userId={id}";
    //This is where we get the RestTemplate and add the message converters
    RestTemplate restTemplate = new RestTemplate();
    restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());

    restTemplate.delete(url, User.class, id);
    return true;
  }

  @Override
  public boolean createNewUser(User user) {

    String url = BASE_URL + "user";
    HttpHeaders requestHeaders = new HttpHeaders();
    // Set the Content-Type header
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    //create the request body
    MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
    body.add("user[id]", String.valueOf(user.getId()));
    body.add("user[first_name]", user.getFirstName());
    body.add("user[last_name]", user.getLastName());

    //create the request entity
    HttpEntity<?> requestEntity = new HttpEntity<Object>(body, requestHeaders);
    //Get the RestTemplate and add the message converters
    RestTemplate restTemplate = new RestTemplate();

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    messageConverters.add(new FormHttpMessageConverter());
    messageConverters.add(new StringHttpMessageConverter());
    restTemplate.setMessageConverters(messageConverters);
    try {
      ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
      HttpStatus status = response.getStatusCode();
      if (status == HttpStatus.CREATED) {
        return true;
      } else {
        return false;
      }
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return false;
    }
  }

  @Override
  public User updateUser(User updatedUser) {
    String url = BASE_URL + "updateuser?userid={id}";
    HttpHeaders requestHeaders = new HttpHeaders();
    // Set the Content-Type header
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

    MultiValueMap<String, String> body = new LinkedMultiValueMap<String, String>();
    body.add("user[id]", String.valueOf(updatedUser.getId()));
    body.add("user[first_name]", updatedUser.getFirstName());
    body.add("user[last_name]", updatedUser.getLastName());

    //create the request entity
    HttpEntity<?> requestEntity = new HttpEntity<Object>(body, requestHeaders);
    RestTemplate restTemplate = new RestTemplate();

    List<HttpMessageConverter<?>> messageConverters = new ArrayList<HttpMessageConverter<?>>();
    messageConverters.add(new FormHttpMessageConverter());
    messageConverters.add(new StringHttpMessageConverter());
    restTemplate.setMessageConverters(messageConverters);
    try {
      ResponseEntity<User> response = restTemplate.exchange(url, HttpMethod.PUT, requestEntity, User.class, updatedUser.getId());
      HttpStatus status = response.getStatusCode();
      if (status == HttpStatus.CREATED) {
        return response.getBody();
      } else {
        return null;
      }
    } catch (HttpClientErrorException e) {
      e.printStackTrace();
      return null;
    }
  }

  private HttpEntity<?> getRequestEntity() {
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
    return new HttpEntity<Object>(requestHeaders);
  }

}


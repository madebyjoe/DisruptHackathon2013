package com.chevroletpass.api;

import org.springframework.http.ContentCodingType;
import org.springframework.http.HttpAuthentication;
import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.Collections;

/**
 * Created on 9/7/13.
 */
public class BasicAuth {

  protected HttpHeaders getAuthenticatedRequestHeaders(String username, String password){

    // Set the BasicAuth needed for server
    HttpAuthentication authHeader = new HttpBasicAuthentication(username,password);
    HttpHeaders requestHeaders = new HttpHeaders();
    requestHeaders.setAuthorization(authHeader);
    // Set the Content-Type header
    requestHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    // set the accept type to json
    requestHeaders.setAccept(Collections.singletonList(new MediaType("application", "json")));
    requestHeaders.setAcceptEncoding(ContentCodingType.GZIP);

    return requestHeaders;
  }

  //create the request entity
  //HttpEntity<?> requestEntity = new HttpEntity<Object>(getAuthenticatedRequestHeaders("myUserName", "myPassword"));

  //create the request entity POST
  //HttpEntity<?> requestEntity = new HttpEntity<Object>(body, getAuthenticatedRequestHeaders("myUserName", "myPassword"));



//  ResponseEntity<UserContainer> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, UserContainer.class);
//  UserContainer resultContainer = (UserContainer) response.getBody();
//  HttpHeaders rHeaders = response.getHeaders();
//  List<String> val = rHeaders.get("Set-Cookie");
//  if(null != val) {
//    cookie = val.get(0);
//    Editor ed = prefs.edit();
//    String cookie = val.get(0);
//    ed.putString(PREFS_KEY_USER_COOKIE, cookie);
//    ed.commit();
//  }

//  SharedPreferences mPrefs = getSharedPreferences(PREFS_PRIVATE_DATA, Context.MODE_PRIVATE);
//  String cookie = mPrefs.getString(PREFS_KEY_USER_COOKIE,"no data");
//  HttpHeaders requestHeaders = getAuthenticatedRequestHeaders("myUserName","myPassword");
////set the session cookie
//  requestHeaders.set("Cookie", cookie);
//  //create the request entity
//  HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
//  RestTemplate restTemplate = new RestTemplate();
//  restTemplate.setMessageConverters(getMessageConverters());
//  HttpClient httpClient = HttpUtils.getNewHttpClient();
//  restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
//  try {
//    ResponseEntity<UserDetailContainer>response= restTemplate.exchange(url, HttpMethod.GET, requestEntity, UserDetailContainer.class);
//    UserDetailContainer resultContainer = (UserDetailContainer) response.getBody();
//    return resultContainer.getUserDetail();
//// more detailed exception handling should be implemented here
//  }catch(Exception ex){
//    ex.printStackTrace();
//    return null;
//  }

}

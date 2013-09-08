package com.chevroletpass.api;

import java.util.ArrayList;

/**
 * Created on 9/7/13.
 */
public interface IUserService {
  public ArrayList getAllUsers();
  public User getUserById(long id);
  public boolean deleteUserById(long id);
  public boolean createNewUser(User user);
  public User updateUser(User updatedUser);
}

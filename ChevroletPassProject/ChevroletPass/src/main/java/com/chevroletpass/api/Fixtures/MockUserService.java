package com.chevroletpass.api.Fixtures;

import com.chevroletpass.api.IUserService;
import com.chevroletpass.api.json.User;

import java.util.ArrayList;

/**
 * Created on 9/7/13.
 */
public class MockUserService implements IUserService {
  @Override
  public ArrayList getAllUsers() {
    return null;
  }

  @Override
  public User getUserById(long id) {
    return null;
  }

  @Override
  public boolean deleteUserById(long id) {
    return false;
  }

  @Override
  public boolean createNewUser(User user) {
    return false;
  }

  @Override
  public User updateUser(User updatedUser) {
    return null;
  }
}

package io.kimmking.rpcfx.demo.provider;

import io.kimmking.rpcfx.demo.api.User;
import io.kimmking.rpcfx.demo.api.UserService;
import io.my.MyService;

@MyService
public class UserServiceImpl implements UserService {

    @Override
    public User findById(int id) {
        return new User(id, "KK" + System.currentTimeMillis());
    }

    @Override
    public String getUserName(String userName) {
        return null;
    }
}

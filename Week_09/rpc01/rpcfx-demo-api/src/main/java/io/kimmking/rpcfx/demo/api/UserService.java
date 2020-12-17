package io.kimmking.rpcfx.demo.api;

public interface UserService {

    User findById(int id);

    String getUserName(String userName);

}

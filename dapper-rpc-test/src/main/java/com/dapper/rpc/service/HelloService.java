package com.dapper.rpc.service;

public interface HelloService {
    String hello(String name);

    String hello(String name, Integer age);
}

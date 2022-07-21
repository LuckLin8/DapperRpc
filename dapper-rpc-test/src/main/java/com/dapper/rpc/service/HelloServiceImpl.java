package com.dapper.rpc.service;


public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return "Hello " + name;
    }

    @Override
    public String hello(String name, Integer age) {
        return name + " is " + age;
    }
}

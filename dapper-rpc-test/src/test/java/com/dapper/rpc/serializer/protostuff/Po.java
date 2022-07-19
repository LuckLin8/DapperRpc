package com.dapper.rpc.serializer.protostuff;

import java.util.Arrays;
import java.util.List;

public class Po {
	
	private Integer id;
	private String name;
	private String remark;
	private Integer age;
	private int[] array;
	private InnerPo innerPo;
	private List<String> more;
	
	public Po(){}
	
	public Po(Integer id,String name,String remark,Integer age,
			int[] array,InnerPo innerPo,List<String> more){
		this.id = id;
		this.name = name;
		this.remark = remark;
	    this.age = age;
	    this.array = array;
	    this.innerPo = innerPo;
	    this.more = more;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public int[] getArray() {
		return array;
	}
	public void setArray(int[] array) {
		this.array = array;
	}
	public InnerPo getInnerPo() {
		return innerPo;
	}
	public void setInnerPo(InnerPo innerPo) {
		this.innerPo = innerPo;
	}
 
	public List<String> getMore() {
		return more;
	}
 
	public void setMore(List<String> more) {
		this.more = more;
	}
	
 
  
	@Override
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		sb.append("id:"+id+"\n");
		sb.append("name:"+name+"\n");
		sb.append("remark:"+remark+"\n");
		sb.append("age:"+age+"\n");
		sb.append("array:"+ Arrays.toString(array)+"\n");
		sb.append("innerPo:"+innerPo.toString()+"\n");
	    sb.append("more:"+more);
		return  sb.toString();
	}
	
	
}
 
 
class InnerPo{
	private Integer id;
	private String name;
	
	public InnerPo(){}
	
	public InnerPo(Integer id,String name){
		this.id = id;
		this.name = name;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	@Override
	public String toString() {
		return id+"-"+name;
	}
	
}
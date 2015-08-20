package testHashArrayListMap;


import org.junit.Test;

import MaQiao.MaQiaoHashArryListMap.MQHashArrayListMap;

//import java.sql.Array;
//import java.util.ArrayList;
//import java.util.Arrays;
public class test2 {
	@Test
	public void test() {

	MQHashArrayListMap<String,String> MQhalm=new MQHashArrayListMap<String,String>();	
	MQhalm.put("d", "dd");
	MQhalm.put("c", "cc");
	MQhalm.put("b", "bb");
	MQhalm.put("a", "aa");
	MQhalm.put("a", "ee");
	System.out.println(MQhalm.toString());
	}


}

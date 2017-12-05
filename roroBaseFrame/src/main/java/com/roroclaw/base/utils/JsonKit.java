package com.roroclaw.base.utils;

import java.beans.Visibility;
import java.io.IOException;
import java.util.Map;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

public class JsonKit {
	public static Map json2Map(String resultStr) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Map<String, Object> maps = mapper.readValue(resultStr,
				Map.class);
		return maps;
	}
	
	public static Object json2Obj(String resultStr,Class objClass) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper();
		Object object = mapper.readValue(resultStr,
				objClass);
		return object;
	}
	
	public static String Array2Str(Object[] objs) throws JsonParseException, JsonMappingException, IOException{
		ObjectMapper mapper = new ObjectMapper(); 
        String json = mapper.writeValueAsString(objs); 
        return json;
	}
}

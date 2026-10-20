package com.tlias;


import java.io.PrintStream;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;




import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@SpringBootTest
class TliasApplicationTests {
	
	static{
		try {
			System.setOut(new PrintStream(System.out, true, "UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	/*@Autowired
	private DeptMapper deptMapper;

	@Test
	void contextLoads() {
	}
	@Test
	public void testfindAll(){
		List<Dept> list = deptMapper.findAll();
		list.forEach(System.out::println);
	}*/

	
	@Test
	public void testMakeJWT(){
		//密钥
		final String SECRET = "this-is-my-super-secure-jwt-secret-key-123456";
		//生成Key对象
		final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
		//封装自定义数据
		Map<String,Object> data = new HashMap<>();
		data.put("id", "1");
		data.put("username", "songjiang");
		
		//生成JWT令牌
		String jwt = Jwts.builder()
		.addClaims(data)
		.setIssuedAt(new Date())
		.setExpiration(new Date(System.currentTimeMillis() + 5000))
		.signWith(key, SignatureAlgorithm.HS256)
		.compact();
		
		

		//输出到控制台
		System.out.println(jwt);
	}
	
	@Test
	public void testParseJWT(){ 
		try{
			Thread.sleep(6000);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		final String SECRET = "this-is-my-super-secure-jwt-secret-key-123456";
		final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());
		String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJpZCI6IjEiLCJ1c2VybmFtZSI6InNvbmdqaWFuZyIsImlhdCI6MTc3MzQ5MTUxNCwiZXhwIjoxNzczNDkxNTE5fQ.b6VlSmJuv8WaKuhAgEc3P5LjlDt4hRdl9FuvmUnbEhU";

		Claims claims = Jwts.parserBuilder()
		.setSigningKey(key)
		.build()
		.parseClaimsJws(jwt)
		.getBody();
		System.out.println(claims);
	}



	
}

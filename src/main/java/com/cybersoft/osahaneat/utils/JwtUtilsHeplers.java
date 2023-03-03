package com.cybersoft.osahaneat.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Calendar;
import java.util.Date;

@Component
public class JwtUtilsHeplers {

    @Value("${jwt.privateKey}")
    private String privateKey;

    private long expiredTime = 8 * 60 * 60 * 1000;

    public String generateToken(String data){
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
//        Calendar calendar = Calendar.getInstance();
//        calendar.getTimeInMillis();
        Date date = new Date();
        long currentDateMilis = date.getTime() + expiredTime;
        Date expiredDate = new Date(currentDateMilis);

        String jwt = Jwts.builder()
                .setSubject(data) //Dữ liệu muốn lưu kèm khi mã hóa JWT để sau này lấy ra xử dụng
                .signWith(key) //Key mã hóa
                .setExpiration(expiredDate)
                .compact();

        System.out.println("token : " + jwt);
        return jwt;
    }

    public boolean verifyToken(String token){
        try{
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        }catch (Exception e){
            return false;
        }
    }

    public String getDataFromToken(String token){
        try{
            SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(privateKey));
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
        }catch (Exception e){
            return "";
        }
    }

}

package com.atguigu.sk.controller;

import com.atguigu.sk.utils.JedisPoolUtil;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

import java.util.Random;

@RestController
public class PhoneController {

    private String code;
    private String phone;

    Jedis jedis = new Jedis("192.168.44.135", 6379);


    @PostMapping(value = "/sendphone", produces = "text/plain;charset=UTF-8")
    public String phoneTest(String phone) {

        jedis.expire(phone + "_time", 3600);
        jedis.setnx(phone + "_time", "3");
        if (!jedis.get(phone + "_time").equals("0")) {
            this.phone = phone;
            code = new Random().nextInt(9999) + "";
            System.out.println(code);
        }
        jedis.set(phone + "_code", code);
        jedis.expire(phone + "_code", 120);
        System.out.println(jedis.get(phone + "_time"));
        if (jedis.get(phone + "_time").equals("0")) {
            return "您今天已经没有机会了";
        }
        jedis.decrBy(phone + "_time", 1);
        return "请在两分钟之内输入验证码";
    }

    @PostMapping(value = "/docode", produces = "text/plain;charset=UTF-8")
    public String docode(String code) {
        String msg = "";
        if (jedis.ttl(phone + "_code") >= 0) {
            if (this.code.equals(code)) {
                msg = "验证成功";
            } else {
                msg = "验证码错误";
            }
        } else {
            msg = "验证码已过期";
        }
        //System.out.println(msg);
        return msg;
    }




    /*@PostMapping(value = "/seckill", produces = "text/plain;charset=UTF-8")
    public String seckill(String pid) {
        Jedis jedis2 = new Jedis("192.168.44.135", 6379);
        String userid=new Random().nextInt(9999)+"";
        String idkey="sk:"+pid+":qt";
        String useridkey="sk:"+pid+":user";
        if (jedis2.sismember(useridkey, userid)){
            System.out.println("请勿重复秒杀");
            jedis2.close();
            return "请勿重复秒杀";
        }
        jedis2.watch(idkey);
        if (StringUtils.isEmpty(jedis2.get(idkey))){
            System.out.println("秒杀尚未开始");
            jedis2.close();
            return "秒杀尚未开始";
        }
        if (Integer.parseInt(jedis2.get(idkey))<=0){
            System.out.println("已经卖完了");
            jedis2.close();
            return "已经卖完了";
        }
        Transaction multi = jedis2.multi();
        multi.decr(idkey);
        multi.sadd(useridkey, userid);
        multi.exec();
        System.out.println(userid+" 成功抢到");
        jedis2.close();
        return "秒杀成功";
    }*/

    static String secKillScript = "local userid=KEYS[1];\r\n"
            + "local prodid=KEYS[2];\r\n"
            + "local qtkey='sk:'..prodid..\":qt\";\r\n"
            + "local usersKey='sk:'..prodid..\":usr\";\r\n"
            + "local userExists=redis.call(\"sismember\",usersKey,userid);\r\n"
            + "if tonumber(userExists)==1 then \r\n"
            + "   return 2;\r\n"
            + "end\r\n"
            + "local num= redis.call(\"get\" ,qtkey);\r\n"
            + "if tonumber(num)<=0 then \r\n"
            + "   return 0;\r\n"
            + "else \r\n"
            + "   redis.call(\"decr\",qtkey);\r\n"
            + "   redis.call(\"sadd\",usersKey,userid);\r\n"
            + "end\r\n"
            + "return 1";


    @PostMapping(value = "/seckill", produces = "text/plain;charset=UTF-8")
    public String seckill(String pid) {
        //Jedis jedis2 = new Jedis("192.168.44.135", 6379);
        String userid = new Random().nextInt(9999) + "";
        //String idkey = "sk:" + pid + ":qt";
       // String useridkey = "sk:" + pid + ":user";
        Jedis jedis2 = JedisPoolUtil.getJedisPoolInstance().getResource();
        String scriptLoad = jedis2.scriptLoad(secKillScript);
        Object eval = jedis2.evalsha(scriptLoad, 2, userid, pid);
        jedis2.close();
        Integer result = (int) (long) eval;
        if (result == 2) {
            System.out.println("请勿重复秒杀");
            return "请勿重复秒杀";
        } else if (result == 1) {
            System.out.println(userid + " 成功抢到");
            return "秒杀成功";
        } else {
            System.out.println("已经卖完了");
            return "已经卖完了";
        }
    }

}

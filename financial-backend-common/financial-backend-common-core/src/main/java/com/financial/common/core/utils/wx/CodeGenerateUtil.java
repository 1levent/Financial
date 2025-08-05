package com.financial.common.core.utils.wx;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * 验证码生成器
 * @author xinyi
 */
public class CodeGenerateUtil {
    public static final Integer CODE_LEN = 4;

//    private static final Random random = new Random();
//
//    private static final List<String> specialCodes = Arrays.asList(
//            "666", "888", "000", "999", "555", "222", "333", "777",
//            "520", "911",
//            "234", "345", "456", "567", "678", "789"
//    );
//
//    public static String genCode(int cnt) {
//        if (cnt >= specialCodes.size()) {
//            int num = random.nextInt(1000);
//            if (num >= 100 && num <= 200) {
//                // 100-200之间的数字作为关键词回复，不用于验证码
//                return genCode(cnt);
//            }
//            return String.format("%0" + CODE_LEN + "d", num);
//        } else {
//            return specialCodes.get(cnt);
//        }
//    }
    public static String genCode(){
        Random random = new Random();
        StringBuilder captchaBuilder = new StringBuilder();

        for (int i = 0; i < CODE_LEN; i++) {
            // 生成0到9之间的随机数
            int digit = random.nextInt(10);
            captchaBuilder.append(digit);
        }

        return captchaBuilder.toString();
    }

    public static boolean isVerifyCode(String content) {
        if (!NumberUtils.isDigits(content) || content.length() != CodeGenerateUtil.CODE_LEN) {
            return false;
        }

//        int num = Integer.parseInt(content);
//        return num < 100 || num > 200;
        return true;
    }
}
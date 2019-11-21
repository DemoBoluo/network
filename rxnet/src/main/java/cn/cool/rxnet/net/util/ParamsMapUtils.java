package cn.cool.rxnet.net.util;

import java.util.Map;

/**
 * Created by TRZ on 2017/10/20.
 * 备注：
 */

public class ParamsMapUtils extends BaseParamsMapUtils {

    private static Map<String, String> mapparam;


    public static Map<String, String> getMapparam() {
        return  mapparam = BaseParamsMapUtils.getParamsMap();
    }

    /**
     *   首页 列表详情
     * @param identification
     * @return
     */
    public static Map<String, String> getHomeCate(String identification) {
        mapparam = getMapparam();
        mapparam.put("identification", identification);
        return mapparam;
    }

}

package cn.cool.rxnet.net.dns;

import android.text.TextUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;

import cn.cool.rxnet.net.base.NetWorkApi;
import cn.cool.rxnet.net.util.Lutils;
import cn.cool.rxnet.net.util.ObjectHelper;
import okhttp3.Dns;

/**
 *@author boluo
 *classdescrption：DNS解析失败
 *version：1.0
 *date： 2019/11/29
 */
public class HttpDns implements Dns {
    private NetWorkApi mNetWorkApi;
    public HttpDns(NetWorkApi netWorkApi){
        ObjectHelper.requireNonNull(netWorkApi,"HttpDns construct param netWorkApi is null");
        this.mNetWorkApi = netWorkApi;
    }
    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (TextUtils.isEmpty(hostname)){
            throw new UnknownHostException("HttpDns hostname == null");
        }
        String ip = mNetWorkApi.getIpByHost(hostname);
        if (!TextUtils.isEmpty(ip)) {
            List<InetAddress> netAddresses = Arrays.asList(InetAddress.getAllByName(ip));
            Lutils.i("HttpDns", "netAddresses:" + netAddresses);
            return netAddresses;
        }
        return SYSTEM.lookup(hostname);
    }
}

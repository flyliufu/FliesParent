package com.lefu8.fliesparent.frame;

import android.text.TextUtils;
import android.util.Log;
import com.lefu8.fliesparent.context.App;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import okhttp3.Dns;

/**
 * dns
*@author zyc
*created at 2017/6/19 9:06
*/

public class LefuHttpDNS implements Dns {

  @Override public List<InetAddress> lookup(String hostname) throws UnknownHostException {

    try {
      String ipByHostAsync = App.httpdns.getIpByHostAsync(hostname);
      if (ipByHostAsync != null && !TextUtils.isEmpty(ipByHostAsync)){
        Log.i("LefuHttpDNS","ipByHostAsync" + ipByHostAsync);
        return Arrays.asList(InetAddress.getAllByName(ipByHostAsync));
      }
    } catch (Exception e) {
      return SYSTEM.lookup(hostname);
    }
    return SYSTEM.lookup(hostname);
  }
}

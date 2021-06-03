package com.bz.datax.hook;

import com.alibaba.datax.common.spi.Hook;
import com.alibaba.datax.common.util.Configuration;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将DataX的job执行结果通过钉钉汇报$
 *
 * @author zhang.zw
 * @date: 2021-06-02 11:24
 **/
public class DingTalkReport implements Hook {
	private static final Logger LOG = LoggerFactory.getLogger(DingTalkReport.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat(
		"yyyy-MM-dd HH:mm:ss");

	@Override
	public String getName() {
		return "DingTalkReportHook";
	}

	@Override
	public void invoke(Configuration configuration, Map<String, Number> map) {
		LOG.debug(configuration.beautify());
		//{writeSucceedRecords=1248,
		// readSucceedRecords=1247,
		// totalErrorBytes=0,
		// writeSucceedBytes=81477,
		// byteSpeed=0,
		// totalErrorRecords=0,
		// recordSpeed=0,
		// waitReaderTime=308600221,
		// writeReceivedBytes=81477,
		// stage=1,
		// waitWriterTime=6348796,
		// percentage=1.0,
		// totalReadRecords=1247,
		// writeReceivedRecords=1248,
		// readSucceedBytes=81477,
		// totalReadBytes=81477}
		LOG.debug(map.toString());

		// 从job的json配置中读取自定义的参数
		String accessToken = getString(configuration.get("job.dingTalkReporter.accessToken"));
		String title = getString(configuration.get("job.dingTalkReporter.title"));
		String secret = getString(configuration.get("job.dingTalkReporter.secret"));
		String defaultTemplate = "# %s \n > %s \n - 成功读:%d \n - 成功写:%d \n - 等待%d秒";
		String time = dateFormat.format(System.currentTimeMillis());
		// 好像没办法拿到执行时间，如果需要更多map中没有的信息，可以改写Hook接口
		long totalWaitTime = TimeUnit.SECONDS.convert(map.get("waitReaderTime").longValue() + map.get("waitWriterTime").longValue(), TimeUnit.NANOSECONDS);

		String content = String.format(defaultTemplate, title, time, map.get("readSucceedRecords").longValue(), map.get("writeSucceedRecords").longValue(), totalWaitTime);

		DingTalkUtil.send(title, content, accessToken, secret);
	}

	String getString(Object obj){
		return null == obj ? "" : String.valueOf(obj);
	}

}

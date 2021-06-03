package com.bz.datax.hook;

import com.dingtalk.api.DefaultDingTalkClient;
import com.dingtalk.api.DingTalkClient;
import com.dingtalk.api.request.OapiRobotSendRequest;
import com.dingtalk.api.response.OapiRobotSendResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 钉钉工具类$
 *
 * @author zhang.zw
 * @date: 2021-06-02 13:56
 **/
public class DingTalkUtil {
	private static final Logger LOG = LoggerFactory.getLogger(DingTalkUtil.class);
	private static final String URL = "https://oapi.dingtalk.com/robot/send?access_token=";

	/**
	 *
	 * @param title         标题
	 * @param content       内容
	 * @param accessToken   访问令牌
	 * @param secret        加密签名（可选）
	 */
	public static void send(String title, String content, String accessToken, String secret) {
		if(isEmpty(title) || isEmpty(content) || isEmpty(accessToken)){
			LOG.error("Check Param");
			return;
		}
		try {
			DingTalkClient client = new DefaultDingTalkClient(buildUrl(accessToken, secret));
			OapiRobotSendRequest request = new OapiRobotSendRequest();
			request.setMsgtype("markdown");
			OapiRobotSendRequest.Markdown markdown = new OapiRobotSendRequest.Markdown();
			markdown.setTitle(title);
			markdown.setText(content);
			request.setMarkdown(markdown);
			OapiRobotSendResponse response = client.execute(request);
			LOG.info("Send DingTalk Message Result:{}-{}", response.getErrmsg(), response.getErrcode());
		}catch (Exception e){
			LOG.error("Send DingTalk Message Error", e);
		}
	}

	static String buildUrl(String accessToken, String secret)
		throws NoSuchAlgorithmException, UnsupportedEncodingException, InvalidKeyException {
		if(isEmpty(secret)){
			return URL + accessToken;
		}
		Long timestamp = System.currentTimeMillis();
		String stringToSign = timestamp + "\n" + secret;
		Mac mac = Mac.getInstance("HmacSHA256");
		mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), "HmacSHA256"));
		byte[] signData = mac.doFinal(stringToSign.getBytes("UTF-8"));
		String sign = URLEncoder.encode(new String(Base64.encodeBase64(signData)),"UTF-8");
		LOG.debug("sign:{}", sign);
		return URL + accessToken + "&timestamp=" + timestamp + "&sign=" + sign;
	}

	public static boolean isEmpty(String value){
		return null == value || "".equals(value);
	}
}

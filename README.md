# DataX 任务结果汇报 —— 钉钉

本插件通过Hook方式，实现DataX执行结果通过钉钉自定义机器人通知到群。

# 接入方式

1. 创建文件结构

```text
-datax
--hook
---dingtalk(可自定义）
----report-hook.jar
```

2. 引入项目依赖

将"taobao-sdk-java-auto_1479188381469-20210528.jar"（钉钉官方SDK）放入"datax/lib"下即可。

3. job配置
在job下新增"dingTalkReporter"字段，用来配置钉钉机器人。其中accessToken、title为**必填**，secret为**选填**
```json
{
    "job": {
        "dingTalkReporter": {
            "accessToken": "d9e5b4f89xxxxxxxxxxx",
            "secret": "SECc008578xxxxxxx",
            "title": "Test表同步"
        },
        "setting": {
            "speed": {
                "byte":10485760
            },
            "errorLimit": {
                "record": 0,
                "percentage": 0.02
            }
        },
……
}
```
**启动效果：**

```text
2021-06-03 10:39:08.673 [job-0] INFO  HookInvoker - Invoke hook [DingTalkReportHook], path: /datax/hook/dingtalk
2021-06-03 10:39:09.415 [job-0] INFO  DingTalkUtil - Send DingTalk Message Result:ok-0
```

**通知效果：**

![image](https://github.com/mchange/datax-dingtalk-report/blob/main/doc/1.jpg)

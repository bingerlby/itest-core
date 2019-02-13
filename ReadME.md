###测试基础包

- #####com.lby.itest.http.HttpDriver

    - 基于Rest-Assured封装的http请求工具包，可通过配置文件配置http参数
    - 配置文件位置：src/main/resources/http/httpconf.properties
        - 支持参数：
            1. http.connection.timeout
            2. http.socket.timeout
            3. http.connection.manager.timeout
            4. proxy.host
            5. proxy.port
    - 支持方法：GET/POST
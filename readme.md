# APK增量包合成工具类
将增量包(xxx.patch) 和 旧版本APK文件 合并成新的 APK文件,从而达到升级

## 调用步骤
1. 服务端事先通过 bsdiff工具 生成差异包(old-to-new.patch)
2. 客户端获取本地应用的原始安装apk 
3. 调用工具类 jfeng-bspatch_v1.0.jar 中的 com.jfeng.bspatchlibrary.BsPatch的方法bspatch
   进行合成新的APK
4. 获取新的APK文件的MD5值和服务端返回的MD5进行比较是否相等,相等表示合成的新APK是本次升级后的安装文件
5. 执行安装新APK (在安装的时候注意适配Android N(7.0),有StrictMode API限制)(通过FileProvider解决)


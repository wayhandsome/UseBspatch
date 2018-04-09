# APK增量包合成工具类的使用Demo

## 工具类主要文件
 合成工具类主要包含两个文件: jfeng-bspatch_v1.0.jar, libbspatch.so

## 工具类作用
 将增量包(xxx.patch) 和 旧版本APK文件 合并成新的 APK文件

## 使用步骤
1. 服务端事先通过 bsdiff工具 生成差异包(old-to-new.patch)
2. 将 工具包 jfeng-bspatch_v1.0.jar 拷贝到 app/libs 目录下
3. 将 适配7种CPU类型的libbspatch.so 拷贝到 app/src/main/jniLibs 目录下
4. 客户端获取本地应用的原始安装apk 
5. 调用工具类 jfeng-bspatch_v1.0.jar 中的 com.jfeng.bspatchlibrary.BsPatch的方法bspatch
   进行合成新的APK
6. 获取新的APK文件的MD5值和服务端返回的MD5进行比较是否相等,相等表示合成的新APK是本次升级后的安装文件
7. 执行安装新APK (在安装的时候注意适配Android N(7.0),有StrictMode API限制)(通过FileProvider解决)

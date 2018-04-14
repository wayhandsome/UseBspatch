# APK增量包合成工具类的使用Demo

## 工具类主要文件
 合成工具类主要包含两个文件: jfeng-bspatch_v1.0.jar, libbspatch.so

## 工具类作用
 将增量包(xxx.patch) 和 旧版本APK文件 合并成新的 APK文件

## 使用步骤
1. 事先通过 bsdiff工具(差异包比较工具) 生成差异包(Eg:old-to-new.patch)
   
   或者使用我自己制作的差异包生成工具 [UseBsdiff.apk](https://github.com/wayhandsome/UseBsdiff/blob/master/UseBsdiff.apk)

   (其内部也是使用了 bsdiff工具提供的源码构建成so和jar,调用了bsdiff工具对外提供的方法)

2. 将 工具包 jfeng-bspatch_v1.0.jar 拷贝到 app/libs 目录下

3. 将 适配7种CPU类型的libbspatch.so 拷贝到 app/src/main/jniLibs 目录下

4. app/proguard-rules.pro 增加如下
   ```
   -dontwarn com.jfeng.bspatchlibrary.**
   -keep class com.jfeng.bspatchlibrary.** { *; }
   ```
5. 客户端获取本地应用的原始安装apk(详情见com.jfeng.usebspatch.MainActivity)

6. 调用工具类 jfeng-bspatch_v1.0.jar 中的 com.jfeng.bspatchlibrary.BsPatch的方法bspatch

   进行合成新的APK(详情见com.jfeng.usebspatch.MainActivity)

7. 获取新的APK文件的MD5值和服务端返回的MD5进行比较是否相等,相等表示合成的新APK是本次升级后的安装文件(详情见com.jfeng.usebspatch.MainActivity)

8. 执行安装新APK (在安装的时候注意适配Android N(7.0),有StrictMode API限制)(通过FileProvider解决)


# APK�������ϳɹ������ʹ��Demo

## ��������Ҫ�ļ�
 �ϳɹ�������Ҫ���������ļ�: jfeng-bspatch_v1.0.jar, libbspatch.so

## ����������
 ��������(xxx.patch) �� �ɰ汾APK�ļ� �ϲ����µ� APK�ļ�

## ʹ�ò���
1. ���������ͨ�� bsdiff���� ���ɲ����(old-to-new.patch)

2. �� ���߰� jfeng-bspatch_v1.0.jar ������ app/libs Ŀ¼��

3. �� ����7��CPU���͵�libbspatch.so ������ app/src/main/jniLibs Ŀ¼��

4. app/proguard-rules.pro ��������
   ```
   -dontwarn com.jfeng.bspatchlibrary.**
   -keep class com.jfeng.bspatchlibrary.** { *; }
   ```
5. �ͻ��˻�ȡ����Ӧ�õ�ԭʼ��װapk(�����com.jfeng.usebspatch.MainActivity)

6. ���ù����� jfeng-bspatch_v1.0.jar �е� com.jfeng.bspatchlibrary.BsPatch�ķ���bspatch

   ���кϳ��µ�APK(�����com.jfeng.usebspatch.MainActivity)

7. ��ȡ�µ�APK�ļ���MD5ֵ�ͷ���˷��ص�MD5���бȽ��Ƿ����,��ȱ�ʾ�ϳɵ���APK�Ǳ���������İ�װ�ļ�(�����com.jfeng.usebspatch.MainActivity)

8. ִ�а�װ��APK (�ڰ�װ��ʱ��ע������Android N(7.0),��StrictMode API����)(ͨ��FileProvider���)

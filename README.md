# android_opencv
opencv samples



# 引进模型，或者创建模型



# 脸部跟踪



# C++ 运行时库



编译排查错错可能用的到



https://developer.android.google.cn/ndk/guides/cpp-support?hl=en



### Selecting a C++ Runtime

#### CMake

The default for CMake is `c++_static`.

You can specify `c++_shared`, `c++static`, `none`, or `system` using the `ANDROID_STL` variable in your module-level `build.gradle` file. To learn more, see the documentation for [ANDROID_STL](https://developer.android.google.cn/ndk/guides/cmake#android_stl) in CMake.

#### ndk-build

The default for ndk-build is `none`.

You can specify `c++_shared`, `c++static`, `none`, or `system` using the `APP_STL` variable in your [Application.mk](https://developer.android.google.cn/ndk/guides/application_mk) file. For example:

```makefile
APP_STL := c++_shared
```

ndk-build only allows you to select one runtime for your app, and can only do in [Application.mk](https://developer.android.google.cn/ndk/guides/application_mk).
# MediaSelector

一个图片视频选择器

# 功能特点

* 支持按媒体资源类型展示
* 支持通过相机拍照获取图片
* 支持通过相机录像获取视频
* 支持设置录像最长时间

# 使用方法

## 1、导入依赖

       
        allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
       
        dependencies {
	        implementation 'com.github.dzs-yaodi:MediaSelector:1.0.1'
	}
       
        
## 2、方法调用
 
 （1）查询全部
  
                MediaSelector
                      .create(this)
                      .choose(MimeType.ofAll())
                      .showCamera(true)
                      .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                      .maxSelectable(9)
                      .addListToSelectPaths(list)
                      .imageEngine(new GlideEngine())
                      .start(REQUEST_MEDIA);
		      
（2）单独只查询图片

		MediaSelector
                      .create(this)
                      .choose(MimeType.ofImage())
                      .showCamera(true)
                      .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                      .maxSelectable(9)
                      .addListToSelectPaths(list)
                      .imageEngine(new GlideEngine())
                      .start(REQUEST_MEDIA);
		      
（3）单独只查询视频

		MediaSelector
                      .create(this)
                      .choose(MimeType.ofVideo())
                      .showCamera(true)
                      .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                      .maxSelectable(1)
                      .addListToSelectPaths(list)
                      .imageEngine(new GlideEngine())
                      .start(REQUEST_MEDIA);
	
（4）单独只查询视频 （视频可在当前页面播放和切换）
     
     在实际运行环境下，部分视频在 系统自带的播放器中无法播放，所以引入了一个三方框架
     
     使用该方法前需要引入gsyVideoPlayer
     
     implementation 'com.github.CarGuo.GSYVideoPlayer:gsyVideoPlayer-java:v7.1.5'
     implementation 'com.github.CarGuo.GSYVideoPlayer:GSYVideoPlayer-exo2:v7.1.5'
     
     选择器架包中使用的是compileOnly，所以需要使用该方法的， 必须自己再引用依赖
              
	      MediaSelector
                      .create(this)
                      .choose(MimeType.ofVideo())
                      .showCamera(true)
                      .captureStrategy(new CaptureStrategy(true,"com.xw.mediaselector.MyProvider"))
                      .maxSelectable(1)
                      .toCustomVideo(true)
                      .addListToSelectPaths(list)
                      .imageEngine(new GlideEngine())
                      .start(REQUEST_VIDEO);
                      
                      
   # 1.0.2 版本
   
   处理onActivityResult 过时的问题
   
   Router.getInstance()
          .choose(MimeType.ofAll())
          .showCamera(true)
          .captureStrategy(new CaptureStrategy(true,getPackageName() + ".MyProvider"))
          .maxSelectable(9 - list.size())
          .addListToSelectPaths(list)
          .imageEngine(new GlideEngine())
          .startLauncher(result -> {
              if (result.getData() != null) {
                
                                            
              }
          });
   
                      
                      
                      

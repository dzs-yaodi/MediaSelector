# MediaSelector

一个图片视频选择器

#功能特点

* 支持按媒体资源类型展示
* 支持通过相机拍照获取图片
* 支持通过相机录像获取视频
* 支持设置录像最长时间

# 使用方法

## 1、导入依赖

        ```
        allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
        ```
        ```
        dependencies {
	        implementation 'com.github.dzs-yaodi:MediaSelector:1.0.0'
	}
        ```
        
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
                      
                      
                      
                      
                      
                      
                      

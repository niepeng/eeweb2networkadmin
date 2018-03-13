// 浏览器限制

(function () {
    var dom = [];  
    //用于判定页面是否加载完毕  
    dom.isReady  = false;  
    //用于添加要执行的函数  
    dom.ready = function(fn){  
      dom.initReady();  
      if ( dom.isReady ) {  
        fn()  
      } else {  
        dom.push( fn );  
      }  
    }  
    //开始初始化domReady函数，判定页面的加载情况  
    dom.initReady = function() {  
      if (arguments.callee.used ) {  
        return;  
      }  
      arguments.callee.used = true;  
      //用于window.onload的内部，即window.onload = function(){ dom.ready(fn) }  
      //这时页面肯定加载完毕，立即执行所有函数  
      if ( document.readyState === "complete" ) {  
        return dom.fireReady();  
      }  
      if (document.attachEvent) {//IE5 IE6 IE7 IE8 IE9 midnight  
        //当页面包含图片时，onreadystatechange事件会触发在window.onload之后，  
        //换言之，它只能正确地执行于页面不包含二进制资源或非常少或者被缓存时  
        document.attachEvent("onreadystatechange", function(e) {  
          if ( document.readyState == "complete" ) {  
            document.detachEvent("onreadystatechange", arguments.callee );  
            dom.fireReady();  
          }  
        });  
        //doScroll方法通常只会正确执行于一个全新的页面  
        (function(){  
          if ( dom.isReady ) {  
            return;  
          }  
          //doScroll存在于所有标签而不管其是否支持滚动条  
          //这里如果用document.documentElement.doScroll()，我们需要判定其是否位于顶层document  
          var node = new Image  
          try {  
            node.doScroll();  
            node = null//防止IE内存泄漏  
          } catch( e ) {  
            //javascrpt最短时钟间隔为16ms，这里取其倍数  
            //http://blog.csdn.net/aimingoo/archive/2006/12/21/1451556.aspx  
            setTimeout( arguments.callee, 64 );  
            return;  
          }  
          dom.fireReady();  
        });  
      } else {//Safari3.1+,Chrome，Firefox2+ ，Opera9+，注意DOMContentLoaded是非标准事件  
        //https://developer.mozilla.org/en/Gecko-Specific_DOM_Events  
        document.addEventListener( "DOMContentLoaded", function() {  
          document.removeEventListener( "DOMContentLoaded",  arguments.callee , false );  
          dom.fireReady();  
        }, false );  
      }  
    };  
    //执行所有在window.onload之前放入的函数  
    dom.fireReady = function() {  
      if ( !dom.isReady ) {  
        if ( !document.body ) {  
          return setTimeout( dom.fireReady, 16 );  
        }  
        dom.isReady = true;  
        if ( dom.length ) {  
          for(var i=0, fn;fn = dom[i];i++)  
            fn()  
        }  
      }  
    }  
    
    dom.ready(function () {
    	var ua = navigator.userAgent;
    	var chromeIndex = ua.indexOf("Chrome");
    	var safariIndex = ua.indexOf("Safari");
    	var isChrome = false;
    	// 谷歌浏览器
    	if (chromeIndex > 0 && safariIndex > chromeIndex) {
    		var v = ua.substring(chromeIndex+7, safariIndex);
    		v = parseInt(v);
    		if (!isNaN(v) && v >= 38) {
    			isChrome = true;
    		}
    	}
    	
    	if (!isChrome) {
    		//var $body = $(document.body);
    		//$body.append('<div style="position:fixed;_position:absolute;top:0;left:0;z-index:2;width:100%;height:100%;background:#000;opacity:0.65"></div>');
    		//$body.append('<div style="position:relative;z-index:3;margin:100px auto;padding:30px 0;width:500px;border:5px solid orange;border-radius:10px;text-align:center;font-size:24px;color:#4f96f0;background:#fffae3;box-shadow:0 10px 10px #000"><img src="../css/images/cat-error-2.png" alt="猫"><div style="height:20px;"></div>抱歉！只支持最新版Chrome浏览器</div>');
    		
    		var mask = document.createElement("div");
    		mask.style.cssText = 'position:fixed;_position:absolute;top:0;left:0;z-index:2;width:100%;height:100%;background:#000;opacity:0.65';

    		var win = document.createElement("div");
    		win.style.cssText = 'position:relative;z-index:3;margin:100px auto;padding:30px 0;width:500px;border:5px solid orange;border-radius:10px;text-align:center;font-size:24px;color:#4f96f0;background:#fffae3;box-shadow:0 10px 10px #000';
    		win.innerHTML = '<img src="../css/images/cat-error-2.png" alt="猫"><div style="height:20px;"></div>抱歉！只支持最新版Chrome浏览器';
    		
    		document.body.appendChild(mask);
    		document.body.appendChild(win);
    	}
    });
	
})();

/**
 * 倒计时组件
 */

$.fn.extend({
	countdown: function(config) {
		var defConfig = {
			time: 60,	// 开始倒计时间(秒)
			inCountdownMsg: "{0}秒后重试",	// 正在倒计时显示的文字
			countDownOverMsg: false,	// 倒计时完成显示的文字，没有则显示倒计时之前的值
			beforeCountdown: false, // 开始倒计时要执行的函数
			countdownOver: false,	// 倒计时结束要执行的函数
			eventType: "click"
		};
		config = $.extend(defConfig, config);
		
		var element = this.get(0);

		var tagName = element.tagName.toLowerCase();
		
		config.defMsg = (tagName == "input" || tagName == "textarea" ? this.val() : this.text() );
		
		// 检测参数


		this.data("cdConfig", config);

		if( config.eventType ) {
			this.on(config.eventType, function() {
				$(this).startCountdown();
			});
		}
		return this;
	},
	startCountdown: function() {
		var config = this.data("cdConfig");

		if( !config || this.data("cdDoing") ) {
			return this;
		}
		this.data("cdDoing", true);
		var $this = this;

		if( $.isFunction( config.beforeCountdown ) && config.beforeCountdown( this, config ) === false ) {
			this.data("cdDoing", false)
			return this;
		}

		var time = config.time;

		var tagName = this.get(0).tagName.toLowerCase();
		var changeFunc = ( tagName == "input" || tagName == "textarea" ? "val" : "text" );

		var countdown = function(){
			if(time > 0) {
				$this[changeFunc]( config.inCountdownMsg.replace("{0}", time--) );
				timer = setTimeout(countdown, 1000);
			}
			else {
				clearTimeout(timer);
				$this.data("cdDoing", false);
				if( $.isFunction( config.countdownOver ) && config.countdownOver( this, config ) === false ) {
					return this;
				}
				$this[changeFunc]( config.countDownOverMsg || config.defMsg );
			}
		};
		countdown();
		return this;
	}
});

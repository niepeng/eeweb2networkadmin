/*
 * 基于 artDialog 进一步包装
 */

define(function(require) {
	var $ = require("jquery");
	var artDialog = require("art-dialog/dialog");
	
	return {
		alert: function(config) {
			var defConfig = {
				title: "消息",
				fixed: true,
				ok: function(){}
			};
			if( typeof config === "string" || typeof config === "number" ) {
				defConfig.content = config;
			}
			config = $.extend( defConfig, config );
			config.content = '<div class="alert-content">'+ config.content +'</div>';
			artDialog( config ).showModal();
		},
		show: function(config) {
			var defConfig = {
				title: "消息",
				fixed: true,
				ok: function(){}
			};
			if( typeof config === "string" || typeof config === "number" ) {
				defConfig.content = config;
			}
			config = $.extend( defConfig, config );
			config.content = '<div class="alert-content">'+ config.content +'</div>';
			artDialog( config ).show();
		}
	};
});

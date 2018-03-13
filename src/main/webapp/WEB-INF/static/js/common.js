$(function () {
	// 页面栏目定位
	var pathname = self.location.pathname;
	var search = self.location.search.substr(1);
	
	function isMatch(url) {
		var rs = (/(\/[^\?]+)(?:\?([^#]+))?/g).exec(url);
		if (!rs) {
			return false;
		}
		
		var urlPathname = rs[1];
		var urlSearch = rs[2];
		
		var matched = urlPathname == pathname;

		// 如果有查询参数 还需要检查查询参数是否匹配
		if (urlSearch && matched) {
			var queriesMatched = true;
			var urlQueries = urlSearch.split("&");
			for (var i=0, l=urlQueries.length; i<l; i++) {
				if (search.indexOf(urlQueries[i]) < 0) {
					queriesMatched = false;
					break;
				}
			}
			return queriesMatched;
		}
		return matched;
	}

	// 将项目列表放到一个临时数组里面，方便进行排序
	var menuItems = [];
	$("#primary_nav .submenu a").each(function(index) {
		menuItems.push($(this));
	});
	
	
	// 排序
	menuItems.sort(function ($a, $b) {
		var urlA = $a.data("url");
		var urlB = $b.data("url");
		
		// 先检查是否有查询参数
		var searchA = urlA.indexOf("?");
		var searchB = urlB.indexOf("?");
		// 都有查询参数
		if (searchA > -1 && searchB > -1) {
			// 检查谁的参数更多
			return urlA.split("&").length < urlB.split("&").length;
		}
		// 都没有查询参数
		else if (searchA < 0 && searchB < 0) {
			// 谁更长
			return urlA.length < urlB.length;
		}
		
		// 谁更长
		return searchA < searchB;
	});
	
	$.each(menuItems, function (i, $item) {
		var thisURL = $item.attr("data-url");
		if (isMatch(thisURL)) {
			var $parent = $item.parent();
			$parent.addClass("active");
			$parent.parents("li").addClass("active open");
			return false;
		}
	});
})





function checkNull(obj) {
  if(!obj.val()) {
    obj.addClass("red");
    return false;
  }
  obj.removeClass("red");
  return true;
}

function checkIsChecked(obj) {
    if(!obj.val()) {
        obj.addClass("red");
        return false;
    }
    obj.removeClass("red");
    return true;
}

function checkFormatFloat(obj) {
  var data = obj.val();
  if(data > 0) {
    obj.removeClass("red");
    return true;
  }
  alert("格式错误");
  obj.addClass("red");
  return false;
}


function checkFormatFloatAnd0(obj) {
  var data = obj.val();
  if(data >= 0) {
    obj.removeClass("red");
    return true;
  }
  alert("格式错误");
  obj.addClass("red");
  return false;
}



function checkFormatInt(obj) {
  var data = obj.val();
  if(data % 1 == 0) {
    obj.removeClass("red");
    return true;
  }
  alert("格式错误");
  obj.addClass("red");
  return false;
}

function checkFormatDate(obj) {
  var data = obj.val();
  var flag = validateDate(data);
  if(flag) {
    obj.removeClass("red");
    return true;
  }
  alert("格式错误");
  obj.addClass("red");
  return false;
}

function checkFormatDateTime(obj) {
  var data = obj.val();
  var flag = validateDateTime(data);
  if(flag) {
    obj.removeClass("red");
    return true;
  }
  alert("格式错误");
  obj.addClass("red");
  return false;
}

function checkFormatDateTimeNoSecond(obj) {
  var data = obj.val();
  var flag = validateDateTimeNoSecond(data);
  if(flag) {
    obj.removeClass("red");
    return true;
  }
  alert("格式错误");
  obj.addClass("red");
  return false;
}

/**
 * yyyy-MM-dd HH:mm:ss
 * @param testdate
 * @returns {boolean}
 */
function validateDateTimeNoSecond(testdate) {
  var date_regex = /((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]/;
  var res = date_regex.test(testdate);
  if(res) {
    var ymd = testdate.match(/(\d{4})-(\d+)-(\d+).*/);
    var year = parseInt(ymd[1]);
    var month = parseInt(ymd[2]);
    var day = parseInt(ymd[3]);
    if(day > 28) {
      //获取当月的最后一天
      var lastDay = new Date(year, month, 0).getDate();
      return (lastDay >= day);
    }
    return true;
  }
  return false;
}

/**
 * yyyy-MM-dd HH:mm:ss
 * @param testdate
 * @returns {boolean}
 */
function validateDateTime(testdate) {
  var date_regex = /((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01]) ([01]?[0-9]|2[0-3]):[0-5][0-9]:[0-5][0-9]/;
  var res = date_regex.test(testdate);
  if(res) {
    var ymd = testdate.match(/(\d{4})-(\d+)-(\d+).*/);
    var year = parseInt(ymd[1]);
    var month = parseInt(ymd[2]);
    var day = parseInt(ymd[3]);
    if(day > 28) {
      //获取当月的最后一天
      var lastDay = new Date(year, month, 0).getDate();
      return (lastDay >= day);
    }
    return true;
  }
  return false;
}

/**
 * yyyy-MM-dd
 * @param testdate
 * @returns {boolean}
 */
function validateDate(testdate) {
  var date_regex = /((19|20)[0-9]{2})-(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])/;
  var res = date_regex.test(testdate);
  if(res) {
    var ymd = testdate.match(/(\d{4})-(\d+)-(\d+).*/);
    var year = parseInt(ymd[1]);
    var month = parseInt(ymd[2]);
    var day = parseInt(ymd[3]);
    if(day > 28) {
      //获取当月的最后一天
      var lastDay = new Date(year, month, 0).getDate();
      return (lastDay >= day);
    }
    return true;
  }
  return false;
}

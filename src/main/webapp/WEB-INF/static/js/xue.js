
function changeDeviceType(obj) {
  if(!checkDeviceType()) {
    obj.checked = false;
    return;
  }
  refushShow();
  contrlOut(document.getElementById("deviceType_256").checked);
}

function refushShow() {
  contrlTemp(document.getElementById("deviceType_1").checked);
  contrlHumi(document.getElementById("deviceType_2").checked);
  contrlShine(document.getElementById("deviceType_8").checked);
  contrlPressure(document.getElementById("deviceType_16").checked);
  contrlIn(document.getElementById("deviceType_32").checked || document.getElementById("deviceType_64").checked || document.getElementById("deviceType_128").checked);
}


function checkDeviceType() {
  var deviceTypesObject = document.getElementsByName("deviceTypes");

  // 如果开关量输出被选中,那么其他的类型不能选择了
  var isOutChecked = document.getElementById("deviceType_256").checked;
  if (isOutChecked) {
    for (k in deviceTypesObject) {
      if (deviceTypesObject[k].checked && deviceTypesObject[k].value != 256) {
        alert("设备类型如果选中了开关量输出,那么不能选中其他类型,请调整");
        return false;
      }
    }
  }

  // 开关量输入只能选择一个
  var inNum = (document.getElementById("deviceType_32").checked) ? 1 : 0;
  inNum += ((document.getElementById("deviceType_64").checked) ? 1 : 0);
  inNum += ((document.getElementById("deviceType_128").checked) ? 1 : 0);
  if(inNum > 1) {
    alert("开关量输入设备只能选择一个");
    return false;
  }
  return true;
}

function doSubmitDevice() {
  if(document.getElementById("sn").value == "") {
    alert("sn号不能为空");
    return false;
  }
  if(document.getElementById("name").value == "") {
    alert("名称不能为空");
    return false;
  }
  if(document.getElementById("tag").value == "") {
    alert("标签不能为空");
    return false;
  }

  var address = document.getElementById("address").value;
  if(address == "" || address % 1 != 0) {
    alert("设备地址不能为空并且是整数");
    return false;
  }
  return true;

}

function contrlTemp(isShow) {
  if(isShow) {
    document.getElementById("tempDiv").style.display = "block";
  } else {
    document.getElementById("tempDiv").style.display = "none";
  }
}

function contrlHumi(isShow) {
  if(isShow) {
    document.getElementById("humiDiv").style.display = "block";
  } else {
    document.getElementById("humiDiv").style.display = "none";
  }
}

function contrlShine(isShow) {
  if(isShow) {
    document.getElementById("shineDiv").style.display = "block";
  } else {
    document.getElementById("shineDiv").style.display = "none";
  }
}

function contrlPressure(isShow) {
  if(isShow) {
    document.getElementById("pressureDiv").style.display = "block";
  } else {
    document.getElementById("pressureDiv").style.display = "none";
  }
}

function contrlIn(isShow) {
  if(isShow) {
    document.getElementById("inDiv").style.display = "block";
  } else {
    document.getElementById("inDiv").style.display = "none";
  }
}

function contrlOut(isShow) {
  if(isShow) {
    document.getElementById("outDiv").style.display = "block";
  } else {
    document.getElementById("outDiv").style.display = "none";
  }
  refushShow();
}



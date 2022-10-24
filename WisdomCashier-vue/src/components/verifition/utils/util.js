export function resetSize(vm) {
  var img_width, img_height, bar_width, bar_height; //图片的宽度、高度，移动条的宽度、高度

  var parentWidth = vm.$el.parentNode.offsetWidth || window.offsetWidth;
  var parentHeight = vm.$el.parentNode.offsetHeight || window.offsetHeight;
  if (vm.imgSize.width.indexOf("%") != -1) {
    img_width = (parseInt(vm.imgSize.width) / 100) * parentWidth + "px";
  } else {
    img_width = vm.imgSize.width;
  }

  if (vm.imgSize.height.indexOf("%") != -1) {
    img_height = (parseInt(vm.imgSize.height) / 100) * parentHeight + "px";
  } else {
    img_height = vm.imgSize.height;
  }

  if (vm.barSize.width.indexOf("%") != -1) {
    bar_width = (parseInt(vm.barSize.width) / 100) * parentWidth + "px";
  } else {
    bar_width = vm.barSize.width;
  }

  if (vm.barSize.height.indexOf("%") != -1) {
    bar_height = (parseInt(vm.barSize.height) / 100) * parentHeight + "px";
  } else {
    bar_height = vm.barSize.height;
  }

  return {
    imgWidth: img_width,
    imgHeight: img_height,
    barWidth: bar_width,
    barHeight: bar_height,
  };
}

/* att_zone : 첨부된 이미지들이 표시될 영역을 나타내는 id, 
     btn : file tag의 id값 */
(imageView = function imageView(att_zone, btn) {
  var attZone = document.getElementById(att_zone);
  var btnAtt = document.getElementById(btn);
  var sel_files = [];

  // 이미지와 체크 박스를 감싸고 있는 div 속성
  var div_style =
    "display:inline-block; position:relative;overflow: hidden;" +
    "width:250px;height:180px;margin:5px;z-index:1";
  // 미리보기 이미지 속성
  var img_style = "width:100%; height:100%; z-index:none; object-fit:contain;";
  // 이미지안에 표시되는 체크박스의 속성
  var chk_style =
    "width:25px;height:25px;position:absolute;font-size:20px;border:none;border-radius: 50%;" +
    "right:8px;top:8px;z-index:999;background-color:rgba(72,72,72,0.7);color: #fff";

  btnAtt.onchange = function (e) {
    var files = e.target.files;
    var fileArr = Array.prototype.slice.call(files);
    for (f of fileArr) {
      imageLoader(f);
    }
  };

  // 탐색기에서 드래그앤 드롭 사용
  attZone.addEventListener(
    "dragenter",
    function (e) {
      e.preventDefault();
      e.stopPropagation();
    },
    false
  );

  attZone.addEventListener(
    "dragover",
    function (e) {
      e.preventDefault();
      e.stopPropagation();
    },
    false
  );

  attZone.addEventListener(
    "drop",
    function (e) {
      var files = {};
      e.preventDefault();
      e.stopPropagation();
      var dt = e.dataTransfer;
      files = dt.files;
      for (f of files) {
        imageLoader(f);
      }
    },
    false
  );

  /*첨부된 이미리즐을 배열에 넣고 미리보기 */
  imageLoader = function (file) {
    sel_files.push(file);
    var reader = new FileReader();
    reader.onload = function (ee) {
      let img = document.createElement("img");
      img.setAttribute("style", img_style);
      img.src = ee.target.result;
      attZone.appendChild(makeDiv(img, file));
    };

    reader.readAsDataURL(file);
  };

  /*첨부된 파일이 있는 경우 checkbox와 함께 attZone에 추가할 div를 만들어 반환 */
  makeDiv = function (img, file) {
    var div = document.createElement("div");
    div.setAttribute("style", div_style);

    var btn = document.createElement("input");
    btn.setAttribute("type", "button");
    btn.setAttribute("value", "x");
    btn.setAttribute("delFile", file.name);
    btn.setAttribute("style", chk_style);
    btn.onclick = function (ev) {
      var ele = ev.srcElement;
      var delFile = ele.getAttribute("delFile");
      for (var i = 0; i < sel_files.length; i++) {
        if (delFile == sel_files[i].name) {
          sel_files.splice(i, 1);
        }
      }

      dt = new DataTransfer();
      for (f in sel_files) {
        var file = sel_files[f];
        dt.items.add(file);
      }
      btnAtt.files = dt.files;
      var p = ele.parentNode;
      attZone.removeChild(p);
    };
    div.appendChild(img);
    div.appendChild(btn);
    return div;
  };
})("att_zone", "btnAtt");

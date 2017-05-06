// create input modal
let manName="";
let tdName="";

$(document).ready(function(){
    $('[data-toggle="tooltip"]').tooltip();
    $('[data-toggle="popover"]').popover();
    loadData();
    testHtml= $("#testData").html();
    });


function Calculate(){
  let res = getAllValues();
  if(res.isEmpty){
    alert('you must fill all values');
    return;
  }

  let data = res.data;
  let totals = {
    'Right_pp' : 0,
    'Right_lt' : 0,
    'Left_lt' : 0,
    'Right_pp' : 0
  };

  for(let key in data){
      if(key.indexOf('Right_pp') > -1)
        totals.Right_pp += parseInt(data[key]);

      else if(key.indexOf('Right_lt') > -1)
        totals.Right_lt += parseInt(data[key]);

      else if(key.indexOf('Left_lt') > -1)
        totals.Left_lt += parseInt(data[key]);
      else if(key.indexOf('Left_pp' > -1))
        totals.Left_pp += parseInt(data[key]);
  }
    $('#Right_lt_total').attr('value',totals.Right_lt);
    $('#Right_pp_total').attr('value',totals.Right_pp);
    $('#Left_lt_total').attr('value',totals.Left_lt);
    $('#Left_pp_total').attr('value',totals.Left_pp);

    let musData = res.musData;
    let musTotals = {
      'L' : 0,
      'R' : 0
    }

    for(let key in musData){
      if(key.indexOf('L') > -1)
        musTotals.L += parseInt(musData[key]);
      else if(key.indexOf('R') > -1)
        musTotals.R += parseInt(musData[key]);
    }

    $('#mus_total_Left').attr('value',musTotals.R)
    $('#mus_total_Right').attr('value',musTotals.L)






    $('#sensory_L').attr('value','C1')
    $('#sensory_R').attr('value','C2')


    $('#sensory2_L').attr('value','C2')
    $('#sensory2_R').attr('value','C3')

    $('#motor_L').attr('value','C1')
    $('#motor_R').attr('value','C2')


    $('#motor2_L').attr('value','C2')
    $('#motor2_R').attr('value','C3')

    $('#complete').attr('value','I');
    $('#scale').attr('value','C');

}

function getAllValues(){
  let data = {};
  let isEmpty;

  $('#data-table > tbody  > tr > td > input').each(function(){
    let id = $(this).attr('id')
      let val = $(this).val();
      let rOnly = $(this).attr('readonly');

      if((!val || val == '') && !rOnly){
        isEmpty = true;
      }else if(!rOnly)
      data[id] = val;  })

    let musData = {};


    $('#mus > tbody  > tr > td > input').each(function(){
      let id = $(this).attr('id')
        let val = $(this).val();
        let rOnly = $(this).attr('readonly');

        if((!val || val == '') && !rOnly){
          isEmpty = true;
        }else if(!rOnly)
        musData[id] = val;
    })

  return {isEmpty : isEmpty, data : data, musData : musData};
}
function loadData()
{
  $('#data-table input:text').change( function (){
      let text = $(this).val();
      let id = $(this).attr('id');

      if(text.toLowerCase() == 'nt')
        return;

      let max = id.indexOf('m') > -1 ? '5' : '3';

      if(text.charAt(0) < max && text.charAt(0) >= '0'){
        $(this).val(text.charAt(0));
        let pathId = id.substring(0, id.indexOf('_', id.indexOf('_')+1));

        let text2;
        if(id.charAt(id.length-1) == 'p')
          text2 = pathId + '_lt';
        else {
          text2 = pathId + '_pp'
        }
        setDermatomValAndColor(pathId, id, parseInt(text.charAt(0)), text2);

      } else{
        $(this).val('')
        alert('In correct value !! \n' + text)
      }

      });



      $('path').click( function() {
              let dermatomId = $(this).attr('data-name');
              showFreePop(dermatomId, setDermatorTestResult);
      });

  $('label').click( function() {
      value=$(this).attr("value");
      });






}



      $('#mus input:text').change( function (){
          let text = $(this).val();
          let id = $(this).attr('id');

          if(text.toLowerCase() == 'nt')
            return;

          let max = '5'

          if(text.charAt(0) <= max && text.charAt(0) >= '0'){
            $(this).val(text.charAt(0));
          }else {
            $(this).val('');

          }

    })

function deleteCase(caseId,link){
 $.ajax(
    {
        url :link,
        type: "POST",
        success:function(json, textStatus, jqXHR)
        {
         $("#"+caseId).remove();
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            show('Something Went Wrong .. In Deleting Case');
        }
    });

}


function editTest(link,editLink)
{

        let testHtml= $("#testData").html();
$.ajax(
    {
        url :link,
        type: "POST",
        success:function(json, textStatus, jqXHR)
        {
        $("#testData").html($.parseHTML(json.testData));
        $("#testName").val(json.testName);
        $("#testInfo").val(json.testInfo);
        $("#saveTest").text("Edit And Calculate Test");

        $("#testForm").attr("action",editLink);
        $( "#s2" ).click();
        loadData();
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //if fails
            show('Something Went Wrong .. In Editing Test');
        }
    });
}

function updateHuman()
{
        // $("#testData").html(testHtml);
        loadData();
}


function deleteTest(testId,link){
 $.ajax(
    {
        url :link,
        type: "POST",
        success:function(json, textStatus, jqXHR)
        {
         $('div[id=\"'+testId+'\"]').remove();
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //if fails
            show('Something Went Wrong .. In Deleting Test');
        }
    });

}



$("#testForm").submit(function(e){
        Calculate();
        $("#testContent").attr("value",$("#testData").html());
});





$("#caseForm").submit(function(e){
    let postData = $(this).serializeArray();
    let formURL = $(this).attr("action");
    let userName =document.getElementById('userName').innerHTML;
    $.ajax(
    {
        url : "/users/"+userName+"/createCase",
        type: "POST",
        crossDomain: true,
        data : postData,
        success:function(json, textStatus, jqXHR)
        {
            getCases(userName);
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            //if fails
            show('Something went wrong');
        }
    });
    e.preventDefault(); //STOP default action

  $("#newCase").modal("hide");
});





    function setval(mName,tName)
    {
        manName=mName;
        tdName=tName;

    }


    function setDermatorTestResult(dermatomId, value, autoFill, testType){
      let textInputId = dermatomId;
      let textInputId2 = dermatomId; // i need the two test boxes [lt, pp] to minimize the value

      // check NT value
      if(testType > 2 && value == 3)
      value = 'NT';
      else if(value == 6)
      value = 'NT';


      // catch box input
      if(testType == 0){
        textInputId += '_lt';
        textInputId2 += '_pp'
      }
      else if(testType == 1){
        textInputId += '_pp'
        textInputId2 += '_lt'
      } else{
            let textElementId  = dermatomId + '_m'
            let textElement = document.getElementById(textElementId);
            textElement.value = value
            return;
      }




      setDermatomValAndColor(dermatomId, textInputId, value, textInputId2);

      if(autoFill){

      }

    }

    function setDermatomValAndColor(pathDataName, textDermatomId, val, textDermatomId2){
      let boxColor;
      let pathColor;

      let textElement = document.getElementById(textDermatomId);
      let textElement2 = document.getElementById(textDermatomId2);

      let v1 = val;
      let v2;
      if(textElement2)
      v2 = textElement2.value;
      let minVal = v1;

      if(v1 && v2)
          minVal = Math.min(parseInt(v1), parseInt(v2))


      switch (val) {
        case 0: pathColor = 'rgb(255, 51, 51)'
                boxColor = 'rgba(255, 51, 51, 0.5)'
          break;

        case 1: pathColor = 'rgb(255, 255, 51)'
                boxColor = 'rgba(255, 255, 51, 0.5)'
          break;

        case 2: pathColor = 'rgb(153, 255, 51)'
                boxColor = 'rgba(153, 255, 51, 0.5)'
          break;

        default : pathColor = 'rgb(184, 184, 148)';
                 boxColor = 'rgba(184, 184, 148, 0.5)'
      }

      if(v1&&v2)
      switch (minVal) {
        case 0: pathColor = 'rgb(255, 51, 51)'
          break;

        case 1: pathColor = 'rgb(255, 255, 51)'
          break;

        case 2: pathColor = 'rgb(153, 255, 51)'
        break;

        default : pathColor = 'rgb(184, 184, 148)';
      }


      $("path[data-name='"+ pathDataName +"']" ).attr("fill", pathColor);


      $("#"+textDermatomId).attr("value",val);


      textElement.value = val;
      textElement.style.backgroundColor = boxColor;

    }

function getValue(value, noAutoFill, testType)
{
        index = $('#'+tdName).parent().parent().index();

        /// test type is >> 0 light touch, 1 >> pinprik >> 2 musles


        let man ='xxx';

        $('#data-table > tbody  > tr').each(function(i) {
          if(i == 1 && !noAutoFill)
            return;

        if(i>=index){
         if(tdName.includes("Right") && testType == 0){
                     td=$(this).find("input:eq(0)");
                     man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-3);
         }
         if(tdName.includes("Left") && testType == 0){
                     td=$(this).find("input:eq(1)");
                     man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-3);
         }
         if(tdName.includes("Right") && testType == 1){
                     td=$(this).find("input:eq(2)");
                     man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-3);
         }
         if(tdName.includes("Left") && testType == 1){
                  td=$(this).find("input:eq(3)");
                  man=td.attr('id').substring(0,($(this).find('input').attr('id')).length-4);
         }

    if(value==0){
        $( "path[data-name='"+man+"']" ).attr("fill","rgb(255, 51, 51)");
        td.attr("value",value);
        td.css("background","rgba(204, 51, 0,.5)");
    }
    if(value==1){
        $( "path[data-name='"+man+"']" ).attr("fill","rgb(255, 255, 51)");
        td.css("background","rgba(255, 204, 0,.5)");
        td.attr("value",value);
        }
        if(value==2){
            $( "path[data-name='"+man+"']" ).attr("fill","rgb(153, 255, 51)");
            td.css("background","rgba(0, 153, 0,.5)");
            td.attr("value",value);
                    }
            if(value==3){
                $( "path[data-name='"+man+"']" ).attr("fill","rgb(184, 184, 148)");
                td.css("background","rgba(138, 138, 92,.5)");
                td.attr("value",value);
                                       }

  if( $('#downValues').is(':checked') ==false)
        return false;

                                                }

  if(noAutoFill)
    return;

});

}


function setTdVal(){

}

function saveData()
{

let svgElements= $('.row').find('svg');

//replace all svgs with a temp canvas
svgElements.each(function () {
    let canvas, xml;

    canvas = document.createElement("canvas");
    canvas.className = "screenShotTempCanvas";
    //convert SVG into a XML string
    xml = (new XMLSerializer()).serializeToString(this);


canvas.width  = $(this).width();
canvas.height = $(this).height();
    //draw the SVG onto a canvas
    canvg(canvas, xml);
    $(canvas).insertAfter(this);
    //hide the SVG element
    this.className = "tempHide";
    $(this).hide();


 html2canvas($('.col-md-9'), {
           background :'#F8FBFB',
           onrendered: function (canvas) {
            let img = canvas.toDataURL("image/png");
                 let link = document.createElement("a");
                link.download = "test.png";
                link.href = img;
                link.click();

                let doc = new jsPDF("p", "mm", "a4");
                    let width = doc.internal.pageSize.width;
                    let height = doc.internal.pageSize.height;
                    doc.addImage(img, 'PNG',0,0,width, height);
                    doc.saveData('test');


let blob = dataURItoBlob(img);
let fd = new FormData();
fd.append("file", blob);

 $.ajax({
                type: "POST",
                url: "/showTest",
                data: fd,
                contentType: false,
                processData: false,
                cache: false,
                /*beforeSend: function(xhr, settings) {
                    xhr.setRequestHeader("Content-Type", "multipart/form-data;boundary=gc0p4Jq0M2Yt08jU534c0p");
                    settings.data = {name: "file", file: inputElement.files[0]};
                },*/
                success: function (result) {
                    if ( result.reseponseInfo == "SUCCESS" ) {
                        show('mbrook');
                    } else {

                    }
                },
                error: function (result) {
                    console.log(result.responseText);
                }
            });



            }

         });

  $(canvas).remove();
  $(this).show();

});

}



function dataURItoBlob(dataURI) {
    // convert base64/URLEncoded data component to raw binary data held in a string
    let byteString;
    if (dataURI.split(',')[0].indexOf('base64') >= 0)
        byteString = atob(dataURI.split(',')[1]);
    else
        byteString = unescape(dataURI.split(',')[1]);

    // separate out the mime component
    let mimeString = dataURI.split(',')[0].split(':')[1].split(';')[0];

    // write the bytes of the string to a typed array
    let ia = new Uint8Array(byteString.length);
    for (let i = 0; i < byteString.length; i++) {
        ia[i] = byteString.charCodeAt(i);
    }

    return new Blob([ia], {type:mimeString});
}



function show(message)
{

        $("#modal").remove();
        modal="<div class=\"modal fade\" id=\"modal\" role=\"dialog\">"+
            "        <div class= \"modal-dialog modal-md\">"+
            "            <div class=\"modal-content\">"+
            "                <div class=\"modal-header\">"+
            "                    <button type=\"button\" class=\"close\" data-dismiss=\"modal\">&times;</button>"+
            "                    <h4 class=\"modal-title\">Info</h4>"+
            "                </div>"+
            "                <div class=\"modal-body\">"+
            "                    <h5 class=\"text-danger\">"+message+
            "                    </h5><div class=\"modal-footer\">"+
            "                        <button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\">Close</button>"+
            "                    </div>"+
            "                </div>"+
            "            </div>"+
            "        </div>"+
            "    </div>"
        $("body").append(modal);
        $("#modal").modal();
}


function sendTo(link)
{

 $.ajax(
    {
        url : link,
        type: "POST",
        crossDomain: true,
        beforeSend: function() {
            show("Sending...");
        },
        success:function(json, textStatus, jqXHR)
        {
            show("Email Has Been Sent Successfully");
        },
        error: function(jqXHR, textStatus, errorThrown)
        {
            show('Something went wrong at sending Email .. please try again later :)');
        }
    });
}





















//  ------------------------------- freePop ----------------------------------





const rect_w = 30;
const rect_h = 30;
let x, y;
let canvas;
let state = 0; // 0 = no buttons, 1 = type of input, 2 = light touch, 3 = pin prick, 4 = museles strength
let rectangles = [];
let darkColors = [];
let lightColors = [];
let musLightColor;
let musDarkColor;
let headerLightColor;
let headerDarkColor;
let typeLightCol;
let typeDarkcolor;
let clickedButton;
let amt = 0;
let rect_size_F = 0.1;
let rectRound = 0;

let headerText;
let buttonTypeText = ['LT', 'PP', 'MS'];

function setup(){
  canvas = createCanvas(720, 400);
  canvas.parent('Harthain')
  // canvas.class('hid')
  frameRate(20);
  textSize(19);
  textFont('Calibri')
  noStroke();

  darkColors =  [color(204, 51, 0), color(255, 204, 0), color(0, 153, 0), color(138, 138, 92)];

  lightColors = [color(255, 83, 26), color(255, 255, 51), color(0, 230, 0), color(184, 184, 148)]

  musLightColor = color(77, 148, 255);
  musDarkColor = color(0, 102, 255)

  headerDarkColor = darkColors[3];
  headerLightColor = lightColors[3];

  typeLightCol = musLightColor;
  typeDarkcolor = musDarkColor;

  rectRound = 20;
  headerText = 'C2_L'

}





function draw(){
  if(!state)
    return;

    background(0)
    update();

  if(rect_size_F + 0.25 < 1)
    rect_size_F += 0.25;

  else
    rect_size_F = 1;

  if(amt < 1)
    amt += 0.1;


  // draw header
  if(state != 1){
    fill(color(184, 184, 148));
    rect(rectangles[0].x + 10, rectangles[0].y - 21, 100, 20, 20)
    fill(255);
    text(headerText, rectangles[0].x + 25, rectangles[0].y - 5);
  }

  for (var i = 0; i < rectangles.length; i++) {
    let rectX = rectangles[i].x;
    let rectY = rectangles[i].y;
    let c = rectangles[i].c;
    let t = rectangles[i].t;

    fill(c);
    rect(rectX, rectY, rect_w*rect_size_F, rect_h*rect_size_F, 5);

    fill(255);
    let f = t.length == 1 ? 3 : 6;
    text(t, rectX + (rect_w/f), rectY + (rect_h-7));
  }
}



function update(){
   clear();

  for (var i = 0; i < rectangles.length; i++) {
      let rx = rectangles[i].x;
      let ry = rectangles[i].y;
      let rectPoint = [[rx,ry], [rx+rect_w,ry], [rx+rect_w,ry+rect_h],[rx, rect_h+ry]];

      if(pointInRect([mouseX,mouseY], rectPoint)){
        if(state == 1)
          rectangles[i].c = typeLightCol;
        else if(state == 2 || state == 3)
          rectangles[i].c = lightColors[i];
        else
          rectangles[i].c = i == 6 ? lightColors[3] :   musLightColor;
      }
       else{
        if(state == 1)
          rectangles[i].c = typeDarkcolor;
        else if(state == 2 || state == 3)
          rectangles[i].c = i == 6 ? darkColors[3] : darkColors[i];
        else
          rectangles[i].c = i == 6 ? darkColors[3] : musDarkColor;

      }
  }

}


function mousePressed() {
  if(mouseX > width || mouseX < 0 || mouseY > height || mouseY < 0)
    return;

  if(!state){
    // state = 1;
    // canvas.class('canvas')
    //
    // x = mouseX;
    // y = mouseY;
    // setPopUp()
    // redraw();

  } else{
    for (var i = 0; i < rectangles.length; i++) {

        let rx = rectangles[i].x;
        let ry = rectangles[i].y;
        let rectPoint = [[rx,ry], [rx+rect_w,ry],[rx+rect_w,ry+rect_h] ,[rx, rect_h+ry]]
        if(pointInRect([mouseX,mouseY], rectPoint)){

          if(state == 1){
            clickedButton = i;
            state = i + 2;
            reInit();
            setPopUp();
            return;
          } else {
            // call the callback function
            let value = i;
            let testType = clickedButton;
            fun(headerText, value, true, testType);
            break;
          }

        }
    }

    state = 0;
    reInit()
    canvas.class('hid');
  }
}

function reInit(){
  amt = 0;
  rect_size_F = 0.1;
}


function setPopUp(){
  rectangles = [];
  let off = state != 4 ? 10 : 20;
  let rectNumber;

  if(state == 1){
      rectNumber = showMus ? 3 : 2;

    }
  else if(state == 2 || state == 3)
    rectNumber = 4;
  else
    rectNumber = 7;


    let startX = x;
    let startY = y;

    if(x+ 5*rect_w > width){
      startX = width - (5*rect_w+20)
    }

    if(y - (rect_h + off + 20) < 0)
      startY = y + (rect_h + off+20)


    let xx = startX;

    for (var i = 0; i < rectNumber; i++) {
      let text = i + '';
      if(i + 1 == rectNumber)
        text = 'NT'

      if(state == 1)
        text = buttonTypeText[i];

      rectangles.push({
        x : xx,
        y : startY - (rect_h + off),
        t : text,
        c : color(0)
      });


      xx += rect_w+1;
      if(i == 3){
        off = -11;
        xx = startX;

      }
    }
  }


function pointInRect(pt,rect,precision) {

  var p = precision || 6;
  var rectArea = 0.5*Math.abs(
    (rect[0][1] - rect[2][1]) * (rect[3][0] - rect[1][0])
    + (rect[1][1] - rect[3][1]) * (rect[0][0] - rect[2][0])
  );
  var triangleArea = rect.reduce(function(prev,cur, i, arr) {
    var j = i == arr.length-1 ? 0 : i+1;
    return prev + 0.5*Math.abs(
      pt[0] * (arr[i][1] - arr[j][1])
      + arr[i][0] * (arr[j][1] - pt[1])
      + arr[j][0] * (pt[1] - arr[i][1])
    );
  }, 0);
  return fix(triangleArea,p) == fix(rectArea,p);
}
// fix to the precision
function fix(n,p) {
  return parseInt(n * Math.pow(10,p));
};

let showMus;

 function showFreePop(text, callback){
   if(!text)
    return;

    for (var i = 0; i < mus.length; i++) {
      if(text.indexOf(mus[i]) > -1)
        showMus = true;
      else {
        showMus = false;
      }
    }

  headerText = text;

  reInit()
  state = 1;
  canvas.class('canvas')
  fun = callback;
  x = mouseX;
  y = mouseY;
  setPopUp()
  redraw();
}



let mus = ['T1', 'C8', 'C7', 'C6', 'C5', 'L2', 'L3', 'L4', 'L5', 'S1'];

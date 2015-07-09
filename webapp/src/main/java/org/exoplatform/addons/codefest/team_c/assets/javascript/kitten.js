function diffImageOk(img)
{
    if(img.src.match('/team-c-addon-webapp/image/okblack.png')) img.src = "/team-c-addon-webapp/image/okgrey.png";
    else img.src = "/team-c-addon-webapp/image/okblack.png";
}

function diffImageNok(img)
{
    if(img.src.match('/team-c-addon-webapp/image/nokblack.png')) img.src = "/team-c-addon-webapp/image/nokgrey.png";
    else img.src = "/team-c-addon-webapp/image/nokblack.png";
}

$(document).ready(function() {
  var $main = $('.meeting-main-part');
  var $input = $main.find('input[name="username"]');
  $input.on('keypress', function(e) {
    if (e.keyCode == 13 && $input.val() !== '') {
      var addUserUrl = $main.closest('.jz').find('div[data-method-id="KittenSaverController.addUser"]').data('url');
      var $clientarea = $('.clientarea');
      $.ajax({
        method : 'POST',
        url : addUserUrl,
        data : {
          username : $input.val(),
          start : parseFloat($clientarea.data('starttime')),
          end : parseFloat($clientarea.data('endtime'))
        },
        success : function(data) {
          $clientarea.html($clientarea.html() + data);
          band.trigger('change', [band.start, band.end]);
        }
      });
    }
  });
  
  $('.addOption').on('click', function() {
    var addChoiceUrl = $main.closest('.jz').find('div[data-method-id="KittenSaverController.addOption"]').data('url');   

    band.choices.push([band.start, band.end]);

    $.ajax({
      method : 'POST',
      url : addChoiceUrl,
      data : {
        start : band.start,
        end : band.end
      },
      success : function(data) {
        $('.choice').html($('.choice').html() + ' / [' + data.start + ", " + data.end + "]");
      }
    });
  });
  
  $('.addMeeting').on('click', function() {
    var addMeetingUrl = $main.closest('.jz').find('div[data-method-id="KittenSaverController.addMeeting"]').data('url');   

    var participants = [];
    $('.container').each(function() {
      participants.push($(this).data('user'));
    });
    
    var options = [];
    $.each(band.choices, function(idx, elem) {
      options.push(elem[0] + "/" + elem[1]);
    });
    
    $.ajax({
      method : 'POST',
      url : addMeetingUrl,
      data : {
        title : $('.meeting-main-part input[name="title"]').val(),
        description : $('.meeting-main-part input[name="description"]').val(),
        participants : participants.join(','),
        options : options.join(','),
        start : band.start,
        end : band.end
      },
      success : function(data) {
        window.location = data.url;
      }
    });
  });
});